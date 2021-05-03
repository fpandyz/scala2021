package scala2021.ashinkarev.task04

object Program extends App {
  def canReturnChange(coins: List[Int] , neededChange: Int): Boolean = {
    if (coins.length == 1) {
      return coins(0) == neededChange
    }
    
    return true
  }

  println("Hello scala!")
}
