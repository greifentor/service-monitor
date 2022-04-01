#!/bin/bash

echo Setting Service-Monitor project version to: $1

mvn versions:set -DnewVersion=$1
mvn versions:set -DnewVersion=$1

mvn clean install -Dmaven.test.skip=true
