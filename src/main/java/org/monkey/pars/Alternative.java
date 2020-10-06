package org.monkey.pars;

import org.monkey.gram.RealizeType;
import org.monkey.lexer.*;

import java.util.HashMap;
import java.util.List;

public class Alternative extends RepetIn {
    public List<RepetIn> elements;

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

    public void addElements(List<RepetIn> elements) {
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
    public String realizeString() {
        return null;
    }
}
