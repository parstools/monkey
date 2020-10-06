package org.monkey.gram;

import org.monkey.lexer.LexerRule;

import java.util.HashMap;

public interface NtUpdatable {
    void updateNtRef(HashMap<String, Nonterminal> parserMap);
    void updateLexerRef(HashMap<String, LexerRule> lexerMap);
}
