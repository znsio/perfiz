# Perfiz - A Dockerised Performance Test Setup
* Run Gatling Tests (Karate Gatling at the moment) without Java on your local machine or writing any Scala simulations
* Visualize your Gatling Performance Test Metrics and Application Metrics side by side in Grafana in real-time

Jump to [quick-start](https://github.com/znsio/perfiz#quick-start)

### Under the hood

* Gatling and Karate-Gatling
* Docker and Docker-Compose
* Grafana
* Prometheus Time Series DB
* Hooks for CAdvisor and NodeExporter

### Why

[Gatling](https://gatling.io/) is a capable load testing tool.
Being able to re-use [Karate](https://intuit.github.io/karate/) API tests as Gatling performance tests with [Karate-Gatling](https://github.com/intuit/karate/tree/master/karate-gatling) helps reduce effort in building a Perf Test Suite.

As long term users of the above tools we started seeing some patterns which we can potentially bundle as a re-usable setup.
* **Gatling Scala DSL as YAML** - To leverage Karate scripts in Gatling, we still need to write simulations in [Gatling Scala DSL](https://github.com/intuit/karate/tree/master/karate-gatling#usage). While we like Scala and the Gatling DSL, it can seem like extra effort to non-Scala Devs. Especially if we are not leveraging any of the slightly more advanced features such as Feeders etc. So we came up with a [YAML wrapper](https://github.com/znsio/perfiz#perfiz-yaml-documentation) on Gatling Scala DSL to bypass the Scala Simulation file step while still staying close Gatling vocabulary.
* **Gatling Reports as Live Grafana Dashboards** - [Gatling reports](https://gatling.io/docs/current/general/reports/) are comprehensive. However we often need to plot the Requests Per Second, Response Times, User Metrics etc. with X-Axis as time so that we can plot application metrics on the same time series to identify patterns. Also we sometimes need to monitor the test in realtime and we cannot wait for the report to be published after the test run. We configured Gatling to publish real time monitoring data and setup re-usable Grafana Dashboards to visualize it.
* **Application Performance Metrics and Gatling Metrics Side by Side** - The purpose of a load test is to see how the application behaves as load pattern varies. So we added Prometheus to capture Application Metrics and now we can create Grafana Panels which show both Gatling Metrics and Application Metrics side by side. With Grafana [Shared Tooltip](https://grafana.com/docs/grafana/latest/whatsnew/whats-new-in-v4-1/#shared-tooltip) we can correlate application behavior and load test pattern.
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
    #~/my-perf-tests/karateFeatures/getApiTest.feature
    Feature: Get API Test
      Scenario: Ping
        Given url '<Any URL which returns a 200 and is accessible to you>'
        When method get
        Then status 200
    ```
* Change directory to ```~/my-perf-tests``` and create **perfiz.yml** file with below content
    ```yaml
    karateFeaturesDir: "karateFeatures"
    karateFeatures:
      - karateFile: "getApiTest.feature"
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
* Your Directory Structure should look something like this now
    ```shell script
    ~/my-perf-tests
      perfiz.yaml
      karateFeatures
        getApiTest.feature
    ```
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

### Prometheus Configuration

Create ```<you project root dir>/perfiz/prometheus/prometheus.yml``` and add your scrape configs.

Example:

```yaml
global:
  scrape_interval:     5s
  evaluation_interval: 5s

rule_files:

scrape_configs:
  - job_name: cadvisor
    scrape_interval: 5s
    static_configs:
      - targets:
          - cadvisor:8080
  - job_name: mysql
    scrape_interval: 5s
    static_configs:
      - targets:
          - host.docker.internal:9104
```

You can query data with PromQL on [Prometheus Expression Browser](http://localhost:9090/graph)

Demo Project: [perfiz-demo](https://github.com/znsio/perfiz-demo#prometheus-and-grafana-configuration)

### Grafana Dashboards

* [Official Community Built Dashboards](https://grafana.com/grafana/dashboards)
    * Download and save JSON to ```<your project root dir>/perfiz/dashboards```
    * Perfiz will pick it up at startup and load it into Grafana
    * This way you will also be able to checkin these JSONs to your version control and share it with your team
    * Example: [JVM Dashboard](https://github.com/znsio/perfiz-demo/blob/main/perfiz/dashboards/jvm-dashboard_rev17.json)
* Custom / Modified Dashboards
    * We often have to customize dashboards as per our project context
    * After making these changes save the [JSON Model](https://grafana.com/docs/grafana/latest/dashboards/json-model/) to ```<your project root dir>/perfiz/dashboards```

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
    uriPatterns:
      - "/books/{isbn}" #This helps you define part in the URL that change, Gatling will capture this as a single URL with varius parameters for ISBN
      - "/book/{isbn}/author/{authorId}" #If you are familiar with karate-gatling then this is similar to Karate Protocol - https://intuit.github.io/karate/karate-gatling/#usage
  #You can keep adding more such Karate Files
  # - karateFile: test.feature
  #   gatlingSimulationName: test
  #   loadPattern:
  #     - patternType: "rampUsers"
  #     - userCount: "60"
  #     - duration: "30 minutes"
  #   and so on...
```

### CAdvisor Configuration - Optional

We monitor Perfiz's own container metrics through CAdvisor.
It works well on Mac OS at the moment. We are still testing Windows and Linux.
You can disable the CAdvisor setup in ```$PERFIZ_HOME/docker-compose.yml``` if this is not a priority to you.

## Developers

To create a release push an annotated tag. Example:
```shell script
git tag -a <release version> -m "<release message>"
git push origin <release version>
```
