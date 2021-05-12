package scala2021.ashinkarev.task07

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import org.scalamock.scalatest.MockFactory
import org.scalamock.function.MockFunction
import org.scalamock.function.MockFunction1

class UtilsSuite extends AnyFunSuite 
  with TableDrivenPropertyChecks 
  with ScalaCheckDrivenPropertyChecks 
  with Matchers
  with MockFactory
{

  import Utils.withConnection
  import Utils.withResource
  import Utils.withFile
  import Utils.withFileInputStream

  test("withConnection happy path") {
    withConnection(port = 9000) {
      conn => conn.run
    }
  }

  test("withFile happy path") {
    val readmeExists = withFile(path = "README.md") {
      file => file.exists()
    }

    readmeExists should be (true)
  }

  test("withFileInputStream happy path") {
    withFileInputStream(path = "README.md") {
      fis => {
        val bytes = fis.available()
        println(s"Count bytes are $bytes")
      }
    }
  }

  test("withResource happy path => resourceFactory and cleanup called exactly once and body called with the created resource") {
    val connection = new PrintConnection(9000);

    val mockedConnectionFactory = mockFunction[Connection];

    val mockedRun = mockFunction[Connection, Any];
    val mockedCleanup = mockFunction[Connection, Unit];

    mockedConnectionFactory.expects().onCall(() => connection).once()

    mockedRun expects(connection) repeat (1)
    mockedCleanup expects(connection) repeat (1)

    withResource(mockedConnectionFactory){ mockedRun }(mockedCleanup)
  }

  test("withResource with exception in the constructor => throws original exception and cannot cleanup since there is no resource being created succesfully") {
    val errorMessage = "Cannot create port, sorry.";

    val mockedConnectionFactory = mockFunction[Connection];

    val mockedRun = mockFunction[Connection, Any];
    val mockedCleanup = mockFunction[Connection, Unit];

    mockedConnectionFactory.expects().throws(new Exception(errorMessage))

    mockedRun expects(*) repeat (0)
    mockedCleanup expects(*) repeat (0)

    val thrown = intercept[Exception] {
      withResource(mockedConnectionFactory){ mockedRun }(mockedCleanup)
    }

    thrown.getMessage should be (errorMessage);
  }

  test("withResource with exception in the body => throws original exception and makes cleanup") {
    val errorMessage = "Cannot end my run, sorry.";

    verifyErrorCase(
      errorMessage,
      (mockedRun, _) => mockedRun.expects(*).throws(new Exception(errorMessage)),
      (mockedCleanup, connection) => mockedCleanup expects(connection) repeat (1)
    )
  }

  test("withResource with exception in cleanup => throws cleanup exception") {
    val errorMessage = "Cannot cleanup, sorry.";

    verifyErrorCase(
      errorMessage,
      (mockedRun, connection) => mockedRun expects(connection) repeat (1),
      (mockedCleanup, _) => mockedCleanup.expects(*).throws(new Exception(errorMessage))
    )
  }
  
  test("withResource with exception in the body and in cleanup => throws body exception") {
    val errorMessage = "Cannot end my run, sorry.";

    verifyErrorCase(
      errorMessage,
      (mockedRun, _) => mockedRun.expects(*).throws(new Exception(errorMessage)),
      (mockedCleanup, _) => mockedCleanup.expects(*).throws(new Exception("Cannot do cleanup, sorry."))
    )
  }

  def verifyErrorCase(
    errorMessage: String,
    mockedRunSetup: (MockFunction1[Connection, Any], Connection) => Any,
    mockedCleanupSetup: (MockFunction1[Connection, Unit], Connection) => Any
  ) {
    val mockedConnection = mock[Connection];

    val mockedRun = mockFunction[Connection, Any];
    val mockedCleanup = mockFunction[Connection, Unit];

    mockedRunSetup(mockedRun, mockedConnection)
    mockedCleanupSetup(mockedCleanup, mockedConnection)

    val thrown = intercept[Exception] {
      withResource(() => mockedConnection){ mockedRun }(mockedCleanup)
    }

    thrown.getMessage should be (errorMessage);
  }
}
