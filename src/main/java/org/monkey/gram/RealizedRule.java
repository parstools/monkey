package org.monkey.gram;

import org.monkey.lexer.Type;
import org.monkey.pars.Atom;

import java.util.ArrayList;
import java.util.List;

public class RealizedRule {
    public List<RepetOut> list = new ArrayList<>();
    public int useCount = 0;
    public int ntCount = 0;
    public List<Object> nodes = new ArrayList<>();

    public int size() {
        return list.size();
    }

    public RealizedRule(List<RepetOut> list) {
        this.list.addAll(list);
    }

    public RealizedRule() {
    }

    public void add(RepetOut elem) {
        list.add(elem);
    }

    public void addAll(List<RepetOut> list1) {
        this.list.addAll(list1);
    }

    public void addAll(RealizedRule rule) {
        addAll(rule.list);
    }

    public void computeNTcount() {
        ntCount = 0;
        for (var elem: list)
            if (elem.getClass()==Atom.class && ((Atom)elem).cargoNtRule!=null)
                ntCount++;
    }

    public List<Nonterminal> getPumpedNt() {
        List<Nonterminal> pumpedNt = new ArrayList<>();
        for (var elem: list)
            if (elem.getClass()==Serie.class) {
                List<Nonterminal> sublist = ((Serie) elem).getPumpedNt();
                pumpedNt.addAll(sublist);
            }
        return pumpedNt;
    }
}
