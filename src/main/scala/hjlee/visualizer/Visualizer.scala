package hjlee.visualizer

import org.denigma.threejs._
import org.scalajs.dom.raw.HTMLCanvasElement
import org.scalajs.dom
import dom._
import hjlee.visualizer.control.{CameraControl, KeyControl}
import hjlee.visualizer.jsFacade.Stats

import org.scalajs.dom.ext.KeyCode

import scala.collection.mutable.ArrayBuffer
//import org.scalajs.jquery.{JQueryEventObject, jQuery}

import scala.scalajs.js
//import scala.scalajs.js.Dynamic.{global => g}
import scalatags.JsDom.all._

class Visualizer(stream: js.Dynamic) {

  val analyser = new Analyser(stream)

  var width: Double = window.innerWidth
  var height: Double = window.innerHeight-4

  val cameraControl = new CameraControl(width, height)

  val scene = new Scene()
  val renderer = new WebGLRenderer()

  val keyControl = new KeyControl

  def windowResize(): Unit = {
    width = window.innerWidth
    // window.innerHeight-4 : to prevent scrollbars
    height = window.innerHeight-4
    renderer.setSize(width, height)
    // camera with new aspect
    cameraControl.setSize(width, height)
    // not working
//    camera.asInstanceOf[js.Dynamic].fov = 15
//    camera.asInstanceOf[js.Dynamic].aspect = width/height
    sceneMaker.setSize()
  }


  // SceneMakers need 'stats'
//  val stats = js.Dynamic.newInstance(g.Stats)()
  val stats = new Stats();

  var sceneMaker : SceneMaker = new KissSceneMaker(this)

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
    Visualizer.start(render, analyser.audioFrameLength * 1000 / 2)
  }

  def renderControls(content: Element, canvas: HTMLCanvasElement) = {
    val controlDiv =
      div(
        id := "control-box",
        position := "absolute",
        top := "0px",
        right := "0px",
        //        zIndex := 9000,
        //        display := "-webkit-box",
        //        display := "-moz-box",
        //        display := "-ms-flexbox",
        //        display := "-webkit-flex",
        //        display := "flex",
        color := "green"
      ).render
    content.appendChild(controlDiv)

    val hideControlBtn = button("Hide Controls").render
    controlDiv.appendChild(hideControlBtn)

    val controls = div(id := "controls").render
    controlDiv.appendChild(controls)

    var showingControl = true
    hideControlBtn.onclick = (e: dom.Event) => {
      showingControl = !showingControl
      hideControlBtn.innerHTML = if (showingControl) "Hide Controls" else "Show Controls"
      controls.style.visibility = if (showingControl) "visible" else "hidden"
    }

    controls.appendChild(keyControl.render)

    document.onkeydown = keyControl.onkeydown

    keyControl.addKeyAction(KeyCode.Up, "r up"){
      cameraControl.angleX += 1 * Math.PI/180
      cameraControl.setCamera()
    }
    keyControl.addKeyAction(KeyCode.Down, "r down"){
      cameraControl.angleX -= 1 * Math.PI/180
      cameraControl.setCamera()
    }
    keyControl.addKeyAction(KeyCode.Right, "r right"){
      cameraControl.angleY += 1 * Math.PI/180
      cameraControl.setCamera()
    }
    keyControl.addKeyAction(KeyCode.Left, "r left"){
      cameraControl.angleY -= 1 * Math.PI/180
      cameraControl.setCamera()
    }

  }

  def render(t: Double) = {
    sceneMaker.render()
  }


}

object Visualizer {


  var id : Int = 0
  def animationLoop(render: Double => Unit): Double => Unit = (t: Double) => {
    render(t)
    id = window.requestAnimationFrame(animationLoop(render))
  }

  def start(render: Double => Unit, interval: Double = 0) = {
    if (interval == 0) {
      val fun = animationLoop(render)
      id = window.requestAnimationFrame(fun)
    }
    else {
      id = window.setInterval(() => render(window.performance.now()), interval)
    }
  }
}