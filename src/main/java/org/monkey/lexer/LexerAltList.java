package org.monkey.lexer;

import java.util.HashMap;
import java.util.List;

public class LexerAltList extends Repetitive {
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

    @Override
    public Type getType() {
        return Type.LexerAltList;
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

    @Override
    public void updateLexerRef(HashMap<String, LexerRule> lexerMap) {
        for (var elem: altList) {
            elem.updateLexerRef(lexerMap);
        }
    }
}
