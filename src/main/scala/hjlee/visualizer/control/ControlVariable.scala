package hjlee.visualizer.control

/**
  * Created by hjlee on 9/26/16.
  */
trait ControlVariable[T] {
  def get: T
  def inc: T
  def dec: T
  def set(v: T): Unit
}

