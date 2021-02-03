#! /bin/sh
mkdir -p .m2
docker run -it --rm --name performance-testing \
  -v "$(pwd)/.m2":/root/.m2 \
  -v "$(pwd)":/usr/src/performance-testing \
  -v "$1":/usr/src/karate-features \
  -e KARATE_FEATURES=/usr/src/karate-features \
  -w /usr/src/performance-testing \
  --network metrics-monitor_default \
  maven:3.6-jdk-8 mvn test-compile gatling:test -DKARATE_FEATURE="$2"