package hjlee.visualizer

import scala.scalajs.js
import js.Dynamic.{global => g}
import js.{Dynamic, Function1, JSApp}
//import dom.window.navigator

/**
  * Created by hjlee on 9/14/16.
  */
object Main extends  JSApp
{
  def main(): Unit = {
    val navigator = g.navigator

    val successFun: Function1[Dynamic, Unit] = (stream: Dynamic) => {
      val visualizer = new Visualizer(stream)
      visualizer.start()
    }

    val failFun: Function1[Dynamic, Unit] = (error: Dynamic) => {
      println("Can't getUserMedia: " + error)
    }

    val getUserMedia: Dynamic = (navigator.getUserMedia ||
      navigator.webkitGetUserMedia ||
      navigator.mozGetUserMedia ||
      navigator.msGetUserMedia)

    getUserMedia.call(navigator,
      js.Dynamic.literal(audio = true),
      successFun, failFun
    )

    // not many browser support this, don't bother
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
