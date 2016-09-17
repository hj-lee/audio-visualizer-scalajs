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

import scala.scalajs.js.Function1
import scala.scalajs.js.annotation.JSExport
import scala.util.Random

/**
  * Created by hjlee on 9/14/16.
  */

//@js.native
//object AudioContext extends js.Object

class Visualizer(stream: js.Dynamic) {
  /////////
  // analyzer
  val audioCtx = new AudioContext()
  val analyser = audioCtx.createAnalyser()

  val maxShoingFrequency = 20000.0

  val sampleRate = audioCtx.sampleRate
  println(sampleRate)

  val source = audioCtx.createMediaStreamSource(stream.asInstanceOf[MediaStream])
  source.connect(analyser)


  val defaultFftSize: Int = {
    val expFrameRate = sampleRate / 15
    val log2FrameRate = Math.ceil(Math.log(expFrameRate)/Math.log(2))
    println("log2fr " + log2FrameRate)
    val fftSize = Math.pow(2, log2FrameRate).asInstanceOf[Int]
    Math.min(32768, fftSize)
  }
  var currentFftSize = defaultFftSize
  println("fftsize " + currentFftSize)

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

  analyser.fftSize = currentFftSize
  analyser.smoothingTimeConstant = 0

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
    val audioFrameLength = currentFftSize.asInstanceOf[Double] / sampleRate
    Visualizer.start(render, 0 * audioFrameLength * 1000 / 2)
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