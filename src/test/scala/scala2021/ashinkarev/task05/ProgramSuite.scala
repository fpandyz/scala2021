package scala2021.ashinkarev.task05

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ProgramSuite extends AnyFunSuite with TableDrivenPropertyChecks with ScalaCheckDrivenPropertyChecks with Matchers {

  import Program.findManagerName

  test("findManagerName happy path") {
    findManagerName("Steve") should be (Some("Steve"))
    findManagerName("Mark") should be (Some("Steve"))
    findManagerName("Igor") should be (Some("Igor"))
  }

  test("findManagerName for unknown name => None") {
    findManagerName("John") should be (None)
  }

  test("findManagerName for unknown department => None") {
    findManagerName("Christy") should be (None)
  }

  test("findManagerName for unknown manager employee => None") {
    findManagerName("Naveen") should be (None)
  }

  test("findManagerName for unknown department manager => None") {
    findManagerName("Megan") should be (None)
  }
}
