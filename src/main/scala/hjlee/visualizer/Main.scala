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
object Main extends  JSApp
{
  def main(): Unit = {
    g.navigator.getUserMedia = (g.navigator.getUserMedia ||
      g.navigator.webkitGetUserMedia ||
      g.navigator.mozGetUserMedia ||
      g.navigator.msGetUserMedia)

    g.navigator.getUserMedia(
      js.Dynamic.literal(audio = true),
      (stream: js.Dynamic) => {
        val visualizer = new Visualizer(stream)
        visualizer.start()
      }, (error: js.Dynamic) => {
        println("Can't getUserMedia: " + error)
      }
    )

    // not many browser support this
//    val um = g.navigator.mediaDevices.getUserMedia(
//      js.Dynamic.literal(audio = true)).`then`((stream: js.Dynamic) => {
//        val app = new Visualizer(stream)
//        app.start()
//      }).`catch`((error: js.Dynamic) => {
//        println("Can't getUserMedia: " + error)
//      }
//    )
  }
}
