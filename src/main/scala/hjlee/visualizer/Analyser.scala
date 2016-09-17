package hjlee.visualizer

import org.scalajs.dom.experimental.mediastream.MediaStream
import org.scalajs.dom.raw.AudioContext

import scala.scalajs.js
import scala.scalajs.js.typedarray.Float32Array

/**
  * Created by hjlee on 9/14/16.
  */


class Analyser(stream: js.Dynamic) {
  def getFloatTimeDomainData(timeData: Float32Array) = analyser.getFloatTimeDomainData(timeData)

  val audioContext = new AudioContext
  val analyser = audioContext.createAnalyser()
  val sampleRate = audioContext.sampleRate
  val source = audioContext.createMediaStreamSource(stream.asInstanceOf[MediaStream])
  source.connect(analyser)

  var minShowingFrequency = 80.0
  var maxShowingFrequency = 12000.0

  val defaultFftSize: Int = {
    val expFrameRate = sampleRate / 15
    val log2FrameRate = Math.ceil(Math.log(expFrameRate)/Math.log(2))
    println("log2fr " + log2FrameRate)
    val fftSize = Math.pow(2, log2FrameRate).asInstanceOf[Int]
    Math.min(32768, fftSize)
  }

  private[this] var _fftSize: Int = 0

  private[this] var _audioFrameLength: Double = 0
  def audioFrameLength: Double = _audioFrameLength

  private[this] var _minDrawIndex: Int = 0
  def minDrawIndex: Int = _minDrawIndex

  private[this] var _maxDrawIndex: Int = 0
  def maxDrawIndex: Int = _maxDrawIndex


  // initialize
  fftSize = defaultFftSize

  // methods
  def fftSize = _fftSize
  def fftSize_=(size: Int) = {
    _fftSize = size
    println("fftsize " + _fftSize)

    analyser.fftSize = _fftSize
    analyser.smoothingTimeConstant = 0

    _audioFrameLength = _fftSize.asInstanceOf[Double] / sampleRate
    _maxDrawIndex = Math.min(frequencyToIndex(maxShowingFrequency),
      frequencyBinCount-1).asInstanceOf[Int]

    _minDrawIndex = Math.min(frequencyToIndex(minShowingFrequency),
      frequencyBinCount-2).asInstanceOf[Int]

    println("man draw freq: " + maxDrawIndex)
    println("min draw freq: " + minDrawIndex)
  }

  def frequencyBinCount = analyser.frequencyBinCount

  def frequencyToIndex(frequency: Double): Double = {
    frequency / sampleRate * fftSize
  }
}
