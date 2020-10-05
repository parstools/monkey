package org.monkey.pars;

import org.monkey.pars.Alternative;

import java.util.List;

public class ParserRule {
    List<Alternative> alternatives;
    String name;
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
}
