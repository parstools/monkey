package org.monkey.tree;

import org.monkey.lexer.Token;

public class TermNode  extends Node {
    public TermNode(Token token) {
        this.ntORt = token;
    }

    @Override
    void print() {
        Token t = (Token)ntORt;
        System.out.print(t.toString()+" ");
    }
}
