package com.github.beothorn.agent.transformer;

import com.github.beothorn.agent.parser.ClassAndMethodMatcher;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

import static com.github.beothorn.agent.logging.Log.LogLevel.DEBUG;
import static com.github.beothorn.agent.logging.Log.log;
import static net.bytebuddy.matcher.ElementMatchers.*;

public class CallRecorder implements Transformer {
        private final Advice adviceForFunction;
        private final Advice adviceForConstructor;
        private final Advice adviceForStatic;
        private final List<ClassAndMethodMatcher> filters;

        public CallRecorder(
            Advice adviceForFunction,
            Advice adviceForConstructor,
            Advice adviceForStatic,
            List<ClassAndMethodMatcher> filters
        ) {
            this.adviceForFunction = adviceForFunction;
            this.adviceForConstructor = adviceForConstructor;
            this.adviceForStatic = adviceForStatic;
            this.filters = filters;
            log(DEBUG, () -> "Filters "+ Arrays.toString(this.filters.toArray()));
        }

        public DynamicType.Builder<?> transform(
                DynamicType.Builder<?> builder,
                TypeDescription typeDescription,
                ClassLoader ignoredClassLoader,
                JavaModule ignoredModule
        ) {
            return getBuilder(builder, typeDescription);
        }

        @Override
        public DynamicType.Builder<?> transform(
            DynamicType.Builder<?> builder,
            TypeDescription typeDescription,
            ClassLoader classLoader,
            JavaModule module,
            ProtectionDomain protectionDomain
        ) {
            return getBuilder(builder, typeDescription);
        }

        private DynamicType.Builder<?> getBuilder(
            DynamicType.Builder<?> builder,
            TypeDescription typeDescription
        ) {
            String canonicalName = typeDescription.getCanonicalName();
            log(DEBUG, "Transform '"+ canonicalName +"'");

            ElementMatcher.Junction<MethodDescription> funMatcherMethod = isMethod();

            for (final ClassAndMethodMatcher classAndMethodFilter : filters) {
                boolean classShouldMatchOnlySomeFunctions = classAndMethodFilter.classMatcher.matches(typeDescription);
                if (classShouldMatchOnlySomeFunctions) {
                    funMatcherMethod = funMatcherMethod.and(classAndMethodFilter.methodMatcher);
                    log(DEBUG, "Match function in ["+canonicalName+"]: "+classAndMethodFilter.methodMatcher);
                    return builder
                        .visit(adviceForFunction.on(funMatcherMethod))
                        .visit(adviceForStatic.on(funMatcherMethod.and(isStatic())));
                }
            }

            DynamicType.Builder<?> finalBulder = builder;

            log(DEBUG, "Match all functions in "+canonicalName);
            if (adviceForConstructor != null) {
                finalBulder = finalBulder.visit(adviceForConstructor.on(isConstructor()));
            }
            if (adviceForFunction != null) {
                finalBulder = finalBulder
                    .visit(adviceForFunction.on(funMatcherMethod))
                    .visit(adviceForStatic.on(funMatcherMethod.and(isStatic())));
            }
            return finalBulder;
        }
    }