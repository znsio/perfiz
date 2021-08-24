package org.znsio.perfiz

import java.io.{File, FileInputStream}
import java.util.{ArrayList, List}

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

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

object PerfizConfiguration {
  def apply(): PerfizConfiguration = new Yaml(new Constructor(classOf[PerfizConfiguration])).load(
    new FileInputStream(new File(System.getProperty("PERFIZ")))
  )
}

class KarateFeature {
  @BeanProperty
  var karateFile: String = _

  @BeanProperty
  var gatlingSimulationName: String = _

  @BeanProperty
  var loadPattern: List[GatlingWorkLoadModelStep] = new ArrayList[GatlingWorkLoadModelStep]()

  @BeanProperty
  var uriPatterns: List[String] = new ArrayList[String]()
}

class GatlingWorkLoadModelStep {
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

object OpenWorkloadModel {
  val NothingFor = "nothingFor"
  val AtOnceUsers = "atOnceUsers"
  val RampUsers = "rampUsers"
  val ConstantUsersPerSecond = "constantUsersPerSec"
  val RampUsersPerSecond = "rampUsersPerSec"
  val HeavisideUsers = "heavisideUsers"
}

object ClosedWorkloadModel {
  val ConstantConcurrentUsers = "constantConcurrentUsers"
  val RampConcurrentUsers = "rampConcurrentUsers"
}