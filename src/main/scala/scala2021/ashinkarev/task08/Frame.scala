package scala2021.ashinkarev.task08


case class Frame(
  number: Int,
  throws: List[Int] = List(),
) {
  def score = throws.sum;

  def isStrike = if (throws.isEmpty) false else throws(0) == 10;
  def isSpare = if (throws.length < 2) false else throws(0) + throws(1) == 10;

  def complete = if (throws.isEmpty) false else throws(0) match {
    case 10 => throws.length == 3
    case _ if (throws.length == 2) => score < 10 
    case _ if (throws.length == 3) => true 
  }

  def updateFrame(currentThrow: Int) = {
    Frame(
      number = number, 
      throws = throws :+ currentThrow
    )
  }
}
