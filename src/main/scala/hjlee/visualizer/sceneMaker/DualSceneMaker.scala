package hjlee.visualizer.sceneMaker

import hjlee.visualizer.Visualizer

import scala.scalajs.js.typedarray.Float32Array

/**
  * Created by hjlee on 9/22/16.
  */
class DualSceneMaker(app: Visualizer) extends SceneMaker(app) {
//  private[this] var _renderFrame = 2.0
//  override def renderFramePerAudioFrame = _renderFrame

  override def renderFramePerAudioFrame: Double = fftMaker.renderFramePerAudioFrame

  val fftMaker = new KissSceneMaker(app)

  val waveMaker = new WaveSceneMaker(app) {
    override def getIndexRange(): (Int, Int) = (
      (app.analyser.fftSize - app.analyser.fftSize / renderFramePerAudioFrame).asInstanceOf[Int],
      app.analyser.fftSize-1)
  }


  override def render(): Unit = {
    app.stats.begin()

    val (time, fft) = app.analyser.getTimeDomainFrequencyData()

    waveMaker.addDataToScene(time)
    fftMaker.addDataToScene(fft)

    app.renderer.render(app.scene, app.cameraControl.camera)
    app.stats.end()
  }
  override def setSize(width: Double, height: Double): Unit = {
//    super.setSize(width, height)
    waveMaker.setSize(width/2, height/2)
    fftMaker.setSize(width/2,height/2)
    waveMaker.center.x = -width/3.9
    fftMaker.center.x = width/3.9
  }

  override def clear(): Unit = {
    waveMaker.clear()
    fftMaker.clear()
  }
}
