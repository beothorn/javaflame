# Javaflame

Simple and easy flame graph for java.  
No servers or open connections, just plug the agent and get the results.  
[Latest release v10.0.0](https://github.com/beothorn/javaflame/releases/download/v10.0.0/javaAgent.jar)

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

### Flags

- no_capturing_values Record only function call, no parameter value or return value will be recorded.  
This is slower but a great view for debugging.
Example: `java -javaagent:javaAgent.jar=no_capturing_values -jar yourApp.jar`
- no_constructor Will ignore constructors
Example: `java -javaagent:javaAgent.jar=no_constructor -jar yourApp.jar`
- core_classes Will include java core classes. More useful in conjunction with filters to check, for example, network calls.
Example: `java -javaagent:javaAgent.jar=core_classes -jar yourApp.jar`
- no_snapshots Dump the stack only when JVM goes down. Beware, this will use a lot of memory! You probably don't want that.
- qualified_functions Print the qualified function name, ownerClass.functionName

### Configurations

- log:LEVEL Specifies the log level. Available levels in order: NONE,ERROR,INFO,WARN,DEBUG
Example: `java -javaagent:javaAgent.jar=log:NONE -jar yourApp.jar`
- exclude:qualifed.name.part Will exclude classes which contain the qualified name on them.
Example: `java -javaagent:javaAgent.jar=exclude:com.github.myApp -jar yourApp.jar`
- filter:qualified.name.part Will instrument only classes that contains this string on their qualified name. You probably want to set this to you app package to avoid out of memory with huge spans.
Example: `java -javaagent:javaAgent.jar=exclude:com.github.myApp -jar yourApp.jar`
- out:path Specifies the output directory. 
Example: `java -javaagent:javaAgent.jar=out:/tmp/flameOut -jar yourApp.jar`

## Libraries used

https://github.com/spiermar/d3-flame-graph  
https://bytebuddy.net  

## TODO

- agent parameter: snapshot interval
- timestamps on spans
- timestamp filter
- search
