package com.github.beothorn.agent.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ASTNode {

    public Token token;

    FLAG HERE

    public ASTNode[] children;

    private ASTNode(Token token, ASTNode[] children) {
        this.token = token;
        this.children = children;
    }

    public static ASTNode n(Token token) {
        return new ASTNode(token, new ASTNode[0]);
    }

    public static ASTNode n(Token token, ASTNode... children) {
        return new ASTNode(token, children);
    }

    public <T> T apply(Assembler<T> assembler) throws CompilationException {
        // If it has no children, it is a string for the default matcher
        if (children.length == 0) {
            return assembler.assembleDefaultMatcher(token);
        }
        // If it is a function call, it is the same as if it had no children, just not using the default matcher
        // but the declared one instead. for example named(org.foo.ClassXYZ)
        if (TokenType.FUNCTION_CALL.equals(token.type)) {
            return assembler.assembleDeclaredMatcher(token, children[0].token.value);
        }
        // If it has children, it is some operand that needs to be resolved recursively
        // for example: ClassX||ClassY
        // for example: ClassX||(org.foo&&Test)
        List<Object> results = new ArrayList<>();
        for (ASTNode child : children) {
            results.add(child.apply(assembler));
        }
        // After all children is resolved, the operator can finally be assembled
        //noinspection unchecked
        return assembler.assemble(token, (List<T>) results);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ASTNode astNode = (ASTNode) o;
        return Objects.equals(token, astNode.token) && Arrays.equals(children, astNode.children);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(token);
        result = 31 * result + Arrays.hashCode(children);
        return result;
    }

    @Override
    public String toString() {
        if (children.length > 0) {
            return token.value + Arrays.toString(children);
        }
        return token.value;
    }
}
