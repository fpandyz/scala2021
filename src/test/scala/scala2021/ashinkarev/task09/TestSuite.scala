package scala2021.ashinkarev.task09

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class TestSuite extends AnyFunSuite with TableDrivenPropertyChecks with ScalaCheckDrivenPropertyChecks with Matchers {
  test("test") {
    val conversion: Conversion = Map(
      (EUR, USD) -> 1.13,
      (EUR, GBP) -> 0.71,
      (USD, EUR) -> 0.88,
      (USD, GBP) -> 0.63,
      (GBP, EUR) -> 1.40,
      (GBP, USD) -> 1.59
    )

    implicit val converter = Converter(conversion);

    val sum = 42(USD) + 35(EUR);

    (sum to GBP).amount should be (51.3765);
  }
}

