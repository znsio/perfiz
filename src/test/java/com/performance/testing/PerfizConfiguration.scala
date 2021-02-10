package com.performance.testing

import com.fasterxml.jackson.annotation.{JsonAnySetter, JsonIgnore, JsonInclude, JsonProperty, JsonPropertyOrder}

import scala.beans.BeanProperty
import java.util.{ArrayList, HashMap, List, Map}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(Array("karateFeatures"))
class PerfizConfiguration {
  @JsonProperty("karateFeatures")
  @BeanProperty
  var karateFeatures: List[KarateFeature] = new ArrayList[KarateFeature]()

  @JsonIgnore
  @BeanProperty
  var additionalProperties: Map[String, Any] = new HashMap[String, Any]()

  @JsonAnySetter
  def setAdditionalProperty(name: String, value: AnyRef) {
    this.additionalProperties.put(name, value)
  }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(Array("karateFile", "loadPattern", "uriPatterns"))
class KarateFeature {

  @JsonProperty("karateFile")
  @BeanProperty
  var karateFile: String = _

  @JsonProperty("loadPattern")
  @BeanProperty
  var loadPattern: List[LoadPattern] = new ArrayList[LoadPattern]()

  @JsonProperty("uriPatterns")
  @BeanProperty
  var uriPatterns: List[String] = new ArrayList[String]()

  @JsonIgnore
  @BeanProperty
  var additionalProperties: Map[String, Any] = new HashMap[String, Any]()

  @JsonAnySetter
  def setAdditionalProperty(name: String, value: AnyRef) {
    this.additionalProperties.put(name, value)
  }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(Array("patternType", "userCount", "duration"))
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

  @JsonIgnore
  @BeanProperty
  var additionalProperties: Map[String, Any] = new HashMap[String, Any]()

  @JsonAnySetter
  def setAdditionalProperty(name: String, value: AnyRef) {
    this.additionalProperties.put(name, value)
  }
}