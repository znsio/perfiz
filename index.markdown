---
layout: default
title: Home
nav_order: 1
description: "Perfiz is a Dockerised API Performance Test Setup"
permalink: /
---

# A Dockerised API Performance Test Setup
* Run Gatling Tests (Karate Gatling at the moment) without Java on your local machine or writing any Scala simulations
* Visualize your Gatling Performance Test Metrics and Application Metrics side by side in Grafana in real-time

![Grafana Screenshot](/assets/grafana-test.png)

[Get started now](/documentation/quickstart.html){: .btn .btn-primary .fs-5 .mb-4 .mb-md-0 .mr-2 } [View it on GitHub](//github.com/znsio/perfiz){: .btn .fs-5 .mb-4 .mb-md-0 }

[![CI Build](https://github.com/znsio/perfiz/actions/workflows/CI.yml/badge.svg)](https://github.com/znsio/perfiz/actions/workflows/CI.yml)

## How does it work?

Perfiz enables "Shift-Left" Performance Testing by helping you to identify performance issues early in your development cycle.

It leverages Docker to run a uniform setup on your **local machine**

![Perfiz Architecture](/assets/perfiz-architecture.gif)

and on **higher environments**

![Perfiz in Higher Environments](assets/perfiz-in-higher-environment.gif)

## Developers

To create a release push an annotated tag. Example:
```shell
git tag -a <release version> -m "<release message>"
git push origin <release version>
```