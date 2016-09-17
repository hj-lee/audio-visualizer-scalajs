package hjlee.visualizer

import org.scalajs.dom.AudioContext
import org.scalajs.dom.experimental.mediastream.MediaStream
import org.scalajs.dom.document
import org.scalajs.dom.window

import scala.scalajs.js
import js.Dynamic.{global => g}
import scala.scalajs.js.typedarray.{Float32Array, Uint8Array}
import org.scalajs.jquery.{JQueryEventObject, jQuery}
import org.denigma.threejs._
import org.scalajs.dom
import org.scalajs.dom.raw.AnalyserNode

import scala.scalajs.js.Function1
import scala.scalajs.js.annotation.JSExport
import scala.util.Random




class Visualizer(stream: js.Dynamic) {

  val analyser = new Analyser(stream)


  var width: Double = window.innerWidth
  var height: Double = window.innerHeight-4

  val scene = new Scene()
  val renderer = new WebGLRenderer()

  def newCamera() = {
    new PerspectiveCamera(15, width/height, 0.1, 20000)
  }
  var camera: Camera = newCamera()

  def windowResize(): Unit = {
    width = window.innerWidth
    // window.innerHeight-4 : to prevent scrollbars
    height = window.innerHeight-4
    renderer.setSize(width, height)
    camera = newCamera()
    sceneMaker.setSize()
  }



  val stats = js.Dynamic.newInstance(g.Stats)()

  var sceneMaker : SceneMaker = new KissSceneMaker(this)
  def start(): Unit = {
    windowResize()
    val body = jQuery("body")
    body.append(renderer.domElement)
    body.append(stats.dom)

    jQuery(window).resize((event: JQueryEventObject) => {
      windowResize()
    })

    Visualizer.start(render, analyser.audioFrameLength * 1000 / 2)
  }

  def render() = {
    sceneMaker.render()
  }
}

object Visualizer {
  var id : Int = 0
  def animationLoop(render: () => Unit): () => Unit = () => {
    render()
    id = g.window.requestAnimationFrame(animationLoop(render)).asInstanceOf[Int]
  }

  def start(render: () => Unit, interval: Double = 0) = {
    if (interval == 0) animationLoop(render)()
    else {
      id = window.setInterval(render, interval)
    }
  }

}