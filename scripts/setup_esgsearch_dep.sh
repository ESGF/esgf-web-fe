#!/bin/bash

if [ $# -ne 1 ]; then
    echo "need a path to the search jar"
    exit 1
fi

mvn install:install-file \
    -DgroupId=org.esgf.search \
    -DartifactId=org.esgf.search \
    -Dversion=1.0.0.1 -Dpackaging=jar \
    -Dfile=$1

echo "Search jar installed"