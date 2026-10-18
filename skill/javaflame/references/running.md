# Running applications and JUnit tests

Examples use Bash. Replace package, test and project paths with those discovered in the target repository. `SKILL_DIR` is the absolute directory containing this skill's SKILL.md. Use its bundled `javaflame.jar`; do not rebuild or download another agent unnecessarily. `RUN_DIR` is the existing parent passed to `out:`; `OUT="$RUN_DIR/javaflame"` is the actual generated output directory. The agent always appends this child directory. Create the parent first or the agent falls back to the working directory.

## Direct application launch

```bash
SKILL_DIR=/absolute/path/to/installed/javaflame
RUN_DIR="$PWD/.javaflame/checkout-reproduction"
OUT="$RUN_DIR/javaflame"
mkdir -p "$RUN_DIR"
java "-javaagent:$SKILL_DIR/javaflame.jar=out:$RUN_DIR,filter:nameStartsWith(com.example.app.)" -jar build/libs/app.jar
```

For a classpath launch, place the same JVM argument before `-cp ... com.example.Main`. For framework/build-tool launch tasks, configure the spawned application's JVM arguments, not just the build process. Keep normal application arguments, profiles and environment. Use a new output folder for every run.

## Maven: Surefire JUnit tests

Use `./mvnw` when available, otherwise `mvn`. Surefire's `argLine` is passed to **forked test JVMs**; `MAVEN_OPTS` only configures Maven itself. Inspect the POM, active profiles, existing `argLine` and JaCoCo settings first.

For a project without an existing argLine provider:

```bash
RUN_DIR="$PWD/target/javaflame/OrderServiceTest-createsOrder"
OUT="$RUN_DIR/javaflame"
mkdir -p "$RUN_DIR"
./mvnw '-Dtest=OrderServiceTest#createsOrder' \
  -DforkCount=1 -DreuseForks=true \
  "-DargLine=-javaagent:\"$SKILL_DIR/javaflame.jar=out:$RUN_DIR,filter:nameStartsWith(com.example.app.)\"" test
```

If the project already has JVM flags, coverage agents or plugin-defined argLine, append the Javaflame argument to the effective test JVM configuration. Do not blindly replace `-DargLine`: a hardcoded plugin configuration can ignore it, and JaCoCo may populate argLine later. A temporary profile can retain existing arguments and use Maven's late `@{argLine}` substitution where appropriate; define the property when the coverage plugin is inactive so the placeholder does not reach Java literally. Keep temporary edits isolated and remove only your own edits afterward.

For integration tests managed by Failsafe, select `'-Dit.test=OrderServiceIT#createsOrder'` and use `verify`, configuring the **Failsafe** argLine and forks. Check which other tests the lifecycle executes. In a multi-module reactor, focus on the test module (prebuild required dependencies separately) or assign distinct module output directories. A single `out` shared by several forked JVMs corrupts/overwrites captures. Validate test counts and reports as well as trace existence; a successful build that skipped the requested test is not a reproduction.

[Surefire JVM argument and test selection reference](https://maven.apache.org/surefire/maven-surefire-plugin/test-mojo.html).

## Gradle: JUnit test workers

Gradle test workers are separate JVMs. `org.gradle.jvmargs` configures the daemon, not tests. Use the bundled [javaflame.init.gradle](../javaflame.init.gradle) to append JVM arguments without changing the project's build files. This Groovy init script also works when the project uses Kotlin DSL. It preserves existing JVM arguments, including coverage agents, and limits the selected test task to one worker.

```bash
RUN_DIR="$PWD/build/javaflame/OrderServiceTest-createsOrder"
OUT="$RUN_DIR/javaflame"
mkdir -p "$RUN_DIR"
./gradlew --init-script "$SKILL_DIR/javaflame.init.gradle" \
  "-Djavaflame.jar=$SKILL_DIR/javaflame.jar" "-Djavaflame.out=$RUN_DIR" \
  '-Djavaflame.filter=nameStartsWith(com.example.app.)' '-Djavaflame.task=:test' \
  :test --tests 'com.example.app.OrderServiceTest.createsOrder' \
  --rerun-tasks --no-build-cache --no-configuration-cache
```

For modules/custom integration test tasks, set both the selected task and `javaflame.task`, for example `:service:integrationTest`. Leave the repository's JUnit engine configuration intact. The existing test task determines JUnit 4/5 support; Javaflame does not supply a test runner. `--rerun-tasks --no-build-cache` ensures a fresh capture instead of an up-to-date/cached result. Single-worker settings do not disable JUnit-level parallelism; account for multiple trace threads or temporarily serialize JUnit if needed for the reproduction.

Use `--no-configuration-cache` because this invocation-specific init script configures projects after evaluation. Select the JDK required by the project (for example, a local Java 17 installation for a Java 17/Spring Boot 3 build). Inspect dependencies of the chosen test task: some repositories attach frontend builds or browser tests. Exclude them only if the focused Java test does not need their outputs; do not copy project-specific task exclusions into unrelated projects.

[Gradle Test JVM arguments and fork settings](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.Test.html).

## Verify and report

Verify the run really exercised the requested scenario and produced `OUT/index.html` and a parseable `OUT/data.js`. Analyze through the explorer, including when the test itself fails: failed tests are often the intended capture. Always report the absolute output directory and output page link. If there is no data, inspect the agent log, package filter, selected test and fork configuration before retrying. Do not turn off filtering to diagnose a missing capture.

With the bundled v27.1.0 agent, Surefire can report a corrupted-channel warning when Javaflame logs during JVM startup. Inspect the Surefire dumpstream and test report to distinguish startup logging from an actual test failure; `log:NONE` in the agent options can suppress its startup logs. Gradle can also log a Byte Buddy `NoSuchTypeException` for an internal worker class from the agent's debug listener. Neither diagnostic alone proves that the requested application calls were captured: verify the selected test count, failures and errors, then search the trace for the expected application methods and values. Report any remaining diagnostic rather than treating a successful build as proof of a complete trace.
