package org.monkey.lexer;

import org.monkey.pars.Repetitive;

public class TokenSimple extends Repetitive {
    public String s;
    public TokenSimple(String s) {
        this.s = s;
    }
    @Override
    public Type getType() {
        return Type.tokenSimple;
    }

    public String toString() {
        return s;
    }
}
