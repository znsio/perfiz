---
layout: page
title: About
nav_order: 5
---

# Why Perfiz?

[Gatling](https://gatling.io/) is a capable load testing tool.
Being able to re-use [Karate](https://intuit.github.io/karate/) API tests as Gatling performance tests with [Karate-Gatling](https://github.com/intuit/karate/tree/master/karate-gatling) helps reduce effort in building a Perf Test Suite.

As long term users of the above tools we started seeing some patterns which we can potentially bundle as a re-usable setup.
* **Gatling Scala DSL as YAML** - To leverage Karate scripts in Gatling, we still need to write simulations in [Gatling Scala DSL](https://github.com/intuit/karate/tree/master/karate-gatling#usage). While we like Scala and the Gatling DSL, it can seem like extra effort to non-Scala Devs. Especially if we are not leveraging any of the slightly more advanced features such as Feeders etc. So we came up with a [YAML wrapper](/perfiz-config-syntax.markdown) on Gatling Scala DSL to bypass the Scala Simulation file step while still staying close Gatling vocabulary.
* **Gatling Reports as Live Grafana Dashboards** - [Gatling reports](https://gatling.io/docs/current/general/reports/) are comprehensive. However these reports are only generated after the test run is completed and does not allow us to deep dive, zoom-in and analyse specific points during the test. To solve this, we configured Gatling to publish real time monitoring data and setup re-usable Grafana Dashboards to visualize it.
* **Application Performance Metrics and Gatling Metrics Side by Side** - The purpose of a load test is to see how the application behaves with load patterns. So we added Prometheus to capture Application Metrics and now we can create Grafana Panels which show both Gatling Metrics and Application Metrics side by side. With Grafana [Shared Tooltip](https://grafana.com/docs/grafana/latest/whatsnew/whats-new-in-v4-1/#shared-tooltip) we can correlate application behavior and load test pattern.
* **Containerised Approach** - Perfiz is completely Dockerised to avoid the lengthy process required to setup all of the above tools. We pre-package Grafana with the right data-sources and dashboard panels so that you can concentrate on your actual load test.

# How does Perfiz help?

While we are opinionated about what a performance test setup should be, it is up to you to decide how you want to leverage Perfiz.
* **Monitoring Platform** - No Gatling or Karate Tests, no problem. Perfiz can be a "quick to spin up", Performance Monitoring Stack.
* **Learning Platform** - Perf Testing is a lot more than just tooling. Also setting up the tools can be quite daunting to people who are new to this area. Developers and Testers can avoid getting stuck in getting their setup right and instead focus on learning how to design, run and analyse Performance Tests. Even as an experienced Perf Tester / Tuner, it may help you experiment quickly and focus on learning about application behavior on your local machine.

In short **use it how you like it**

# What it is?

* Embody **“Shift Left”** strategy by enabling Developers to run Performance Tests on their local machines
* **Containerize** the setup so that it is equally easy to run on local machine, lower environments and perf test environments
* **Live Dashboard** that can visualize Load Test metrics and Application metrics together to help correlate load with Application behavior
* **Re-usable** setup that is agnostic to application tech stack
* Requires **no / low code** (config only) to leverage API tests or Specs as Perf Test Suite
* **Pre-configured** setup with Canned Dashboards, Data Source setup, monitoring hooks etc. to help Developers get started quickly

# What it is not?

* Not a replacement for Perf Test Tools. It builds on them.
* Does not replace perf testing skills
  * It just makes your life easy in getting your test runs.
  * You still need to understand load profiles and best practices for running perf tests
  * You still need to design the tests and what you want to learn from it
* While it is opinionated about what makes a good test setup
  * It does not push any specific tools
  * Examples
    * We started InfluxDB and then moved to Prometheus, we can support other time series DBs also
    * We are using Gatling now; however, we may support other tools as we see fit
