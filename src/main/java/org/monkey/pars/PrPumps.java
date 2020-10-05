package org.monkey.pars;

import org.monkey.gram.Nonterminal;
import org.monkey.gram.Serie;
import org.monkey.lexer.Repetitive;

import java.util.ArrayList;
import java.util.List;

public class PrPumps {
    ParserRule pr;
    List<Pump> pumps = new ArrayList<>();

    public PrPumps(Repetitive sym) {
        this.pr = (ParserRule)sym;
    }

    void addPump(Pump pump) {
        pumps.add(pump);
    }
}
