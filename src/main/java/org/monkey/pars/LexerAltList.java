package org.monkey.pars;

import java.util.List;

public class LexerAltList extends LexerElement{
    List<LexerAlt> altList;

    public void addAlts(List<LexerAlt> altList) {
        this.altList = altList;
    }
}
