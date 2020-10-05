package org.monkey.pars;

import org.monkey.lexer.Repetitions;
import org.monkey.lexer.Repetitive;
import org.monkey.lexer.Type;

import java.util.List;

public class Pump extends Element {
    List<Repetitive> list;
    int pumpPRcount() {
        int counter = 0;
        for (var sym: list)
            if (sym instanceof ParserRule) counter++;
        return counter;
    }

    @Override
    public Type getType() {
        return Type.Pump;
    }

    public void add(Repetitive elem) {
        list.add(elem);
    }

    public void addAllOnce(List<Repetitive> xlist) {
        for (int i=0; i<xlist.size(); i++) {
            list.add(xlist.get(i));
        }
    }

    boolean pumpContain(ParserRule pr) {
        for (var sym: list)
            if (sym==pr) return true;
        return false;
    }

}
