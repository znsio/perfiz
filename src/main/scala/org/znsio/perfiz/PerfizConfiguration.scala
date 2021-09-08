package org.znsio.perfiz

import java.io.{File, FileInputStream}
import java.util.{ArrayList, List}

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import scala.beans.BeanProperty
import scala.jdk.CollectionConverters._
import scala.language.postfixOps

class PerfizConfiguration {
  @deprecated("Will be removed in upcoming version. Replaced by gatlingScenarios.")
  @BeanProperty
  var karateFeatures: List[GatlingScenario] = new ArrayList[GatlingScenario]()
  @BeanProperty
  var gatlingScenarios: List[GatlingScenario] = new ArrayList[GatlingScenario]()
  @BeanProperty
  var karateEnv: String = _
  @BeanProperty
  var karateFeaturesDir: String = _
  @BeanProperty
  var gatlingSimulationsDir: String = _
  @BeanProperty
  var gatlingSimulationClass: String = _

  def gatlingScenariosAsList: scala.List[GatlingScenario] = {
    karateFeatures.asScala.toList ++ gatlingScenarios.asScala.toList
  }
}

object PerfizConfiguration {
  def apply(): PerfizConfiguration = new Yaml(new Constructor(classOf[PerfizConfiguration])).load(
    new FileInputStream(new File(System.getProperty("PERFIZ")))
  )
}