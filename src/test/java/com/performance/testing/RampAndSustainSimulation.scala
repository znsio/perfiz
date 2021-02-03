package com.performance.testing

import com.intuit.karate.gatling.PreDef._
import io.gatling.core.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps

class RampAndSustainSimulation extends Simulation {
  val rampAndSustainScenario = scenario("RampAndSustain")
    .exec(karateFeature("classpath:" + System.getProperty("KARATE_FEATURE")))

  val protocol = karateProtocol()

  setUp(
    rampAndSustainScenario.inject(
      rampUsers(3) during (3 seconds),
      constantUsersPerSec(3) during (3 seconds)
    ).protocols(protocol)
  )
}
