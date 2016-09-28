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
  def newCamera() = {
    new PerspectiveCamera(15, width/height, 0.1, 20000)
  }
  var camera: Camera = newCamera()

  val angleXdeg = new CircularVariable(10, 1, -180, 180)
  val angleYdeg = new CircularVariable(0, 1, -180, 180)
  val translation = new Vector3(0,0,0)
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

    camera.translateX(translation.x)
    camera.translateY(translation.y)
    camera.translateZ(translation.z)
  }


  // mouse control
  def attachMouseControl(canvas: Element){
    var startPos: (Double, Double) = (0, 0)
    var down = false
    var baseAngleX = 0.0
    var baseAngleY = 0.0
    var baseX = translation.x
    var baseY = translation.y

    val onmousedown = (e: dom.MouseEvent) => {
      down = true
      startPos = (e.clientX, e.clientY)
      baseAngleX = angleXdeg.get
      baseAngleY = angleYdeg.get
      baseX = translation.x
      baseY = translation.y
    }
    val onmouseup = (e: dom.MouseEvent) => {
      down = false
    }

    val onmousemove = (e: dom.MouseEvent) => {
      if (down) {
        val xDiff = e.clientX - startPos._1
        val yDiff = e.clientY - startPos._2
        if(e.shiftKey) {
          translation.x = baseX - xDiff
          translation.y = baseY + yDiff
        } else {
          angleXdeg.set(baseAngleX + yDiff.asInstanceOf[Int])
          angleYdeg.set(baseAngleY + xDiff.asInstanceOf[Int])
        }
        setCamera()
      }
    }
    val onmousewheel = (e: WheelEvent) => {
      translation.z += e.deltaY
      setCamera()
    }

    canvas.onmousedown = onmousedown
    canvas.onmouseup = onmouseup
    canvas.onmouseleave = onmouseup
    canvas.onmousemove = onmousemove
    canvas.onmousewheel = onmousewheel
  }

  def attachKeyControl(keyControl: KeyControl) = {
    import CameraControl.moveStep
    keyControl.addKeyAction(KeyCode.Up, "r up") {
      this.angleXdeg.inc()
      this.setCamera()
    }
    keyControl.addKeyAction(KeyCode.Down, "r down") {
      this.angleXdeg.dec()
      this.setCamera()
    }
    keyControl.addKeyAction(KeyCode.Right, "r right") {
      this.angleYdeg.inc()
      this.setCamera()
    }
    keyControl.addKeyAction(KeyCode.Left, "r left") {
      this.angleYdeg.dec()
      this.setCamera()
    }
    //
    keyControl.addKeyAction(KeyCode.W, "t up") {
      this.translation.y += moveStep
      this.setCamera()
    }
    keyControl.addKeyAction(KeyCode.S, "t down") {
      this.translation.y -= moveStep
      this.setCamera()
    }
    keyControl.addKeyAction(KeyCode.D, "t right") {
      this.translation.x += moveStep
      this.setCamera()
    }
    keyControl.addKeyAction(KeyCode.A, "t left") {
      this.translation.x -= moveStep
      this.setCamera()
    }
    keyControl.addKeyAction(KeyCode.Q, "t forward") {
      this.translation.z -= moveStep
      this.setCamera()
    }
    keyControl.addKeyAction(KeyCode.Z, "t backward") {
      this.translation.z += moveStep
      this.setCamera()
    }
  }

}

object CameraControl {
  val moveStep: Int = 10
}
