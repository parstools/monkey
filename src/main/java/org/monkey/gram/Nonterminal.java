package org.monkey.gram;

import org.monkey.lexer.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Nonterminal extends AltList {
    String name;
    public Nonterminal(String name) {
        this.name = name;
    }

    public  List<RealizedRule> realizedRules;
    public HashMap<Nonterminal,NtPumps> realizedChilds;

    private HashMap<Nonterminal,NtPumps> makeRealizedChilds() {
        HashMap<Nonterminal,NtPumps> ntPumps = new HashMap<>();
        for (var rule: realizedRules) {
            for (var sym : rule.list) {
                if (sym instanceof Nonterminal) {
                    if (!ntPumps.containsKey(sym))
                        ntPumps.put((Nonterminal)sym, new NtPumps(sym));
                } else if (sym instanceof Serie) {
                    for (var subsym : ((Serie)sym).list) {
                        if (subsym instanceof Nonterminal)
                        {
                            if (!ntPumps.containsKey(subsym))
                                ntPumps.put((Nonterminal)subsym, new NtPumps(subsym));
                            ntPumps.get(subsym).addPump((Serie)sym);
                        }
                    }
                }
            }
        }
        return ntPumps;
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
        PumpRule bestpr = new PumpRule();
        for (var parent: parents) {
            PumpRule pr = parent.getBestPump(nt);
            if (Nonterminal.betterPump(pr.s, bestpr.s)) bestpr = pr;
        }
        return bestpr;
    }

    public void checkNotCovered(List<PumpRule> pumps) {
        if (visited) return;
        visited = true;
        for (int i=0; i<realizedRules.size(); i++) {
            var rule = realizedRules.get(i);
            if (rule.useCount==0 && i>0) {
                System.out.println(i);
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
        PumpRule bestpr = new PumpRule();
        for (var rule: realizedRules) {
            for (var sym: rule.list)
                if (sym instanceof Serie) {
                    if (((Serie)sym).pumpContain(nt))
                        if (betterPump((Serie)sym,bestpr.s)) {
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
}
