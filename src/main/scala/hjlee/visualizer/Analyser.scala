package hjlee.visualizer

import hjlee.visualizer.jsFacade.KissFFT

import org.scalajs.dom.experimental.mediastream.MediaStream
import org.scalajs.dom.raw.AudioContext

import scala.scalajs.js
import js.typedarray.Float32Array

/**
  * Created by hjlee on 9/14/16.
  */
class Analyser(stream: js.Dynamic) {
  val audioContext = new AudioContext
  val analyser = audioContext.createAnalyser()
  val sampleRate = audioContext.sampleRate
  println("sampleRate: " + sampleRate)
  val source = audioContext.createMediaStreamSource(stream.asInstanceOf[MediaStream])
  source.connect(analyser)

  var minShowingFrequency = 40.0
  var maxShowingFrequency = 16000.0

  val defaultFftSize: Int = calcDefaultFftSize

  private[this] var _audioFrameLength: Double = 0
  def audioFrameLength: Double = _audioFrameLength

  private[this] var _minDrawIndex: Int = 0
  def minDrawIndex: Int = _minDrawIndex

  private[this] var _maxDrawIndex: Int = 0
  def maxDrawIndex: Int = _maxDrawIndex

  private[this] var _timeData : Float32Array = new Float32Array(0)
  // initialize
  fftSize = defaultFftSize

  // methods
  def fftSize = analyser.fftSize

  def fftSize_=(fftSize: Int) = {
    require(fftSize >= Analyser.MIN_FFT_SIZE)
    require(fftSize <= Analyser.MAX_FFT_SIZE)
    require(isPower2(fftSize))

    println("fftsize " + analyser.fftSize)

    analyser.fftSize = fftSize
    analyser.smoothingTimeConstant = 0

    _audioFrameLength = fftSize.asInstanceOf[Double] / sampleRate
    _maxDrawIndex = Math.min(frequencyToIndex(maxShowingFrequency),
      frequencyBinCount-1).asInstanceOf[Int]

    _minDrawIndex = Math.min(frequencyToIndex(minShowingFrequency),
      frequencyBinCount-2).asInstanceOf[Int]

    println("man draw freq: " + maxDrawIndex)
    println("min draw freq: " + minDrawIndex)

    if(_timeData.length != fftSize) {
      _timeData = new Float32Array(fftSize)
    }
  }

  // delegate methods
  def getFloatTimeDomainData(timeData: Float32Array) = analyser.getFloatTimeDomainData(timeData)

  def frequencyBinCount = analyser.frequencyBinCount

  def getFrequencyData() = {
    val kissFft = Analyser.getKiss(fftSize)
    analyser.getFloatTimeDomainData(_timeData)
    kissFft.forward(_timeData).asInstanceOf[Float32Array]
  }

  // helper methods
  private def calcDefaultFftSize: Int = {
    val expFrameRate = sampleRate / 15
    val log2FrameRate = Math.ceil(Math.log(expFrameRate) / Math.log(2))
    println("log2fr " + log2FrameRate)
    val fftSize = Math.pow(2, log2FrameRate).asInstanceOf[Int]
    Math.min(32768, fftSize)
  }

  private def frequencyToIndex(frequency: Double): Double = {
    frequency / sampleRate * fftSize
  }

  private def isPower2(n: Double): Boolean = {
    val log2n = Math.log(n) / Math.log(2)
    // It works for n <= 32768
    log2n == Math.floor(log2n)
  }

}

object Analyser {
  val MIN_FFT_SIZE = 256
  val MAX_FFT_SIZE = 32768

  private var kissMap = scala.collection.mutable.Map[Int, KissFFT]()

  private def getKiss(fftSize: Int) = {
    kissMap.getOrElseUpdate(fftSize, new KissFFT(fftSize))
  }
}