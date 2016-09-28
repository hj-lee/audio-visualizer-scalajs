package hjlee.visualizer.control

import org.scalajs.dom.KeyboardEvent
import org.scalajs.dom.html.TableRow

import scala.collection.mutable
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._


/**
  * Created by hjlee on 9/20/16.
  */
class KeyControler {
  val codeActionMap: mutable.Map[Int, () => Unit] = mutable.Map()

  val keyControls: mutable.ArrayBuffer[KeyControl] = mutable.ArrayBuffer()

  val onkeydown = (e: KeyboardEvent) => {
    println(e.keyCode)
    codeActionMap.get(e.keyCode).foreach(action => {
      action()
      e.preventDefault()
    })
  }

  def addKeyContorl(kc: KeyControl): Unit = {
    keyControls += kc
    kc.keyActions.foreach{ pair =>
      val (keyCode, action) = pair
      codeActionMap.update(keyCode, action)
    }
  }

  def addKeyAction(keyCode: Int, description: String)(action: => Unit) = {
    codeActionMap.update(keyCode, () => action)
  }

  def render() = {
    table(
      keyControls.map{kc =>
        kc.genTableRow()
      }
    ).render
  }
}

trait KeyControl {
  def keyActions : Seq[(Int, ()=>Unit)]
  def genTableRow(): TypedTag[TableRow]
}