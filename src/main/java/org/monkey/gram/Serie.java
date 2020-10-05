package org.monkey.gram;

import org.monkey.lexer.LexerRule;
import org.monkey.lexer.Repetitions;
import org.monkey.lexer.Repetitive;
import org.monkey.lexer.Type;
import org.monkey.pars.RealizeType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Serie extends Repetitive {
    public List<Repetitive> list = new ArrayList<>();
    protected List<Repetitions> reps = new ArrayList<>();

    public Type getType() {return Type.serie;}

    @Override
    public String realizeString() {
        return null;
    }

    public List<Nonterminal> getChildNT() {
        List<Nonterminal> ntList = new ArrayList<>();
        for (Repetitive sym:list) {
            List<Nonterminal> ntSubList;
            if (sym instanceof Nonterminal) {
                ntSubList = null; //nothing to avoid loop recurrence
                ntList.add((Nonterminal)sym);
            }
            else if (sym.getClass()== AltSet.class)
                ntSubList = ((AltSet)sym).getChildNT();
            else if (sym instanceof Serie)
                ntSubList = ((Serie)sym).getChildNT();
            else
                ntSubList = null;
            if (ntSubList!=null)
                ntList.addAll(ntSubList);
        }
        ntList = new ArrayList<>(new HashSet<>(ntList));//remove duplicates
        return ntList;
    }

    public void add(Repetitive x, Repetitions rep) {
        list.add(x);
        reps.add(rep);
    }

    public void addAllOnce(List<Repetitive> xlist) {
        for (int i=0; i<xlist.size(); i++) {
            list.add(xlist.get(i));
            reps.add(Repetitions.once);
        }
    }

    RealizedRule removePump(RealizedRule rule) {
        RealizedRule result = new RealizedRule();
        for (var elem : rule.list)
            if (elem instanceof Nonterminal || elem instanceof LexerRule)
                result.add(elem);
        return result;
    }

    private List<RealizedRule> makeSubRulesTwice(List<RealizedRule> subrules) {
        List<RealizedRule> result = new ArrayList<>();
        for (var rule: subrules) {
            RealizedRule resRule = new RealizedRule();
            resRule.addAll(rule);
            resRule.addAll(removePump(rule));
            result.add(resRule);
        }
        return result;
    }

    public List<RealizedRule> makeRealizedRules() {
        List<RealizedRule> rules = new ArrayList<>();
        RealizeType type = getRealizeType();
        if (type==RealizeType.simple) {
            RealizedRule rule = new RealizedRule(list);
            rules.add(rule);
        } else if (type==RealizeType.rep) {
            RealizedRule rule = new RealizedRule();
            for (int i=0; i<list.size(); i++) {
                var rep = reps.get(i);
                var elem = list.get(i);
                if (rep==Repetitions.once || rep==Repetitions.oneOrMore)
                   rule.add(elem);
            }
            rules.add(rule);
            rule = new RealizedRule();
            for (int i=0; i<list.size(); i++) {
                var rep = reps.get(i);
                var elem = list.get(i);
                if (rep==Repetitions.once || rep==Repetitions.maybe)
                    rule.add(elem);
                else {
                    rule.add(elem);
                    rule.add(elem);
                    if (elem instanceof Nonterminal) {
                        Serie pump = new Serie();
                        pump.add(elem, Repetitions.once);
                        rule.add(pump);
                    }
                }
            }
            rules.add(rule);
        } else if (type==RealizeType.complex) {
            List<List<RealizedRule>> list3 = makeList3();
            int max = maxSubrulesCount(list3);
            for (int i=0; i<max; i++) {
                int index3 = 0;
                RealizedRule rule = new RealizedRule();
                for (var elem : list) {
                    if (elem instanceof LexerRule || elem instanceof Nonterminal)
                        rule.add(elem);
                    else if (elem instanceof  Serie || elem.getClass()== AltSet.class) {
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

    private int maxSubrulesCount(List<List<RealizedRule>> list3) {
        int max = 0;
        for(var l: list3) {
            max = Math.max(max,l.size());
        }
        return max;
    }

    private List<List<RealizedRule>> makeList3() {
        List<List<RealizedRule>> subruleList = new ArrayList<>();
        for (int i=0; i<list.size(); i++) {
            var rep = reps.get(i);
            var elem = list.get(i);
            if (elem instanceof  Serie) {
                var subrules = ((Serie)elem).makeRealizedRules();
                List<RealizedRule> newRules = new ArrayList<>();
                switch (rep) {
                    case once:newRules.addAll(subrules); break;
                    case maybe:{
                        RealizedRule emptyRule = new RealizedRule();
                        newRules.add(emptyRule);
                        newRules.addAll(subrules);
                    }break;
                    case oneOrMore:{
                        newRules.addAll(subrules);
                        newRules.addAll(makeSubRulesTwice(subrules));
                        var pump = ((Serie)elem).setRepOnce();
                        if (pump!=null)
                            newRules.get(0).add(pump);
                    } break;
                    case zeroOrMore:{
                        RealizedRule emptyRule = new RealizedRule();
                        newRules.add(emptyRule);
                        newRules.addAll(makeSubRulesTwice(subrules));
                        var pump = ((Serie)elem).setRepOnce();
                        if (pump!=null)
                            newRules.get(0).add(pump);
                    } break;
                }
                subruleList.add(newRules);
            }
            else if  (elem.getClass()== AltSet.class) {
                var subrules = ((AltSet)elem).makeRealizedRules();
                List<RealizedRule> newRules = new ArrayList<>();
                switch (rep) {
                    case once:newRules.addAll(subrules); break;
                    case maybe:{
                        RealizedRule emptyRule = new RealizedRule();
                        newRules.add(emptyRule);
                        newRules.addAll(subrules);
                    }break;
                    case oneOrMore:{
                        newRules.addAll(subrules);
                        newRules.add(join(subrules));
                        newRules.get(0).add(((AltSet)elem).setRepOnce());
                    } break;
                    case zeroOrMore:{
                        RealizedRule emptyRule = new RealizedRule();
                        newRules.add(emptyRule);
                        newRules.add(join(subrules));
                        newRules.get(0).add(((AltSet)elem).setRepOnce());
                    } break;
                }
                subruleList.add(newRules);
            }
        }
        return subruleList;
    }

    private RealizedRule join(List<RealizedRule> subrules) {
        RealizedRule result = new RealizedRule();
        for (var l: subrules)
            result.addAll(l);
        return result;
    }

    Serie setRepOnce() {
        Serie resSerie = new Serie();
        for (int i=0; i<list.size(); i++) {
            var elem = list.get(i);
            var rep = reps.get(i);
            if (elem instanceof LexerRule || elem instanceof Nonterminal)
                resSerie.add(elem, Repetitions.once);
            else if (elem instanceof Serie) {
                var subserie = ((Serie)elem).setRepOnce().list;
                resSerie.addAllOnce(subserie);
            } if (elem.getClass()== AltSet.class) {
                if (rep==Repetitions.oneOrMore || rep==Repetitions.zeroOrMore) {
                    var subserie = ((AltSet) elem).setRepOnce().list;
                    resSerie.addAllOnce(subserie);
                } else {
                    return null;
                }
            }
        }
        return resSerie;
    }

    private RealizeType getRealizeType() {
        for (var elem: list)
            if (elem instanceof  Serie || elem.getClass()== AltSet.class)
                return RealizeType.complex;
        for (var rep: reps)
            if (rep!=Repetitions.once)
                return RealizeType.rep;
        return RealizeType.simple;
    }

    boolean pumpContain(Nonterminal nt) {
        for (var sym: list)
            if (sym==nt) return true;
        return false;
    }

    int pumpNTcount() {
        int counter = 0;
        for (var sym: list)
            if (sym instanceof Nonterminal) counter++;
        return counter;
    }

}
