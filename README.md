# Javaflame

Simple and easy flame graph for java.  
No servers or open connections, just plug the agent and get the results.  
[Latest release v11.0.0](https://github.com/beothorn/javaflame/releases/download/v11.0.0/javaAgent.jar)

Want to see it in action? [Check out this rendering of some sort algorithms flamegraphs](https://beothorn.github.io/javaflame).

# Screenshots  

![flamegraph detailed](https://github.com/beothorn/javaflame/blob/main/screenshotDetailed.png?raw=true)

## Usage

`java -javaagent:javaAgent.jar -jar yourApp.jar` 

or with arguments:  

`java -javaagent:javaAgent.jar=flags,configuration:value -jar yourApp.jar` 

For example, this will silently output a flame graph including function parameter values and the return.  

`java -javaagent:javaAgent.jar=log:NONE,out:C:/graphs -jar yourApp.jar` 

Anything without exclusions will generate lots of data. Either it will not render or you will need to filter it first.

## Arguments

### Arguments

| Flag | Description | Example |
| --- | --- | --- |
| no_capturing_values | Record only function call, no parameter value or return value will be recorded. This is faster and measures performance more accurately. | `java -javaagent:javaAgent.jar=no_capturing_values -jar yourApp.jar` |
| no_constructor | Will ignore constructors | `java -javaagent:javaAgent.jar=no_constructor -jar yourApp.jar` |
| core_classes | Will include Java core classes. More useful in conjunction with filters to check, for example, network calls. | `java -javaagent:javaAgent.jar=core_classes -jar yourApp.jar` |
| no_snapshots | Dump the stack only when JVM goes down. Beware, this will use a lot of memory! You probably don't want that. | `java -javaagent:javaAgent.jar=no_snapshots -jar yourApp.jar` |
| qualified_functions | Print the qualified function name, ownerClass.functionName | `java -javaagent:javaAgent.jar=qualified_functions -jar yourApp.jar` |
| exclude:qualified.name.part | Will exclude classes which contain the qualified name on them. | `java -javaagent:javaAgent.jar=exclude:com.github.myApp,com.foo.bar -jar yourApp.jar` |
| filter:qualified.name.part | Will instrument only classes that contains this string on their qualified name. You probably want to set this to you app package to avoid out of memory with huge spans. | `java -javaagent:javaAgent.jar=filter:com.github.myApp,com.github.other -jar yourApp.jar` |
| startRecordingTriggerFunction:method | Will start recording the stack only when the function with this name is called. If the name needs the qualified name depends on the qualified_functions flag. Function name is the same as the one that goes on the stack. | `java -javaagent:javaAgent.jar=startRecordingTriggerFunction:MyObject.afterSetup -jar yourApp.jar` |
| stopRecordingTriggerFunction:method | Will stop recording the stack when the function with this name is called. If the name needs the qualified name depends on the qualified_functions flag. Function name is the same as the one that goes on the stack. | `java -javaagent:javaAgent.jar=stopRecordingTriggerFunction:MyObject.afterJobIsDone -jar yourApp.jar` |
| out:path | Specifies the output directory. | `java -javaagent:javaAgent.jar=out:/tmp/flameOut -jar yourApp.jar` |


## Libraries used

https://github.com/spiermar/d3-flame-graph  
https://bytebuddy.net  

## TODO

- agent parameter: snapshot interval
- timestamps on spans
- timestamp filter
- search
