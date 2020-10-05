package org.monkey.lexer;

public abstract class LexerElement {
    public Repetitions rep;

    protected String suffixToString() {
        if (rep==Repetitions.maybe)
            return "?";
        else if (rep==Repetitions.oneOrMore)
            return "+";
        else if (rep==Repetitions.zeroOrMore)
            return "*";
        else
            return "";
    }

    public abstract String realizeString();
}
