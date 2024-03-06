package com.github.beothorn.agent.parser;

import java.util.Objects;

public class Token {
    public enum TokenType {
        STRING_VALUE, FUNCTION_CALL, OPERATOR_OR, OPERATOR_AND, OPERATOR_NOT, OPEN_PAREN, CLOSE_PAREN
    }

    public TokenType type;
    public String value;

    private Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public static Token t(TokenType type, String value) {
        return new Token(type, value);
    }

    public static Token and() {
        return new Token(TokenType.OPERATOR_AND, "&&");
    }

    public static Token or() {
        return new Token(TokenType.OPERATOR_OR, "||");
    }

    public static Token not() {
        return new Token(TokenType.OPERATOR_NOT, "!");
    }

    public static Token openParen() {
        return new Token(TokenType.OPEN_PAREN, "(");
    }

    public static Token closeParen() {
        return new Token(TokenType.CLOSE_PAREN, ")");
    }

    public static Token string(String value) {
        return new Token(TokenType.STRING_VALUE, value);
    }

    public static Token function(String value) {
        return new Token(TokenType.FUNCTION_CALL, value);
    }

    @Override
    public String toString() {
        return "t("+type.name() + ", \"" + value + "\")";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Token token = (Token) o;
        return type == token.type && Objects.equals(value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}