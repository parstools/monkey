package org.monkey.pars;

import org.monkey.lexer.Type;
import org.monkey.tree.RuleManager;

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

    public String realizeString() {
        if (alternatives.isEmpty()) return "";
        int index = RuleManager.generator.nextInt(alternatives.size());
        var alt = alternatives.get(index);
        return alt.realizeString();
    }
}
