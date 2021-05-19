# Perfiz - A Dockerised API Performance Test Setup
* Run Gatling Tests (Karate Gatling at the moment) without Java on your local machine or writing any Scala simulations
* Visualize your Gatling Performance Test Metrics and Application Metrics side by side in Grafana in real-time

Jump to [quick-start](https://github.com/znsio/perfiz#quick-start)

### Under the hood

Below are the projects that Perfiz leverages under the hood. Quite literally Perfiz is "Standing on the shoulders of giants".

* Gatling and Karate-Gatling
* Docker and Docker-Compose
* Grafana
* Prometheus Time Series DB
* Hooks for CAdvisor and NodeExporter

### Why?

[Gatling](https://gatling.io/) is a capable load testing tool.
Being able to re-use [Karate](https://intuit.github.io/karate/) API tests as Gatling performance tests with [Karate-Gatling](https://github.com/intuit/karate/tree/master/karate-gatling) helps reduce effort in building a Perf Test Suite.

As long term users of the above tools we started seeing some patterns which we can potentially bundle as a re-usable setup.
* **Gatling Scala DSL as YAML** - To leverage Karate scripts in Gatling, we still need to write simulations in [Gatling Scala DSL](https://github.com/intuit/karate/tree/master/karate-gatling#usage). While we like Scala and the Gatling DSL, it can seem like extra effort to non-Scala Devs. Especially if we are not leveraging any of the slightly more advanced features such as Feeders etc. So we came up with a [YAML wrapper](https://github.com/znsio/perfiz#perfiz-yaml-documentation) on Gatling Scala DSL to bypass the Scala Simulation file step while still staying close Gatling vocabulary.
* **Gatling Reports as Live Grafana Dashboards** - [Gatling reports](https://gatling.io/docs/current/general/reports/) are comprehensive. However these reports are only generated after the test run is completed and do not allow us to deep dive, zoom in and analyse specific points during the test. To solve this, we configured Gatling to publish real time monitoring data and setup re-usable Grafana Dashboards to visualize it.
* **Application Performance Metrics and Gatling Metrics Side by Side** - The purpose of a load test is to see how the application behaves with load patterns. So we added Prometheus to capture Application Metrics and now we can create Grafana Panels which show both Gatling Metrics and Application Metrics side by side. With Grafana [Shared Tooltip](https://grafana.com/docs/grafana/latest/whatsnew/whats-new-in-v4-1/#shared-tooltip) we can correlate application behavior and load test pattern.
* **Containerised Approach** - Perfiz is completely Dockerised to avoid the lengthy process required to setup all of the above tools. We pre-package Grafana with the right data-sources and dashboard panels so that you can concentrate on your actual load test.

### How?

While we are opinionated about what a performance test setup should be, it is up to you to decide how you want to leverage Perfiz.
* **Monitoring Platform** - No Gatling or Karate Tests, no problem. Perfiz can be a "quick to spin up", Performance Monitoring Stack.
* **Performance Testing Learning Platform** - Perf Testing is a lot more than just the tooling and the setup can be quite daunting to people who are new to this area. Developers and Testers can avoid getting stuck in getting their setup right and instead focus on learning how design, run and analyse Performance Tests. Even as an experienced Perf Tester / Tuner, it may help you expereiment quickly and focus on learning about application behavior on your local machine.

In short **use it how you like it**

## Quick Start
Go to [Perfiz Demo](https://github.com/znsio/perfiz-demo) to get your Perf test setup running in less than 5 min

## Practise Exercise
Try [Practise Exercise](https://github.com/znsio/perfiz-demo/tree/practise-exercise) to get a sense of how easy it is integrate Perfiz into your project.

## Platforms
Tested on MacOS and Linux. Windows will be supported soon.

## Detailed Tutorial
This a detailed tutorial where you will be able to setup Perfiz on any of your existing Apps
* **Pre-requisites** - Docker and Docker-Compose
* **Setup Perfiz**
  * Download the latest [Perfiz release zip file](https://github.com/znsio/perfiz/releases) file and unzip to a location of your choice
  * Set ```PERFIZ_HOME``` environment variable and add it to your ```PATH```.
      ```shell script
      export PERFIZ_HOME=<path to perfiz dir>
      ```
  * IMPORTANT: Make sure Docker is running
* **Setting up your Project**
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
  * Change directory to ```~/my-perf-tests``` and run below command.
    ```shell script
    $PERFIZ_HOME/perfiz.sh init
    ```
    This will create a set of default configurations and related folder structure
  * Add below line to your .gitingore file to avoid checking in Grafana and Prometheus data.
    ```
    perfiz/*_data
    ```
  * Update **perfiz.yml** file which was created in above step with below content
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
      perfiz.yml
      karateFeatures
        getApiTest.feature
    ```
* **Starting Perfiz Monitoring Stack**
  * Run below command to start Grafana and Prometheus based stack in Docker
    ```shell script
    $PERFIZ_HOME/perfiz.sh start
    ```
  * Launch Grafana on your browser on localhost:3000. It may ask you to change the password.
    * UserName - admin
    * Password - admin
  * You now have a Performance Testing Monitoring setup running. This includes
    * Prometheus - to gather your application metrics
    * Grafana
      * Pre-configured with Dashboards to monitor your Gatling tests in real-time
      * Pre-configured to the above Prometheus DB as data source
  * You can see the details of the above setup on Docker Dashboard
* **Running your Perf Test**
  * Now you can run the Karate feature we created in step 1 as a Gatling test with below command inside ```~/my-perf-tests```
    ```shell script
    $PERFIZ_HOME/perfiz.sh test
    ```
  * The metrics will be visible in realtime on a pre-configured sample Grafana Dashboard (localhost:3000) called "Perfiz Performance Metrics Monitor"
  * This is a short test that only runs for about 15 seconds, feel free to play around with the load pattern to increase the duration.
  Refer to [Perfiz YAML Configuration](https://github.com/znsio/perfiz#perfiz-yaml-documentation) to understand the above setup in detail.
* **Stop Perfiz** - Run below command to stop all Perfiz Docker Containers
    ```shell script
    $PERFIZ_HOME/perfiz.sh stop
    ```

### Prometheus Configuration

Create / Update ```<you project root dir>/perfiz/prometheus/prometheus.yml``` and add your scrape configs.

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

### Perfiz Configuration YAML Documentation

Perfiz Configuration File is where we define
* Which tests / scenarios to run
* Load Pattern
* URL Patterns
* and more

You can create as many Perfiz Configuration files as you like for each setup. Example:
* Load Test for 15 min in Dev Env
* Soak Test for 2 hours in Staging Env

Example: [perfiz-demo/perfiz-staging-load-test.yml](https://github.com/znsio/perfiz-demo/blob/main/perfiz-staging-load-test.yml)

Each of these files now allow you to codify your tests and check them in for fellow developers.

The default name for the config file is "perfiz.yml". So when you run a Perfiz Command without a config file argument it will look for 'perfiz.yml' and pick it up.

Below are all the parameters in Perfiz Config File. 

```yaml
karateFeaturesDir: #Relative Path from you repo root to the directory containing Karate Feature Files
                   #This is also the directory which contains your karate-config.js, Perfiz will make sure this file gets picked up by Karate Gatling
                   #Example: src/test/karateFeatures
karateEnv: #This sets the karate.env. Provide any env that is defined in your karate-config.js that resides in the above "karateFeaturesDir"
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

**IMPORTANT: Load Patterns**

Because Perfiz Leverages Gatling, it is important that we understand the [Open vs Closed Worlkload Models](https://gatling.io/docs/current/general/simulation_setup/) and **avoid mixing them**.

* Open model keywords
  * nothingFor
  * atOnceUsers
  * rampUsers
  * constantUsersPerSec
  * rampUsersPerSec
  * heavisideUsers
* Closed model keywords - WIP in Perfiz

### CAdvisor Configuration - Optional

We monitor Perfiz's own container metrics through CAdvisor.
It works well on Mac OS at the moment. We are still testing Windows and Linux.
You can disable the CAdvisor setup in ```$PERFIZ_HOME/docker-compose.yml``` if this is not a priority to you.

### Running Gatling Scala Simulations

Our preferred approach to writing a Load test is to leverage API tests. However if you have a compelling reason to write to Scala Simualations instead of leveraging a Karate Feature

and / or

to support advanced usecases such as leveraging Karate Gatling Feeders, refer to this [perfiz-demo](https://github.com/znsio/perfiz-demo/tree/scala_simulations_and_feeders#advanced) branch.

## Developers

To create a release push an annotated tag. Example:
```shell script
git tag -a <release version> -m "<release message>"
git push origin <release version>
```
