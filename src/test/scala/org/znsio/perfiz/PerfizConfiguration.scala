package org.znsio.perfiz

import com.fasterxml.jackson.annotation.{JsonAnySetter, JsonIgnore, JsonInclude, JsonProperty, JsonPropertyOrder}

import scala.beans.BeanProperty
import java.util.{ArrayList, HashMap, List, Map}

class PerfizConfiguration {
  @JsonProperty("karateFeatures")
  @BeanProperty
  var karateFeatures: List[KarateFeature] = new ArrayList[KarateFeature]()
}

class KarateFeature {
  @JsonProperty("karateFile")
  @BeanProperty
  var karateFile: String = _

  @JsonProperty("gatlingSimulationName")
  @BeanProperty
  var gatlingSimulationName: String = _

  @JsonProperty("loadPattern")
  @BeanProperty
  var loadPattern: List[LoadPattern] = new ArrayList[LoadPattern]()

  @JsonProperty("uriPatterns")
  @BeanProperty
  var uriPatterns: List[String] = new ArrayList[String]()
}

class LoadPattern {
  @JsonProperty("patternType")
  @BeanProperty
  var patternType: String = _

  @JsonProperty("userCount")
  @BeanProperty
  var userCount: String = _

  @JsonProperty("duration")
  @BeanProperty
  var duration: String = _

  @JsonProperty("randomised")
  @BeanProperty
  var randomised: Boolean = _

  @JsonProperty("targetUserCount")
  @BeanProperty
  var targetUserCount: String = _
}