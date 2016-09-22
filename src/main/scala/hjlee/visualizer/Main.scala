package hjlee.visualizer

import scala.scalajs.js
import js.Dynamic.{global => g}
import js.{Dynamic, Function1, JSApp}
//import dom.window.navigator
import org.scalajs.dom.document

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
      document.getElementById("content").innerHTML = "Can't getUserMedia"
    }

    val md = navigator.mediaDevices
    if(!js.isUndefined(md) && !js.isUndefined(md.getUserMedia)) {
      md.getUserMedia(js.Dynamic.literal(audio = true)).`then`(successFun).`catch`(failFun)
    }
    else {
      val getUserMedia: Dynamic = (navigator.getUserMedia ||
        navigator.webkitGetUserMedia ||
        navigator.mozGetUserMedia ||
        navigator.msGetUserMedia)
      if(!js.isUndefined(getUserMedia)) {
        getUserMedia.call(navigator,
          js.Dynamic.literal(audio = true),
          successFun, failFun
        )
      }
      else {
        failFun(navigator)
      }
    }

  }
}
