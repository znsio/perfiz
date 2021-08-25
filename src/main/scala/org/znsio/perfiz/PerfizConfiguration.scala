package org.znsio.perfiz

import java.io.{File, FileInputStream}
import java.util.{ArrayList, List}

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import scala.beans.BeanProperty
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