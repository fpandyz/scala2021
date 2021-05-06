package scala2021.ashinkarev.task04

import scala.annotation.tailrec

object Program extends App {
  def canReturnChange(coins: List[Int] , neededChange: Int): Boolean = {
    @tailrec
    def canReturnChange(coins: List[Int] , neededChange: Int, index: Int, sum: Int): Boolean = {
      if (coins.length == 0) {
        return false
      }

      if (coins.length == 1) {
        return coins(0) == neededChange
      }

      if (coins.length == index) {
        return neededChange == sum
      }

      val currentCoin = coins(index)

      val sumWithCurrentCoin = sum + currentCoin

      if (sumWithCurrentCoin == neededChange) {
        return true
      }

      val nextSum = if (sumWithCurrentCoin < neededChange) sumWithCurrentCoin else sum

      canReturnChange(coins, neededChange, index + 1, nextSum)
    }

    canReturnChange(coins.sortWith(_>_), neededChange, 0, 0)
  }
}
