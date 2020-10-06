package org.monkey.tree;

import org.monkey.lexer.LexerRule;

public class TermNode  extends Node {
    LexerRule term;
    public TermNode(LexerRule token) {
        this.term = token;
    }

    @Override
    void print() {
        System.out.print(term.realizeString()+" ");
    }
}
