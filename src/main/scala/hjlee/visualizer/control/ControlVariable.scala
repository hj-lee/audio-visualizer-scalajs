package hjlee.visualizer.control

/**
  * Created by hjlee on 9/26/16.
  */
trait ControlVariable[T] {
  def get: T
  def inc(steps: Int = 1): Unit
  def dec(steps: Int = 1): Unit
  def set(v: T): Unit
}

object ControlVariable {
  def newDegreeVar(initial: Double = 0, increment: Double = 1) = {
    new CircularVariable(initial, increment, -180, 180)
  }

  def newSimpleVar(initial: Double = 0, increment: Double = 1) = {
    new SimpleVariable(initial, increment)
  }
}