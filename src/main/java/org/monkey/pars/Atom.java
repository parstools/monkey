package org.monkey.pars;

import org.monkey.gram.Nonterminal;
import org.monkey.gram.NtUpdatable;
import org.monkey.lexer.LexerRule;
import org.monkey.lexer.RefKind;
import org.monkey.lexer.Repetitive;
import org.monkey.lexer.Type;

import java.util.HashMap;

public class Atom extends Repetitive implements NtUpdatable {
    public String cargo;
    public RefKind kind;
    public LexerRule cargoLexerRule = null;
    public Nonterminal cargoNtRule = null;

    @Override
    public String toString() {
        return cargo+suffixToString();
    }

    @Override
    public Type getType() {
        return Type.Atom;
    }

    @Override
    public String realizeString() {
        return null;
    }

    @Override
    public void updateLexerRef(HashMap<String, LexerRule> lexerMap) {
        if (kind==RefKind.TokenRef)
            if (lexerMap.containsKey(cargo))
                cargoLexerRule = lexerMap.get(cargo);
    }

    @Override
    public void updateNtRef(HashMap<String, Nonterminal> parserMap) {
        if (kind==RefKind.RuleRef)
            if (parserMap.containsKey(cargo))
                cargoNtRule = parserMap.get(cargo);
    }
}
