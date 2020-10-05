package org.monkey.pars;

import org.monkey.lexer.LexerRule;
import org.monkey.lexer.Repetitions;
import org.monkey.lexer.Repetitive;
import org.monkey.lexer.Type;

import java.util.*;

public class Alternative extends Element{
    List<Repetitive> elements;

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

    public void addElements(List<Element> Repetitive) {
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

    RealizedRule1 removePump(RealizedRule1 rule) {
        RealizedRule1 result = new RealizedRule1();
        for (var elem : rule.list)
            if (elem instanceof ParserRule || elem instanceof LexerRule)
                result.add(elem);
        return result;
    }

    private List<RealizedRule1> makeSubRulesTwice(List<RealizedRule1> subrules) {
        List<RealizedRule1> result = new ArrayList<>();
        for (var rule: subrules) {
            RealizedRule1 resRule = new RealizedRule1();
            resRule.addAll(rule);
            resRule.addAll(removePump(rule));
            result.add(resRule);
        }
        return result;
    }

    Pump setRepOnce() {
        Pump resSerie = new Pump();
        for (int i=0; i<elements.size(); i++) {
            var elem = elements.get(i);
            var rep = elem.rep;
            if (elem instanceof LexerRule || elem instanceof ParserRule)
                resSerie.add(elem);
            else if (elem instanceof Alternative) {
                var subserie = ((Alternative)elem).setRepOnce().list;
                resSerie.addAllOnce(subserie);
            } if (elem.getClass()== AltList.class) {
                if (rep==Repetitions.oneOrMore || rep==Repetitions.zeroOrMore) {
                    var subserie = ((AltList) elem).setRepOnce().list;
                    resSerie.addAllOnce(subserie);
                } else {
                    return null;
                }
            }
        }
        return resSerie;
    }

    private RealizedRule1 join(List<RealizedRule1> subrules) {
        RealizedRule1 result = new RealizedRule1();
        for (var l: subrules)
            result.addAll(l);
        return result;
    }

    private List<List<RealizedRule1>> makeList3() {
        List<List<RealizedRule1>> subruleList = new ArrayList<>();
        for (int i=0; i<elements.size(); i++) {
            var elem = elements.get(i);
            var rep = elem.rep;
            if (elem instanceof Alternative) {
                var subrules = ((Alternative)elem).makeRealizedRules();
                List<RealizedRule1> newRules = new ArrayList<>();
                switch (rep) {
                    case once:newRules.addAll(subrules); break;
                    case maybe:{
                        RealizedRule1 emptyRule = new RealizedRule1();
                        newRules.add(emptyRule);
                        newRules.addAll(subrules);
                    }break;
                    case oneOrMore:{
                        newRules.addAll(subrules);
                        newRules.addAll(makeSubRulesTwice(subrules));
                        var pump = ((Alternative)elem).setRepOnce();
                        if (pump!=null)
                            newRules.get(0).add(pump);
                    } break;
                    case zeroOrMore:{
                        RealizedRule1 emptyRule = new RealizedRule1();
                        newRules.add(emptyRule);
                        newRules.addAll(makeSubRulesTwice(subrules));
                        var pump = ((Alternative)elem).setRepOnce();
                        if (pump!=null)
                            newRules.get(0).add(pump);
                    } break;
                }
                subruleList.add(newRules);
            }
            else if  (elem.getClass()== AltList.class) {
                var subrules = ((AltList)elem).makeRealizedRules();
                List<RealizedRule1> newRules = new ArrayList<>();
                switch (rep) {
                    case once:newRules.addAll(subrules); break;
                    case maybe:{
                        RealizedRule1 emptyRule = new RealizedRule1();
                        newRules.add(emptyRule);
                        newRules.addAll(subrules);
                    }break;
                    case oneOrMore:{
                        newRules.addAll(subrules);
                        newRules.add(join(subrules));
                        newRules.get(0).add(((AltList)elem).setRepOnce());
                    } break;
                    case zeroOrMore:{
                        RealizedRule1 emptyRule = new RealizedRule1();
                        newRules.add(emptyRule);
                        newRules.add(join(subrules));
                        newRules.get(0).add(((AltList)elem).setRepOnce());
                    } break;
                }
                subruleList.add(newRules);
            }
        }
        return subruleList;
    }

    private int maxSubrulesCount(List<List<RealizedRule1>> list3) {
        int max = 0;
        for(var l: list3) {
            max = Math.max(max,l.size());
        }
        return max;
    }

    public List<RealizedRule1> makeRealizedRules() {
        List<RealizedRule1> rules = new ArrayList<>();
        RealizeType type = getRealizeType();
        if (type==RealizeType.simple) {
            RealizedRule1 rule = new RealizedRule1(elements);
            rules.add(rule);
        } else if (type==RealizeType.rep) {
            RealizedRule1 rule = new RealizedRule1();
            for (int i=0; i<elements.size(); i++) {
                var elem = elements.get(i);
                var rep = elem.rep;
                if (rep== Repetitions.once || rep==Repetitions.oneOrMore)
                    rule.add(elem);
            }
            rules.add(rule);
            rule = new RealizedRule1();
            for (int i=0; i<elements.size(); i++) {
                var elem = elements.get(i);
                var rep = elem.rep;
                if (rep==Repetitions.once || rep==Repetitions.maybe)
                    rule.add(elem);
                else {
                    rule.add(elem);
                    rule.add(elem);
                    if (elem instanceof ParserRule) {
                        Pump pump = new Pump();
                        pump.add(elem);
                        rule.add(pump);
                    }
                }
            }
            rules.add(rule);
        } else if (type==RealizeType.complex) {
            List<List<RealizedRule1>> list3 = makeList3();
            int max = maxSubrulesCount(list3);
            for (int i=0; i<max; i++) {
                int index3 = 0;
                RealizedRule1 rule = new RealizedRule1();
                for (var elem : elements) {
                    if (elem instanceof LexerRule || elem instanceof ParserRule)
                        rule.add(elem);
                    else if (elem instanceof  Alternative || elem.getClass()== AltList.class) {
                        var list2 = list3.get(index3);
                        var list1 = list2.get(i % list2.size());
                        rule.addAll(list1);
                        index3++;
                    }
                }
                rules.add(rule);
            }
        }
        return rules;
    }

    @Override
    public Type getType() {
        return Type.Alternative;
    }

    public List<ParserRule> getChildPR() {
        List<ParserRule> prList = new ArrayList<>();
        for (Repetitive sym:elements) {
            List<ParserRule> prSubList;
            if (sym instanceof ParserRule) {
                prSubList = null; //nothing to avoid loop recurrence
                prList.add((ParserRule)sym);
            }
            else if (sym.getClass()== AltList.class)
                prSubList = ((AltList)sym).getChildPR();
            else if (sym instanceof Alternative)
                prSubList = ((Alternative)sym).getChildPR();
            else
                prSubList = null;
            if (prSubList!=null)
                prList.addAll(prSubList);
        }
        prList = new ArrayList<>(new HashSet<>(prList));//remove duplicates
        return prList;
    }
}
