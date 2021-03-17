#! /bin/sh
if [[ -z "${PERFIZ_HOME}" ]]; then
  echo 'Please set PERFIZ_HOME environment variable. Exiting.';
  exit 1;
fi

if [ $# -ne 1 ]; then
  echo "usage perfiz.sh [start | stop]"
  exit 1
fi

case "$1" in
  (start)
    echo "Starting Perfiz..."
    ;;
  (stop)
    echo "Stopping Perfiz Docker Containers..."
    docker-compose -f "$PERFIZ_HOME/docker-compose.yml" down
    exit 0
    ;;
  (*)
    echo "usage perfiz.sh [start | stop]"
    exit 1
    ;;
esac

PERFIZ_YML=perfiz.yml
if [[ -e "$PERFIZ_YML" ]]; then
  echo "Picking configurations in $PERFIZ_YML..."
else
  echo "$PERFIZ_YML not found. Please see https://github.com/znsio/perfiz for instructins. Exiting."
  exit 1;
fi

GRAFANA_DASHBOARDS_DIRECTORY="$PWD/perfiz/dashboards"
if [[ -d "$GRAFANA_DASHBOARDS_DIRECTORY" ]]; then
  echo "Copying Grafana Dashboard jsons in $GRAFANA_DASHBOARDS_DIRECTORY"
  find "$GRAFANA_DASHBOARDS_DIRECTORY" -name '*.json' -exec cp -prv '{}' "$PERFIZ_HOME/prometheus-metrics-monitor/grafana/dashboards" ';'
fi

echo "Starting Perfiz Docker Containers..."
docker-compose -f "$PERFIZ_HOME/docker-compose.yml" up -d

echo "Navigate to http://localhost:3000 for Grafana"

echo "Starting Gatling Tests..."

PERFIZ_MAVEN_REPO="$PERFIZ_HOME/.m2"
if [[ -d "$PERFIZ_MAVEN_REPO" ]]; then
  echo "$PERFIZ_MAVEN_REPO available. Skipping Maven Dependency Download."
else
  echo "$PERFIZ_MAVEN_REPO does not exist. Maven dependencies will be run downloaded. This may take a while..."
fi

docker run -it --rm --name perfiz-gatling \
  -v "$PERFIZ_MAVEN_REPO":/root/.m2 \
  -v "$PERFIZ_HOME":/usr/src/performance-testing \
  -v "$PWD":/usr/src/karate-features \
  -v "$PWD/$PERFIZ_YML":/usr/src/perfiz.yml \
  -e KARATE_FEATURES=/usr/src/karate-features \
  -w /usr/src/performance-testing \
  --network perfiz-network \
  maven:3.6-jdk-8 mvn clean test-compile gatling:test -DPERFIZ=/usr/src/perfiz.yml