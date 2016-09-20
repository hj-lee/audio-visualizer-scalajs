package hjlee.visualizer.control

import hjlee.visualizer.Visualizer
import org.denigma.threejs.{Camera, PerspectiveCamera, Vector3}
import org.scalajs.dom
import org.scalajs.dom.WheelEvent

/**
  * Created by hjlee on 9/19/16.
  */
class CameraControl(var width: Double, var height: Double) {
//  var width = app.width
//  var height = app.height

  def newCamera() = {
    new PerspectiveCamera(15, width/height, 0.1, 20000)
  }
  var camera: Camera = newCamera()

  var angleX = 0 * Math.PI/180
  var angleY = 0 * Math.PI/180
  var translation = new Vector3(0,0,0)
  var distance = 4.1 * height
  var poi = new Vector3(0, 0, 0)

  def setSize(width: Double, height: Double) = {
    this.width = width
    this.height = height
    camera = newCamera()
//    angleX = 0 * Math.PI/180
//    angleY = 0 * Math.PI/180
//    translation = new Vector3(0,0,0)
    distance = 4.1 * height
    poi = new Vector3(0, 0, 0)
    setCamera()
  }


  def setCamera() = {
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

  var startPos: (Double, Double) = (0, 0)
  var down = false
  var baseAngleX = 0.0
  var baseAngleY = 0.0
  def onmousedown(e: dom.MouseEvent) = {
    down = true
    startPos = (e.clientX, e.clientY)
    baseAngleX = angleX
    baseAngleY = angleY
  }
  def onmouseup(e: dom.MouseEvent) = {
    down = false
  }

  def onmousemove(e: dom.MouseEvent) = {
    if (down) {
      val xDiff = e.clientX - startPos._1
      val yDiff = e.clientY - startPos._2
      angleX = baseAngleX + yDiff * Math.PI / 180
      angleY = baseAngleY + xDiff * Math.PI / 180
      setCamera()
    }
  }
  def onmousewheel(e: WheelEvent) = {
//    translation.x += e.deltaX
    translation.z += e.deltaY
//    translation.y += e.deltaZ
    setCamera()
  }
}
