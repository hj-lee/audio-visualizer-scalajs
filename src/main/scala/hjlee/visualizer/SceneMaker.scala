package hjlee.visualizer

import scala.scalajs.js
import js.Dynamic.{global => g}

import org.denigma.threejs.{Camera, Vector3}

/**
  * Created by hjlee on 9/14/16.
  */
abstract class SceneMaker(val app: Visualizer) {

  def render()

  def setSize()
}
