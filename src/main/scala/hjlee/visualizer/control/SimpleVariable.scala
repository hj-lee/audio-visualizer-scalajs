package hjlee.visualizer.control

import scala.math.ScalaNumber

/**
  * Created by hjlee on 9/26/16.
  */
class SimpleVariable[T](val initial: T, val increment: T)(implicit num: Numeric[T]) extends ControlVariable[T]{
  var value = initial
  import num._
//  type T = Int

  override def get: T = value

  override def inc: T = {
    value += increment
    value
  }

  override def dec: T = {
    value -= increment
    value
  }

  override def set(v: T): Unit = {
    value = v
  }
}
