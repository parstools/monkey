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
}
