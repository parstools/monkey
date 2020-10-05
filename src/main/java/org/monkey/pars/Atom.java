package org.monkey.pars;

import org.monkey.lexer.LexerRule;
import org.monkey.lexer.RefKind;
import org.monkey.lexer.Repetitive;
import org.monkey.lexer.Type;

import java.util.HashMap;

public class Atom extends Repetitive {
    public String cargo;
    public RefKind kind;
    public LexerRule cargoLexerRule = null;

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
}
