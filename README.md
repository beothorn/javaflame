# javaflame

Simple and easy flame graph for java.  
No servers or open connections, just plug the agent and get the results.  
[Latest release v6.0.0](https://github.com/beothorn/javaflame/releases/download/v6.0.0/javaAgent.jar)

## Usage

`java -javaagent:javaAgent.jar -jar yourApp.jar` 

or with arguments:  

`java -javaagent:javaAgent.jar=flags,configuration:value -jar yourApp.jar` 

For example, this will silently output a flame graph including values.  

`java -javaagent:javaAgent.jar=detailed,log:NONE,out:C:/graphs -jar yourApp.jar` 

Anything without exclusions will generate lots of data. Either it will not render or you will need to filter it first.

## Arguments

### Flags

- detailed Specifies detailed mode, all parameter values will be recorded with a toString() call.  
This is slower but a great view for debugging.
Example: `java -javaagent:javaAgent.jar=detailed,out:/tmp/flameOut -jar yourApp.jar`
- no_constructor Will ignore constructors
- core_classes Will include java core classes. More useful in conjunction with filters to check, for example, network calls.

### Configurations

- log:LEVEL Specifies the log level. Available levels in order: NONE,ERROR,INFO,WARN,DEBUG
- exclude:qualifed.name.part Will exclude classes which contain the qualified name on them.
- filter:qualified.name.part Will instrument only classes that contains this string on their qualified name. You probably want to set this to you app package to avoid out of memory with huge spans.
- out:path Specifies the output directory. Example: `java -javaagent:javaAgent.jar=out:/tmp/flameOut -jar yourApp.jar`

# Screenshots

Performance:  

![flamegraph](https://github.com/beothorn/javaflame/blob/main/screenshot.png?raw=true)

Detailed mode (for debugging):  

![flamegraph detailed](https://github.com/beothorn/javaflame/blob/main/screenshotDetailed.png?raw=true)


## Libraries used

https://github.com/spiermar/d3-flame-graph  
https://bytebuddy.net  

## TODO

- agent parameter: snapshot interval
- timestamps on spans
- timestamp filter
- search
