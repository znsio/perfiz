package org.znsio.perfiz

import scala.beans.BeanProperty
import scala.concurrent.duration.{Duration, FiniteDuration}

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