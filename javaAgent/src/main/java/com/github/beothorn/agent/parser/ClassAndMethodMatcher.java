package com.github.beothorn.agent.parser;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

/***
 * A pair of two matchers, one for class and one for the function.
 * Example: for the expression (ClassA||ClassB)#(fA||fB)
 * The matcher for classes is ClassA||ClassB
 * and the matcher for functions that match the classes is fA||fB
 * Matching entries would be:
 * ClassA#fA
 * ClassA#fB
 * ClassB#fA
 * ClassB#fB
 */
public class ClassAndMethodMatcher {

    public final ElementMatcher.Junction<NamedElement> classMatcher;
    public final ElementMatcher.Junction<MethodDescription> methodMatcher;

    public static ClassAndMethodMatcher matcher(
            final ElementMatcher.Junction<NamedElement> classMatcher,
            final ElementMatcher.Junction<MethodDescription> methodDescription
    ){
        return new ClassAndMethodMatcher(classMatcher, methodDescription);
    }

    private ClassAndMethodMatcher(
            final ElementMatcher.Junction<NamedElement> classMatcher,
            final ElementMatcher.Junction<MethodDescription> methodDescription
    ) {
        this.classMatcher = classMatcher;
        this.methodMatcher = methodDescription;
    }

    @Override
    public String toString() {
        return "matcher(classMatcher: "+this.classMatcher+", methodMatcher: "+this.methodMatcher+")";
    }
}
