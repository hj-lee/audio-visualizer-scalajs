package hjlee.visualizer.control

/**
  * Created by hjlee on 9/26/16.
  */
class CircularDouble(initial: Double, inc: Double, val lower: Double, val upper: Double)
  extends LimitedVariable[Double](initial, inc, lower, upper) {
  val range = upper - lower

  override protected def _checkBoundary(): Unit = {
    if (value > upper) {
      value = (value - lower) % range + lower
    }
    else if (value < lower) {
      value = (value - lower) % range + upper
    }
  }
}
