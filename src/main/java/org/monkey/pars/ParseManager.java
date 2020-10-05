package org.monkey.pars;

import org.antlr.parser.antlr4.ANTLRv4Parser;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.ArrayList;
import java.util.List;

public class ParseManager {
    public static List<LexerRule> createLexerRules(List<ANTLRv4Parser.LexerRuleSpecContext> lexerRules) throws Exception {
        List<LexerRule> resultList = new ArrayList<>();
        for (var rule: lexerRules) {
            LexerRule token = createLexerRule(rule);
            resultList.add(token);
        }
        return resultList;
    }

    private static LexerRule createLexerRule(ANTLRv4Parser.LexerRuleSpecContext ruleCtx) throws Exception {
        String name = getLexerRuleName(ruleCtx);
        List<ANTLRv4Parser.LexerAltContext> alternativesCtx = getAlternativesCtx3(ruleCtx);
        List<LexerAlt>  alternatives = createLexerAlts(alternativesCtx);
        LexerRule lr = new LexerRule(name);
        lr.addAlternatives(alternatives);
        return lr;
    }

    private static String getLexerRuleName(ANTLRv4Parser.LexerRuleSpecContext ctx) {
        for (int i=0; i<ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof TerminalNodeImpl) {
                TerminalNodeImpl term = (TerminalNodeImpl)ctx.getChild(i);
                return term.getText();
            }
        }
        throw new ParseException("Not found name of lexer rule");
    }

    public static List<ParserRule> createParserRules(List<ANTLRv4Parser.ParserRuleSpecContext> parserRules) throws Exception {
        List<ParserRule> resultList = new ArrayList<>();
        for (var rule: parserRules) {
            ParserRule pr = createParserRule(rule);
            resultList.add(pr);
        }
        return resultList;
    }

    private static ParserRule createParserRule(ANTLRv4Parser.ParserRuleSpecContext ruleCtx) throws Exception {
        String name = getParseRuleName(ruleCtx);
        List<ANTLRv4Parser.AlternativeContext> alternativesCtx = getAlternativesCtx1(ruleCtx);
        List<Alternative>  alternatives = createAlternatives(alternativesCtx);
        ParserRule pr = new ParserRule(name);
        pr.addAlternatives(alternatives);
        return pr;
    }

    private static List<Alternative> createAlternatives(List<ANTLRv4Parser.AlternativeContext> alternativesCtx) throws Exception {
        List<Alternative>  alternatives = new ArrayList<>();
        for (var altCtx: alternativesCtx)
            alternatives.add(createAlternative(altCtx));
        return alternatives;
    }

    private static List<LexerAlt> createLexerAlts(List<ANTLRv4Parser.LexerAltContext> alternativesCtx) {
        List<LexerAlt>  alternatives = new ArrayList<>();
        for (var altCtx: alternativesCtx)
            alternatives.add(createLexerAlt(altCtx));
        return alternatives;
    }

    private static LexerAlt createLexerAlt(ANTLRv4Parser.LexerAltContext altCtx) {
        LexerAlt alternative = new LexerAlt();
        List<ANTLRv4Parser.LexerElementContext> elementsCtx = getLexerElementsCtx(altCtx);
        List<LexerElement> elements = createLexerElements(elementsCtx);
        alternative.addLexerElements(elements);
        return alternative;
    }

    private static List<LexerElement> createLexerElements(List<ANTLRv4Parser.LexerElementContext> elementsCtx) {
        if (elementsCtx==null) return null;
        List<LexerElement> elements = new ArrayList<>();
        for (var ctx: elementsCtx)
            elements.add(createLexerElement(ctx));
        return elements;
    }

    private static List<ANTLRv4Parser.LexerElementContext> getLexerElementsCtx(ANTLRv4Parser.LexerAltContext altCtx) {
        if (altCtx.children==null) return null;
        List<ANTLRv4Parser.LexerElementContext> list = new ArrayList<>();
        for (var ctx: altCtx.children) {
            if (ctx instanceof ANTLRv4Parser.LexerElementContext) {
                var elemCtx = (ANTLRv4Parser.LexerElementContext)ctx;
                list.add(elemCtx);
            }
        }
        return list;
    }

    private static Alternative createAlternative(ANTLRv4Parser.AlternativeContext altCtx) throws Exception {
        Alternative alternative = new Alternative();
        List<ANTLRv4Parser.ElementContext> elementsCtx = getElementsCtx(altCtx);
        List<Element> elements = createElements(elementsCtx);
        alternative.addElements(elements);
        return alternative;
    }

    private static List<Element> createElements(List<ANTLRv4Parser.ElementContext> elementsCtx) throws Exception {
        if (elementsCtx==null) return null;
        List<Element>  elements = new ArrayList<>();
        for (var ctx: elementsCtx)
            elements.add(createElement(ctx));
        return elements;
    }

    private static LexerElement createLexerElement(ANTLRv4Parser.LexerElementContext ctx) {
        var childCtx = ctx.getChild(0);
        if (childCtx instanceof ANTLRv4Parser.LexerAtomContext) {
            Repetitions rep = Repetitions.once;
            if (ctx.getChildCount()>1)
                rep = repFromSuffix((ANTLRv4Parser.EbnfSuffixContext)ctx.getChild(1));
            return createLexerAtom((ANTLRv4Parser.LexerAtomContext)childCtx, rep);
        }
        else if (childCtx instanceof ANTLRv4Parser.LexerBlockContext) {
            Repetitions rep = Repetitions.once;
            if (ctx.getChildCount()>1)
                rep = repFromSuffix((ANTLRv4Parser.EbnfSuffixContext)ctx.getChild(1));
            return createLexerBlock((ANTLRv4Parser.LexerBlockContext)childCtx, rep);
        } else throw new ParseException("not supported element Type "+childCtx.getClass().toString());
    }

    private static LexerElement createLexerBlock(ANTLRv4Parser.LexerBlockContext ctx, Repetitions rep) {
        for (var el: ctx.children) {
            if (el instanceof ANTLRv4Parser.LexerAltListContext) {
                LexerAltList block = createLexerAltList((ANTLRv4Parser.LexerAltListContext) el);
                block.rep  = rep;
                return block;
            }
        }
        throw new ParseException("no alexerAltList");
    }

    private static LexerAltList createLexerAltList(ANTLRv4Parser.LexerAltListContext ctx) {
        LexerAltList result = new LexerAltList();
        var altListCtx = getAlternativesCtx4(ctx);
        List<LexerAlt>  altList = createLexerAlts(altListCtx);
        result.addAlts(altList);
        return result;
    }

    private static Element createElement(ANTLRv4Parser.ElementContext ctx) throws Exception {
            var childCtx = ctx.getChild(0);
            if (childCtx instanceof ANTLRv4Parser.AtomContext) {
                Repetitions rep = Repetitions.once;
                if (ctx.getChildCount()>1)
                    rep = repFromSuffix((ANTLRv4Parser.EbnfSuffixContext)ctx.getChild(1));
                return createAtom((ANTLRv4Parser.AtomContext)childCtx, rep);
            }
            else if (childCtx instanceof ANTLRv4Parser.EbnfContext) {
                return createEbnf((ANTLRv4Parser.EbnfContext)childCtx);
            } else throw new ParseException("not supported element Type "+childCtx.getClass().toString());
    }

    private static Element createEbnf(ANTLRv4Parser.EbnfContext ctx) throws Exception {
        Ebnf ebnf = new Ebnf();
        Repetitions rep = Repetitions.once;
        if (ctx.getChildCount()>1)
            rep = repFromSuffix((ANTLRv4Parser.BlockSuffixContext)ctx.getChild(1));
        ANTLRv4Parser.BlockContext blockCtx = (ANTLRv4Parser.BlockContext)ctx.getChild(0);
        for (var el: blockCtx.children) {
            if (el instanceof ANTLRv4Parser.AltListContext) {
                AltList block = createAltList((ANTLRv4Parser.AltListContext) el);
                block.rep  = rep;
                return block;
            }
        }
        throw new ParseException("bad block");
    }

    private static AltList createAltList(ANTLRv4Parser.AltListContext ctx) throws Exception {
        AltList result = new AltList();
        var altListCtx = getAlternativesCtx2(ctx);
        List<Alternative>  altList = createAlternatives(altListCtx);
        result.addAlts(altList);
        return result;
    }

    private static LexerAtom createLexerAtom(ANTLRv4Parser.LexerAtomContext ctx, Repetitions rep) {
        LexerAtom atom = new LexerAtom();
        var childCtx = ctx.getChild(0);
        if (childCtx instanceof ANTLRv4Parser.TerminalContext) {
            var child2Ctx = childCtx.getChild(0);
            atom.cargo = ((TerminalNodeImpl)child2Ctx).getText();
            if (atom.cargo.charAt(0)=='\'')
                atom.kind = RefKind.TokenLiteral;
            else
                atom.kind = RefKind.TokenRef;
        }
        else throw new ParseException("Atom - not implemented alternative");
        atom.rep = rep;
        return atom;
    }

    private static Element createAtom(ANTLRv4Parser.AtomContext ctx, Repetitions rep) {
        Atom atom = new Atom();
        var childCtx = ctx.getChild(0);
        if (childCtx instanceof ANTLRv4Parser.TerminalContext) {
            var child2Ctx = childCtx.getChild(0);
            atom.cargo = ((TerminalNodeImpl)child2Ctx).getText();
            if (atom.cargo.charAt(0)=='\'')
                atom.kind = RefKind.TokenLiteral;
            else
                atom.kind = RefKind.TokenRef;
        } else if (childCtx instanceof TerminalNodeImpl) {
            atom.kind = RefKind.RuleRef;
            atom.cargo = ((TerminalNodeImpl)childCtx).getText();
        }
        else throw new ParseException("Atom - not implemented alternative");
        atom.rep = rep;
        return atom;
    }

    private static Repetitions repFromSuffix(ANTLRv4Parser.BlockSuffixContext ctx) {
        return repFromSuffix((ANTLRv4Parser.EbnfSuffixContext)ctx.getChild(0));
    }

    private static Repetitions repFromSuffix(ANTLRv4Parser.EbnfSuffixContext ctx) {
        if (ctx.getChild(0) instanceof TerminalNodeImpl) {
            TerminalNodeImpl term = (TerminalNodeImpl)ctx.getChild(0);
            String repStr = term.getText();
            switch (repStr) {
               case "?": return Repetitions.maybe;
                case "*": return Repetitions.zeroOrMore;
                case "+": return Repetitions.oneOrMore;
                default: throw new ParseException("must be ? * +");
            }
        } else throw new ParseException("must be terminal");
    }

    private static List<ANTLRv4Parser.ElementContext> getElementsCtx(ANTLRv4Parser.AlternativeContext altCtx) {
        if (altCtx.children==null) return null;
        List<ANTLRv4Parser.ElementContext> list = new ArrayList<>();
        for (var ctx: altCtx.children) {
            if (ctx instanceof ANTLRv4Parser.ElementContext) {
                var elemCtx = (ANTLRv4Parser.ElementContext)ctx;
                list.add(elemCtx);
            }
        }
        return list;
    }

    private static List<ANTLRv4Parser.AlternativeContext> getAlternativesCtx1(ANTLRv4Parser.ParserRuleSpecContext ctx) throws Exception {
        List<ANTLRv4Parser.AlternativeContext> alternativesCtx = new ArrayList<>();
        ANTLRv4Parser.RuleBlockContext block = null;
        for (int i=0; i<ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof ANTLRv4Parser.RuleBlockContext) {
                block = (ANTLRv4Parser.RuleBlockContext)ctx.getChild(i);
            }
        }
        if (block==null) throw new ParseException("not found RuleBlock");
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

    private static List<ANTLRv4Parser.LexerAltContext> getAlternativesCtx3(ANTLRv4Parser.LexerRuleSpecContext ctx) throws Exception {
        List<ANTLRv4Parser.LexerAltContext> alternativesCtx = new ArrayList<>();
        ANTLRv4Parser.LexerRuleBlockContext block = null;
        for (int i=0; i<ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof ANTLRv4Parser.LexerRuleBlockContext) {
                block = (ANTLRv4Parser.LexerRuleBlockContext)ctx.getChild(i);
            }
        }
        if (block==null) throw new ParseException("not found LexerRuleBlock");
        ANTLRv4Parser.LexerAltListContext altList = (ANTLRv4Parser.LexerAltListContext)block.getChild(0);
        for (var elem: altList.children) {
            if (elem instanceof ANTLRv4Parser.LexerAltContext) {
                alternativesCtx.add((ANTLRv4Parser.LexerAltContext)elem);
            }
        }
        return alternativesCtx;
    }

    private static List<ANTLRv4Parser.AlternativeContext> getAlternativesCtx2(ANTLRv4Parser.AltListContext ctx) throws Exception {
        List<ANTLRv4Parser.AlternativeContext> alternativesCtx = new ArrayList<>();
        for (var el: ctx.children) {
            if (el instanceof ANTLRv4Parser.AlternativeContext) {
                alternativesCtx.add((ANTLRv4Parser.AlternativeContext)el);
            }
        }
        return alternativesCtx;
    }

    private static List<ANTLRv4Parser.LexerAltContext> getAlternativesCtx4(ANTLRv4Parser.LexerAltListContext ctx) {
        List<ANTLRv4Parser.LexerAltContext> alternativesCtx = new ArrayList<>();
        for (var el: ctx.children) {
            if (el instanceof ANTLRv4Parser.LexerAltContext) {
                alternativesCtx.add((ANTLRv4Parser.LexerAltContext)el);
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
        throw new ParseException("Not found name of parsing rule");
    }
}
