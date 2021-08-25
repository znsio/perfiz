package org.znsio.perfiz

import java.io.{File, FileInputStream}
import java.util.{ArrayList, List}

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.znsio.perfiz.ClosedWorkloadModel._
import org.znsio.perfiz.OpenWorkloadModel._

import scala.beans.BeanProperty
import scala.concurrent.duration.{Duration, FiniteDuration}

import scala.jdk.CollectionConverters._
import scala.language.postfixOps

class PerfizConfiguration {
  @BeanProperty
  var karateFeatures: List[KarateFeature] = new ArrayList[KarateFeature]()
  @BeanProperty
  var karateEnv: String = _
  @BeanProperty
  var karateFeaturesDir: String = _
  @BeanProperty
  var gatlingSimulationsDir: String = _

  def karateFeaturesAsList = {
    karateFeatures.asScala.toList
  }
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

  def openWorkloadModelSteps = loadPattern.asScala.toList.filter(loadPattern => {
    val openModelLoadPatterns = scala.List(NothingFor, AtOnceUsers, RampUsers, ConstantUsersPerSecond, RampUsersPerSecond, HeavisideUsers)
    openModelLoadPatterns.contains(loadPattern.patternType)
  })

  def closedWorkloadModelSteps = loadPattern.asScala.toList.filter(loadPattern => {
    val closedModelLoadPatterns = scala.List(ConstantConcurrentUsers, RampConcurrentUsers)
    closedModelLoadPatterns.contains(loadPattern.patternType)
  })
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