package org.znsio.perfiz

import org.scalatest.freespec.AnyFreeSpec

import scala.concurrent.duration.{Duration, FiniteDuration}

class GatlingWorkLoadModelStepSpec extends AnyFreeSpec {
  "given a gatling workload model step" - {

    "when duration is set in minutes" - {
      val gatlingWorkLoadModelStep = new GatlingWorkLoadModelStep
      val twoMinutesString = "2 minutes"
      gatlingWorkLoadModelStep.duration = twoMinutesString

      "should convert to Finite Duration" in {
        val twoMinutes = Duration(twoMinutesString).asInstanceOf[FiniteDuration]
        assert(gatlingWorkLoadModelStep.durationAsFiniteDuration.equals(twoMinutes))
      }
    }

    "when duration is set in seconds" - {
      val gatlingWorkLoadModelStep = new GatlingWorkLoadModelStep
      val oneSecondString = "1 second"
      gatlingWorkLoadModelStep.duration =  oneSecondString

      "should convert to Finite Duration" in {
        val oneSecond = Duration(oneSecondString).asInstanceOf[FiniteDuration]
        assert(gatlingWorkLoadModelStep.durationAsFiniteDuration.equals(oneSecond))
      }
    }
  }
}
