#!/bin/bash

pushd ./javaExampleApp
rm -rf build
gradle assemble
popd

pushd ./javaAgent
rm -rf build
gradle assemble
popd

java -javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp -jar ./javaExampleApp/build/libs/javaExampleApp.jar
