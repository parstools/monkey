package org.monkey.pars;

import org.monkey.lexer.RefKind;
import org.monkey.lexer.Type;

public class Atom extends Element {
    public String cargo;
    public RefKind kind;

    @Override
    public String toString() {
        return cargo+suffixToString();
    }

    @Override
    public Type getType() {
        return Type.Atom;
    }
}
