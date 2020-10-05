package org.monkey.pars;

public class Atom extends Element {
    public String cargo;
    public RefKind kind;

    @Override
    public String toString() {
        return cargo+suffixToString();
    }
}
