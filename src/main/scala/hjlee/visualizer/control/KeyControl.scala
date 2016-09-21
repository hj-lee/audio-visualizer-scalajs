package hjlee.visualizer.control

import org.scalajs.dom
import org.scalajs.dom.KeyboardEvent

import scalatags.JsDom.all._
import collection.mutable


/**
  * Created by hjlee on 9/20/16.
  */
class KeyControl {
  val codeActionMap: mutable.Map[Int, () => Unit] = mutable.Map()

  val onkeydown = (e: KeyboardEvent) => {
    println(e.keyCode)
    codeActionMap.get(e.keyCode).foreach(action => {
      action()
      e.preventDefault()
    })
  }

  def addKeyAction(keyCode: Int, description: String)(action: => Unit) = {
    codeActionMap.update(keyCode, () => action)
  }

  def render() = {
    table(
      (0 to 5).map(i => {
        tr(td(i.toString))
      })
    ).render
  }
}
