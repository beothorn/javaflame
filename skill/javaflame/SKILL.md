---
name: javaflame
description: Investigate Java application behavior and JUnit tests by dumping call traces, argument and return values. Run or write focused Maven/Gradle JUnit tests with the bundled Java agent, explore large data.js captures with bounded queries, and produce evidence-based call diagrams or controlled dependency experiments.
---

# Javaflame

Use the bundled `javaflame.jar` to record calls and `javaflameDataExplorer.js` to investigate them. Resolve both paths relative to this SKILL.md's installed directory, not the application's working directory. The explorer requires Node.js 18+; the application requires its normal JDK and build tools.

## Capture a focused reproduction

1. Identify the question, smallest reproduction or JUnit test, application root package, JDK, and normal launch command. Inspect existing build/test configuration before adding JVM arguments.
2. Always start instrumentation with the application's root package, for example `filter:nameStartsWith(com.example.app.)`. Expand to relevant dependency classes or narrow to the suspected methods as evidence warrants. Keep every filter tied to the current goal. Avoid capturing the whole JVM.
3. Choose a persistent, absolute run directory in the target project, such as `target/javaflame/OrderServiceTest-createsOrder` for Maven, `build/javaflame/OrderServiceTest-createsOrder` for Gradle, or `.javaflame/checkout-reproduction` for a normal launch. Use the test name for test captures; add a run suffix for repeats and a module suffix when necessary. **Create this directory before launch** and pass it as `out:`. The agent creates a `javaflame/` child: the actual output folder is `<run-directory>/javaflame`, with `index.html` and `data.js` inside. A nonexistent `out:` falls back to the working directory. Never reuse a directory containing a capture: the agent resets output files. Keep outputs outside the installed skill folder.
4. **Always tell the user the absolute output folder and the path to its `index.html`**, both when announcing the run and in the final answer. They must be able to open the output page themselves. After execution, verify `index.html` and `data.js` exist; if capture failed, report the intended folder and explicitly say no valid page/capture was produced.
5. Attach the agent to the JVM executing the application or tests. Use [references/running.md](references/running.md) for direct Java, Maven Surefire/Failsafe, and Gradle examples. Preserve existing JVM options and test setup. Give each JVM a separate output folder or serialize a focused single-fork test run. Do not set JAVA_TOOL_OPTIONS globally: it can instrument build daemons and multiple workers into the same output directory.
6. Exercise the reproduction and allow normal JVM shutdown to flush recording. For long-running applications use snapshots, and analyze a stable copy or stop the process normally. An incomplete file is not evidence of an absent call. Keep the run command, filter, test outcome and hypothesis with the result; avoid committing generated traces unless requested.

Quote the entire `-javaagent:...` argument because expressions contain shell operators. Agent configuration is comma-separated, so avoid commas in paths/expressions. Values are captured with `toString()` and timing is affected by instrumentation: Javaflame is a debugging tool, not a precise profiler.

## JUnit examples: Maven and Gradle

Run these Bash examples from the target project's root, substituting its package and test names. Set `SKILL_DIR` to this skill's absolute installation directory. Use a new `RUN_DIR` for each capture and announce the actual `$OUT/index.html` path before running.

Maven example: run `ExampleTest.mapsFoo` with the application root package filter. Use `./mvnw` instead of `mvn` when the project supplies a wrapper. This example assumes no existing Surefire `argLine`; otherwise preserve its JVM flags/agents as described in [references/running.md](references/running.md).

```bash
SKILL_DIR=/absolute/path/to/installed/javaflame
RUN_DIR="$PWD/target/javaflame/ExampleTest-mapsFoo"
OUT="$RUN_DIR/javaflame"
mkdir -p "$RUN_DIR"
mvn '-Dtest=ExampleTest#mapsFoo' -DforkCount=1 -DreuseForks=true \
  "-DargLine=-javaagent:\"$SKILL_DIR/javaflame.jar=out:$RUN_DIR,filter:nameStartsWith(com.example.)\"" test
node "$SKILL_DIR/javaflameDataExplorer.js" "$OUT/data.js" summary
```

Gradle example: the bundled init script appends `-javaagent` to the selected **test worker's** JVM, preserves existing JVM arguments, and uses one worker for the output folder. Start with `nameStartsWith(com.example.)`; once evidence points to `FooService`, the example's filter narrows recording to that class and the test caller.

```bash
SKILL_DIR=/absolute/path/to/installed/javaflame
RUN_DIR="$PWD/build/javaflame/ExampleTest-mapsFoo-focused"
OUT="$RUN_DIR/javaflame"
mkdir -p "$RUN_DIR"
./gradlew --init-script "$SKILL_DIR/javaflame.init.gradle" \
  "-Djavaflame.jar=$SKILL_DIR/javaflame.jar" "-Djavaflame.out=$RUN_DIR" \
  '-Djavaflame.filter=named(com.example.FooService)||named(com.example.ExampleTest)' \
  '-Djavaflame.task=:test' :test --tests 'com.example.ExampleTest.mapsFoo' \
  --rerun-tasks --no-build-cache --no-configuration-cache
node "$SKILL_DIR/javaflameDataExplorer.js" "$OUT/data.js" summary
```

