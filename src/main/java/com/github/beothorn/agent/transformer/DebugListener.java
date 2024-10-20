package com.github.beothorn.agent.transformer;

import net.bytebuddy.agent.builder.AgentBuilder.Listener;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

import static com.github.beothorn.agent.logging.Log.LogLevel.*;
import static com.github.beothorn.agent.logging.Log.log;

public class DebugListener implements Listener {

    private final Set<String> debugTransformedClasses = new HashSet<>();
    private final Set<String> debugDiscoveredClasses = new HashSet<>();
    private final Set<String> debugIgnoredClasses = new HashSet<>();
    private final Set<String> debugErrorClasses = new HashSet<>();
    private final Set<String> debugCompletedClasses = new HashSet<>();

    public void writeDebugFiles(
        String snapshotDirectoryAbsolutePath
    ) {
        writeDebugFile(debugTransformedClasses, snapshotDirectoryAbsolutePath, "debugTransformedClasses.txt");
        writeDebugFile(debugDiscoveredClasses, snapshotDirectoryAbsolutePath, "debugDiscoveredClasses.txt");
        writeDebugFile(debugIgnoredClasses, snapshotDirectoryAbsolutePath, "debugIgnoredClasses.txt");
        writeDebugFile(debugErrorClasses, snapshotDirectoryAbsolutePath, "debugErrorClasses.txt");
        writeDebugFile(debugCompletedClasses, snapshotDirectoryAbsolutePath, "debugCompletedClasses.txt");
    }

    public static void writeDebugFile(
            final Set<String> debugClassesToWrite,
            String snapshotDirectoryAbsolutePath,
            final String file
    ) {
        if (!debugClassesToWrite.isEmpty()){
            synchronized (debugClassesToWrite) {
                try {
                    File debugLogFile = new File(snapshotDirectoryAbsolutePath, file);
                    if (debugLogFile.exists()) {
                        RandomAccessFile raf = new RandomAccessFile(debugLogFile, "rw");
                        for (String className : debugClassesToWrite) {
                            raf.writeBytes(className + "\n");
                        }
                        raf.close();
                    } else {
                        try (FileWriter fw = new FileWriter(debugLogFile)) {
                            for (String className : debugClassesToWrite) {
                                fw.write(className + "\n");
                            }
                            fw.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    debugClassesToWrite.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

        @Override
        public void onDiscovery(
                String typeName,
                ClassLoader classLoader,
                JavaModule module,
                boolean loaded
        ) {
            log(TRACE, "onDiscovery(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
            synchronized (debugDiscoveredClasses) {
                debugDiscoveredClasses.add(typeName);
            }
        }

        @Override
        public void onTransformation(
            TypeDescription typeDescription,
            ClassLoader classLoader,
            JavaModule module,
            boolean loaded,
            DynamicType dynamicType
        ) {
            log(DEBUG, "onTransformation(TypeDescription typeDescription='"+typeDescription+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"', " +
                    "DynamicType dynamicType='"+dynamicType+"')");
            synchronized (debugTransformedClasses) {
                debugTransformedClasses.add(typeDescription.getCanonicalName());
            }
        }

        @Override
        public void onIgnored(
            TypeDescription typeDescription,
            ClassLoader classLoader,
            JavaModule module,
            boolean loaded
        ) {
            log(TRACE, "onIgnored(TypeDescription typeDescription='"+typeDescription+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
            synchronized (debugIgnoredClasses) {
                debugIgnoredClasses.add(typeDescription.getCanonicalName());
            }
        }

        @Override
        public void onError(
            String typeName,
            ClassLoader classLoader,
            JavaModule module,
            boolean loaded,
            Throwable throwable
        ) {
            throwable.printStackTrace();
            log(ERROR, "onError(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"', " +
                    "Throwable throwable='"+throwable+"')");
            synchronized (debugErrorClasses) {
                debugErrorClasses.add(typeName);
            }
        }

        @Override
        public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
            log(TRACE, "onComplete(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
            synchronized (debugCompletedClasses) {
                debugCompletedClasses.add(typeName);
            }
        }
    }