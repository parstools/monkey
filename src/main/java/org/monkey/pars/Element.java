package org.monkey.pars;

import org.monkey.lexer.Repetitions;
import org.monkey.lexer.Repetitive;

//@todo remove it
public abstract class Element extends Repetitive {

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
