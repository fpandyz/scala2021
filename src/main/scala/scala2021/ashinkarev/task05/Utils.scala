package scala2021.ashinkarev.task05

object Utils {
  def toOption[A, B](either: Either[A, B]): Option[B] = {
    either match {
      case Right(value) => Some(value)
      case Left(value) => None
    }
  }
}