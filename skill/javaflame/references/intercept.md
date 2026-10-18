# Interception experiments

The bundled agent invokes a public static callback after a matched method executes. It supplies receiver, reflected method/constructor, arguments and return value. Static calls have a null receiver; constructor callbacks run after construction and receive the new instance as the return value.

```java
package com.example.experiment;

import java.lang.reflect.Executable;

public final class Observer {
    public static void afterCall(Object self, Executable methodOrConstructor,
                                 Object[] arguments, Object returnValue) {
        // Inspect these objects or modify an accessible mutable object to test
        // a specific hypothesis. The original call has already happened.
        System.err.println("Observed: " + methodOrConstructor);
    }
}
```

Compile the callback onto the application's/test worker's runtime classpath. The agent does not inject it; test source is convenient for JUnit experiments. Exclude callback code from the capture filter when appropriate to avoid noise. Prefer a narrow dependency method target:

```bash
java "-javaagent:$SKILL_DIR/javaflame.jar=out:$RUN_DIR,filter:(nameStartsWith(com.example.app.))||(org.postgresql.jdbc.PgConnection#prepareStatement),intercept:(org.postgresql.jdbc.PgConnection#prepareStatement)>com.example.experiment.Observer#afterCall" \
  -cp "$APP_CLASSPATH" com.example.app.Main
```

Create `RUN_DIR` first as in [running.md](running.md); the output page is `$RUN_DIR/javaflame/index.html`. For tests, append the same `intercept:...` setting to the agent argument in the Maven/Gradle recipes. Match constructors with `#new`. The interceptor must be resolvable by the application's class loader; inspect agent errors if it is not invoked.

This is **not method replacement**. The callback is void and the original body already ran. Reassigning callback variables does not replace the caller's return value or arguments. Mutating a shared mutable object can affect subsequent execution, but cannot undo a database write or HTTP request already performed. Instance/static exception-path behavior differs in the implementation; do not treat the callback as a guaranteed exception observer.

Use this for focused observations or controlled object-state experiments on application or dependency classes. If the question needs skipping a library call, forcing a scalar return, or replacing a function, use a test double or another appropriate mechanism and clearly distinguish it from Javaflame interception. Capture the baseline and experiment in different named directories, compare the same scenario, and report exactly what changed. Do not infer authorization to make external writes from a request to inspect a trace.
