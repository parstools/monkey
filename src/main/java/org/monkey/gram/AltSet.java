package org.monkey.gram;

import org.monkey.lexer.LexerRule;
import org.monkey.lexer.Repetitive;
import org.monkey.lexer.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AltSet extends Repetitive implements NtUpdatable{
    protected List<Serie> list = new ArrayList<>();

    public Type getType() {return Type.altList;}

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

    @Override
    public void updateNtRef(HashMap<String, Nonterminal> parserMap) {
        for (var elem: list) {
            elem.updateNtRef(parserMap);
        }
    }

    public void add(Serie alt) {
        list.add(alt);
    }

    public List<Nonterminal> getChildNT() {
        List<Nonterminal> ntList = new ArrayList<>();
        for (Serie alt:list) {
            List<Nonterminal> ntSubList = alt.getChildNT();
            ntList.addAll(ntSubList);
        }
        ntList = new ArrayList<>(new HashSet<>(ntList));//remove duplicates
        return ntList;
    }

    public List<RealizedRule> makeRealizedRules() {
        List<RealizedRule> rules = new ArrayList<>();
        for (var alt : list){
            var subrules = alt.makeRealizedRules();
            rules.addAll(subrules);
        }
        return rules;
    }

    public Serie setRepOnce() {
        Serie resSerie = new Serie();
        for (int i=0; i<list.size(); i++) {
            var elem = list.get(i);
            var subserie = elem.setRepOnce().list;
            resSerie.addAllOnce(subserie);
        }
        return resSerie;
    }
}
