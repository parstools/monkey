package org.monkey.gram;

import org.monkey.lexer.Repetitive;
import org.monkey.pars.AltList;
import org.monkey.pars.Alternative;
import org.monkey.pars.Atom;
import org.monkey.pars.ParserRule;

public class Importer {
    public static Repetitive imp(AltList altList) {
        if (altList.alternatives.size()==1)
            return imp(altList.alternatives.get(0));
        else {
            AltSet altSet = new AltSet();
            for (Alternative alt : altList.alternatives) {
                altSet.add(imp(alt));
            }
            return altSet;
        }
    }

    public static Serie imp(Alternative alternative) {
        Serie result = new Serie();
        for (Repetitive elem: alternative.elements) {
            if (elem.getClass()==Atom.class)
                result.add(elem, elem.rep);
            else if (elem.getClass()==AltList.class)
                result.add(imp((AltList)elem), elem.rep);
        }
        return result;
    }

    public static Nonterminal imp(ParserRule pr) {
        Nonterminal nt = new Nonterminal(pr.name);
        for (Alternative alt : pr.alternatives) {
            nt.add(imp(alt));
        }
        return nt;
    }
}
