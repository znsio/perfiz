# Performance Testing Metrics Monitor

## Purpose

One click TIG (Telegraf InfluxDb Grafana) setup that monitors your Gatling Tests and Application metrics
* Docker based, requires no local setup
* Leverages Karate tests through Karate Gatling

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
* Launch Grafana on your browser on localhost:3000
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
  * The above configuration has one karateFeature per Karate Feature file
  * The load pattern that should be run with that file is listed under load pattern
  * You can repeat the karateFeature section as many times as the number of feature files you need run 
* Now you can run the Karate feature we created in step 1 as a Gatling test with below command
```shell script
./run-performance-tests.sh ~/KarateFeatures ~/perfiz.yaml
```
  * The first parameter is the base folder of your Karate Feature Files
  * NOTE: Perfiz will look for the feature files in the above based folder.
  If the feature files are directly under the base folder, then you can mention just the feature file name in the perfiz.yaml as shown above.
  However if you have sub-folders inside the base folder please mention the relative path example ~/KarateFeatures/bing/bingsearch.yaml should be mentioned as bing/bingsearch.yaml in perfiz.yaml
  * The second parameter is the location of the perfiz.yaml file
* The metrics will be visible on Grafana Dashboard
