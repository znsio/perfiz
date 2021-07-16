---
layout: default
title: Installation and Upgrades
nav_order: 2
---

**Platforms** - Tested on MacOS and Linux. Windows will be supported soon.

## Installation

* **Pre-requisites**
  * Docker >= 20.10.0
  * docker-compose >= 1.29.0
* **Setup Perfiz**
  * Download the latest [Perfiz release zip file](https://github.com/znsio/perfiz/releases) file and unzip to a location of your choice
  * Set ```PERFIZ_HOME``` environment variable and add it to your ```PATH```.

```shell
export PERFIZ_HOME=<path to perfiz dir>
```
    
## Updating Perfiz

Paste below script in a macOS Terminal or Linux shell prompt. 

```shell
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/znsio/perfiz/main/update.sh)"
```

On versions prior to 0.0.9 (check ```$PERFIZ_HOME/.VERSION```), please update manually by deleting the $PERFIZ_HOME folder and download latest version and unzip it.

## Initializing your project with Perfiz

* Run below command inside your Project Root Directory to add Perfiz related files and folders

```shell
$PERFIZ_HOME/perfiz.sh init
```

* Your project directory structure will look something like this

```shell
<your project root dir>/
  <your project files>
  perfiz.yml # perfiz config template
  perfiz/
    gatling/
      gatling.conf # template file
    prometheus/
      prometheus.yml # template file
    dashboards/
      dashboard.json # sample grafana dashboard
```

* Add below line to your .gitingore file to avoid checking in Grafana and Prometheus data.

```shell
perfiz/*_data
```