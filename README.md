# javaflame

Simple and easy flame graph for java.  
No servers or open connections, just plug the agent and get the results.  
[Latest release v0.0.1](https://github.com/beothorn/javaflame/releases/download/v1.0.0/javaAgent.jar)

## Usage

`java -javaagent:javaAgent.jar -jar yourApp.jar`  

## Arguments

- mode: Specifies detailed mode, all parameter values will be recorded with a toString() call.  
This is slower but a great view for debugging. Only mode:detailed available for now. Example: `java -javaagent:javaAgent.jar=mode:detailed,out:/tmp/flameOut -jar yourApp.jar`
- out: Specifies the output directory. Example: `java -javaagent:javaAgent.jar=out:/tmp/flameOut -jar yourApp.jar`

![flamegraph](https://github.com/beothorn/javaflame/blob/main/screenshot.png?raw=true)


## Libraries used

https://github.com/spiermar/d3-flame-graph
ByteBuddy

## TODO

- agent parameter: snapshot interval
- timestamps on spans
- join spans on page
- timestamp filter
- search
