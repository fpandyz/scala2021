package scala2021.ashinkarev.task08

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GameSuite extends AnyFunSuite with TableDrivenPropertyChecks with ScalaCheckDrivenPropertyChecks with Matchers {
  test("empty string => 0") {
    new Game("").score() should be (0)
  }  
  
  test("first strike throw => 10") {
    new Game("X").score() should be (10)
  }

  test("first frame spare throw => 10") {
    new Game("7/").score() should be (10)
  }

  test("first frame not all pins for 2 throws => sum of throws") {
    new Game("72").score() should be (9)
  }

  test("strike, 7 => | 10 + 7 | + | 7 |") {
    new Game("X|7").score() should be (24)
  }

  test("strike, spare => | 10 + 7 + 3 | + | 7 + 3 |") {
    new Game("X|7/").score() should be (30)
  }
  
  test("strike, spare, 7 => | 10 + 7 + 3 | + | 7 + 3 + 7 | + | 7 |") {
    new Game("X|7/|7").score() should be (44)
  }

  test("strike, spare, 7, 2 => | 10 + 7 + 3 | + | 7 + 3 + 7 | + | 7 + 2 |") {
    new Game("X|7/|72").score() should be (46)
  }

  test("strike, spare, 7, 2, spare, strike => | 10 + 7 + 3 | + | 7 + 3 + 7 | + | 7 + 2 | + | 9 + 1 + 10 | + | 10 |") {
    new Game("X|7/|72|9/|X").score() should be (76)
  }
}
