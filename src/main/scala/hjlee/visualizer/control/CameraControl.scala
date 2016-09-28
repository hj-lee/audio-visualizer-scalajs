package hjlee.visualizer.control

import org.denigma.threejs.{Camera, PerspectiveCamera, Vector3}
import org.scalajs.dom
import org.scalajs.dom.WheelEvent
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html.Element

/**
  * Created by hjlee on 9/19/16.
  */
class CameraControl(var width: Double, var height: Double) {
  import CameraControl.moveStep

  def newCamera() = {
    new PerspectiveCamera(15, width/height, 0.1, 20000)
  }
  var camera: Camera = newCamera()

  val angleXdeg = ControlVariable.newDegreeVar(10)
  val angleYdeg = ControlVariable.newDegreeVar(0)
//  val translation = new Vector3(0,0,0)
  private def _makeTrVariable() = ControlVariable.newSimpleVar(0, moveStep)
  val trX = _makeTrVariable()
  val trY = _makeTrVariable()
  val trZ = _makeTrVariable()

  var distance = 4.1 * height
  val poi = new Vector3(0, 0, 0)

  def setSize(width: Double, height: Double) = {
    this.width = width
    this.height = height
    // easiest way to reset aspect
    camera = newCamera()
    distance = 4.1 * height
    setCamera()
  }


  def setCamera() = {
    val angleX = angleXdeg.get * Math.PI/180
    val angleY = angleYdeg.get * Math.PI/180

    camera.position.x = poi.x +
      distance * Math.sin(angleY)
    camera.position.y = poi.y +
      distance * Math.sin(angleX) * Math.cos(angleY)
    camera.position.z = poi.z +
      distance * Math.cos(angleX) * Math.cos(angleY)

    camera.rotation.x = -angleX
    camera.rotation.y = angleY

    camera.translateX(trX.get)
    camera.translateY(trY.get)
    camera.translateZ(trZ.get)
  }


  // mouse control
  def attachMouseControl(canvas: Element){
    var startPos: (Double, Double) = (0, 0)
    var down = false
    var baseAngleX = 0.0
    var baseAngleY = 0.0
    var baseX = trX.get
    var baseY = trY.get

    val onmousedown = (e: dom.MouseEvent) => {
      down = true
      startPos = (e.clientX, e.clientY)
      baseAngleX = angleXdeg.get
      baseAngleY = angleYdeg.get
      baseX = trX.get
      baseY = trY.get
    }
    val onmouseup = (e: dom.MouseEvent) => {
      down = false
    }

    val onmousemove = (e: dom.MouseEvent) => {
      if (down) {
        val xDiff = e.clientX - startPos._1
        val yDiff = e.clientY - startPos._2
        if(e.shiftKey) {
          trX.set(baseX - xDiff)
          trY.set(baseY + yDiff)
        } else {
          angleXdeg.set(baseAngleX + yDiff.asInstanceOf[Int])
          angleYdeg.set(baseAngleY + xDiff.asInstanceOf[Int])
        }
        setCamera()
      }
    }
    val onmousewheel = (e: WheelEvent) => {
      val sign = Math.signum(e.deltaY)
      trZ.inc(sign.asInstanceOf[Int])
      setCamera()
    }

    canvas.onmousedown = onmousedown
    canvas.onmouseup = onmouseup
    canvas.onmouseleave = onmouseup
    canvas.onmousemove = onmousemove
    canvas.onmousewheel = onmousewheel
  }

  def attachKeyControl(keyControler: KeyControler) = {
//    keyControl.addKeyAction(KeyCode.Up, "r up") {
//      this.angleXdeg.inc()
//      this.setCamera()
//    }
//    keyControl.addKeyAction(KeyCode.Down, "r down") {
//      this.angleXdeg.dec()
//      this.setCamera()
//    }
    keyControler.addKeyContorl(
      angleXdeg.makeKeyControl("Angle X:", KeyCode.Down, "Down", KeyCode.Up, "Up", {this.setCamera()})
    )

    keyControler.addKeyAction(KeyCode.Right, "r right") {
      this.angleYdeg.inc()
      this.setCamera()
    }
    keyControler.addKeyAction(KeyCode.Left, "r left") {
      this.angleYdeg.dec()
      this.setCamera()
    }
    //
    keyControler.addKeyAction(KeyCode.W, "t up") {
      this.trY.inc()
      this.setCamera()
    }
    keyControler.addKeyAction(KeyCode.S, "t down") {
      this.trY.dec()
      this.setCamera()
    }
    keyControler.addKeyAction(KeyCode.D, "t right") {
      this.trX.inc()
      this.setCamera()
    }
    keyControler.addKeyAction(KeyCode.A, "t left") {
      this.trX.dec()
      this.setCamera()
    }
    keyControler.addKeyAction(KeyCode.Q, "t forward") {
      this.trZ.dec()
      this.setCamera()
    }
    keyControler.addKeyAction(KeyCode.Z, "t backward") {
      this.trZ.inc()
      this.setCamera()
    }
  }

}

object CameraControl {
  val moveStep: Int = 10
}
