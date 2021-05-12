package scala2021.ashinkarev.task05

object Utils {
  def toOption[A, B](either: Either[A, B]): Option[B] = {
    either match {
      case Right(value) => Some(value)
      case Left(value) => None
    }
  }

  def toEitherWithErrorMessage[A](option: Option[A], erorrFactory: () => String): Either[String, A] = {
    option match {
      case Some(value) => Right(value)
      case None => Left(erorrFactory())
    }
  }
}
