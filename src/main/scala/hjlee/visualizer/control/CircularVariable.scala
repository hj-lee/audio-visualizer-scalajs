package hjlee.visualizer.control

/**
  * Created by hjlee on 9/26/16.
  */
class CircularVariable(initial: Double, increment: Double, val lower: Double, val upper: Double)
  extends SimpleVariable[Double](initial, increment) {

  val range = upper - lower
  override def inc: Double = {
    super.inc
    circle()
  }

  override def dec: Double = {
    super.dec
    circle()
  }

  override def set(v: Double): Unit = {
    super.set(v)
    circle()
  }

  def circle(): Double = {
    if (value > upper) {
      value = (value - lower) % range + lower
    }
    else if (value < lower) {
      value = (value - lower) % range + lower + range
    }
    value
  }
}
