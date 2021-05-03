package scala2021.ashinkarev.task04

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ProgramSuite extends AnyFunSuite with TableDrivenPropertyChecks with ScalaCheckDrivenPropertyChecks with Matchers {

  import Program.canReturnChange

  test("single coin list => true for the same coin") {
    assert(canReturnChange(List(1), 1))
  }

  test("single coin list => false for a different coin") {
    assert(canReturnChange(List(1), 2) == false)
    assert(canReturnChange(List(2), 1) == false)
  }
}
