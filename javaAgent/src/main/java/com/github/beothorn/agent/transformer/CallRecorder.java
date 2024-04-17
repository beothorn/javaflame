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
import static net.bytebuddy.matcher.ElementMatchers.isConstructor;
import static net.bytebuddy.matcher.ElementMatchers.isMethod;

public class CallRecorder implements Transformer {
        private final Advice adviceForFunction;
        private final Advice adviceForConstructor;
        private final List<ClassAndMethodMatcher> filters;

        public CallRecorder(
            Advice adviceForFunction,
            Advice adviceForConstructor,
            List<ClassAndMethodMatcher> filters
        ) {
            this.adviceForFunction = adviceForFunction;
            this.adviceForConstructor = adviceForConstructor;
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
                if (classAndMethodFilter.classMatcher.matches(typeDescription)) {
                    funMatcherMethod = funMatcherMethod.and(classAndMethodFilter.methodMatcher);
                    log(DEBUG, "Match function in ["+canonicalName+"]: "+classAndMethodFilter.methodMatcher);
                    return builder.visit(adviceForFunction.on(funMatcherMethod));
                }
            }

            log(DEBUG, "Match all functions in "+canonicalName);
            if (adviceForConstructor == null) {
                return builder.visit(adviceForFunction.on(funMatcherMethod));
            }
            if (adviceForFunction == null) {
                return builder.visit(adviceForConstructor.on(funMatcherMethod));
            }
            return builder.visit(adviceForConstructor.on(isConstructor()))
                    .visit(adviceForFunction.on(funMatcherMethod));
        }
    }