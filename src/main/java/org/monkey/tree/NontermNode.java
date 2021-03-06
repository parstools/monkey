package org.monkey.tree;

import org.monkey.gram.Nonterminal;
import org.monkey.gram.PumpRule;
import org.monkey.gram.RealizedRule;
import org.monkey.gram.Serie;
import org.monkey.lexer.LexerRule;
import org.monkey.lexer.Type;
import org.monkey.pars.Atom;

import java.util.ArrayList;
import java.util.List;

public class NontermNode extends Node {
    protected List<Node> childs = new ArrayList<>();

    int actualAlt;
    int pumpCount;
    Nonterminal nt;

    public NontermNode(Nonterminal nonterminal) {
        this.nt = nonterminal;
    }

    private boolean better(RealizedRule rule, RealizedRule choosedRule) {
        if (rule.useCount==choosedRule.useCount)
            if (nt.isManyCalled)
                return rule.size() < choosedRule.size();
            else
                return rule.size() > choosedRule.size();
        else
            return rule.useCount < choosedRule.useCount;
    }

    private static boolean betterTerm(RealizedRule rule, RealizedRule choosedRule) {
        return rule.ntCount < choosedRule.ntCount;
    }

    RealizedRule choosedRule;

    int h;

    public void buildTree(int h) {
        this.h = h;
        //int altCount = nonterminal.getAltCount();
        //int realRepCount = nonterminal.getRealRepCount();
        int choosedIndex = 0;
        choosedRule = nt.realizedRules.get(choosedIndex);
        for (int i=1; i<nt.realizedRules.size(); i++) {
            RealizedRule rule = nt.realizedRules.get(i);
            if (better(rule, choosedRule)){
                choosedIndex = i;
                choosedRule = rule;
            }
        }
        if (choosedRule.useCount>=1) {
            choosedIndex = 0;
            choosedRule = nt.realizedRules.get(choosedIndex);
            for (int i=1; i<nt.realizedRules.size(); i++) {
                RealizedRule rule = nt.realizedRules.get(i);
                if (betterTerm(rule, choosedRule)){
                    choosedIndex = i;
                    choosedRule = rule;
                }
            }
        }
        choosedRule.useCount++;
        choosedRule.nodes.add(this);
        for (var elem: choosedRule.list) {
            if (elem.getClass()!=Atom.class) continue;
            Atom atom = (Atom)elem;
            if (atom.cargoNtRule!=null) {
                NontermNode node = new NontermNode(atom.cargoNtRule);
                childs.add(node);
            }
            else if (atom.cargoLexerRule!=null) {
                TermNode node = new TermNode(atom.cargoLexerRule);
                childs.add(node);
            }
        }
        for (var elem: childs)
            if (elem.getClass()==NontermNode.class)
                ((NontermNode)elem).buildTree(h+1);
    }

    @Override
    public void print() {
        for (var elem: childs)
            elem.print();
    }

    public void expandPump(PumpRule pr) {
        assert(pr.r==choosedRule);
        int index = choosedRule.list.indexOf(pr.s);
        assert(index>=0);
        Serie pump = (Serie)choosedRule.list.get(index);
        int pumpSize = pump.list.size();
        for (int k=0; k<pr.count; k++)
        for (int i=0; i<pumpSize; i++) {
            var elem = pump.list.get(i);
            if (elem.getClass()==Atom.class) {
                Atom atom = (Atom)elem;
                if (atom.cargoNtRule!=null) {
                    NontermNode node = new NontermNode(atom.cargoNtRule);
                    childs.add(index+pumpSize*k+i, node);
                }
                else if (atom.cargoLexerRule!=null) {
                    TermNode node = new TermNode(atom.cargoLexerRule);
                    childs.add(index+pumpSize*k+i, node);
                }
            }
        }
        for (int k=0; k<pr.count; k++)
        for (int i=0; i<pumpSize; i++) {
            Node node = childs.get(index+pumpSize*k+i);
            if (node.getClass()==NontermNode.class)
                ((NontermNode)node).buildTree(h+1);
        }
    }
}
