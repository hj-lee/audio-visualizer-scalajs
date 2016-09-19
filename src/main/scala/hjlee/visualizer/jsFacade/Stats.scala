package hjlee.visualizer.jsFacade

import org.scalajs.dom.raw.HTMLDivElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

/**
  * Created by hjlee on 9/19/16.
  */
@JSName("Stats")
@js.native
class Stats extends js.Object {
  def dom: HTMLDivElement = js.native

  def begin(): Unit = js.native
  def end(): Unit = js.native
}
