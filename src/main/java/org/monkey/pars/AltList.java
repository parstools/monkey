package org.monkey.pars;

import org.monkey.gram.Nonterminal;
import org.monkey.gram.NtUpdatable;
import org.monkey.lexer.LexerRule;
import org.monkey.lexer.RepetIn;
import org.monkey.lexer.Type;

import java.util.HashMap;
import java.util.List;

public class AltList extends RepetIn{
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
    public String realizeString() {
        return null;
    }
}
