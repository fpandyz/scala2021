package scala2021.ashinkarev.task09

case class Converter(conversion: Conversion) {
  def conversionRate(from: Currency, to: Currency): BigDecimal = {
    if (from == to) 1
    else conversion((from, to))
  }

  def convert(sourceMoney: Money, targetCurrency: Currency)(implicit converter: Converter): Money = {
    val rate = conversionRate(sourceMoney.currency, targetCurrency)

    Money(sourceMoney.amount * rate, targetCurrency)
  }
}

object Converter {
  implicit val converter = new Converter(Map())
}

