package hjlee.visualizer.sceneMaker

import hjlee.visualizer.Visualizer
import org.denigma.threejs.{Color, Geometry, Line, LineBasicMaterial, LineMaterial, Vector3}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.scalajs.js.typedarray.Float32Array

/**
  * Created by hjlee on 9/14/16.
  */
class KissSceneMaker(app: Visualizer) extends WaveSceneMaker(app) {
  override val renderFramePerAudioFrame: Double = 2.0

  var lxFactor = width / Math.log(width)

  /////////////////////////////////////////////////////////////////////
  //
  override def setSize(width: Double, height: Double): Unit = {
    super.setSize(width, height)
    lxFactor = width / Math.log(width)
  }

  ////////////////////////////////////////////////////////

  override def idxToX(i: Int): Double = Math.log1p(i)
  //    def idxToX(i: Int): Double = i
  ////  basically same
  //    def idxToX(i: Int): Double = app.analyser.sampleRate / app.analyser.fftSize * (i+1)
  //    def idxToX(i: Int): Double = Math.log(app.analyser.sampleRate / app.analyser.fftSize * (i+1))

  override def getData(): Float32Array = {
    app.analyser.getFrequencyData()
  }

  override def getIndexRange(): (Int, Int) = {
    (app.analyser.minDrawIndex, app.analyser.maxDrawIndex)
  }

  override def getYZ(out: Float32Array, i: Int): (Double, Double) = {
    val (y,z) = (out(i * 2), out(i * 2 + 1))
    //      val y = out(i*2)
    //      val z = out(i*2+1)

    val abs = Math.sqrt(y*y + z*z)
    //      val ly = Math.log1p(abs)*lxFactor/5.0
    val ly = Math.log1p(abs*0.1)*lxFactor/3.0
    //      val ly = abs * 0.15
    //      val ly = Math.sqrt(abs) * 7.0
    (ly, 0)
  }
}
