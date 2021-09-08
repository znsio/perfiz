package org.znsio.perfiz

import java.util.{ArrayList, List}

import org.znsio.perfiz.ClosedWorkloadModel.{ConstantConcurrentUsers, RampConcurrentUsers}
import org.znsio.perfiz.OpenWorkloadModel._

import scala.beans.BeanProperty
import scala.jdk.CollectionConverters._
import scala.language.postfixOps

class GatlingScenario {
  @BeanProperty
  var karateFile: String = _

  @deprecated("Will be removed in upcoming version. Replaced by gatlingScenarioName.")
  @BeanProperty
  var gatlingSimulationName: String = _

  @BeanProperty
  var gatlingScenarioName: String = _

  @BeanProperty
  var loadPattern: List[GatlingWorkLoadModelStep] = new ArrayList[GatlingWorkLoadModelStep]()

  @BeanProperty
  var uriPatterns: List[String] = new ArrayList[String]()

  def name(): String = Option(gatlingScenarioName) filterNot (_.isEmpty) getOrElse gatlingSimulationName

  def openWorkloadModelSteps: Seq[GatlingWorkLoadModelStep] = loadPattern.asScala.toList.filter(loadPattern => {
    val openModelLoadPatterns = scala.List(NothingFor, AtOnceUsers, RampUsers, ConstantUsersPerSecond, RampUsersPerSecond, HeavisideUsers)
    openModelLoadPatterns.contains(loadPattern.patternType)
  })

  def closedWorkloadModelSteps: Seq[GatlingWorkLoadModelStep] = loadPattern.asScala.toList.filter(loadPattern => {
    val closedModelLoadPatterns = scala.List(ConstantConcurrentUsers, RampConcurrentUsers)
    closedModelLoadPatterns.contains(loadPattern.patternType)
  })
}