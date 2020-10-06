package org.monkey.lexer;

import org.monkey.gram.Nonterminal;
import org.monkey.gram.Updatable;

import java.util.HashMap;
import java.util.List;

public class LexerRule implements Updatable {
    public String name;
    List<LexerAlt> alternatives;

    public LexerRule(String name) {
        this.name = name;
    }

    public void addAlternatives(List<LexerAlt> alternatives) {
        this.alternatives = alternatives;
    }

    @Override
    public String toString() {
        String s = name + ": ";
        boolean first = true;
        for (var alt : alternatives) {
            if (!first) s += " |";
            first = false;
            s += alt.toString();
        }
        s += ";";
        return s;
    }

    public String realizeString() {
        if (alternatives.isEmpty()) return "";
        int index = LexerManager.generator.nextInt(alternatives.size());
        var alt = alternatives.get(index);
        return alt.realizeString();
    }

    @Override
    public void updateNtRef(HashMap<String, Nonterminal> parserMap) {
    }

    @Override
    public void updateLexerRef(HashMap<String, LexerRule> lexerMap) {
        for (var elem:  alternatives) {
            elem.updateLexerRef(lexerMap);
        }
    }
}
