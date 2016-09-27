package hjlee.visualizer.control

/**
  * Created by hjlee on 9/26/16.
  */
trait ControlVariable {
  def get: Double
  def inc(steps: Int = 1): Unit
  def dec(steps: Int = 1): Unit
  def set(v: Double): Unit
}

