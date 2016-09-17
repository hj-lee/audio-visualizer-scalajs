package hjlee.visualizer

import scala.scalajs.js
import js.Dynamic.{global => g}

import org.denigma.threejs.{Camera, Vector3}

/**
  * Created by hjlee on 9/14/16.
  */
abstract class SceneMaker(val app: Visualizer) {

  def render()

  def setSize(): Unit = {
    val camera = app.camera
    val width = app.width
    val height = app.height

    val angleX = 0 * Math.PI/180
    val angleY = 0 * Math.PI/180
    val translation = new Vector3(0,0,0)
    val distance = 4.1 * height
    val poi = new Vector3(0, 0, 0)
    camera.position.x = poi.x +
      distance * Math.sin(angleY)
    camera.position.y = poi.y +
      distance * Math.sin(angleX) * Math.cos(angleY)
    camera.position.z = poi.z +
      distance * Math.cos(angleX) * Math.cos(angleY)

    camera.rotation.x = -angleX
    camera.rotation.y = angleY

  }

  def frequencyToIndex(frequency: Double): Double = {
    frequency / app.sampleRate * app.analyser.fftSize
  }
}
