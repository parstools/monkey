package org.monkey.pars;

import java.util.List;

public class AltList extends Element {
    List<Alternative> altList;
    public void addAlts(List<Alternative> altList) {
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
