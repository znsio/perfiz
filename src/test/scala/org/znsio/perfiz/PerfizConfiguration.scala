package org.znsio.perfiz

import com.fasterxml.jackson.annotation.{JsonAnySetter, JsonIgnore, JsonInclude, JsonProperty, JsonPropertyOrder}

import scala.beans.BeanProperty
import java.util.{ArrayList, HashMap, List, Map}

import scala.concurrent.duration.{Duration, FiniteDuration}

class PerfizConfiguration {
  @BeanProperty
  var karateFeatures: List[KarateFeature] = new ArrayList[KarateFeature]()
  @BeanProperty
  var karateFeaturesDir: String = _
}

class KarateFeature {
  @BeanProperty
  var karateFile: String = _

  @BeanProperty
  var gatlingSimulationName: String = _

  @BeanProperty
  var loadPattern: List[LoadPattern] = new ArrayList[LoadPattern]()

  @BeanProperty
  var uriPatterns: List[String] = new ArrayList[String]()
}

class LoadPattern {
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

object LoadPattern {
  val NothingFor = "nothingFor"
  val AtOnceUsers = "atOnceUsers"
  val RampUsers = "rampUsers"
  val ConstantUsersPerSecond = "constantUsersPerSec"
  val RampUsersPerSecond = "rampUsersPerSec"
  val HeavisideUsers = "heavisideUsers"
}