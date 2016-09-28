package hjlee.visualizer.control

/**
  * Created by hjlee on 9/28/16.
  */
class CircularInt(initial: Int = 0, increment: Int = 1, lowerLimit: Int = 0, upperLimit: Int)
  extends LimitedVariable[Int](initial, increment, lowerLimit, upperLimit) {
  override protected def _checkBoundary(): Unit = {
    if (value < lowerLimit) value = upperLimit
    else if (value > upperLimit) value = lowerLimit
  }
}
