package org.monkey.pars;

import org.monkey.gram.Nonterminal;
import org.monkey.gram.Updatable;
import org.monkey.gram.RepetOut;
import org.monkey.lexer.*;

import java.util.HashMap;

public class Atom extends RepetIn implements Updatable, RepetOut {
    public String cargo;
    public RefKind kind;
    public LexerRule cargoLexerRule = null;
    public Nonterminal cargoNtRule = null;

    @Override
    public String toString() {
        return cargo+suffixToString();
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

    @Override
    public String realizeString() {
        return null;
    }
}
