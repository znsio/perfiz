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

mkdir -p .m2

PERFIZ_YML=perfiz.yml
if [[ -e "$PERFIZ_YML" ]]; then
  echo "Picking configurations in $PERFIZ_YML..."
else
  echo "$PERFIZ_YML not found. Please see https://github.com/znsio/perfiz for instructins. Exiting."
  exit 1;
fi

echo "Starting Perfiz Docker Containers..."
docker-compose -f "$PERFIZ_HOME/docker-compose.yml" up -d

echo "Navigate to http://localhost:3000 for Grafana"

echo "Starting Gatling Tests..."

docker run -it --rm --name perfiz-gatling \
  -v "$PERFIZ_HOME/.m2":/root/.m2 \
  -v "$PERFIZ_HOME":/usr/src/performance-testing \
  -v "$PWD":/usr/src/karate-features \
  -v "$PWD/$PERFIZ_YML":/usr/src/perfiz.yml \
  -e KARATE_FEATURES=/usr/src/karate-features \
  -w /usr/src/performance-testing \
  --network perfiz-network \
  maven:3.6-jdk-8 mvn clean test-compile gatling:test -DPERFIZ=/usr/src/perfiz.yml