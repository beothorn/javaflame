package com.github.beothorn.agent;

import com.github.beothorn.agent.logging.Log;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.beothorn.agent.logging.Log.LogLevel.INFO;

public class CommandLine {
    private static final String ARGUMENT_FILTER = "filter";
    private static final String ARGUMENT_INTERCEPT_CONSTRUCTOR = "interceptConstructor";
    private static final String ARGUMENT_INTERCEPT_METHOD_ENTRY = "interceptMethodEntry";
    private static final String ARGUMENT_INTERCEPT_METHOD_EXIT = "interceptMethodExit";
    private static final String ARGUMENT_START_RECORDING_FUNCTION = "startRecordingTriggerFunction";
    private static final String ARGUMENT_STOP_RECORDING_FUNCTION = "stopRecordingTriggerFunction";
    private static final String ARGUMENT_LOG_LEVEL = "log";
    private static final String ARGUMENT_OUTPUT_FOLDER = "out";
    private static final String FLAG_NO_CAPTURING_VALUES = "no_capturing_values";
    private static final String FLAG_CORE_CLASSES = "core_classes";
    private static final String FLAG_NO_SNAPSHOTS = "no_snapshots";
    private static final String FLAG_QUALIFIED_FUNCTIONS = "qualified_functions";
    private static final String FLAG_CAPTURE_STACKTRACE = "capture_stacktrace";

    private static final String[] VALID_ARGS = new String[]{
        ARGUMENT_FILTER,
        ARGUMENT_INTERCEPT_CONSTRUCTOR,
        ARGUMENT_START_RECORDING_FUNCTION,
        ARGUMENT_STOP_RECORDING_FUNCTION,
        ARGUMENT_LOG_LEVEL,
        ARGUMENT_OUTPUT_FOLDER,
        FLAG_NO_CAPTURING_VALUES
    };

    private static final String[] VALID_FLAGS = new String[]{
            FLAG_NO_CAPTURING_VALUES,
            FLAG_CORE_CLASSES,
            FLAG_NO_SNAPSHOTS,
            FLAG_QUALIFIED_FUNCTIONS,
            FLAG_CAPTURE_STACKTRACE
    };

    public static void validateArguments(String argument){
        String[] arguments = argument.split(",");
        for (final String arg : arguments) {
            if (arg.isEmpty()) continue;
            String argOrFlag = arg;
            if (arg.contains(":")){
                argOrFlag = arg.split(":")[0];
            }
            boolean containsArg = Arrays.asList(VALID_ARGS).contains(argOrFlag);
            boolean containsFlag = Arrays.asList(VALID_FLAGS).contains(argOrFlag);
            if (!containsArg && !containsFlag) {
                throw new RuntimeException("Unknown argument '"+arg+"' on '" + argument + "'. "+
                        "Valid arguments are: "+Arrays.toString(VALID_ARGS)+
                        ". Valid flags are: "+Arrays.toString(VALID_FLAGS));
            }
        }
    }

    public static boolean isOnArguments(
        String flag,
        String arguments
    ){
        return Pattern.compile("(^|,)" + flag + "(,|$)").matcher(arguments).find();
    }

    public static String[] allFlagsOnArgument(String arguments){
        return Arrays.stream(VALID_FLAGS)
                .filter(f -> isOnArguments(f, arguments))
                .toArray(String[]::new);
    }

    public static boolean argumentHasQualifiedFunctions(String argument){
        return isOnArguments(FLAG_QUALIFIED_FUNCTIONS, argument);
    }

    public static boolean argumentHasNoCaptureValuesMode(String argument){
        return isOnArguments(FLAG_NO_CAPTURING_VALUES, argument);
    }

    public static boolean argumentHasShouldCaptureStackTraces(String argument){
        return isOnArguments(FLAG_CAPTURE_STACKTRACE, argument);
    }

    public static boolean argumentHasIncludeCoreClasses(String argument){
        return isOnArguments(FLAG_CORE_CLASSES, argument);
    }

    public static boolean argumentHasNoSnapshotsMode(String argument){
        return isOnArguments(FLAG_NO_SNAPSHOTS, argument);
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
                ARGUMENT_INTERCEPT_CONSTRUCTOR
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
}
