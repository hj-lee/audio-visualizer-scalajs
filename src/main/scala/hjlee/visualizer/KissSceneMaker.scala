package hjlee.visualizer

import org.denigma.threejs
import threejs.{Camera, Color, Geometry, Line, LineBasicMaterial, Vector3}

import scala.scalajs.js.typedarray.Float32Array
import scala.scalajs.js
import js.Dynamic.{global => g}

/**
  * Created by hjlee on 9/14/16.
  */
class KissSceneMaker(app: Visualizer) extends SceneMaker(app) {
  var preGeo: Option[Geometry] = None
  var preLine: Option[Line] = None

  val mat = new LineBasicMaterial()
  mat.color = new Color(0xffffff)

  val timeData = new Float32Array(app.currentFftSize)
  val kissFft = js.Dynamic.newInstance(g.KissFFT)(app.currentFftSize)


  val maxDrawFreq = Math.min(app.maxShoingFrequency / app.sampleRate * app.analyser.fftSize,
    app.analyser.frequencyBinCount-1)

//  val maxDrawFreq = app.analyser.frequencyBinCount-1
  println("mdf: " + maxDrawFreq)

  setSize()

  var frameCnt = 0

  var unitWidth = app.width / maxDrawFreq
  var lxFactor = app.width / Math.log(app.width)

  override def render(): Unit = {
    preLine.foreach(app.scene.remove(_))
    preGeo.foreach(_.dispose)

    val width = app.width

    app.analyser.fftSize = app.currentFftSize
    app.analyser.getFloatTimeDomainData(timeData)
    val out = kissFft.forward(timeData).asInstanceOf[Float32Array]

    val geometry = new Geometry
    var preX: Double = -1000
    for (i <- 0 to maxDrawFreq.asInstanceOf[Int]) {
      val x = unitWidth * i
      val y = out(i*2)
      val z = out(i*2+1)

      val lx = Math.log1p(x) * lxFactor - app.width/2
      val ly = Math.log1p(Math.sqrt(y*y + z*z))*lxFactor/5.0
      if (x - preX > 0.9) {
        geometry.vertices.push(new Vector3(lx, ly, 0))
        preX = x
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
  }

  override def setSize(): Unit = {
    super.setSize()
    unitWidth = app.width / maxDrawFreq
    lxFactor = app.width / Math.log(app.width)
  }
}
