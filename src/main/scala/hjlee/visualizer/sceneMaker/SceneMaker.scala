package hjlee.visualizer.sceneMaker

import hjlee.visualizer.Visualizer

import scala.scalajs.js.Dynamic.{global => g}

/**
  * Created by hjlee on 9/14/16.
  */
abstract class SceneMaker(app: Visualizer) {
  def render()
  def setSize() = {}
}

object SceneMaker {

}