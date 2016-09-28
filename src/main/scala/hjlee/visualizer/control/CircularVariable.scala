package hjlee.visualizer.control

/**
  * Created by hjlee on 9/26/16.
  */
class CircularVariable(initial: Double, inc: Double, val lower: Double, val upper: Double)
  extends SimpleVariable(initial, inc) {


  val range = upper - lower
  override def incImpl(steps: Int = 1): Unit = {
    super.incImpl(steps)
    _circle()
  }

  override def decImpl(steps: Int = 1): Unit = {
    super.decImpl(steps)
    _circle()
  }

  override def setImpl(v: Double): Unit = {
    super.setImpl(v)
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
