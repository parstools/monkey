package org.monkey.lexer;

import org.monkey.gram.Nonterminal;
import org.monkey.gram.Updatable;

import java.util.HashMap;
import java.util.List;

public class LexerAlt implements Updatable {
    List<RepetIn> elements;

    public void addLexerElements(List<RepetIn> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        boolean first = true;
        String s = "";
        if (elements==null) return "";
        for (var el:elements) {
            if (!first)  s+= " ";
            first = false;
            s+=el.toString();
        }
        return s;
    }

    public String realizeString() {
        String s = "";
        for (var element: elements)
            s += element.realizeString();
        return s;
    }

    @Override
    public void updateNtRef(HashMap<String, Nonterminal> parserMap) {
    }

    @Override
    public void updateLexerRef(HashMap<String, LexerRule> lexerMap) {
        for (var elem: elements) {
            ((Updatable)elem).updateLexerRef(lexerMap);
        }
    }
}
