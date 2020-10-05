package org.monkey.lexer;

public abstract class Repetitive {
    public Repetitions rep;
    public abstract Type getType();// {return Type.repetitive;}

    public abstract String realizeString();

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
}
