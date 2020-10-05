package org.monkey.tree;

import org.monkey.pars.LexerRule;

public class TermNode  extends Node {
    public TermNode(LexerRule token) {
        this.ntORt = token;
    }

    @Override
    void print() {
        LexerRule t = (LexerRule)ntORt;
        System.out.print(t.toString()+" ");
    }
}
