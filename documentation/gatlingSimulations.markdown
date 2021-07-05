---
layout: default
title: Gatling Simulations
parent: Documentation
nav_order: 4
---

### Running Gatling Scala Simulations (5 min)
A majority of load patterns can be described with [Perfiz YAML syntax](/perfiz-config-syntax.markdown).
Also, our preferred approach is to reuse Karate API tests as Gatling Performance Tests instead of writing Scala Simulations from scratch.

However, if you need to author Gatling Scala Simulations for advances use cases like
* Gatling [Meta DSL](https://gatling.io/docs/current/general/simulation_setup/#meta-dsl)
* Karate Gatling [Feeders](https://intuit.github.io/karate/karate-gatling/#feeders)
* OR, you have already invested in Gatling Scala Simulations

Perfiz supports running Gatling Scala Simulations with or without Karate API Tests.

Go to [Perfiz Advanced Demo](https://github.com/znsio/perfiz-demo/tree/scala_simulations_and_feeders#advanced)