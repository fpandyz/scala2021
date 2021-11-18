package scala2021.ashinkarev.task08

import scala.annotation.tailrec

object Symbols {
  val STRIKE = 'X';
  val SPARE = '/';
  val MISS = '-';
  val FRAME = '|';
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

      val tuple = symbol match {
        case Symbols.STRIKE => {
          val newFrameState = currentFrame.updateFrame(10);

          (newFrameState, updateFrames(frames, newFrameState));
        }
        case Symbols.SPARE => {
          val newFrameState = currentFrame.updateFrame(10 - currentFrame.throws.last);

          (newFrameState, updateFrames(frames, newFrameState));
        }
        case Symbols.FRAME => {
          val nextFrame = Frame(currentFrame.number + 1);

          val tenFramesPast = frames.length == 10;
          
          (nextFrame, if (tenFramesPast) frames else frames :+ nextFrame);
        }
        case Symbols.MISS => {
          val newFrameState = currentFrame.updateFrame(0);

          (newFrameState, updateFrames(frames, newFrameState));
        }
        case _ => {
          val newFrameState = currentFrame.updateFrame(symbol.asDigit);

          (newFrameState, updateFrames(frames, newFrameState));
        }
      }
      
      calculateScore(
        inputSymbolIndex = inputSymbolIndex + 1, 
        currentFrame = tuple._1,
        frames = tuple._2,
      )
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
          secondFrameOfLastThree.throws.length match {
            case 2 if (secondFrameOfLastThree.isStrike) => secondFrameOfLastThree.updateFrame(currentThrow)
            case 2 if (secondFrameOfLastThree.isSpare) => secondFrameOfLastThree.updateFrame(currentThrow)
            case 1 if (secondFrameOfLastThree.isStrike) => secondFrameOfLastThree.updateFrame(currentThrow)
            case 1 if (secondFrameOfLastThree.isSpare) => secondFrameOfLastThree.updateFrame(currentThrow)
            case _ => Frame(
              secondFrameOfLastThree.number,
              throws = secondFrameOfLastThree.throws
            )
          },
          thirdFrameOfLastThree.updateFrame(currentThrow),
        )
      }
      case 2 => {
        val firstFrameOfLastThree = lastFramesThatCouldUpdateScore(0);
        val secondFrameOfLastThree = lastFramesThatCouldUpdateScore(1);

        List(
          Frame(
            number = firstFrameOfLastThree.number, 
            throws = firstFrameOfLastThree.throws.length match {
              case 3 => firstFrameOfLastThree.throws
              case 2 if (!firstFrameOfLastThree.isStrike && !firstFrameOfLastThree.isSpare) => firstFrameOfLastThree.throws
              case _ => firstFrameOfLastThree.throws :+ currentThrow
            }
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
