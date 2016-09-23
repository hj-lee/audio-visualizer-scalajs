package hjlee.visualizer.sceneMaker

import hjlee.visualizer.Visualizer
import org.denigma.threejs.{Color, Geometry, Line, LineBasicMaterial, LineMaterial, Vector3}

import scala.collection.mutable.ArrayBuffer
import scala.scalajs.js.typedarray.Float32Array

/**
  * Created by hjlee on 9/22/16.
  */
class WaveSceneMaker(app: Visualizer) extends SceneMaker(app) {
  override val renderFramePerAudioFrame: Double = 1.0

  val mat = new LineBasicMaterial()
  mat.color = new Color(0xffffff)

  val nOfMaterials = 50
  val prevMats = new Array[LineMaterial](nOfMaterials)

  var nOfObject = 30
  val prevObjects: ArrayBuffer[Line] = ArrayBuffer()
  val zStep: Double = -200
  val keepStep = 1

  val skipXdiff: Double = 0.5

  val center = new Vector3()

  var frameCnt = 0

  prepareMaterials()

  /////////////////////////////////////////////////////////////////////
  //

  def prepareMaterials(): Unit = {
    for (i <- prevMats.indices) {
      val addColor =
        if (i % 2 == 0) {
          Math.floor(256 * 256 * 256 * (1.0 * (nOfMaterials - i) / nOfMaterials))
        } else {
          Math.floor(256 * (1.0 * i / nOfMaterials))
        }
      var c = 256 * 125 + addColor.asInstanceOf[Int]
      val oldMat = new LineBasicMaterial()
      oldMat.color = new Color(c)
      prevMats(i) = oldMat
    }
  }

  ///////////////////////////////////////////////////////////

  def removeOldObjects(): Unit = {
    if (prevObjects.size >= nOfObject) {
      val oldObj = prevObjects(0)
      app.scene.remove(oldObj)
      oldObj.geometry.dispose()
//      oldObj.material.dispose()
      prevObjects.remove(0)
    }
  }


  def processOldObjects(): Unit = {
    if (keepStep <= 1) {
      prevObjects.foreach(line => {
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
    //    prevObjects.indices.foreach(i => {
    //      val line = prevObjects(i)
    //      line.material = prevMats(i % nOfMaterials)
    //    })
  }

  //////////////////////////////////////////////////////////////////////////////
  def idxToX(i: Int): Double = i

  def getData(): Float32Array = {
    app.analyser.getTimeDomainData()
  }

  def getIndexRange(): (Int, Int) = {
    (0, app.analyser.fftSize-1)
  }

  def getYZ(out: Float32Array, i: Int): (Double, Double) = {
    (out(i) * height / 2, 0)
  }
  //
  /**
    * render
    */
  def addDataToScene(out: Float32Array): Unit = {
    removeOldObjects()
    processOldObjects()

    val geometry = new Geometry
    var preLx: Double = -100000

    val (minIdx, maxIdx) = getIndexRange()

    val minX = idxToX(minIdx)
    val maxX = idxToX(maxIdx)
    val xWidth = maxX - minX


    for (i <- minIdx to maxIdx) {
      val x = idxToX(i)
      val (y, z) = getYZ(out, i)

      val lx = ((x - minX) - xWidth / 2) * width / xWidth
      if (lx - preLx > skipXdiff || i == maxIdx) {
        geometry.vertices.push(new Vector3(lx, y, z))
        preLx = lx
      }
    }
    geometry.vertices.push(new Vector3(width / 2, 0 , 0))
    geometry.vertices.push(new Vector3(-width / 2, 0, 0))

    val line = new Line(geometry, mat)
    if(center.x != 0) line.translateX(center.x)
    if(center.y != 0) line.translateY(center.y)
    if(center.z != 0) line.translateZ(center.z)
    app.scene.add(line)

    prevObjects += line
    frameCnt += 1
  }
  override def render(): Unit = {
    app.stats.begin()

    val out: Float32Array = getData()

    addDataToScene(out)

    app.renderer.render(app.scene, app.cameraControl.camera)
    app.stats.end()
  }

  //////////////////////////////////////////////////////////////////////////////
  override def clear(): Unit = {
    prevObjects.foreach(oldObj => {
      app.scene.remove(oldObj)
      oldObj.geometry.dispose()
//      oldObj.material.dispose()
    });
    prevObjects.clear()
  }

  override def finalize(): Unit = {
    super.finalize()
    prevObjects.foreach(line => {
      line.geometry.dispose()
    })
    mat.dispose()
    prevMats.foreach(mat => {
      mat.dispose()
    })
  }
}