The init script constructs `-javaagent:<jar>=out:<run-directory>,filter:<expression>` for the worker. `org.gradle.jvmargs` alone would configure the build daemon, not the test. For a module use its full task path in both places, for example `-Djavaflame.task=:foo:test` and `:foo:test`. Use the project's required JDK. Inspect any extra tasks attached to `test`; exclude unrelated frontend tasks only when they are unnecessary for the selected Java test. See [references/running.md](references/running.md) for coverage-agent integration and Failsafe tests.

## Explore without loading data.js into context

**Never read, cat, paste, or load the complete `data.js` into model context.** Do not search raw lines: a single line may contain most of a trace. Execute the explorer instead. It parses data without executing JavaScript and merges repeated call IDs across snapshots.

```bash
node "$SKILL_DIR/javaflameDataExplorer.js" "$OUT/data.js" summary
node "$SKILL_DIR/javaflameDataExplorer.js" "$OUT/data.js" threads
node "$SKILL_DIR/javaflameDataExplorer.js" "$OUT/data.js" list --depth 1 --limit 10
node "$SKILL_DIR/javaflameDataExplorer.js" "$OUT/data.js" search --query OrderService --limit 10
node "$SKILL_DIR/javaflameDataExplorer.js" "$OUT/data.js" show --id CALL_ID
node "$SKILL_DIR/javaflameDataExplorer.js" "$OUT/data.js" ancestors --id CALL_ID
```

Set `SKILL_DIR` to this skill's absolute installation directory and `OUT` to the actual capture directory. Obtain call IDs from results; do not assume IDs are stable between runs. Use `--thread` when an ID is ambiguous. Begin with summary, thread and shallow views, find a relevant call, then inspect its values and callers. Follow `nextOffset` to paginate only as needed. Strings and nested arrays are truncated explicitly; use `show --field arguments.0.value --text-offset 160 --text-limit 160` for another slice. An empty result is limited by the capture filter and query, not proof the application never made the call.

Read [references/explorer.md](references/explorer.md) for all commands, reusable query sessions, limits and diagram generation. Prefer a session for repeated queries on a large trace. The script keeps one record per unique call in memory; filtering at instrumentation time remains essential even though output is bounded.

## Follow dependencies and run experiments

Recording can target methods in libraries as well as application code. Keep application callers when investigating SQL, HTTP or gRPC and add the actual implementation discovered from dependencies/source. Parenthesize each class-and-method branch: `filter:(nameStartsWith(com.example.app.))||(org.postgresql.jdbc.PgConnection#prepareStatement)`. A `#` after an unparenthesized OR applies to the whole preceding class expression. Database preparation is not proof of execution; choose execute methods if that is the question. For HTTP/gRPC, locate the concrete client/transport methods in the installed library rather than assuming interface methods are instrumented. Use `core_classes` only when the target actually belongs to Java core classes, with a narrow filter.

For an experiment, state the hypothesis, preserve a baseline, change one controlled input or hook, and write a new capture folder. Read [references/intercept.md](references/intercept.md) before using `intercept:`. **The bundled agent's intercept hook runs after the original method; it does not replace/skip the method body or assign a replacement return value.** It can observe dependency calls and mutate accessible objects for controlled experiments. Use ordinary test doubles or another explicitly chosen mechanism if true replacement is needed; do not claim intercept provides it.

You can write a focused JUnit test yourself in the current project when existing tests do not reproduce the question, then run it with Javaflame using the examples above. Follow the project's existing JUnit version and test conventions; choose inputs and assertions that distinguish the hypothesis, and name the capture folder after the new test.

You can also write experimental code in the current project to replace calls to libraries: for example, inject a fake `FooClient`, wrap a dependency behind an adapter, or use the project's mocking framework to return a controlled result. Put replacements in test code or a reversible experiment configuration where possible, and ensure the application actually uses that replacement. Compare baseline and experiment traces under equivalent filters, explain which library call was replaced and how, and retain useful regression tests while removing temporary wiring when finished. Use an `intercept` callback for after-call observations or mutable-object experiments; use test doubles/adapters/mocks for true call replacement.

## Explain findings

Cite thread and call IDs, relevant arguments/returns, and the recorded caller path. Distinguish observed calls from hypotheses and uninstrumented gaps. Use bounded `diagram` output or selected caller/callee rows to produce a Mermaid diagram when useful. A trace tree shows recorded synchronous nesting, not proven asynchronous causality or complete application architecture. End with the reproduction/test outcome, findings, limitations relevant to the question, and **absolute output directory plus `index.html` link**.
