package org.monkey.pars;

import org.monkey.gram.RealizeType;
import org.monkey.lexer.LexerRule;
import org.monkey.lexer.Repetitions;
import org.monkey.lexer.Repetitive;
import org.monkey.lexer.Type;

import java.util.HashMap;
import java.util.List;

public class Alternative extends Repetitive {
    public List<Repetitive> elements;

    @Override
    public String toString() {
        boolean first = true;
        String s = "";
        if (elements==null) return "";
        for (var el:elements) {
            if (!first)  s+= " ";
            first = false;
            s+=el.toString();
        }
        return s;
    }

    public void addElements(List<Repetitive> elements) {
        this.elements = elements;
    }

    private RealizeType getRealizeType() {
        for (var elem: elements)
            if (elem instanceof Alternative || elem.getClass()== AltList.class)
                return RealizeType.complex;
        for (var elem: elements)
            if (elem.rep!=Repetitions.once)
                return RealizeType.rep;
        return RealizeType.simple;
    }

    @Override
    public Type getType() {
        return Type.Alternative;
    }

    @Override
    public String realizeString() {
        return null;
    }

    @Override
    public void updateLexerRef(HashMap<String, LexerRule> lexerMap) {
        for (var elem: elements) {
            elem.updateLexerRef(lexerMap);
        }
    }
}
