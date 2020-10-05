package org.monkey.gram;

import org.antlr.parser.antlr4.ANTLRv4Parser;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.monkey.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class ParsManager {
    private static List<Token> createTerminals(List<ANTLRv4Parser.LexerRuleSpecContext> lexerRules) {
        List<Token> resultList = new ArrayList<>();
        for (var rule: lexerRules) {
            Token token = createTerminal(rule);
            resultList.add(token);
        }
        return resultList;
    }

    private static Token createTerminal(ANTLRv4Parser.LexerRuleSpecContext rule) {
        return null;
    }

    private static List<ParserRule> createParserRules(List<ANTLRv4Parser.ParserRuleSpecContext> parserRules) throws Exception {
        List<ParserRule> resultList = new ArrayList<>();
        for (var rule: parserRules) {
            ParserRule pr = createParserRule(rule);
            resultList.add(pr);
        }
        return resultList;
    }

    private static ParserRule createParserRule(ANTLRv4Parser.ParserRuleSpecContext ruleCtx) throws Exception {
        String name = getParseRuleName(ruleCtx);
        List<ANTLRv4Parser.AlternativeContext> alternativesCtx = getAlternativesCtx(ruleCtx);
        List<Alternative>  alternatives = getAlternatives(alternativesCtx);
        ParserRule pr = new ParserRule(name);
        pr.addAlternatives(alternatives);
        return pr;
    }

    private static List<Alternative> getAlternatives(List<ANTLRv4Parser.AlternativeContext> alternativesCtx) {
        List<Alternative>  alternatives = new ArrayList<>();
        for (var altCtx: alternativesCtx)
            alternatives.add(getAlternative(altCtx));
        return alternatives;
    }

    private static Alternative getAlternative(ANTLRv4Parser.AlternativeContext altCtx) {
        Alternative alternative = new Alternative();
        List<ANTLRv4Parser.ElementContext> elementsCtx = getElementsCtx(altCtx);
        return alternative;
    }

    private static List<ANTLRv4Parser.ElementContext> getElementsCtx(ANTLRv4Parser.AlternativeContext altCtx) {
        List<ANTLRv4Parser.ElementContext> list = new ArrayList<>();
        for (var ctx: altCtx.children) {
            if (ctx instanceof ANTLRv4Parser.ElementContext) {
                var elemCtx = (ANTLRv4Parser.ElementContext)ctx;
                list.add(elemCtx);
            }
        }
        return list;
    }

    private static List<ANTLRv4Parser.AlternativeContext> getAlternativesCtx(ANTLRv4Parser.ParserRuleSpecContext ctx) throws Exception {
        List<ANTLRv4Parser.AlternativeContext> alternativesCtx = new ArrayList<>();
        ANTLRv4Parser.RuleBlockContext block = null;
        for (int i=0; i<ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof ANTLRv4Parser.RuleBlockContext) {
                block = (ANTLRv4Parser.RuleBlockContext)ctx.getChild(i);
            }
        }
        if (block==null) throw new Exception("not found RuleBlock");
        ANTLRv4Parser.RuleAltListContext altList = (ANTLRv4Parser.RuleAltListContext)block.getChild(0);
        for (var elem: altList.children) {
            if (elem instanceof ANTLRv4Parser.LabeledAltContext) {
                var labeledAlt = (ANTLRv4Parser.LabeledAltContext)elem;
                var alternative = (ANTLRv4Parser.AlternativeContext)labeledAlt.getChild(0);
                alternativesCtx.add(alternative);
            }
        }
        return alternativesCtx;
    }

    private static String getParseRuleName(ANTLRv4Parser.ParserRuleSpecContext ctx) throws Exception {
        for (int i=0; i<ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof TerminalNodeImpl) {
                TerminalNodeImpl term = (TerminalNodeImpl)ctx.getChild(i);
                return term.getText();
            }
        }
        throw new Exception("Not found name of parsing rule");
    }
}
