package org.znsio.perfiz

import com.intuit.karate.gatling.PreDef._
import io.gatling.core.Predef._
import io.gatling.core.controller.inject.open._
import io.gatling.core.structure.PopulationBuilder
import org.znsio.perfiz.ClosedWorkloadModel._
import org.znsio.perfiz.OpenWorkloadModel._

import scala.jdk.CollectionConverters._
import scala.language.postfixOps

class PerfizSimulation extends Simulation {

  private val configuration: PerfizConfiguration = PerfizConfiguration()

  private val builders: List[PopulationBuilder] = configuration.getKarateFeatures().asScala.toList.map(karateFeatureConfig => {
    val openInjectionSteps = karateFeatureConfig.getLoadPattern().asScala.toList.filter(loadPattern => {
      val openModelLoadPatterns = List(NothingFor, AtOnceUsers, RampUsers, ConstantUsersPerSecond, RampUsersPerSecond, HeavisideUsers)
      openModelLoadPatterns.contains(loadPattern.patternType)
    }).map(f = loadPattern => {
      val openInjectionStep = loadPattern.getPatternType() match {
        case NothingFor => nothingFor(loadPattern.durationAsFiniteDuration)
        case AtOnceUsers => atOnceUsers(loadPattern.getUserCount())
        case RampUsers => rampUsers(loadPattern.getUserCount()) during
          loadPattern.durationAsFiniteDuration
        case ConstantUsersPerSecond => constantUsersPerSec(loadPattern.getUserCount()) during
          loadPattern.durationAsFiniteDuration
        case RampUsersPerSecond => rampUsersPerSec(loadPattern.getUserCount()) to
          loadPattern.getTargetUserCount() during
          loadPattern.durationAsFiniteDuration
        case HeavisideUsers => heavisideUsers(loadPattern.getUserCount()) during
          loadPattern.durationAsFiniteDuration
      }

      if (loadPattern.randomised) {
        openInjectionStep match {
          case ConstantRateOpenInjection(_, _) => openInjectionStep.asInstanceOf[ConstantRateOpenInjection] randomized
          case RampRateOpenInjection(_, _, _) => openInjectionStep.asInstanceOf[RampRateOpenInjection] randomized
          case _ => openInjectionStep
        }
      } else openInjectionStep
    })
    val closedInjectionSteps = karateFeatureConfig.getLoadPattern().asScala.toList.filter(loadPattern => {
      val closedModelLoadPatterns = List(ConstantConcurrentUsers, RampConcurrentUsers)
      closedModelLoadPatterns.contains(loadPattern.patternType)
    }).map(f = loadPattern => {
      loadPattern.getPatternType() match {
        case ConstantConcurrentUsers => constantConcurrentUsers(loadPattern.getUserCount()) during
          loadPattern.durationAsFiniteDuration
        case RampConcurrentUsers => rampConcurrentUsers(loadPattern.getUserCount()) to
          loadPattern.getTargetUserCount() during
          loadPattern.durationAsFiniteDuration
      }
    })
    val protocol = karateProtocol(
      karateFeatureConfig.getUriPatterns().asScala.map { uriPattern => uriPattern -> Nil }.toList: _*
    )
    if (!openInjectionSteps.isEmpty) {
      scenario(karateFeatureConfig.getGatlingSimulationName()).
        exec(karateFeature("classpath:" + karateFeatureConfig.getKarateFile())).
        inject(openInjectionSteps).
        protocols(protocol)
    } else {
      scenario(karateFeatureConfig.getGatlingSimulationName()).
        exec(karateFeature("classpath:" + karateFeatureConfig.getKarateFile())).
        inject(closedInjectionSteps).
        protocols(protocol)
    }
  })

  setUp(
    builders
  )

}
