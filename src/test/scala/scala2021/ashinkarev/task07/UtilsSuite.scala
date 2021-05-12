package scala2021.ashinkarev.task07

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import org.scalamock.scalatest.MockFactory

class BrokenConnectionConstructor(portFactory: () => Int) extends ConnectionBase(portFactory()) {
}

class BrokenConnectionRun(port: Int) extends ConnectionBase(port) {
  override def run(): Unit = {
    println("Start run override");
    throw new Exception("Cannot end my run, sorry.")
    println("End run override");
  }
}

class UtilsSuite extends AnyFunSuite 
  with TableDrivenPropertyChecks 
  with ScalaCheckDrivenPropertyChecks 
  with Matchers
  with MockFactory
{

  import Utils.withConnection
  import Utils.withResource

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

    val thrown = intercept[Exception] {
      withResource(() => new BrokenConnectionConstructor(() => throw  new Exception(errorMessage))){ conn =>
        conn.run
      }((connection) => connection.close())
    }

    thrown.getMessage should be (errorMessage);
  }

  test("withResource with exception in the body => throws original exception and makes cleanup") {
    val errorMessage = "Cannot end my run, sorry.";

    val thrown = intercept[Exception] {
      withResource(() => new BrokenConnectionRun(9000)){ conn =>
        conn.run
      }((connection) => connection.close())
    }

    thrown.getMessage should be (errorMessage);
  }

  test("withResource with exception in cleanup => throws cleanup exception") {
    val errorMessage = "Cannot cleanup, sorry.";

    val thrown = intercept[Exception] {
      withResource(() => new PrintConnection(9000)){ conn =>
        conn.run
      }((connection) => {
        throw new Exception(errorMessage);
      })
    }

    thrown.getMessage should be (errorMessage);
  }
  
  test("withResource with exception in the body and in cleanup => throws body exception") {
    val errorMessage = "Cannot end my run, sorry.";

    val thrown = intercept[Exception] {
      withResource(() => new BrokenConnectionRun(9000)){ conn =>
        conn.run
      }((connection) => {
        throw new Exception("Cannot do cleanup, sorry.");
      })
    }

    thrown.getMessage should be (errorMessage);
  }
}
