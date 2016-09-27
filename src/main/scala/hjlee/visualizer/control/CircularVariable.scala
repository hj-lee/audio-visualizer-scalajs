package hjlee.visualizer.control

/**
  * Created by hjlee on 9/26/16.
  */
class CircularVariable(initial: Double, increment: Double, val lower: Double, val upper: Double)
  extends SimpleVariable(initial, increment) {

  val range = upper - lower
  override def inc(steps: Int = 1): Double = {
    super.inc(steps)
    _circle()
  }

  override def dec(steps: Int = 1): Double = {
    super.dec(steps)
    _circle()
  }

  override def set(v: Double): Unit = {
    super.set(v)
    _circle()
  }

  private def _circle(): Double = {
    if (value > upper) {
      value = (value - lower) % range + lower
    }
    else if (value < lower) {
      value = (value - lower) % range + upper
    }
    value
  }
}
