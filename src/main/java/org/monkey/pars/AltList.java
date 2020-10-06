package org.monkey.pars;

import org.monkey.lexer.LexerRule;
import org.monkey.lexer.Repetitive;
import org.monkey.lexer.Type;

import java.util.HashMap;
import java.util.List;

public class AltList extends Repetitive {
    public List<Alternative> alternatives;
    public void addAlts(List<Alternative> altList) {
        this.alternatives = altList;
    }

    @Override
    public String toString() {
        boolean first = true;
        String s = "(";
        for (var alt:alternatives) {
            if (!first)  s+= " |";
            first = false;
            s+=alt.toString();
        }
        return s+")"+suffixToString();
    }

    @Override
    public Type getType() {
        return Type.AltList;
    }

    @Override
    public String realizeString() {
        return null;
    }

    @Override
    public void updateLexerRef(HashMap<String, LexerRule> lexerMap) {
        for (var elem: alternatives) {
            elem.updateLexerRef(lexerMap);
        }
    }
}
