package org.monkey.pars;

import org.monkey.lexer.Repetitive;
import org.monkey.lexer.Type;

import java.util.ArrayList;
import java.util.List;

public class RealizedRule1 {
    public List<Repetitive> list = new ArrayList<>();
    public int useCount = 0;
    public int prCount = 0;
    public List<Object> nodes = new ArrayList<>();

    public int size() {
        return list.size();
    }

    public RealizedRule1(List<Repetitive> list) {
        this.list.addAll(list);
    }

    public RealizedRule1() {
    }

    public void add(Repetitive elem) {
        list.add(elem);
    }

    public void addAll(List<Repetitive> list1) {
        this.list.addAll(list1);
    }

    public void addAll(RealizedRule1 rule) {
        addAll(rule.list);
    }

    public void computeNTcount() {
        prCount = 0;
        for (var elem: list)
            if (elem.getType()== Type.nonterminal) prCount++;
    }
}
