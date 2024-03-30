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
#java -javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,no_capturing_values -jar ./javaExampleApp/build/libs/javaExampleApp.jar
#java -javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,filter:nameContains(Test) -jar ./javaExampleApp/build/libs/javaExampleApp.jar
#java "-javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,filter:named(com.github.beothorn.tests.TestFiltersA)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar
#java -javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,filter:\!Test-jar ./javaExampleApp/build/libs/javaExampleApp.jar
#java "-javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,filter:algo&&rithms" -jar ./javaExampleApp/build/libs/javaExampleApp.jar
#java "-javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,filter:algo||arithms" -jar ./javaExampleApp/build/libs/javaExampleApp.jar
#java "-javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,filter:TestFiltersA#functionCCC" -jar ./javaExampleApp/build/libs/javaExampleApp.jar
#java "-javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,filter:nameContains(TestFiltersA)#nameContains(functionCCC)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar
#java "-javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,filter:nameStartsWith(com.github.beothorn.tests.Test)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar
#java "-javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,filter:nameStartsWith(Test)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar
#java "-javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,filter:nameEndsWith(A)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar
#java "-javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,filter:nameMatche(A)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar
#java "-javaagent:./javaAgent/build/libs/javaAgent.jar=log:INFO,out:/tmp,filter:nameMatches(.*TestFilters[AB].*)" -jar ./javaExampleApp/build/libs/javaExampleApp.jar