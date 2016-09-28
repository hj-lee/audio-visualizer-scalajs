package hjlee.visualizer.control
import org.scalajs.dom.html.{TableCell, TableRow}

import collection.mutable
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

/**
  * Created by hjlee on 9/26/16.
  */
trait ControlVariable[T] {

  def get: T
  def inc(steps: Int = 1): Unit = {
    incImpl(steps)
    _notifyChange
  }
  def incImpl(steps: Int = 1): Unit
  def dec(steps: Int = 1): Unit = {
    decImpl(steps)
    _notifyChange
  }
  def decImpl(steps: Int = 1): Unit
  def set(v: T): Unit = {
    setImpl(v)
    _notifyChange
  }
  def setImpl(v: T): Unit



  var contentTds: Option[TableCell] = None

  protected def _notifyChange: Unit = {
    contentTds.foreach { td =>
      td.innerHTML = get.toString
    }
  }

  def makeKeyControl(
                      title: String,
                      decKeyCode: Int,
                      decKey: String,
                      incKeyCode: Int,
                      incKey: String,
                      commonAction: => Unit = {}
                    ): KeyControl =
  {
    val cv = this
    val incAction = () => {cv.inc(); commonAction}
    val decAction = () => {cv.dec(); commonAction}
    new KeyControl {
      override def keyActions: Seq[(Int, () => Unit)] = {
        Seq(
          (decKeyCode, decAction),
          (incKeyCode, incAction)
        )
      }
      override def genTableRow(): TypedTag[TableRow] = {
        val contentTd = td(cv.get.toString).render
        contentTds = Some(contentTd)
        tr(
          td(title),
          td(button(onclick:= decAction)(decKey)),
          contentTd,
          td(button(onclick:= incAction)(incKey))
        )
      }
    }
  }
}

object ControlVariable {
  def newDegreeVar(initial: Double = 0, increment: Double = 1) = {
    new CircularVariable(initial, increment, -180, 180)
  }

  def newSimpleVar(initial: Double = 0, increment: Double = 1) = {
    new SimpleVariable(initial, increment)
  }
}

