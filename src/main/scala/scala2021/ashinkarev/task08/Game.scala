package scala2021.ashinkarev.task08

object Symbols {
  val STRIKE = 'X';
  val SPARE = '/';
  val MISS = '-';
  val FRAME = '|';
}



class Game(input: String) {
  def calculateScore(): Int = {
    var score = 0;

    for(symbol <- input) {
      symbol match {
        case Symbols.STRIKE => score += 10
        case _ => score += 0
      }
    }

    return score;
  }
}
