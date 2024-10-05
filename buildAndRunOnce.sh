#!/bin/bash

pushd ./javaExampleApp
rm -rf build
gradle assemble
popd

pushd ./javaAgent
rm -rf build
gradle jarJava11
popd

rm -rf outputs
mkdir outputs

java -javaagent:./javaAgent/build/libs/javaAgent-java11.jar=out:./outputs/allvalues,port:8998 -jar ./javaExampleApp/build/libs/javaExampleApp.jar
