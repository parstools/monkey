package org.monkey.lexer;

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

    public String realizeString() {
        if (altList.isEmpty()) return "";
        int index = LexerManager.generator.nextInt(altList.size());
        var alt = altList.get(index);
        String s = alt.realizeString();
        int count;
        if (rep== Repetitions.once || rep==Repetitions.maybe)
            count = 1;
        else
            count = 4;
        String result = "";
        for (int j=0; j<count; j++)
            result += s;
        return s;
    }
}
