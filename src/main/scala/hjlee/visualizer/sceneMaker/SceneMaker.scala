package hjlee.visualizer.sceneMaker

import hjlee.visualizer.Visualizer

import scala.scalajs.js.Dynamic.{global => g}

/**
  * Created by hjlee on 9/14/16.
  */
abstract class SceneMaker(app: Visualizer) {
  def name: String

  def renderFramePerAudioFrame: Double

  def render()

  var width: Double = 0
  var height: Double = 0

//  setSize(app.width, app.height)

  def setSize(width: Double, height: Double) = {
    this.width = width
    this.height = height
  }

  def clear()
}

object SceneMaker {

}