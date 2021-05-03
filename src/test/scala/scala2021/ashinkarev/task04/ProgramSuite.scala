package scala2021.ashinkarev.task04

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ProgramSuite extends AnyFunSuite with TableDrivenPropertyChecks with ScalaCheckDrivenPropertyChecks with Matchers {

  import Program.canReturnChange

  test("empty coins list => false fpor any change") {
    assert(canReturnChange(List(), 0) == false)
    assert(canReturnChange(List(), 1) == false)
  }

  test("single coin list => true for the same change") {
    assert(canReturnChange(List(1), 1))
  }

  test("single coin list => false for a different change") {
    assert(canReturnChange(List(1), 2) == false)
    assert(canReturnChange(List(2), 1) == false)
  }
}
