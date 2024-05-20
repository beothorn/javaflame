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

mkdir outputs/allvalues
java -javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/allvalues -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/onlyTestFiltersCConstructor
java -javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/onlyTestFiltersCConstructor,log:DEBUG,filter:TestFiltersC#new -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/testFiltersCConstructorAndFunctionDEF
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/testFiltersCConstructorAndFunctionDEF,log:DEBUG,filter:TestFiltersC#(new||functionDEF)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/noValuesCaptured
java -javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/noValuesCaptured,log:INFO,no_capturing_values -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/nameContainsTest
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/nameContainsTest,log:INFO,filter:nameContains(Test)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/namedTestFiltersA
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/namedTestFiltersA,filter:named(com.github.beothorn.tests.TestFiltersA)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/notTest
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/notTest,filter:\!Test" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/algoAndRithms
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/algoAndRithms,filter:algo&&rithms" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/algoOrRithms
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/algoOrRithms,filter:algo||arithms" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/TestFilterAFunctionCCC
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/TestFilterAFunctionCCC,filter:TestFiltersA#functionCCC" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/nameContainsOnFunctionCCC
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/nameContainsOnFunctionCCC,filter:nameContains(TestFiltersA)#nameContains(functionCCC)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/nameStartsWithTest
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/nameStartsWithTest,filter:nameStartsWith(com.github.beothorn.tests.Test)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/nameStartsWithTestClass
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/nameStartsWithTestClass,log:INFO,filter:nameStartsWith(Test)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/nameEndsWithA
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/nameEndsWithA,filter:nameEndsWith(A)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/nameMatchesA
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/nameMatchesA,filter:nameMatches(A)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/regexNameMatchesTestFiltersAB
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/regexNameMatchesTestFiltersAB,filter:nameMatches(.*TestFilters[AB].*)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

mkdir outputs/capturesStacktrace
java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/capturesStacktrace,filter:com.github.beothorn.sorts,capture_stacktrace" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

echo INTERCEPTIONS

java "-javaagent:./javaAgent/build/libs/javaAgent.jar=filter:NO_FILTER,intercept:TestFiltersA#functionAAA>com.github.beothorn.tests.Interceptor#interceptConstructor" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

java "-javaagent:./javaAgent/build/libs/javaAgent.jar=filter:NO_FILTER,intercept:TestFiltersA#functionNonStatic>com.github.beothorn.tests.Interceptor#interceptMethod" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

java "-javaagent:./javaAgent/build/libs/javaAgent.jar=filter:NO_FILTER,intercept:Common#swap>com.github.beothorn.tests.Interceptor#interceptMethod" -jar ./javaExampleApp/build/libs/javaExampleApp.jar

java "-javaagent:./javaAgent/build/libs/javaAgent.jar=out:./outputs/notTest,startRecordingTriggerFunction:com.github.beothorn.App#startRecording,stopRecordingTriggerFunction:com.github.beothorn.App#stopRecording" -jar ./javaExampleApp/build/libs/javaExampleApp.jar
