package scala2021.ashinkarev.task08

import scala.annotation.tailrec

object Symbols {
  val STRIKE = 'X';
  val SPARE = '/';
  val MISS = '-';
  val FRAME = '|';
}

class Game(input: String) {
  def calculateScore(): Int = {
    @tailrec
    def calculateScore(index: Int, score: Int): Int = {
      if (input.length == 0) {
        return 0;
      }

      if (input.length == index) {
        return score;
      }

      val prevSymbol = if (index == 0) '@' else input(index - 1);
      val symbol = input(index);

      symbol match {
        case Symbols.STRIKE => calculateScore(index + 1, score + 10)
        case Symbols.SPARE => calculateScore(index + 1,  score + (10 - prevSymbol.toInt))
        case _ => calculateScore(index + 1,  score + symbol.toInt)
      }
    }

    calculateScore(0, 0);
  }
}
