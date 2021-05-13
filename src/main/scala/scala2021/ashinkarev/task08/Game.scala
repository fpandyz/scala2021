package scala2021.ashinkarev.task08

import scala.annotation.tailrec

object Symbols {
  val STRIKE = 'X';
  val SPARE = '/';
  val MISS = '-';
  val FRAME = '|';
}

case class Frame(
  number: Int,
  throwNumber: Int,
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

  def updateFrame(currentThrow: Int, newThrowNumber: Int = throwNumber) = {
    Frame(
      number = number, 
      throwNumber = throwNumber,
      throws = throws :+ currentThrow
    )
  }
}

class Game(input: String) {
  def calculateScore(): Int = {
    @tailrec
    def calculateScore(
      inputSymbolIndex: Int,
      currentFrame: Frame,
      frames: List[Frame], 
    ): Int = {
      if (input.length == 0) {
        return 0;
      }

      if (input.length == inputSymbolIndex) {
        return frames.map(_.score).sum;
      }

      val prevSymbol = if (inputSymbolIndex == 0) '@' else input(inputSymbolIndex - 1);
      val symbol = input(inputSymbolIndex);

      symbol match {
        case Symbols.STRIKE => {
          val newFrameState = currentFrame.updateFrame(10);

          calculateScore(
            inputSymbolIndex = inputSymbolIndex + 1, 
            currentFrame = newFrameState,
            frames = frames.dropRight(1) :+ newFrameState,
          )
        }
        case Symbols.SPARE => {
          val newFrameState = currentFrame.updateFrame(10 - currentFrame.throws.last);

          calculateScore(
            inputSymbolIndex = inputSymbolIndex + 1, 
            currentFrame = newFrameState,
            frames = frames.dropRight(1) :+ newFrameState,
          )
        }
        case Symbols.FRAME => {
          val nextFrame = Frame(
            number = currentFrame.number + 1, 
            throwNumber = 1
          )

          calculateScore(
            inputSymbolIndex = inputSymbolIndex + 1, 
            currentFrame = nextFrame,
            frames = frames :+ nextFrame,
          )
        }
        case _ => {
          val currentThrow = symbol.asDigit;

          val newFrameState = currentFrame.updateFrame(symbol.asDigit, currentFrame.throwNumber + 1);

          val numberOfFramesThatCouldUpdate = math.min(frames.length, 3);

          val lastFramesThatCouldUpdateScore = frames.takeRight(numberOfFramesThatCouldUpdate);

          val updatedLastFrames = lastFramesThatCouldUpdateScore.length match {
            case 3 => {
              val firstFrameOfLastThree = lastFramesThatCouldUpdateScore(0);
              val secondFrameOfLastThree = lastFramesThatCouldUpdateScore(1);
              val thirdFrameOfLastThree = lastFramesThatCouldUpdateScore(2);

              List(
                Frame(
                  number = firstFrameOfLastThree.number, 
                  throwNumber = firstFrameOfLastThree.throwNumber,
                  throws = if (firstFrameOfLastThree.isStrike) firstFrameOfLastThree.throws :+ currentThrow else firstFrameOfLastThree.throws
                ),
                Frame(
                  number = secondFrameOfLastThree.number, 
                  throwNumber = secondFrameOfLastThree.throwNumber,
                  throws = if (secondFrameOfLastThree.isStrike || secondFrameOfLastThree.isSpare) secondFrameOfLastThree.throws :+ currentThrow else secondFrameOfLastThree.throws
                ),
                thirdFrameOfLastThree.updateFrame(currentThrow, thirdFrameOfLastThree.throwNumber + 1),
              )
            }
            case 2 => {
              val firstFrameOfLastThree = lastFramesThatCouldUpdateScore(0);
              val secondFrameOfLastThree = lastFramesThatCouldUpdateScore(1);

              List(
                Frame(
                  number = firstFrameOfLastThree.number, 
                  throwNumber = firstFrameOfLastThree.throwNumber,
                  throws = if (firstFrameOfLastThree.isStrike || firstFrameOfLastThree.isSpare) firstFrameOfLastThree.throws :+ currentThrow else firstFrameOfLastThree.throws
                ),
                secondFrameOfLastThree.updateFrame(currentThrow, secondFrameOfLastThree.throwNumber + 1),
              )
            }
            case 1 => List(
              currentFrame.updateFrame(currentThrow, currentFrame.throwNumber + 1)
            )
          };
          
          calculateScore(
            inputSymbolIndex = inputSymbolIndex + 1, 
            currentFrame = newFrameState,
            frames = frames.dropRight(numberOfFramesThatCouldUpdate) ::: updatedLastFrames,
          )
        }
      }
    }

    val firstFrame =  Frame(1, 1);

    calculateScore(
      inputSymbolIndex = 0, 
      currentFrame = firstFrame,
      frames = List(firstFrame), 
    );
  }
}
