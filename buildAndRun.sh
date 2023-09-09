#!/bin/bash

pushd ./javaExampleApp
rm -rf build
gradle assemble
popd

pushd ./javaAgent
rm -rf build
gradle assemble
popd

java -javaagent:./javaAgent/build/libs/javaAgent.jar=mode:detailed,mode:debug,out:/tmp/flameOut -jar ./javaExampleApp/build/libs/javaExampleApp.jar
