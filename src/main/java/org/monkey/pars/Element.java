package org.monkey.pars;

public class Element {
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
}
