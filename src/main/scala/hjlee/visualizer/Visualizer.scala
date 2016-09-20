package hjlee.visualizer

import org.denigma.threejs._
import org.scalajs.dom.raw.HTMLCanvasElement
import org.scalajs.dom
import dom._
import hjlee.visualizer.jsFacade.Stats
//import org.scalajs.jquery.{JQueryEventObject, jQuery}

import scala.scalajs.js
//import scala.scalajs.js.Dynamic.{global => g}
import scalatags.JsDom.all._




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


  // SceneMakers need 'stats'
//  val stats = js.Dynamic.newInstance(g.Stats)()
  val stats = new Stats();

  var sceneMaker : SceneMaker = new KissSceneMaker(this)




  def start(): Unit = {
    windowResize()

    val content = document.getElementById("content")
    val renderCanvas :HTMLCanvasElement = renderer.domElement
    content.appendChild(renderCanvas)
    stats.dom.setAttribute("id", "stats")

    content.appendChild(stats.dom.asInstanceOf[Element])
    window.onresize =
      (e: Event) => {
        windowResize()
      }
//    jQuery(window).resize((event: JQueryEventObject) => {
//      windowResize()
//    })

    renderControls(content, renderCanvas)

    Visualizer.start(render, analyser.audioFrameLength * 1000 / 2)
  }

  def renderControls(content: Element, canvas: HTMLCanvasElement) = {
    val controlDiv =
      div(
        id := "control",
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
      )(
        div("control")
      ).render
    content.appendChild(controlDiv)

    val buttonA = button("A").render
    buttonA.onclick =
      (e: dom.Event) => { buttonA.innerHTML = "A" + buttonA.innerHTML }

    controlDiv.appendChild(buttonA)
    val diffDiv = div().render
    controlDiv.appendChild(diffDiv)

    var startPos: (Double, Double) = (0, 0)
    var down = false
    canvas.onmousedown = (e: dom.MouseEvent) => {
      down = true
      startPos = (e.clientX, e.clientY)
    }
    canvas.onmouseup = (e: dom.MouseEvent) => {
      down = false
      diffDiv.innerHTML = ""
    }
    canvas.onmouseleave = canvas.onmouseup
//    canvas.onmouseout = canvas.onmouseup

    canvas.onmousemove = (e: dom.MouseEvent) => {
      if (down) {
        val xDiff = e.clientX - startPos._1
        val yDiff = e.clientY - startPos._2
        diffDiv.innerHTML = "(" + xDiff + ", " + yDiff + ")" + e.button + "|" + e.buttons
      }
    }
    canvas.onmousewheel = (e: WheelEvent) => {
      // deltaY
      diffDiv.innerHTML = "" + e.deltaX + "/" + e.deltaY + "/" + e.deltaZ
    }
    canvas.addEventListener("touchstart", (e: TouchEvent) => {
      down = true
      startPos = (e.touches(0).clientX, e.touches(0).clientY)
    })
    canvas.addEventListener("touchend", (e: TouchEvent) => {
      down = false;
    })
    canvas.addEventListener("touchmove", (e: TouchEvent) => {
      val xDiff = e.touches(0).clientX - startPos._1
      val yDiff = e.touches(0).clientY - startPos._2
      diffDiv.innerHTML = "(" + xDiff + ", " + yDiff + ")"
    })
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