# javaflame

Simple and easy flame graph for java.  
No servers or open connections, just plug the agent and get the results.

## Usage

`java -javaagent:javaAgent.jar -jar application.jar`  

![flamegraph](https://github.com/beothorn/javaflame/blob/main/screenshot.png?raw=true)


## Libraries used

https://github.com/spiermar/d3-flame-graph

## TODO

- agent parameter: outpout directory
- agent parameter: snapshot interval
- timestamps on spans
- join spans on page
- timestamp filter
- search
