# Performance Testing Metrics Monitor

## Purpose

A completely Dockerised Performance Test Setup to
* Run your Gatling Tests (Karate Gatling at the moment) without setting up Java on your local machine or writing any Scala simulations
* Visualize your Gatling Performance Test Metrics and Application Metrics side by side in Grafana in real-time

Jump to [quick-start](https://github.com/znsio/perfiz#quick-start)

### Under the hood

* Gatling and Karate-Gatling
* Docker and Docker-Compose
* Grafana
* InfluxDB and Prometheus (More on this later)
* Prometheus / Telegraf

### Why

[Gatling](https://gatling.io/) is a capable load testing tool.
Being able to re-use [Karate](https://intuit.github.io/karate/) API tests as Gatling performance tests with [Karate-Gatling](https://github.com/intuit/karate/tree/master/karate-gatling) helps reduce the effort to re-write the API test as a Gatling Scenario.

As long term users of the above tools we started seeing some patterns which we can potentially bundle as a re-usable setup.
* **Gatling Scala DSL as YAML** - To leverage Karate scripts in Gatling, we still need to write simulations in [Gatling Scala DSL](https://github.com/intuit/karate/tree/master/karate-gatling#usage). While we like Scala and the Gatling DSL, it sometimes can seem like extra effort to non-Scala Devs. So we came up with a YAML wrapper on Gatling Scala DSL to bypass the Scala Simulation file step while still staying close Gatling vocabulary.
  ```yaml
  karateFeatures:
    - karateFile: "apis/perf/bookpurchase.feature"
      gatlingSimulationName: "AllGet"
      loadPattern:
        - patternType: "constantUsersPerSec"
          userCount: "1"
          duration: "30 seconds"
      uriPatterns:
      - "/api/books/{isbn}"
      - "/api/books/{isbn}/authors"
  ```
* **Gatling test results as Grafana Dashboards** - [Gatling reports](https://gatling.io/docs/current/general/reports/) are comprehensive. However we often need to plot the Requests Per Second, Response Times, User Metrics etc. with X-Axis as time so that we can plot application metrics on the same time series to identify patterns. Also we sometimes need to monitor the test in realtime and we cannot wait for the report to be published after the test run. We configured Gatling to publish real time monitoring data and setup re-usable Grafana Dashboards to visualize it.
* **Application Performance Metrics** - The purpose of a load test is to see how the application behaves as load pattern varies. Since we already have Grafana Dashboard reading from a time-series DB for Gatling metrics, we just need to send Application metrics also to this DB.
At the moment we support Telegraf and are working on Prometheus.
* **Containerised Approach** - Perfiz is completely Dockerised and avoids the lengthy setup required to achieve the above setup. We pre-package Grafana with the right data-sources and dashboard panels so that you can concentrate on your actual load test.

## Quick Start
* **Pre-requisites** - Docker and Docker-Compose
* Create a Karate feature in a location of your choice, Example:
```gherkin
#~/KarateFeatures/googlesearch.feature
Feature: Google Search
  Scenario: Ping
    Given url 'https://google.com'
    When method get
    Then status 200
```
* Download the latest release zip file or clone this repo and ```cd``` into it
* Run docker-compose.
```shell script
docker-compose up -d
```
* Launch Grafana on your browser on localhost:3000. It may ask you to change the password.
  * UserName - admin
  * Password - admin
* You now have a Performance Testing setup running. This includes
  * Gatling - which can run your Karate API tests as Perf Tests
  * Prometheus - to gather your application metrics
  * Grafana
    * Pre-configured with Dashboards to monitor your Gatling tests in real-time
    * Pre-configured to the above Prometheus DB as data source
* Let us quickly run a test and see this in action. Create a YAML file with below content in a location as per your choice
```yaml
karateFeatures:
  - karateFile: "googlesearch.feature"
    loadPattern:
      - patternType: "nothingFor"
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
```
  * The above configuration has one karateFeature yaml item per Karate Feature file
  * The load pattern that should be run with that file is listed under it and it closely resembles Gatling load patterns
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

### Prometheus Configuration

Add your scrape_configs to ```./prometheus-metrics-monitor/prometheus/prometheus.yml```

### CAdvisor Configuration - Optional

We monitor Perfiz's own container metrics through CAdvisor.
It works well on Mac OS at the moment. We are still testing Windows and Linux.
You can disable the CAdvisor setup in ```docker-compose.yml``` if this is not a priority to you.

### Optional

Perfiz also includes InfluxDB. We use this to gather Gatling perf test metrics.

While the preferred method to gather your app metrics in through Prometheus, you can add below lines to your telegraf.conf to push your application metrics to this InfluxDB.
```
[[outputs.influxdb]]
  urls = ["http://localhost:8086"]
  database = "telegraf"
```

## Developers

To create a release push an annotated tag. Example:
```shell script
git tag -a <release version> -m "<release message>"
git push origin <release version>
```
