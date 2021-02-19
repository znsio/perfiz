package org.znsio.perfiz

import java.io.{File, FileInputStream}

import com.intuit.karate.gatling.PreDef._
import io.gatling.core.Predef._
import io.gatling.core.controller.inject.open._
import io.gatling.core.structure.PopulationBuilder
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.znsio.perfiz.LoadPattern.{AtOnceUsers, ConstantUsersPerSecond, HeavisideUsers, NothingFor, RampUsers, RampUsersPerSecond}

import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.language.postfixOps

class PerfizSimulation extends Simulation {

  private val configuration: PerfizConfiguration = new Yaml(new Constructor(classOf[PerfizConfiguration])).load(
    new FileInputStream(new File(System.getProperty("PERFIZ")))
  )

  private val builders: List[PopulationBuilder] = configuration.getKarateFeatures.asScala.toList.map(karateFeatureConfig => {
    val injections = karateFeatureConfig.getLoadPattern.asScala.toList.map(f = loadPattern => {
      val injectionStep = loadPattern.getPatternType match {
        case NothingFor => nothingFor(loadPattern.durationAsFiniteDuration)
        case AtOnceUsers => atOnceUsers(loadPattern.getUserCount)
        case RampUsers => rampUsers(loadPattern.getUserCount) during
          loadPattern.durationAsFiniteDuration
        case ConstantUsersPerSecond => constantUsersPerSec(loadPattern.getUserCount) during
          loadPattern.durationAsFiniteDuration
        case RampUsersPerSecond => rampUsersPerSec(loadPattern.getUserCount) to
          loadPattern.targetUserCount during
          loadPattern.durationAsFiniteDuration
        case HeavisideUsers => heavisideUsers(loadPattern.getUserCount) during
          loadPattern.durationAsFiniteDuration
      }
      if (loadPattern.randomised) {
        injectionStep match {
          case ConstantRateOpenInjection(_, _) => injectionStep.asInstanceOf[ConstantRateOpenInjection] randomized
          case RampRateOpenInjection(_, _, _) => injectionStep.asInstanceOf[RampRateOpenInjection] randomized
          case _ => injectionStep
        }
      } else injectionStep
    })
    val protocol = karateProtocol(
      karateFeatureConfig.uriPatterns.asScala.map { uriPattern => uriPattern -> Nil }: _*
    )
    scenario(karateFeatureConfig.getGatlingSimulationName).
      exec(karateFeature("classpath:" + karateFeatureConfig.getKarateFile)).
      inject(injections).
      protocols(protocol)
  })

  setUp(
    builders
  )

}
