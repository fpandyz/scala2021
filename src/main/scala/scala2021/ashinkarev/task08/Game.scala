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

class Game(input: String) {
  def score(): Int = {
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
          val currentThrow = 10 - currentFrame.throws.last;

          val newFrameState = currentFrame.updateFrame(currentThrow);

          calculateScore(
            inputSymbolIndex = inputSymbolIndex + 1, 
            currentFrame = newFrameState,
            frames = updateFrames(frames, newFrameState),
          )
        }
        case Symbols.FRAME => {
          val nextFrame = Frame(
            number = currentFrame.number + 1, 
          )

          calculateScore(
            inputSymbolIndex = inputSymbolIndex + 1, 
            currentFrame = nextFrame,
            frames = frames :+ nextFrame,
          )
        }
        case _ => {
          val currentThrow = symbol.asDigit;

          val newFrameState = currentFrame.updateFrame(currentThrow);

          calculateScore(
            inputSymbolIndex = inputSymbolIndex + 1, 
            currentFrame = newFrameState,
            frames = updateFrames(frames, newFrameState),
          )
        }
      }
    }

    val firstFrame =  Frame(1);

    calculateScore(
      inputSymbolIndex = 0, 
      currentFrame = firstFrame,
      frames = List(firstFrame), 
    );
  }

  def updateFrames(frames: List[Frame], newFrameState: Frame): List[Frame] = {
    val currentThrow = newFrameState.throws.last;

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
            throws = if (firstFrameOfLastThree.isStrike && firstFrameOfLastThree.throws.length < 3) firstFrameOfLastThree.throws :+ currentThrow else firstFrameOfLastThree.throws
          ),
          Frame(
            number = firstFrameOfLastThree.number,
            throws = secondFrameOfLastThree.throws.length match {
              case 3 => secondFrameOfLastThree.throws
              case 2 if (secondFrameOfLastThree.isStrike) => secondFrameOfLastThree.throws :+ currentThrow
              case 2 if (secondFrameOfLastThree.isSpare) => secondFrameOfLastThree.throws :+ currentThrow
              case 1 if (secondFrameOfLastThree.isStrike) => secondFrameOfLastThree.throws :+ currentThrow
              case 1 if (secondFrameOfLastThree.isSpare) => secondFrameOfLastThree.throws :+ currentThrow
              case _ => secondFrameOfLastThree.throws
            }
          ),
          thirdFrameOfLastThree.updateFrame(currentThrow),
        )
      }
      case 2 => {
        val firstFrameOfLastThree = lastFramesThatCouldUpdateScore(0);
        val secondFrameOfLastThree = lastFramesThatCouldUpdateScore(1);

        List(
          Frame(
            number = firstFrameOfLastThree.number, 
            throws = if (firstFrameOfLastThree.isStrike || firstFrameOfLastThree.isSpare) firstFrameOfLastThree.throws :+ currentThrow else firstFrameOfLastThree.throws
          ),
          secondFrameOfLastThree.updateFrame(currentThrow),
        )
      }
      case 1 => List(
        newFrameState,
      )
    };

    return frames.dropRight(numberOfFramesThatCouldUpdate) ::: updatedLastFrames;
  }
}
