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

  test("strike, spare, 7, 2, spare, strike, strike, strike => | 10 + 7 + 3 | + | 7 + 3 + 7 | + | 7 + 2 | + | 9 + 1 + 10 | + | 10 + 10 + 10 | + | 10 + 10 | + | 10 |") {
    new Game("X|7/|72|9/|X|X|X").score() should be (126)
  }

  test("strike, spare, 7, 2, spare, strike, strike, strike, 2, 3 => 138") {
    new Game("X|7/|72|9/|X|X|X|23").score() should be (138)
  }
  
  test("strike, spare, 7, 2, spare, strike, strike, strike, 2, 3, spare, 7 => 162") {
    new Game("X|7/|72|9/|X|X|X|23|6/|7").score() should be (162)
  }
  
  test("strike, spare, 7, 2, spare, strike, strike, strike, 2, 3, spare, spare, 3 => 168") {
    new Game("X|7/|72|9/|X|X|X|23|6/|7/||3").score() should be (168)
  }

  test("perfect game => 300") {
    new Game("X|X|X|X|X|X|X|X|X|X||XX").score() should be (300)
  }

  test("5/|5/|5/|5/|5/|5/|5/|5/|5/|5/||5 => 150") {
    new Game("5/|5/|5/|5/|5/|5/|5/|5/|5/|5/||5").score() should be (150)
  }

  test("9-|9-|9-|9-|9-|9-|9-|9-|9-|9-|| => 90") {
    new Game("9-|9-|9-|9-|9-|9-|9-|9-|9-|9-||").score() should be (90)
  }

  test("X|7/|9-|X|-8|8/|-6|X|X|X||81 => 167") {
    new Game("X|7/|9-|X|-8|8/|-6|X|X|X||81").score() should be (167)
  }
}
