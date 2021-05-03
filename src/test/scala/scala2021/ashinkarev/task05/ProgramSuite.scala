package scala2021.ashinkarev.task05

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ProgramSuite extends AnyFunSuite with TableDrivenPropertyChecks with ScalaCheckDrivenPropertyChecks with Matchers {

  import Program.findManagerName
  import Program.findManagerNameOrError

  test("findManagerName happy path") {
    findManagerName("Steve") should be (Some("Steve"))
    findManagerName("Mark") should be (Some("Steve"))
    findManagerName("Igor") should be (Some("Igor"))
  }

  test("findManagerNameOrError happy path") {
    findManagerNameOrError("Steve") should be (Right("Steve"))
    findManagerNameOrError("Mark") should be (Right("Steve"))
    findManagerNameOrError("Igor") should be (Right("Igor"))
  }

  test("findManagerName for unknown name => None") {
    findManagerName("John") should be (None)
  }

  test("findManagerNameOrError for unknown name => Left") {
    findManagerNameOrError("John") should be (Left("Cannot find employee with name: John"))
  }

  test("findManagerName for unknown department => None") {
    findManagerName("Christy") should be (None)
  }

  test("findManagerNameOrError for unknown department => Left") {
    findManagerNameOrError("Christy") should be (Left("Cannot find department with id: 5 for employee with name: Christy"))
  }

  test("findManagerName for unknown manager employee => None") {
    findManagerName("Naveen") should be (None)
  }

  test("findManagerNameOrError for unknown manager employee => Left") {
    findManagerNameOrError("Naveen") should be (Left("Cannot find employee with id: 14"))
  }

  test("findManagerName for unknown department manager => None") {
    findManagerName("Megan") should be (None)
  }

  test("findManagerNameOrError for unknown department manager => Left") {
    findManagerNameOrError("Megan") should be (Left("Cannot find manager for department with name: Research"))
  }
}
