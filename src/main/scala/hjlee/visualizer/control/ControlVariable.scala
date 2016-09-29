package hjlee.visualizer.control
import org.scalajs.dom.html.{TableCell, TableRow}

import collection.mutable
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._

/**
  * Created by hjlee on 9/26/16.
  */
trait ControlVariable[T] {
  protected var value: T
  def get: T = value
  def inc(): Unit = {
    incImpl()
    _notifyChange()
  }
  def incImpl(): Unit
  def dec(steps: Int = 1): Unit = {
    decImpl()
    _notifyChange()
  }
  def decImpl(): Unit
  def set(v: T): Unit = {
    setImpl(v)
    _notifyChange()
  }
  def setImpl(v: T): Unit



//  var observers: Option[TableCell] = None
  private[this] val observers: mutable.ArrayBuffer[T => Unit] = mutable.ArrayBuffer()

  def addObserver(ob : T => Unit) = observers += ob

  protected def _notifyChange(): Unit = {
    observers.foreach(_(get))
  }
  protected def _toString(v: T): String = {
    v.toString
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
        val contentTd = td(_toString(cv.get)).render
        cv.addObserver{ v =>
            contentTd.innerHTML = _toString(v)
        }
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
    new CircularDouble(initial, increment, -180, 180)
  }

  def newSimpleVar(initial: Double = 0, increment: Double = 1) = {
    new SimpleVariable(initial, increment)
  }
}

trait VariableObserver[T] {
  def changed(cv: ControlVariable[T])
}