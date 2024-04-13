package com.github.beothorn.agent.logging;

import com.github.beothorn.agent.MethodInstrumentationAgent;

import java.util.function.Supplier;

import static com.github.beothorn.agent.logging.Log.LogLevel.ERROR;

public class Log {
    public static void log(LogLevel level, String log){
        log(level, () -> log);
    }

    public static void log(LogLevel level, Supplier<String> log){
        if(MethodInstrumentationAgent.currentLevel.shouldPrint(level)){
            if(level.equals(ERROR)){
                System.err.println("[JAVA_AGENT] "+level.name()+" "+log.get());
            }else{
                System.out.println("[JAVA_AGENT] "+level.name()+" "+log.get());
            }
        }
    }

    public enum LogLevel{
        NONE(1),
        ERROR(2),
        INFO(3),
        WARN(4),
        DEBUG(5),
        TRACE(6);

        private final Integer level;

        LogLevel(int level) {
            this.level = level;
        }

        public boolean shouldPrint(LogLevel level) {
            return level.level <= this.level ;
        }
    }
}
