# javaflame

Simple and easy flame graph for java.  
No servers or open connections, just plug the agent and get the results.  
[Latest release v0.0.1](https://github.com/beothorn/javaflame/releases/download/v1.0.0/javaAgent.jar)

## Usage

`java -javaagent:javaAgent.jar -jar yourApp.jar`  

## Arguments

- mode:detailed Specifies detailed mode, all parameter values will be recorded with a toString() call.  
This is slower but a great view for debugging.
Example: `java -javaagent:javaAgent.jar=mode:detailed,out:/tmp/flameOut -jar yourApp.jar`
- mode:debug Will print every method call.
- mode:noconstructor Will ignore constructors
- exclude:qualifed.name.part Will exclude classes which contain the qualified name on them.
- filter:qualified.name.part Will instrument only classes that contains this string on their qualified name. You probably want to set this to you app package to avoid out of memory with huge spans.
- out: Specifies the output directory. Example: `java -javaagent:javaAgent.jar=out:/tmp/flameOut -jar yourApp.jar`

# Screenshots

Performance:  

![flamegraph](https://github.com/beothorn/javaflame/blob/main/screenshot.png?raw=true)

Detailed mode (for debugging):  

![flamegraph detailed](https://github.com/beothorn/javaflame/blob/main/screenshotDetailed.png?raw=true)


## Libraries used

https://github.com/spiermar/d3-flame-graph
ByteBuddy

## TODO

- agent parameter: snapshot interval
- timestamps on spans
- join spans on page
- timestamp filter
- search
