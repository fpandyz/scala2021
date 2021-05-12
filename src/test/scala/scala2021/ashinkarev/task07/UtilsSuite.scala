package scala2021.ashinkarev.task07

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

case class BrokenConnectionConstructor(portFactory: () => Int) extends Connection(portFactory()) {
}

case class BrokenConnectionRun(port: Int) extends Connection(port) {
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
{

  import Utils.withConnection
  import Utils.withResource

  test("withConnection happy path") {
    withConnection(port = 9000) { conn =>
      conn.run
    }
  }

  test("withResource with exception in the constructor => throws original exception and cannot cleanup since there is no resource being created succesfully") {
    val errorMessage = "Cannot create port, sorry.";

    val thrown = intercept[Exception] {
      withResource(() => BrokenConnectionConstructor(() => throw  new Exception(errorMessage))){ conn =>
        conn.run
      }((connection) => connection.close())
    }

    thrown.getMessage should be (errorMessage);
  }

  test("withResource with exception in the body => throws original exception and makes cleanup") {
    val errorMessage = "Cannot end my run, sorry.";

    val thrown = intercept[Exception] {
      withResource(() => BrokenConnectionRun(9000)){ conn =>
        conn.run
      }((connection) => connection.close())
    }

    thrown.getMessage should be (errorMessage);
  }

  test("withResource with exception in cleanup => throws cleanup exception") {
    val errorMessage = "Cannot cleanup, sorry.";

    val thrown = intercept[Exception] {
      withResource(() => PrintConnection(9000)){ conn =>
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
      withResource(() => BrokenConnectionRun(9000)){ conn =>
        conn.run
      }((connection) => {
        throw new Exception("Cannot do cleanup, sorry.");
      })
    }

    thrown.getMessage should be (errorMessage);
  }
}
