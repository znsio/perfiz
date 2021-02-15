# Performance Testing Metrics Monitor

## Purpose

Grafana visualization for Gatling performance test results with Application Performance Metrics.

Jump to [quick-start](https://github.com/znsio/perfiz#quick-start)

### Under the hood

* Karate-Gatling
* Docker and Docker-Compose
* Grafana
* InfluxDB and Prometheus (More on this later)
* Prometheus / Telegraf

### Why

[Gatling](https://gatling.io/) is a capable load testing tool.
Being able to re-use [Karate](https://intuit.github.io/karate/) API tests as Gatling performance tests with [Karate-Gatling](https://github.com/intuit/karate/tree/master/karate-gatling) is all the more reason to leverage it.

As long term users of the above tools we started seeing some patterns which we can potentially bundle as a re-usable setup.
* **Gatling Scala DSL as YAML** - To leverage Karate scripts in Gatling, we need to write simulations in [Gatling Scala DSL](https://github.com/intuit/karate/tree/master/karate-gatling#usage). While we like Scala, sometime it can be a lot of effort just to reuse some Karate files.
So we came up with a YAML wrapper on Gatling Scala DSL to go from Karate features to load test in just a matter of seconds.
* **Gatling test results as Grafana Dashboards** - [Gatling reports](https://gatling.io/docs/current/general/reports/) are comprehensive. However we often need to plot the Requests Per Second, Response Times, User Metrics etc. with X-Axis as time so that we can plot application metrics on the same time series to identify patterns.
Also we sometimes need to monitor the test in realtime and we cannot wait for the report to be published after the test run. We configured Gatling to publish real time monitoring data and setup re-usable Grafana Dashboards to visualize it.
* **Application Performance Metrics** - The purpose of a load test is to see how the application behaves as load pattern varies. Since we already have Grafana Dashboard reading from a time-series DB for Gatling metrics, we just need to send Application metrics also to this DB.
At the moment we support Telegraf and are working on Prometheus.
* **Containerised Approach** - Perfiz is completely Dockerised and avoids the lengthy setup required to achieve the above setup. We pre-package Grafana with the right data-sources and dashboard panels so that you can concentrate on your actual load test.

## Pre-Requisites
* Docker
* Docker-Compose

## Quick Start
* Create a Karate feature in a location of your choice, Example:
```gherkin
#~/KarateFeatures/googlesearch.feature
Feature: Google Search
  Scenario: Ping
    Given url 'https://google.com'
    When method get
    Then status 200
```
* Clone this repo
* Change directory to metrics-monitor and run docker-compose
```shell script
cd metrics-monitor
docker-compose up -d
cd -
```
* Launch Grafana on your browser on localhost:3000. It may ask you to change the password.
  * UserName - admin
  * Password - admin
* You should be able to see the InfluxDB data source already setup and a default Dashboard that has sample Gatling Panels
* Create a YAML file with below content in a location as per your choice
```yaml
karateFeatures:
  - karateFile: "googlesearch.feature"
    loadPattern:
      - patternType: "rampUsers"
        userCount: "3"
        duration: "3 seconds"
      - patternType: "constantUsersPerSec"
        userCount: "3"
        duration: "3 seconds"
```
  * The above configuration has one karateFeature yaml item per Karate Feature file
  * The load pattern that should be run with that file is listed under it
  * You can repeat the karateFeature section as many times as the number of feature files you need run 
* Now you can run the Karate feature we created in step 1 as a Gatling test with below command
```shell script
./perfiz.sh ~/KarateFeatures ~/perfiz.yaml
```
  * The first parameter is the base folder of your Karate Feature Files.
  * NOTE: Perfiz will look for the feature files in the above based folder.
  If the feature files are directly under the base folder, then you can mention just the feature file name in the perfiz.yaml as shown above.
  However if you have sub-folders inside the base folder please mention the relative path example ~/KarateFeatures/bing/bingsearch.yaml should be mentioned as bing/bingsearch.yaml in perfiz.yaml
  * The second parameter is the location of the perfiz.yaml file
* The metrics will be visible on Grafana Dashboard
