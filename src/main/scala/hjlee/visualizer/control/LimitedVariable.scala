package hjlee.visualizer.control

/**
  * Created by hjlee on 9/28/16.
  */
class LimitedVariable[T](initial: T, increment: T, val lowerLimit: T, val upperLimit: T)
                        (implicit num: Numeric[T])
  extends SimpleVariable[T](initial, increment) {
  import num._

  protected def _checkBoundary(): Unit = {
    if (value < lowerLimit) value = lowerLimit
    else if (value > upperLimit) value = upperLimit
  }

  override def incImpl(): Unit = {
    super.incImpl()
    _checkBoundary()
  }

  override def decImpl(): Unit = {
    super.decImpl()
    _checkBoundary()
  }

  override def setImpl(v: T): Unit = {
    super.setImpl(v)
    _checkBoundary()
  }
}


