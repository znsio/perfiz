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
* Now you can run the Karate feature we created in step 1 as a Gatling test with below command
```shell script
./run-performance-tests.sh ~/KarateFeatures/googlesearch.feature
```
* The metrics will be visible on Grafana Dashboard
