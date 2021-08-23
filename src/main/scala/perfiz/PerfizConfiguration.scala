package org.znsio.perfiz

import java.util.{ArrayList, List}

import scala.beans.BeanProperty
import scala.concurrent.duration.{Duration, FiniteDuration}

class PerfizConfiguration {
  @BeanProperty
  var karateFeatures: List[KarateFeature] = new ArrayList[KarateFeature]()
  @BeanProperty
  var karateEnv: String = _
  @BeanProperty
  var karateFeaturesDir: String = _
  @BeanProperty
  var gatlingSimulationsDir: String = _
}

class KarateFeature {
  @BeanProperty
  var karateFile: String = _

  @BeanProperty
  var gatlingSimulationName: String = _

  @BeanProperty
  var loadPattern: List[OpenLoadPattern] = new ArrayList[OpenLoadPattern]()

  @BeanProperty
  var uriPatterns: List[String] = new ArrayList[String]()
}

class OpenLoadPattern {
  @BeanProperty
  var patternType: String = _

  @BeanProperty
  var userCount: Int = _

  @BeanProperty
  var duration: String = _

  @BeanProperty
  var randomised: Boolean = _

  @BeanProperty
  var targetUserCount: Int = _

  def durationAsFiniteDuration = Duration(duration).asInstanceOf[FiniteDuration]
}

object OpenLoadPattern {
  val NothingFor = "nothingFor"
  val AtOnceUsers = "atOnceUsers"
  val RampUsers = "rampUsers"
  val ConstantUsersPerSecond = "constantUsersPerSec"
  val RampUsersPerSecond = "rampUsersPerSec"
  val HeavisideUsers = "heavisideUsers"
}

object ClosedLoadPattern {
  val ConstantConcurrentUsers = "constantConcurrentUsers"
  val RampConcurrentUsers = "rampConcurrentUsers"
}