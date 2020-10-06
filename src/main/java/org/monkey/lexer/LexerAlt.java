package org.monkey.lexer;

import java.util.HashMap;
import java.util.List;

public class LexerAlt {
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

}
