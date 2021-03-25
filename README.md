# Perfiz - A Dockerised Performance Test Setup
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
* **Gatling Scala DSL as YAML** - To leverage Karate scripts in Gatling, we still need to write simulations in [Gatling Scala DSL](https://github.com/intuit/karate/tree/master/karate-gatling#usage). While we like Scala and the Gatling DSL, it sometimes can seem like extra effort to non-Scala Devs.
So we came up with a [YAML wrapper](https://github.com/znsio/perfiz#perfiz-yaml-documentation) on Gatling Scala DSL to bypass the Scala Simulation file step while still staying close Gatling vocabulary.
* **Gatling test results as Grafana Dashboards** - [Gatling reports](https://gatling.io/docs/current/general/reports/) are comprehensive. However we often need to plot the Requests Per Second, Response Times, User Metrics etc. with X-Axis as time so that we can plot application metrics on the same time series to identify patterns. Also we sometimes need to monitor the test in realtime and we cannot wait for the report to be published after the test run. We configured Gatling to publish real time monitoring data and setup re-usable Grafana Dashboards to visualize it.
* **Application Performance Metrics** - The purpose of a load test is to see how the application behaves as load pattern varies. Since we already have Grafana Dashboard reading from a time-series DB for Gatling metrics, we just need to send Application metrics also to this DB.
At the moment we support Telegraf and are working on Prometheus.
* **Containerised Approach** - Perfiz is completely Dockerised and avoids the lengthy setup required to achieve the above setup. We pre-package Grafana with the right data-sources and dashboard panels so that you can concentrate on your actual load test.

## Quick Start
Go to [Perfiz Demo](https://github.com/znsio/perfiz-demo) to get your Perf test setup running in less than 5 min

## Platforms
Tested on MacOS and Linux. Windows will be supported soon.

## Detailed Tutorial
This a detailed tutorial where you will be able to setup Perfiz on any of your existing Apps
* **Pre-requisites** - Docker and Docker-Compose
* Create a Karate feature inside your project directory ([Karate Syntax Reference](https://github.com/intuit/karate)), Example:
In the example below I am keeping my perf test code inside ```~/my-perf-tests``` and Karate API tests inside ```karateFeatures``` directory within the project
```gherkin
#~/my-perf-tests/karateFeatures/googlesearch.feature
Feature: Google Search
  Scenario: Ping
    Given url 'https://google.com'
    When method get
    Then status 200
```
* Change directory to ```~/my-perf-tests``` and create **perfiz.yml** file with below content
```yaml
karateFeaturesDir: "karateFeatures"
karateFeatures:
  - karateFile: "googlesearch.feature"
    gatlingSimulationName: "My Simulation"
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
  * In karateFile property, path to feature file should be relative to the ```perfiz.yml``` file
  * Gatling records related metrics under gatlingSimulationName, which you will be able to visualize in Grafana 
  * The load pattern that should be run with that file is listed under it and it closely resembles [Gatling load patterns](https://gatling.io/docs/current/general/simulation_setup/)
  * You can repeat the karateFeature section as many times as the number of feature files you need run 
* Download the latest [Perfiz release zip file](https://github.com/znsio/perfiz/releases) file and unzip to a location of your choice
* Set ```PERFIZ_HOME``` environment variable and add it to your ```PATH```.
```shell script
export PERFIZ_HOME=<path to perfiz dir>
```
* IMPORTANT: Make sure Docker is running
* Now you can run the Karate feature we created in step 1 as a Gatling test with below command inside ```~/my-perf-tests```
```shell script
$PERFIZ_HOME/perfiz.sh start
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
* The metrics will be visible on Grafana Dashboard
* This is a short test that only runs for about 15 seconds, feel free to play around with the load pattern to increase the duration.
Refer to [Perfiz YAML Configuration](https://github.com/znsio/perfiz#perfiz-yaml-documentation) to understand the above setup in detail.
* Run below command to stop all perfiz Docker Containers
```shell script
$PERFIZ_HOME/perfiz.sh stop
```

### Perfiz YAML Documentation

```yaml
karateFeaturesDir: #Relative Path from you repo root to the directory containing Karate Feature Files
                   #This is also the directory which contains your karate-config.js, Perfiz will make sure this file gets picked up by Karate Gatling
                   #Example: src/test/karateFeatures
karateFeatures: #List of KarateFeatures which need to be run as Load Tests
  - karateFile: #Relative Path from the above karateFeaturesDir to a specific Karate Feature file
                #Example: bookings/movies/reservation.feature, if your overall directory structure is <repo-root>/src/test/karateFeatures/bookings/movies/reservation.feature
    gatlingSimulationName: #A name under which Gatling will aggregate test metrics, prefer a short descriptive name without spaces
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
  #You can keep adding more such Karate Files
  # - karateFile: test.feature
  #   gatlingSimulationName: test
  #   loadPattern:
  #     - patternType: "rampUsers"
  #     - userCount: "60"
  #     - duration: "30 minutes"
  #   and so on...
```

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
