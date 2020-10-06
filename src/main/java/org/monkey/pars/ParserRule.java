package org.monkey.pars;

import org.monkey.lexer.Type;

import java.util.List;

public class ParserRule extends AltList {
    public String name;
    public ParserRule(String name) {
        this.name = name;
    }

    public void addAlternatives(List<Alternative> alternatives) {
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
        return Type.ParserRule;
    }
}
