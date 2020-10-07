package org.monkey.gram;

import org.monkey.lexer.LexerRule;
import org.monkey.lexer.Type;
import org.monkey.pars.Atom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Nonterminal implements Updatable {
    public boolean isManyCalled = false;
    protected List<Serie> list = new ArrayList<>();
    public String name;
    public Nonterminal(String name) {
        this.name = name;
    }

    public  List<RealizedRule> realizedRules;
    public HashMap<Nonterminal,NtPumps> realizedChilds;

    private HashMap<Nonterminal,NtPumps> makeRealizedChilds() {
        HashMap<Nonterminal,NtPumps> ntPumps = new HashMap<>();
        for (var rule: realizedRules) {
            for (var sym : rule.list) {
                if (sym.getClass() == Atom.class) {
                    Nonterminal nt = ((Atom)sym).cargoNtRule;
                    if (nt!=null) {
                        if (!ntPumps.containsKey(nt))
                            ntPumps.put(nt, new NtPumps(nt));
                    }
                } else if (sym instanceof Serie) {
                    for (var subsym : ((Serie)sym).list) {
                        if (subsym.getClass()==Atom.class)
                        {
                            Nonterminal nt = ((Atom)subsym).cargoNtRule;
                            if (!ntPumps.containsKey(nt))
                                ntPumps.put(nt, new NtPumps(nt));
                            ntPumps.get(nt).addPump((Serie)sym);
                        }
                    }
                }
            }
        }
        return ntPumps;
    }

    public List<RealizedRule> makeRealizedRules() {
        List<RealizedRule> rules = new ArrayList<>();
        for (var alt : list){
            var subrules = alt.makeRealizedRules();
            rules.addAll(subrules);
        }
        return rules;
    }

    public void realize() {
        realizedRules = makeRealizedRules();
        for (var rule:realizedRules)
            rule.computeNTcount();
        realizedChilds = makeRealizedChilds();
    }

    public boolean visited = false;
    public Type getType() {return Type.nonterminal;}
    public List<Nonterminal> childs = new ArrayList<>();
    public List<Nonterminal> parents = new ArrayList<>();
    public void setChilds() {childs = getChildNT();}
    public void updateParents() {
        for (var nt: childs)
            nt.parents.add(this);
    }
    public void clearVisitetTree() {
        if (!visited) return;
        visited = false;
        for (var nt: childs) {
            nt.clearVisitetTree();
        }
    }
    public void setChildsTree() {
        if (visited) return;
        visited = true;
        setChilds();
        for (var nt: childs) {
            nt.setChildsTree();
        }
    }

    public void realizeTree() {
        if (visited) return;
        visited = true;
        realize();
        for (var nt: childs) {
            nt.realizeTree();
        }
    }

    public void updateParentsTree() {
        if (visited) return;
        visited = true;
        updateParents();
        for (var nt: childs) {
            nt.updateParentsTree();
        }
    }

    static PumpRule getBestPump(List<Nonterminal> parents, Nonterminal nt) {
        if (parents.size()==0) return null;
        PumpRule bestpr = null;
        for (var parent: parents) {
            PumpRule pr = parent.getBestPump(nt);
            if (pr!=null) {
                if (bestpr==null)
                    bestpr = pr;
                else
                    if (Nonterminal.betterPump(pr.s, bestpr.s)) bestpr = pr;
            }
        }
        return bestpr;
    }

    List<Nonterminal> getPumpedNt() {
        List<Nonterminal> pumpedNt = new ArrayList<>();
        for (var rule:realizedRules) {
            List<Nonterminal> sublist = rule.getPumpedNt();
            pumpedNt.addAll(sublist);
        }
        pumpedNt = new ArrayList<>(new HashSet<>(pumpedNt));//remove duplicates
        return pumpedNt;
    }

    public void setManyCalled(boolean set) {
        if (visited) return;
        isManyCalled = set;
        List<Nonterminal> childs = getChildNT();
        List<Nonterminal> pumpedList = getPumpedNt();
        visited = true;
        for (var child: childs) {
            boolean b;
            if (set) b=true;
            else b = pumpedList.indexOf(child)>=0;
            child.setManyCalled(b);
        }
    }

    public void checkNotCovered(List<PumpRule> pumps) {
        if (visited) return;
        visited = true;
        for (int i=0; i<realizedRules.size(); i++) {
            var rule = realizedRules.get(i);
            if (rule.useCount==0 && i>0) {
                PumpRule bestPump = getBestPump(parents, this);
                if (bestPump!=null) pumps.add(bestPump);
            }
        }
        for (var elem: childs)
            if (elem instanceof Nonterminal) {
                ((Nonterminal)elem).checkNotCovered(pumps);
            }
    }

    public PumpRule getBestPump(Nonterminal nt) {
        PumpRule bestpr = null;
        for (var rule: realizedRules) {
            for (var sym: rule.list)
                if (sym instanceof Serie) {
                    if (((Serie)sym).pumpContain(nt))
                        if (bestpr==null || betterPump((Serie)sym,bestpr.s)) {
                            if (bestpr==null) bestpr = new PumpRule();
                            bestpr.s = (Serie) sym;
                            bestpr.r = rule;
                        }
                }
        }
        return bestpr;
    }

    public static boolean betterPump(Serie pump, Serie choosedPump) {
        if (choosedPump==null) return true;
        else if (pump==null) return false;
        int punpCount = pump.pumpNTcount();
        int punpChoosedCount = choosedPump.pumpNTcount();
        if (punpCount==punpChoosedCount)
            return pump.list.size()<choosedPump.list.size();
        else
            return punpCount<punpChoosedCount;
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

    public void add(Serie alt) {
        list.add(alt);
    }

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
}
