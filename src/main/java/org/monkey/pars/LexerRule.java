package org.monkey.pars;

import org.monkey.lexer.Type;

import java.util.List;

public class LexerRule extends Repetitive {
    String name;
    List<LexerAlt> alternatives;

    public LexerRule(String name) {
        this.name = name;
    }

    public void addAlternatives(List<LexerAlt> alternatives) {
        this.alternatives = alternatives;
    }

    @Override
    public String toString() {
        String s = name+": ";
        boolean first = true;
        for (var alt:alternatives) {
            if (!first)  s+= " |";
            first = false;
            s+=alt.toString();
        }
        s+=";";
        return s;
    }

    @Override
    public Type getType() {
        return Type.LexerRule;
    }
}
