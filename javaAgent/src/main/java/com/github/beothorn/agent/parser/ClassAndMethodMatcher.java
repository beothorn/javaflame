package com.github.beothorn.agent.parser;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.matcher.ElementMatcher;

public class ClassAndMethodMatcher {

    public final ElementMatcher.Junction<NamedElement> classMatcher;
    public final ElementMatcher.Junction<NamedElement> methodMatcher;

    public static ClassAndMethodMatcher matcher(
            final ElementMatcher.Junction<NamedElement> classMatcher,
            final ElementMatcher.Junction<NamedElement> methodDescription
    ){
        return new ClassAndMethodMatcher(classMatcher, methodDescription);
    }

    private ClassAndMethodMatcher(
            final ElementMatcher.Junction<NamedElement> classMatcher,
            final ElementMatcher.Junction<NamedElement> methodDescription
    ) {
        this.classMatcher = classMatcher;
        this.methodMatcher = methodDescription;
    }

}
