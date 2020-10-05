package org.monkey.pars;

import org.monkey.lexer.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ParserRule extends AltList {
    public String name;
    public ParserRule(String name) {
        this.name = name;
    }

    public void addAlternatives(List<Alternative> alternatives) {
        this.alternatives = alternatives;
    }

    @Override
    public String toString() {
        String s = name+": ";
        boolean first = true;
        for (var alt:alternatives) {
            if (!first)  s+= " |";
            first = false;
            s+=alt.toString();
        }
        s+=";";
        return s;
    }

    @Override
    public Type getType() {
        return Type.ParserRule;
    }

    /*
    Realize
     */
    public  List<RealizedRule1> realizedRules;
    public HashMap<ParserRule, PrPumps> realizedChilds;

    private HashMap<ParserRule, PrPumps> makeRealizedChilds() {
        HashMap<ParserRule,PrPumps> PrPumps = new HashMap<>();
        for (var rule: realizedRules) {
            for (var sym : rule.list) {
                if (sym instanceof Atom && ((Atom)sym).cargoParserRule!=null) {
                    if (!PrPumps.containsKey(sym))
                        PrPumps.put((ParserRule)sym, new PrPumps(sym));
                } else if (sym instanceof Pump) {
                    for (var subsym : ((Pump)sym).list) {
                        if (subsym instanceof Atom && ((Atom)subsym).cargoParserRule!=null)
                        {
                            if (!PrPumps.containsKey(subsym))
                                PrPumps.put((ParserRule)subsym, new PrPumps(subsym));
                            PrPumps.get(subsym).addPump((Pump)sym);
                        }
                    }
                }
            }
        }
        return PrPumps;
    }

    public void realize() {
        realizedRules = makeRealizedRules();
        for (var rule:realizedRules)
            rule.computeNTcount();
        realizedChilds = makeRealizedChilds();
    }

    public boolean visited = false;
    public List<ParserRule> childs = new ArrayList<>();
    public List<ParserRule> parents = new ArrayList<>();

    public List<ParserRule> getChildPR() {
        List<ParserRule> ntList = new ArrayList<>();
        for (Alternative alt:alternatives) {
            List<ParserRule> ntSubList = alt.getChildPR();
            ntList.addAll(ntSubList);
        }
        ntList = new ArrayList<>(new HashSet<>(ntList));//remove duplicates
        return ntList;
    }

    public void setChilds() {childs = getChildPR();}
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

    static PumpRule1 getBestPump(List<ParserRule> parents, ParserRule nt) {
        if (parents.size()==0) return null;
        PumpRule1 bestpr = new PumpRule1();
        for (var parent: parents) {
            PumpRule1 pr = parent.getBestPump(nt);
            if (ParserRule.betterPump(pr.s, bestpr.s)) bestpr = pr;
        }
        return bestpr;
    }

    public void checkNotCovered(List<PumpRule1> pumps) {
        if (visited) return;
        visited = true;
        for (int i=0; i<realizedRules.size(); i++) {
            var rule = realizedRules.get(i);
            if (rule.useCount==0 && i>0) {
                System.out.println(i);
                PumpRule1 bestPump = getBestPump(parents, this);
                if (bestPump!=null) pumps.add(bestPump);
            }
        }
        for (var elem: childs)
            elem.checkNotCovered(pumps);
    }

    public PumpRule1 getBestPump(ParserRule pr) {
        PumpRule1 bestpr = new PumpRule1();
        for (var rule: realizedRules) {
            for (var sym: rule.list)
                if (sym instanceof Pump) {
                    if (((Pump)sym).pumpContain(pr))
                        if (betterPump((Pump)sym,bestpr.s)) {
                            bestpr.s = (Pump) sym;
                            bestpr.r = rule;
                        }
                }
        }
        return bestpr;
    }

    public static boolean betterPump(Pump pump, Pump choosedPump) {
        if (choosedPump==null) return true;
        else if (pump==null) return false;
        int punpCount = pump.pumpPRcount();
        int punpChoosedCount = choosedPump.pumpPRcount();
        if (punpCount==punpChoosedCount)
            return pump.list.size()<choosedPump.list.size();
        else
            return punpCount<punpChoosedCount;
    }
}
