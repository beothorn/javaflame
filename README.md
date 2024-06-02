# Javaflame

Capture all function calls including argument and return values.  
Just plug the agent and get the results.   
See function calls, parameters and return values all at once.  
[Latest release v23.0.0](https://github.com/beothorn/javaflame/releases/download/v23.0.0/javaAgent.jar)

Want to see it in action? [Check out this rendering of some sort algorithms flamegraphs](https://beothorn.github.io/javaflame).

# Screenshots  

![flamegraph detailed](https://github.com/beothorn/javaflame/blob/main/screenshotTooltip.png?raw=true)
![flamegraph hierarchy](https://github.com/beothorn/javaflame/blob/main/screenshotHierarchical.png?raw=true)

# What is this for?

This is mainly a debugging tool. The motivation behind it is to have a quick way to get a glimpse of how some logic is handled by looking at the values that are passed around between functions. It is specially nice to use together with your automated tests.  

# Is this a profiler?

No. A profiler, either for memory or performance, tries to have as little impact as possible on the execution of the process.  
Usually, the way it works is by sampling the executon stack (either inside the JVM or the OS kernel) and using it to acess how long a call took. The stack is the very stack of the process.  
That is not what javaflame does. Javaflame injects some bytecode on every function call that matches the filter on its arguments. This bytecode will store the function name on a separate stack and will use reflection to get the parameters. It will also call the toString function for each argument and store the result on the separate stack.  
That means Javaflame impacts performance (because of the time it takes to process toString) and memory (because it stores the argument values).  
You still can use it to have some idea about the performance, if you assume the time it takes to execute the toString and the size of the stack is negligible. This is not guaranteed to be true. If what you need is to be as precise as possible, consider other tools like [async-profiler](https://github.com/async-profiler/async-profiler)

# Features
- See argument values on function calls, not only the function signature. 
- One Flamegraph per thread.
- Filtering on instrumentation, bytecode transformation is only done on classes that match the filter.
- Exclude classes and packages.
- Continuous snapshots.
- Pure html output in a simple file and all captured data available as a JSON at data.js.
- Get the result either as html or optionally through http on a port passed as argument on the command.
- Optional, start and stop recording whenever a method with a certain name is called

# Use cases

Usually without filtering too much data is generated.  
You may start by finding what class you want to understand and filter its namespace and the namespaces of the classes it calls.  
So for example lets say I want to see what functions are called from the example [MergeSort](https://github.com/beothorn/javaflame/blob/main/javaExampleApp/src/main/java/com/github/beothorn/sorts/algorithms/MergeSort.java) and the helper Class [Common.java](https://github.com/beothorn/javaflame/blob/main/javaExampleApp/src/main/java/com/github/beothorn/sorts/Common.java).  
I can use the command:  
`java "-javaagent:/PathTo/javaAgent.jar=filter:com.github.beothorn.sorts.Common||com.github.beothorn.sorts.algorithms.MergeSort" -jar ./javaExampleApp/build/libs/javaExampleApp.jar`  
And all calls coming from MergeSort will show up on the graph, including arguent and return values.  

The nice thing is that this is not restricted to classes on your code. You can filter third party libraries.  
That means if you know where some network call is done, or a GRPC or whatever else, you can build a graph listing all external calls. For example, to log postgres sql calls yoou could do:  
`java -javaagent:/PathTo/javaAgent.jar=filter:org.postgresql.jdbc.PgConnection#prepareStatement -jar yourapp.jar`

# Quick start

Download the jar from the link above (Latest release).  
Start your application with the agent.  

`java -javaagent:javaAgent.jar -jar yourApp.jar` 

or with arguments:  

`java -javaagent:javaAgent.jar=flags,configuration:value -jar yourApp.jar` 

For example, this will silently output a flame graph including function parameter values and the return.  

`java -javaagent:javaAgent.jar=log:NONE,out:C:/graphs -jar yourApp.jar` 

Anything without exclusions will generate lots of data. Either it will not render or you will need to filter it first.

# Arguments

## Arguments

| Flag                | Description | Example |
| ------------------- | ----------- | ------- |
| log:LEVEL           | Outputs log on the stdout. Levels are: NONE, ERROR, INFO, WARN, DEBUG, TRACE | `java -javaagent:javaAgent.jar=log:DEBUG -jar yourApp.jar` |
| port:PORT           | If present, serves the javaflame output on the given port.| `java -javaagent:javaAgent.jar=port:8080 -jar yourApp.jar` |
| no_capturing_values | Record only function call, no parameter value or return value will be recorded. This is faster and measures performance more accurately. | `java -javaagent:javaAgent.jar=no_capturing_values -jar yourApp.jar` |
| core_classes        | Will include Java core classes. More useful in conjunction with filters to check, for example, network calls. | `java -javaagent:javaAgent.jar=core_classes -jar yourApp.jar` |
| no_snapshots        | Dump the stack only when JVM goes down. Beware, this will use a lot of memory! You probably don't want that. | `java -javaagent:javaAgent.jar=no_snapshots -jar yourApp.jar` |
| qualified_functions | Print the qualified function name, ownerClass.functionName | `java -javaagent:javaAgent.jar=qualified_functions -jar yourApp.jar` |
| capture_stacktrace  | Capture stacktraces for calls. Very expensive, use it when analizyng a single method.| `java -javaagent:javaAgent.jar=capture_stacktrace -jar yourApp.jar` |
| filter:expression   | Will instrument only classes for which the qualified name matches the expression, see more below. You probably want to set this to you app package to avoid huge snapshots. | `java "-javaagent:javaAgent.jar=filter:com.github.myApp||store" -jar yourApp.jar` |
| intercept:expression>methodReference | For classes matching the expression will call the methodReference. See details below on Intercepting | `java "-javaagent:javaAgent.jar=intercept:Test||App>com.github.myApp.Interceptor#intercept" -jar .yourApp.jar` |
| startRecordingTriggerFunction:method | Will start recording the stack only when the function with this name is called. This checks only the method name. | `java -javaagent:javaAgent.jar=startRecordingTriggerFunction:com.example.InitializeClass#afterSetup -jar yourApp.jar` |
| stopRecordingTriggerFunction:method | Will stop recording the stack when the function with this name is called. This checks only the method name. | `java -javaagent:javaAgent.jar=stopRecordingTriggerFunction:com.example.FinalizeClass#afterJobIsDone -jar yourApp.jar` |
| out:path            | Specifies the output directory. | `java -javaagent:javaAgent.jar=out:/tmp/flameOut -jar yourApp.jar` |

# Filter Expression

The filer expression argument will match the class and the method you want to capture.  
By default the filter will match any qualified named that contains the string (equivalent to nameContains()) , but you can use different matchers.  
The method part is optional. A filter with no method will match all methods on your class.  
Constructors can be matched by using the keyword new.  

## Boolean operators, Precedence operators and matching functions

You can use boolean operators on your exprssions. Currently supported are || && and !.  
You can also use parenthesis for precedence.  
For matching a function, use a function matcher (see explanation on section Function matcher):  
Examples:  
```
java "-javaagent:/PathTo/javaAgent.jar=filter:\!Bar" -jar yourapp.jar # Escaping the not operator with a slash to make bash happy
```  
This will match all methods on:  
```
com.github.test.Foo
```  
This will not match:  
```
com.github.test.Bar
com.github.test.FooBarBaz
com.github.Bar.Foo
```


```
java "-javaagent:/PathTo/javaAgent.jar=filter:Foo||Bar" -jar yourapp.jar
```  
This will match all methods on:  
```
com.github.test.Foo
com.github.test.Bar
com.github.test.FooBarBaz
com.github.Bar.Foo
```  
This will not match:  
```
com.github.Baz.Qux
```

```
java "-javaagent:/PathTo/javaAgent.jar=filter:Foo&&Bar" -jar yourapp.jar
```  
This will match all methods on:  
```
com.github.Bar.Foo
com.github.test.FooBarBaz
```  
This will not match:  
```
com.github.test.Foo
com.github.test.Bar
```

## Function matcher

You can match an specific function on your class using the operator #. It applies to the whole previous condition, so it may require parenthesis.  
For example, `Foo||Bar#myFun` matches myFun on `Foo||Bar`, but `Foo||(Bar#myFun)` matches any method on class Foo and only myFun on Bar.  

```
java "-javaagent:/PathTo/javaAgent.jar=filter:Bar#myFunction" -jar yourapp.jar
Equivalent to
java "-javaagent:/PathTo/javaAgent.jar=filter:nameContains(Bar)#nameContains(myFunction)" -jar yourapp.jar
```  
This will match:  
```
com.github.test.Bar#zzzmyFunction
```  
This will not match:  
```
com.github.test.Bar#init
```

For constructors, you can use the keyword new. For example, this will match the constructor for Bar:  

```
java "-javaagent:/PathTo/javaAgent.jar=filter:Bar#new" -jar yourapp.jar
```


## Match functions

You can use match functions on your expressions. Match functions can only receive a single string argument.  

### nameContains (Default matcher)

The default matcher. Will match all calls to a class containg the string.  
Example:  
```
java -javaagent:/PathTo/javaAgent.jar=filter:Bar -jar yourapp.jar
Equivalent to
java "-javaagent:/PathTo/javaAgent.jar=filter:nameContains(Bar)" -jar yourapp.jar
```  

This will match all methods on:  
```
com.github.test.Bar
com.github.test.FooBarBaz
com.github.Bar.Foo
```  
This will not match:  
```
com.github.test.Foo
```

### nameContainsIgnoreCase

Same as nameContains() but ignoring case.  

### named

Will match the exact class name containg the string.  
Example:  
```
java "-javaagent:/PathTo/javaAgent.jar=filter:named(com.github.test.Bar)" -jar yourapp.jar
```  

This will match all methods on:  
```
com.github.test.Bar
```  
This will not match:  
```
com.github.test.Foo
com.github.test.BarBaz
```

### namedIgnoreCase

Same as named(), but ignoring case.  

### nameStartsWith

Will match the class name starting with the string (including package name).  
Example:  
```
java "-javaagent:/PathTo/javaAgent.jar=filter:nameStartsWith(com.github.test.Test)" -jar yourapp.jar
```  

This will match all methods on:  
```
com.github.test.Test
com.github.test.TestBar
com.github.test.TestFoo
```  
This will not match:  
```
TestBar
com.github.test.xxx.TestBar
com.github.test.T
```

### nameStartsWithIgnoreCase  

Same as nameStartsWith() but ignoring case.  

### nameEndsWith

Will match the class name ending with the string.  
Example:  
```
java "-javaagent:/PathTo/javaAgent.jar=filter:nameEndsWith(Test)" -jar yourapp.jar
```  

This will match all methods on:  
```
com.github.test.FooTest
com.github.bla.BarTest
ZZTest
```  
This will not match:  
```
com.github.test.TestBar
com.github.test.TestFoo
```


### nameEndsWithIgnoreCase

same as nameEndsWith() ignoring case.  


### nameMatches

Will match the regex argument.  

Will match the class name ending with the string.  
Example:  
```
java "-javaagent:/PathTo/javaAgent.jar=filter:nameMatches(.*Test[AB].*)" -jar yourapp.jar
```  

This will match all methods on:  
```
com.github.test.TestA
com.github.test.TestB
ZZTest
```  
This will not match:  
```
com.github.test.TestC
```

# Intercepting

You may want to intercept functions or constructors using your own implementation. For this you can pass to the agent the qualified name of your function and it will be callled whenever the intercept filter matches.   
The intercept is called after the function returns.  

Set the argument `intercept` with the expression to match the class and function and the reference for the static method to be called.  
The method should be static and have this signature:
```java
public class Interceptor {
    public static void intercept(
        Object self,
        Executable methodOrConstructor,
        Object[] allArguments,
        Object returnValueFromMethod // On constructors, this contains the new instance
    ){
        // Your logic here
    }
}
```
Here the class is `Interceptor` and the method is `intercept` so your command should look like:  
`java "-javaagent:javaAgent.jar=filter:NO_FILTER,intercept:Test>com.github.foo.Interceptor#intercept" -jar myApp.jar`  
Where `filter:NO_FILTER` matches nothing, so to have no output on the flamegraph (and run it faster).  

To match constructors you can use the new keyword:  
`java "-javaagent:javaAgent.jar=filter:NO_FILTER,intercept:Test#new>com.github.foo.Interceptor#intercept" -jar myApp.jar`  
The `Interceptor` class should be on the classpath, it is not injected by the agent.  


# Known issues  

- May conflict with libraries that do bytecode manipulation (For example: Guice)  
- Debugging will not match the source code for methods that match the filter, so turn it off when debugging on IDE.  
