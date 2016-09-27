package hjlee.visualizer.control

import scala.math.ScalaNumber

/**
  * Created by hjlee on 9/26/16.
  */
class SimpleVariable(val initial: Double, val increment: Double) extends ControlVariable{
  var value = initial

  override def get: Double = value

  override def inc(steps: Int = 1): Double = {
    value += increment * steps
    value
  }

  override def dec(steps: Int = 1): Double = {
    value -= increment * steps
    value
  }

  override def set(v: Double): Unit = {
    value = v
  }
}
