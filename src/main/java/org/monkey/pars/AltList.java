package org.monkey.pars;

import org.monkey.lexer.LexerRule;
import org.monkey.lexer.Repetitive;
import org.monkey.lexer.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AltList extends Repetitive {
    public List<Alternative> alternatives;
    public void addAlts(List<Alternative> altList) {
        this.alternatives = altList;
    }

    @Override
    public String toString() {
        boolean first = true;
        String s = "(";
        for (var alt:alternatives) {
            if (!first)  s+= " |";
            first = false;
            s+=alt.toString();
        }
        return s+")"+suffixToString();
    }

    @Override
    public Type getType() {
        return Type.AltList;
    }

    @Override
    public String realizeString() {
        return null;
    }

    @Override
    public void updateLexerRef(HashMap<String, LexerRule> lexerMap) {
        for (var elem: alternatives) {
            elem.updateLexerRef(lexerMap);
        }
    }

    public List<RealizedRule1> makeRealizedRules() {
        List<RealizedRule1> rules = new ArrayList<>();
        for (var alt : alternatives){
            var subrules = alt.makeRealizedRules();
            rules.addAll(subrules);
        }
        return rules;
    }

    public Pump setRepOnce() {
        Pump resSerie = new Pump();
        for (int i=0; i<alternatives.size(); i++) {
            var elem = alternatives.get(i);
            var subserie = elem.setRepOnce().list;
            resSerie.addAllOnce(subserie);
        }
        return resSerie;
    }

    public List<ParserRule> getChildPR() {
        List<ParserRule> ntList = new ArrayList<>();
        for (Alternative alt:alternatives) {
            List<ParserRule> ntSubList = alt.getChildPR();
            ntList.addAll(ntSubList);
        }
        ntList = new ArrayList<>(new HashSet<>(ntList));//remove duplicates
        return ntList;
    }
}
