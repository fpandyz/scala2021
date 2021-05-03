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

  test("validate task examples") {
    assert(canReturnChange(List(2, 4, 6), 8))
    assert(canReturnChange(List(2, 4, 6), 5) == false)
  }

  val coins = List(5, 10, 5, 50, 10, 20, 100, 200, 200, 5, 3)
  
  val tectCasesTable = Table(
    ("change", "expected"),
    (1, false),
    (5, true),
    (10, true),
    (15, true),
    (20, true),
    (24, false),
    (25, true),
    (30, true),
    (35, true),
    (40, true),
    (45, true),
    (50, true),
    (59, false),
    (95, true),
    (98, true),
    (103, true),
    (103, true),
    (608, true),
    (609, false),
    (1000, false),
  )

  test("check add correctly (table)") {
    forAll(tectCasesTable) {
      (change, expected) => {
        canReturnChange(coins, change) should be(expected)
      }
    }
  }
}
