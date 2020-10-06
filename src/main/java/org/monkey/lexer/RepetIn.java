package org.monkey.lexer;

import java.util.HashMap;

public abstract class RepetIn {
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
