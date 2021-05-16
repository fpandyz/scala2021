package scala2021.ashinkarev.task09

trait Currency {
}

object USD extends Currency {
  override val toString = "USD"
}

object EUR extends Currency {
  override val toString = "EUR"
}

object GBP extends Currency {
  override val toString = "GBP"
}

