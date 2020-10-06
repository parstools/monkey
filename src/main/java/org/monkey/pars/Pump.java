package org.monkey.pars;

import org.monkey.gram.Nonterminal;
import org.monkey.lexer.LexerRule;
import org.monkey.lexer.Repetitive;
import org.monkey.lexer.Type;

import java.util.HashMap;
import java.util.List;

public class Pump extends Repetitive {
    List<Repetitive> list;

    int pumpPRcount() {
        int counter = 0;
        for (var sym: list)
            if (sym instanceof Atom && ((Atom)sym).cargoNtRule!=null) counter++;
        return counter;
    }

    @Override
    public Type getType() {
        return Type.Pump;
    }

    @Override
    public String realizeString() {
        return null;
    }

    @Override
    public void updateLexerRef(HashMap<String, LexerRule> lexerMap) {
        for (var elem: list) {
            elem.updateLexerRef(lexerMap);
        }
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
