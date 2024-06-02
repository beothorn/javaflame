#!/bin/bash

pushd ./javaExampleApp
rm -rf build
gradle assemble
popd

pushd ./javaAgent
rm -rf build
gradle assemble
popd

rm -rf outputs
mkdir outputs

java -javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/allvalues -jar ./javaExampleApp/build/libs/javaExampleApp.jar
