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

  // MARK width and height can change
  var camera = new PerspectiveCamera(15, width/height, 1, width * 15)

  def windowResize(): Unit = {
    width = window.innerWidth
    // window.innerHeight-4 : to prevent scrollbars
    height = window.innerHeight-4
    renderer.setSize(width, height)
    camera = new PerspectiveCamera(15, width/height, 1, width * 15)
    sceneMaker.setSize()
  }

  analyser.fftSize = currentFftSize

  var sceneMaker : SceneMaker = new KissSceneMaker(this)
  def start(): Unit = {
    windowResize()
    jQuery("body").append(renderer.domElement)
    jQuery(window).resize((event: JQueryEventObject) => {
      windowResize()
    })
    Visualizer.start(render)
  }

  def render() = {
//    println("render")
    sceneMaker.render()
  }

  //
  // ignore
  def xx(): Unit = {
    analyser.fftSize = currentFftSize
    val arr = new Uint8Array(analyser.fftSize)
//    val arr = new Uint8Array(analyzer.frequencyBinCount)

    val fa = new Float32Array(arr.length)
    val a = new Array[Float](arr.length)


    (1 to 100).foreach((n)=> {
      analyser.getByteTimeDomainData(arr)
//      analyzer.getByteFrequencyData(arr)
      a.indices.foreach(i => {
        fa(i) = arr(i) - 127.5f
        if (Math.abs(fa(i)) < 0.6) fa(i) = 0f
        else fa(i) /= 256.0f
      })

//      analyzer.getFloatTimeDomainData(fa)

      a.indices.foreach(i => {
        a(i) = fa(i)
      })
      println(a.max)
      println(a.min)
      //    println(arr)
    })
  }
}

//object Visualizer {
//  @JSExport
//  def loop(): Unit =  {
//    window.requestAnimationFrame(loop.asInstanceOf[js.Function1[Any,Any]])
//
//  }
//}

object Visualizer {

  def loop(render: () => Unit): () => Unit = () => {
    g.window.requestAnimationFrame(loop(render))
    render()
  }

  def start(render: () => Unit) = {
    loop(render)()
  }
}