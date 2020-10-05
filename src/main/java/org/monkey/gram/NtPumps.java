package org.monkey.gram;

import org.monkey.pars.Repetitive;

import java.util.ArrayList;
import java.util.List;

public class NtPumps {
    Nonterminal nt;
    List<Serie> pumps = new ArrayList<>();

    public NtPumps(Repetitive sym) {
        this.nt = (Nonterminal)sym;
    }

    void addPump(Serie pump) {
        pumps.add(pump);
    }
}
