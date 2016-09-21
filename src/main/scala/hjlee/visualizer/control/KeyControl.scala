package hjlee.visualizer.control

import org.scalajs.dom
import org.scalajs.dom.KeyboardEvent

import scalatags.JsDom.all._
import collection.mutable

/**
  * Created by hjlee on 9/20/16.
  */
class KeyControl {
  val codeActionMap: mutable.Map[String, () => Unit] = mutable.Map()

  val onkeydown = (e: KeyboardEvent) => {
    println(e.key)
    codeActionMap.get(e.key).foreach(action => {
      action()
      e.preventDefault()
    })
  }

  def addKeyAction(key: String, description: String)(action: () => Unit) = {
    codeActionMap.update(key, action)
  }

  def render() = {
    table(
      (0 to 5).map(i => {
        tr(td(i.toString))
      })
    ).render
  }
}
