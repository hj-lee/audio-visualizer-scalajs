package hjlee.visualizer

import org.denigma.threejs.{Color, Geometry, Line, LineBasicMaterial, LineMaterial, Vector3}

import scala.collection.mutable

/**
  * Created by hjlee on 9/14/16.
  */
class KissSceneMaker(app: Visualizer) extends SceneMaker(app) {
  val mat = new LineBasicMaterial()
  mat.color = new Color(0xffffff)

  val nOfMaterials = 50
  val prevMats = new Array[LineMaterial](nOfMaterials)
  for (i <- prevMats.indices) {
    val addColor =
      if (i % 2 == 0) {
        Math.floor(256 * 256 * 256 * (1.0*(nOfMaterials - i)/nOfMaterials))
      } else {
        Math.floor(256 * (1.0*i/nOfMaterials))
      }
    var c = 256*125 + addColor.asInstanceOf[Int]
    val oldMat = new LineBasicMaterial()
    oldMat.color = new Color(c)
    prevMats(i) = oldMat
  }

  setSize()

  var frameCnt = 0

  var lxFactor = app.width / Math.log(app.width)

  def idxToX(i: Int): Double = Math.log1p(i)
  //    def idxToX(i: Int): Double = i
  ////  basically same
  //    def idxToX(i: Int): Double = app.analyser.sampleRate / app.analyser.fftSize * (i+1)
  //    def idxToX(i: Int): Double = Math.log(app.analyser.sampleRate / app.analyser.fftSize * (i+1))

  var nOfObject = 30
  val prevObjects: mutable.ArrayBuffer[Line] = mutable.ArrayBuffer()
  val zStep: Double = -200
  val keepStep = 1

  override def render(): Unit = {
    app.stats.begin()
    // remove old objects
    if (prevObjects.size >= nOfObject) {
      val oldObj = prevObjects(0)
      app.scene.remove(oldObj)
      oldObj.geometry.dispose()
      oldObj.material.dispose()
      prevObjects.remove(0)
    }

    // process previous objects
    {
      if (keepStep <= 1) {
        prevObjects.indices.foreach(i => {
          val line = prevObjects(i)
          line.translateZ(zStep)
        })
      }
      else {
        val cnt = frameCnt % keepStep
        val start = Math.max(prevObjects.size - cnt, 0)
        (start until prevObjects.size).foreach(i => {
          val line = prevObjects(i)
          line.translateZ(zStep)
        })
        if (frameCnt % keepStep == 0 /* && prevObjects.size >= keepStep */ ) {
          (0 until prevObjects.size).foreach(i => {
            val line = prevObjects(i)
            line.translateZ(zStep * (keepStep + 3))
          })
        }
      }
      if (prevObjects.size > 0) {
        val line = prevObjects.last
        line.material = prevMats(frameCnt % nOfMaterials)
      }
//      prevObjects.indices.foreach(i => {
//        val line = prevObjects(i)
//        line.material = prevMats(i % nOfMaterials)
//      })
    }

    val width = app.width

    val out = app.analyser.getFrequencyData()

    val geometry = new Geometry
    var preLx: Double = -100000

    val minIdx: Int = app.analyser.minDrawIndex
    val maxIdx: Int = app.analyser.maxDrawIndex

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
//      val ly = abs * 0.15
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

    app.renderer.render(app.scene, app.cameraControl.camera)

    prevObjects += line
    frameCnt += 1
    app.stats.end()
  }

  def setSize(): Unit = {
    lxFactor = app.width / Math.log(app.width)
  }
}
