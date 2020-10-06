package org.monkey.gram;

import org.monkey.lexer.Repetitive;

import java.util.ArrayList;
import java.util.List;

public class NtPumps {
    Nonterminal nt;
    List<Serie> pumps = new ArrayList<>();

    public NtPumps(Nonterminal sym) {
        this.nt = sym;
    }

    void addPump(Serie pump) {
        pumps.add(pump);
    }
}
