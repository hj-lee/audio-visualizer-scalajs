package hjlee.visualizer.jsFacade

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.typedarray.Float32Array

/**
  * Created by hjlee on 9/19/16.
  */
@JSName("KissFFT")
@js.native
class KissFFT(fftSize: Int) extends js.Object {
  def forward(data: Float32Array): Float32Array = js.native
}
