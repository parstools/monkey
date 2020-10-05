package org.monkey.pars;

public class LexerAtom extends LexerElement {
    public String cargo;
    public RefKind kind;

    @Override
    public String toString() {
        return cargo+suffixToString();
    }
}
