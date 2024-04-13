package com.github.beothorn.agent;

import com.github.beothorn.agent.logging.Log;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.beothorn.agent.CommandLine.Flag.*;
import static com.github.beothorn.agent.logging.Log.LogLevel.INFO;

public class CommandLine {
    private static final String ARGUMENT_FILTER = "filter";
    private static final String ARGUMENT_INTERCEPT_CONSTRUCTOR_FOR = "interceptConstructorFor";
    private static final String ARGUMENT_INTERCEPT_CONSTRUCTOR_WITH = "interceptConstructorWith";
    private static final String ARGUMENT_INTERCEPT_METHOD_ENTRY = "interceptMethodEntry";
    private static final String ARGUMENT_INTERCEPT_METHOD_EXIT = "interceptMethodExit";
    private static final String ARGUMENT_START_RECORDING_FUNCTION = "startRecordingTriggerFunction";
    private static final String ARGUMENT_STOP_RECORDING_FUNCTION = "stopRecordingTriggerFunction";
    private static final String ARGUMENT_LOG_LEVEL = "log";
    private static final String ARGUMENT_OUTPUT_FOLDER = "out";

    public static boolean argumentHasQualifiedFunctions(String argument){
        return QUALIFIED_FUNCTIONS.isOnArguments(argument);
    }

    public static boolean argumentHasNoCaptureValuesMode(String argument){
        return NO_CAPTURING_VALUES.isOnArguments(argument);
    }

    public static boolean argumentHasShouldCaptureStackTraces(String argument){
        return CAPTURE_STACKTRACE.isOnArguments(argument);
    }

    public static boolean argumentHasIncludeCoreClasses(String argument){
        return CORE_CLASSES.isOnArguments(argument);
    }

    public static boolean argumentHasNoSnapshotsMode(String argument){
        return NO_SNAPSHOTS.isOnArguments(argument);
    }

    public static Optional<String> argumentFilter(String argument){
        return matchCommand(
            argument,
            ARGUMENT_FILTER
        );
    }

    public static Optional<String> argumentInterceptConstructorFilter(String argument){
        return matchCommand(
                argument,
                ARGUMENT_INTERCEPT_CONSTRUCTOR_FOR
        );
    }

    public static Optional<String> argumentConstructorInterceptor(String argument){
        return matchCommand(
            argument,
            ARGUMENT_INTERCEPT_CONSTRUCTOR_WITH
        );
    }

    private static Optional<String> matchCommand(
        String argument,
        String command
    ) {
        String regex = command + ":([^,]+)";
        Matcher matcher = Pattern.compile(regex).matcher(argument);
        if(!matcher.find()) return Optional.empty();
        return Optional.of(matcher.group(1));
    }

    public static Optional<String> argumentStartRecordingTriggerFunction(String argument){
        String regex = ARGUMENT_START_RECORDING_FUNCTION + ":([^,]+)";
        Matcher matcher = Pattern.compile(regex).matcher(argument);
        if(!matcher.find()) return Optional.empty();
        return Optional.of(matcher.group(1));
    }

    public static Optional<String> argumentStopRecordingTriggerFunction(String argument){
        String regex = ARGUMENT_STOP_RECORDING_FUNCTION + ":([^,]+)";
        Matcher matcher = Pattern.compile(regex).matcher(argument);
        if(!matcher.find()) return Optional.empty();
        return Optional.of(matcher.group(1));
    }

    public static Log.LogLevel argumentLogLevel(String argument){
        Matcher matcher = Pattern.compile(ARGUMENT_LOG_LEVEL+":([^,]+)")
                .matcher(argument);

        if(matcher.find()) {
            return Log.LogLevel.valueOf(matcher.group(1));
        }

        return INFO;
    }

    public static Optional<String> outputFileOnArgument(String argument){
        if(!argument.contains(ARGUMENT_OUTPUT_FOLDER+":")){
            return Optional.empty();
        }
        String afterOut = argument.split(ARGUMENT_OUTPUT_FOLDER+":")[1];
        int separator = afterOut.indexOf(',');
        if(separator == -1){
           return Optional.of(afterOut);
        }
        String filePath = afterOut.substring(0, separator);
        return Optional.of(filePath);
    }

    public enum Flag{
        NO_CAPTURING_VALUES("no_capturing_values"),
        CORE_CLASSES("core_classes"),
        NO_SNAPSHOTS("no_snapshots"),
        QUALIFIED_FUNCTIONS("qualified_functions"),
        CAPTURE_STACKTRACE("capture_stacktrace"),;

        public final String flagAsString;

        Flag(String flagAsString) {
            this.flagAsString = flagAsString;
        }

        public boolean isOnArguments(String arguments){
            return Pattern.compile("(^|,)" + this.flagAsString + "(,|$)")
                    .matcher(arguments).find();
        }

        public static String[] allFlagsOnArgument(String arguments){
            return Arrays.stream(Flag.values())
                    .filter(f -> f.isOnArguments(arguments))
                    .map(f -> f.flagAsString)
                    .toArray(String[]::new);
        }
    }
}
