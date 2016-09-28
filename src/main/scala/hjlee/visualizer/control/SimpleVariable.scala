package hjlee.visualizer.control

import scala.math.ScalaNumber

/**
  * Created by hjlee on 9/26/16.
  */
class SimpleVariable[T](val initial: T, val increment: T)(implicit num: Numeric[T]) extends ControlVariable[T] {
  import num._
  override protected var value = initial

//  override def get: T = value

  override def incImpl(): Unit = {
    value += increment
  }

  override def decImpl(): Unit = {
    value -= increment
  }

  override def setImpl(v: T): Unit = {
    value = v
  }
}
