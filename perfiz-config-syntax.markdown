---
layout: default
title: Perfiz Config YAML Syntax
nav_order: 3
---

# Perfiz Config YAML Syntax

Perfiz Configuration File is where we define
* Which tests / scenarios to run
* Load Pattern
* URL Patterns
* and more

Each Perfiz Config file is translated to a single Gatling Simulation. Within each simulation you can include multiple karate feature files as separate scenarios.

Below are all the parameters in Perfiz Config File.

```yaml
karateFeaturesDir: #Relative Path from you repo root to the directory containing Karate Feature Files
                   #This is also the directory which contains your karate-config.js, Perfiz will make sure this file gets picked up by Karate Gatling
                   #Example: src/test/karateFeatures
karateEnv: #This sets the karate.env. Provide any env that is defined in your karate-config.js that resides in the above "karateFeaturesDir"
gatlingScenarios: #List of KarateFeatures which need to be run as Load Tests
  - karateFile: #Relative Path from the above karateFeaturesDir to a specific Karate Feature file
                #Example: bookings/movies/reservation.feature, if your overall directory structure is <repo-root>/src/test/karateFeatures/bookings/movies/reservation.feature
    gatlingScenarioName: #A name under which Gatling will aggregate test metrics, prefer a short descriptive name without spaces
                           #You will be able to access this in Grafana under the same name through InfluxDB DataSource
    loadPattern: #List of loads in the order that they need to be generated
      - patternType: #Modelled closely after Gatling - https://gatling.io/docs/current/general/simulation_setup/
                     #IMPORTANT: Please make sure to not mix up Open and Closed models as mentioned in above Gatling Documentation
                     #Example: "nothingFor"
                     #More Examples below
        duration: "3 seconds"
      - patternType: "rampUsers"
        userCount: "3"
        duration: "3 seconds"
      - patternType: "constantUsersPerSec"
        userCount: "3"
        duration: "3 seconds"
        randomised: "false"
      - patternType: "rampUsersPerSec"
        userCount: "3"
        targetUserCount: "6"
        duration: "3 seconds"
        randomised: "true"
    uriPatterns:
      - "/books/{isbn}" #This helps you define part in the URL that change, Gatling will capture this as a single URL with varius parameters for ISBN
      - "/book/{isbn}/author/{authorId}" #If you are familiar with karate-gatling then this is similar to Karate Protocol - https://intuit.github.io/karate/karate-gatling/#usage
  #You can keep adding more such Karate Files
  # - karateFile: test.feature
  #   gatlingScenarioName: test
  #   loadPattern:
  #     - patternType: "rampUsers"
  #     - userCount: "60"
  #     - duration: "30 minutes"
  #   and so on...
```

## Deprecations

As of [release 0.0.32](https://github.com/znsio/perfiz/releases/tag/0.0.32), below config params as deprecated. Please updated with latest config params.
* ```karateFeatures``` renamed to ```gatlingScenarios```
* ```gatlingSimulationName``` renamed to ```gatlingScenarioName```

## **IMPORTANT:** Load Patterns

Because Perfiz Leverages Gatling, it is important that we understand the [Open vs Closed Worlkload Models](https://gatling.io/docs/current/general/simulation_setup/) and **avoid mixing them**.

* Open model keywords
  * nothingFor
  * atOnceUsers
  * rampUsers
  * constantUsersPerSec
  * rampUsersPerSec
  * heavisideUsers
* Closed model keywords
  * constantConcurrentUsers
  * rampConcurrentUsers

## Creating multiple configurations

You can create as many Perfiz Configuration files as you like for each setup. Example:
* Load Test for 15 min in Dev Env
* Soak Test for 2 hours in Staging Env

**Examples:**
* [perfiz-demo/perfiz-staging-load-test.yml](https://github.com/znsio/perfiz-demo/blob/main/perfiz-staging-load-test.yml)
* [perfiz-demo/perfiz-closed-loadpattern.yml](https://github.com/znsio/perfiz-demo/blob/main/perfiz-closed-loadpattern.yml)

Each of these files now allow you to codify your tests and check them in for fellow developers.

The default name for the config file is "perfiz.yml". So when you run a Perfiz Command without a config file argument it will look for 'perfiz.yml' and pick it up.