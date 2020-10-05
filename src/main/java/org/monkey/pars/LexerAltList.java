package org.monkey.pars;

import java.util.List;

public class LexerAltList extends LexerElement{
    List<LexerAlt> altList;

    public void addAlts(List<LexerAlt> altList) {
        this.altList = altList;
    }

    @Override
    public String toString() {
        boolean first = true;
        String s = "(";
        for (var alt:altList) {
            if (!first)  s+= " |";
            first = false;
            s+=alt.toString();
        }
        return s+")"+suffixToString();
    }
}
