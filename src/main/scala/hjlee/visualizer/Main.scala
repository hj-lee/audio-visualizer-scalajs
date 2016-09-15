package hjlee.visualizer

import scala.scalajs.js
import js.Dynamic.{global => g}
import js.JSApp

import org.scalajs.dom.window
//import org.scalajs.dom
//import org.scalajs.dom.document
//import org.scalajs.jquery.JQuery
//
//import org.denigma.threejs.Scene


/**
  * Created by hjlee on 9/14/16.
  */
object Main extends JSApp {
  def main(): Unit = {
//    success(g)
    val um = g.navigator.mediaDevices.getUserMedia(
      js.Dynamic.literal(audio = true)).`then`((stream: js.Dynamic) => {
        val app = new Visualizer(stream)
        app.start()
      }).`catch`((error: js.Dynamic) => {
        println("Can't getUserMedia: " + error)
      }
    )
  }
}
