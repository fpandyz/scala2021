package scala2021.ashinkarev

package object task09 {
  type Conversion = Map[(Currency, Currency), BigDecimal]

  /**
    *  Extensions for this DSL.
    */
  implicit class BigDecimalOps(val value: BigDecimal) extends AnyVal {
    def apply(currency: Currency)(implicit converter: Converter): Money = Money(value, currency)
  }

  implicit class IntOps(val value: Int) extends AnyVal {
    def apply(currency: Currency)(implicit converter: Converter): Money = BigDecimal(value).apply(currency)
  }
}

