package hjlee.visualizer.control

/**
  * Created by hjlee on 9/26/16.
  */
class CircularVariable(initial: Double, inc: Double, val lower: Double, val upper: Double)
  extends SimpleVariable(initial, inc) {

  val range = upper - lower
  override def inc(steps: Int = 1): Unit = {
    super.inc(steps)
    _circle()
  }

  override def dec(steps: Int = 1): Unit = {
    super.dec(steps)
    _circle()
  }

  override def set(v: Double): Unit = {
    super.set(v)
    _circle()
  }

  private def _circle(): Unit = {
    if (value > upper) {
      value = (value - lower) % range + lower
    }
    else if (value < lower) {
      value = (value - lower) % range + upper
    }
  }
  /////////////////////
}
