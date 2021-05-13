package scala2021.ashinkarev.task08

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GameSuite extends AnyFunSuite with TableDrivenPropertyChecks with ScalaCheckDrivenPropertyChecks with Matchers {
  test("empty string => 0") {
    new Game("").calculateScore() should be (0)
  }  
  
  test("first strike throw => 10") {
    new Game("X").calculateScore() should be (10)
  }

  test("first frame spare throw => 10") {
    new Game("7/").calculateScore() should be (10)
  }

  test("first frame not all pins for 2 throws => sum of throws") {
    new Game("72").calculateScore() should be (9)
  }

  test("strike, 7 => | 10 + 7 | + | 7 |") {
    new Game("X|7").calculateScore() should be (24)
  }
}
