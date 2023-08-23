#!/bin/bash

pushd ./javaExampleApp
rm -rf build
gradle assemble
popd

pushd ./javaAgent
rm -rf build
gradle assemble
popd

pushd ./flameServer
rm -rf build
gradle assemble
popd

java -javaagent:./javaAgent/build/libs/javaAgent.jar -cp ./flameServer/build/libs/flameServer.jar -jar ./javaExampleApp/build/libs/javaExampleApp.jar
