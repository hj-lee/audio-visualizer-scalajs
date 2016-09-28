package hjlee.visualizer.control

import scala.math.ScalaNumber

/**
  * Created by hjlee on 9/26/16.
  */
class SimpleVariable(val initial: Double, val increment: Double) extends ControlVariable[Double] {
  var value = initial

  override def get: Double = value

  override def incImpl(steps: Int = 1): Unit = {
    value += increment * steps
  }

  override def decImpl(steps: Int = 1): Unit = {
    value -= increment * steps
  }

  override def setImpl(v: Double): Unit = {
    value = v
  }
}
