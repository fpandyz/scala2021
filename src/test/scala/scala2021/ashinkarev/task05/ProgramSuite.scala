package scala2021.ashinkarev.task05

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.concurrent._
import ExecutionContext.Implicits.global

class ProgramSuite extends AnyFunSuite with TableDrivenPropertyChecks with ScalaCheckDrivenPropertyChecks with Matchers {

  import Program.findManagerName
  import Program.findManagerNameOrError
  import Program.findManagerNameOrErrorAsync

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

  test("findManagerNameOrErrorAsync happy path") {
    findManagerNameOrErrorAsync("Steve") map { result => result should be (Right("Steve")) }
    findManagerNameOrErrorAsync("Mark") map { result => result should be (Right("Steve")) }
    findManagerNameOrErrorAsync("Igor") map { result => result should be (Right("Igor")) }
  }

  test("findManagerName for unknown name => None") {
    findManagerName("John") should be (None)
  }

  val noJohn = Left("Cannot find employee with name: John")

  test("findManagerNameOrError for unknown name => Left") {
    findManagerNameOrError("John") should be (noJohn)
  }

  test("findManagerNameOrErrorAsync for unknown name => Left") {
    findManagerNameOrErrorAsync("John")  map { result => result should be (noJohn) }
  }

  test("findManagerName for unknown department => None") {
    findManagerName("Christy") should be (None)
  }

  val noChristyDepartment = Left("Cannot find department with id: 5 for employee with name: Christy")

  test("findManagerNameOrError for unknown department => Left") {
    findManagerNameOrError("Christy") should be (noChristyDepartment)
  }

  test("findManagerNameOrErrorAsync for unknown department => Left") {
    findManagerNameOrErrorAsync("Christy")  map { result => result should be (noChristyDepartment) }
  }

  test("findManagerName for unknown manager employee => None") {
    findManagerName("Naveen") should be (None)
  }

  val no4Employee = Left("Cannot find employee with id: 14")

  test("findManagerNameOrError for unknown manager employee => Left") {
    findManagerNameOrError("Naveen") should be (no4Employee)
  }

  test("findManagerNameOrErrorAsync for unknown manager employee => Left") {
    findManagerNameOrErrorAsync("Naveen")  map { result => result should be (no4Employee) }
  }

  test("findManagerName for unknown department manager => None") {
    findManagerName("Megan") should be (None)
  }

  val noReasearch = Left("Cannot find manager for department with name: Research")

  test("findManagerNameOrError for unknown department manager => Left") {
    findManagerNameOrError("Megan") should be (noReasearch)
  }

  test("findManagerNameOrErrorAsync for unknown department manager => Left") {
    findManagerNameOrErrorAsync("Megan")  map { result => result should be (noReasearch) }
  }
}
