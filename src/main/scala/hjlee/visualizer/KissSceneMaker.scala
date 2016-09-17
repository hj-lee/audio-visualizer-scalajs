package hjlee.visualizer

import org.denigma.threejs
import threejs.{Camera, Color, Geometry, Line, LineBasicMaterial, Vector3}

import scala.scalajs.js.typedarray.{Float32Array, Uint8Array}
import scala.scalajs.js
import org.scalajs.dom.window
import js.Dynamic.{global => g}

/**
  * Created by hjlee on 9/14/16.
  */
class KissSceneMaker(app: Visualizer) extends SceneMaker(app) {
  var preGeo: Option[Geometry] = None
  var preLine: Option[Line] = None

  val mat = new LineBasicMaterial()
  mat.color = new Color(0xffffff)

  // MARK
  val timeData = new Float32Array(app.currentFftSize)
  val kissFft = js.Dynamic.newInstance(g.KissFFT)(app.currentFftSize)


  val maxDrawFreq = Math.min(frequencyToIndex(app.maxShowingFrequency),
    app.analyser.frequencyBinCount-1)

  val minDrawFreq = Math.min(frequencyToIndex(app.minShowingFrequency), app.analyser.frequencyBinCount-2)

  println("man draw freq: " + maxDrawFreq)
  println("min draw freq: " + minDrawFreq)

  setSize()

  var frameCnt = 0

//  var unitWidth = app.width / maxDrawFreq
  var lxFactor = app.width / Math.log(app.width)

  var prevRender = window.performance.now()
  override def render(): Unit = {
    app.stats.begin()
    preLine.foreach(app.scene.remove(_))
    preGeo.foreach(_.dispose)

    val width = app.width

    app.analyser.fftSize = app.currentFftSize
    app.analyser.getFloatTimeDomainData(timeData)
    val out = kissFft.forward(timeData).asInstanceOf[Float32Array]

    val geometry = new Geometry
    var preLx: Double = -1000

    val minIdx: Int = minDrawFreq.asInstanceOf[Int]
    val maxIdx: Int = maxDrawFreq.asInstanceOf[Int]

    def idxToX(i: Int): Double = Math.log1p(i)

    val minX = idxToX(minIdx)
    val maxX = idxToX(maxIdx)
    val xWidth = maxX - minX
    for (i <- minIdx to maxIdx) {
      val x = idxToX(i)
      val y = out(i*2)
      val z = out(i*2+1)

      val abs = Math.sqrt(y*y + z*z)
//      val ly = Math.log1p(abs)*lxFactor/5.0
      val ly = Math.log1p(abs*0.1)*lxFactor/3.0
//      val ly = abs * 0.2
//      val ly = Math.sqrt(abs) * 7.0

      val lx = ((x - minX) - xWidth/2) * width / xWidth
      if (lx - preLx > 0.9 || i == maxIdx) {

        geometry.vertices.push(new Vector3(lx, ly, 0))
        preLx = lx
      }
    }
    geometry.vertices.push(new Vector3(width/2,0,0))
    geometry.vertices.push(new Vector3(-width/2,0,0))

    val line = new Line(geometry, mat)
    app.scene.add(line)

    app.renderer.render(app.scene, app.camera)

    preGeo = Some(geometry)
    preLine = Some(line)
    frameCnt += 1
    app.stats.end()
  }

  override def setSize(): Unit = {
    super.setSize()
    lxFactor = app.width / Math.log(app.width)
  }
}
