package hjlee.visualizer

import org.denigma.threejs._
import org.scalajs.dom.raw.HTMLCanvasElement
import org.scalajs.dom
import dom._
import hjlee.visualizer.control._
import hjlee.visualizer.jsFacade.Stats
import hjlee.visualizer.sceneMaker._
import org.scalajs.dom.ext.KeyCode

import scala.scalajs.js
import scalatags.JsDom.all._


class Visualizer(stream: js.Dynamic) {

  val analyser = new Analyser(stream)

  var width: Double = window.innerWidth
  var height: Double = window.innerHeight-4

  val cameraControl = new CameraControl(width, height)

  val scene = new Scene()
  val renderer = new WebGLRenderer()

  val keyControler = new KeyControler

  def windowResize(): Unit = {
    width = window.innerWidth
    // window.innerHeight-4 : to prevent scrollbars
    height = window.innerHeight-4
    renderer.setSize(width, height)
    cameraControl.setSize(width, height)
    sceneMaker.setSize(width, height)
  }



  // SceneMakers need 'stats'
  val stats = new Stats();

  val sceneMakers = Array(
    new DualSceneMaker(this),
    new WaveSceneMaker(this),
    new KissSceneMaker(this)
  )
  var sceneMakerIdx = 0
//  var sceneMaker : SceneMaker = new KissSceneMaker(this)
//  var sceneMaker : SceneMaker = new WaveSceneMaker(this)
  var sceneMaker: SceneMaker = sceneMakers(sceneMakerIdx)


  def start(): Unit = {
    windowResize()

    val content = document.getElementById("content")
    val canvas :HTMLCanvasElement = renderer.domElement
    content.appendChild(canvas)
    stats.dom.setAttribute("id", "stats")

    content.appendChild(stats.dom.asInstanceOf[Element])
    window.onresize =
      (e: Event) => {
        windowResize()
      }
    cameraControl.attachMouseControl(canvas)
    renderControls(content, canvas)
    startRender()
  }


  def startRender(): Unit = {
    Visualizer.start(render, analyser.audioFrameLength * 1000 / sceneMaker.renderFramePerAudioFrame)
  }

  def renderControls(content: Element, canvas: HTMLCanvasElement) = {
    val controlDiv =
      div(
        id := "control-box",
        position := "absolute",
        top := "0px",
        right := "0px",
        textAlign := "right",
        //        zIndex := 9000,
        //        display := "-webkit-box",
        //        display := "-moz-box",
        //        display := "-ms-flexbox",
        //        display := "-webkit-flex",
        //        display := "flex",
        color := "white"
      ).render
    content.appendChild(controlDiv)

    val hideControlBtn = button("Hide Controls").render
    controlDiv.appendChild(hideControlBtn)

    val controls = div(id := "controls").render
    controlDiv.appendChild(controls)

    var showingControl = true
    hideControlBtn.onclick = (e: Event) => {
      showingControl = !showingControl
      hideControlBtn.innerHTML = if (showingControl) "Hide Controls" else "Show Controls"
      controls.style.visibility = if (showingControl) "visible" else "hidden"
    }

    document.onkeydown = keyControler.onkeydown

    // MARK
    cameraControl.attachKeyControl(keyControler)

    // fftsize
    val fftSizeVar = new LimitedVariable[Int](analyser.fftSize, 1, Analyser.MIN_FFT_SIZE, Analyser.MAX_FFT_SIZE) {
      override def incImpl(): Unit = {
        value *= 2
        _checkBoundary()
      }
      override def decImpl(): Unit = {
        value /= 2
        _checkBoundary()
      }
      // do nothing
      override def setImpl(v: Int): Unit = {}
    }
    fftSizeVar.addObserver{ newSize =>
      val oldSize = analyser.fftSize
      if(oldSize != newSize) {
        analyser.fftSize = newSize
        startRender()
      }
    }


    keyControler.addKeyContorl(fftSizeVar.makeKeyControl("Fft Size:", KeyCode.Num1, "1", KeyCode.Num2, "2"))

    val sceneMakerVar = new CircularInt(upperLimit = sceneMakers.length-1) {
      override protected def _toString(v: Int): String = {
        sceneMakers(v).name
      }
    }

    sceneMakerVar.addObserver { sceneMakerIdx =>
      val newSceneMaker = sceneMakers(sceneMakerIdx)
      newSceneMaker.setSize(width, height)
      sceneMaker.clear()
      sceneMaker = newSceneMaker
      startRender()
    }

    keyControler.addKeyContorl(sceneMakerVar.makeKeyControl("Style:", KeyCode.Num9, "9", KeyCode.Num0, "0"))

    controls.appendChild(keyControler.render)
  }

  def render(t: Double) = {
    sceneMaker.render()
  }
}

object Visualizer {
  var running = false;
  var id : Int = 0
  def animationLoop(render: Double => Unit): Double => Unit = (t: Double) => {
    render(t)
    id = window.requestAnimationFrame(animationLoop(render))
  }

  def start(render: Double => Unit, interval: Double = 0) = {
    if (interval == 0) {
      if (running) {
        window.cancelAnimationFrame(id)
      }
      val fun = animationLoop(render)
      id = window.requestAnimationFrame(fun)
    }
    else {
      if (running) {
        window.clearInterval(id)
      }
      id = window.setInterval(() => render(window.performance.now()), interval)
    }
    running = true
  }
}