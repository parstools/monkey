package org.monkey.gram;

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
