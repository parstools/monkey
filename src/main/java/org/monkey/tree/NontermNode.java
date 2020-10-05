package org.monkey.tree;

import org.monkey.gram.*;
import org.monkey.lexer.Type;
import org.monkey.lexer.LexerRule;

import java.util.ArrayList;
import java.util.List;

public class NontermNode extends Node {
    protected List<Node> childs = new ArrayList<>();

    int actualAlt;
    int pumpCount;

    public NontermNode(Nonterminal nonterminal) {
        this.ntORt = nonterminal;
    }

    private static boolean better(RealizedRule rule, RealizedRule choosedRule) {
        if (rule.useCount==choosedRule.useCount)
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
        Nonterminal nt = (Nonterminal)ntORt;
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
            if (elem.getType()== Type.nonterminal) {
                NontermNode node = new NontermNode((Nonterminal) elem);
                childs.add(node);
            }
            else if (elem.getType()==Type.LexerRule) {
                TermNode node = new TermNode((LexerRule) elem);
                childs.add(node);
            }
        }
        for (var elem: childs)
            if (elem.ntORt.getType()==Type.nonterminal)
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
            if (elem.getType()==Type.nonterminal) {
                NontermNode node = new NontermNode((Nonterminal) elem);
                childs.add(index+pumpSize*k+i, node);
            }
            else if (elem.getType()==Type.LexerRule) {
                TermNode node = new TermNode((LexerRule) elem);
                childs.add(index+pumpSize*k+i, node);
            }
        }
        for (int k=0; k<pr.count; k++)
        for (int i=0; i<pumpSize; i++) {
            Node node = childs.get(index+pumpSize*k+i);
            if (node.ntORt.getType()==Type.nonterminal)
                ((NontermNode)node).buildTree(h+1);
        }
    }
}
