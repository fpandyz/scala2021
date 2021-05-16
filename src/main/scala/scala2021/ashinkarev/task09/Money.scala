package scala2021.ashinkarev.task09

import scala.math.BigDecimal.RoundingMode
import scala.math.BigDecimal.RoundingMode.RoundingMode

case class Money(amount: BigDecimal, currency: Currency)(implicit converter: Converter) {
  def to(thatCurrency: Currency): Money = {
    val rate = converter.conversionRate(currency, thatCurrency)
    Money(amount * rate, thatCurrency)
  }

  def +(that: Money): Money = {
    val convertedMoney = converter.convert(that, this.currency)

    (this.amount + convertedMoney.amount)(this.currency)
  }
}

