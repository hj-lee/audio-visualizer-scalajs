package hjlee.visualizer.control

/**
  * Created by hjlee on 9/26/16.
  */
class CircularVariable(initial: Double, inc: Double, val lower: Double, val upper: Double)
  extends SimpleVariable[Double](initial, inc) {


  val range = upper - lower
  override def incImpl(): Unit = {
    super.incImpl()
    _circle()
  }

  override def decImpl(): Unit = {
    super.decImpl()
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
