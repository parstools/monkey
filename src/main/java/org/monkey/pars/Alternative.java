package org.monkey.pars;

import java.util.*;

public class Alternative {
    List<Element> elements;

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

    public void addElements(List<Element> elements) {
        this.elements = elements;
    }
}
