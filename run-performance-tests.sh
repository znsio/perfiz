#! /bin/sh
mkdir -p .m2
docker run -it --rm --name performance-testing \
  -v "$(pwd)/.m2":/root/.m2 \
  -v "$(pwd)":/usr/src/performance-testing \
  -v "$1":/usr/src/karate-features \
  -v "$2":/usr/src/perfiz.yaml \
  -e KARATE_FEATURES=/usr/src/karate-features \
  -w /usr/src/performance-testing \
  maven:3.6-jdk-8 mvn clean test-compile gatling:test -DPERFIZ=/usr/src/perfiz.yaml