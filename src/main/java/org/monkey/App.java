package org.monkey;

import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.antlr.parser.antlr4.ANTLRv4Parser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.monkey.gram.AltSet;
import org.monkey.gram.Nonterminal;
import org.monkey.gram.PumpRule;
import org.monkey.gram.Serie;
import org.monkey.lexer.*;
import org.monkey.pars.*;
import org.monkey.tree.NontermNode;
import org.monkey.tree.RuleManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    static Nonterminal initGram2(MonkeyListener extractor) throws Exception {
        List<LexerRule>  rulesL = ParseManager.createLexerRules(extractor.lexerRules);
        List<ParserRule>  rulesP = ParseManager.createParserRules(extractor.parserRules);
        HashMap<String, LexerRule> lexerMap = new HashMap<>();
        HashMap<String, ParserRule> parserMap = new HashMap<>();
        for (var rule: rulesL)
            lexerMap.put(rule.name, rule);
        for (var rule: rulesP)
            parserMap.put(rule.name, rule);
        for (var rule: rulesL)
            rule.updateLexerRef(lexerMap);
        for (var rule: rulesP) {
            rule.updateLexerRef(lexerMap);
            rule.updateParserRef(parserMap);
        }

        for (int i=0; i<rulesL.size(); i++)
            System.out.println(rulesL.get(i).toString());
        Nonterminal e = new Nonterminal("e");
        Nonterminal m = new Nonterminal("m");
        Nonterminal p = new Nonterminal("p");

        ParserRule e1 = rulesP.get(0);
        ParserRule m1 = rulesP.get(1);
        ParserRule p1 = rulesP.get(2);
        var plus = rulesL.get(1);
        var minus = rulesL.get(2);
        var star = rulesL.get(3);
        var div = rulesL.get(4);
        var left = rulesL.get(5);
        var right= rulesL.get(6);
        var literal =  rulesL.get(0);

        Serie alt = new Serie();
        Serie subalt;
        alt.add(m, Repetitions.once);
        Serie serie;
        serie = new Serie();
        AltSet altList = new AltSet();
        subalt = new Serie();
        subalt.add(plus, Repetitions.once);
        altList.add(subalt);
        subalt = new Serie();
        subalt.add(minus, Repetitions.once);
        altList.add(subalt);
        serie.add(altList, Repetitions.once);
        serie.add(m, Repetitions.once);
        alt.add(serie, Repetitions.zeroOrMore);
        e.add(alt);

        alt = new Serie();
        alt.add(p, Repetitions.once);
        serie = new Serie();
        altList = new AltSet();
        subalt = new Serie();
        subalt.add(star, Repetitions.once);
        altList.add(subalt);
        subalt = new Serie();
        subalt.add(div, Repetitions.once);
        altList.add(subalt);
        serie.add(altList, Repetitions.once);
        serie.add(p, Repetitions.once);
        alt.add(serie, Repetitions.zeroOrMore);
        m.add(alt);

        alt = new Serie();
        alt.add(literal, Repetitions.once);
        p.add(alt);
        alt = new Serie();
        alt.add(left, Repetitions.once);
        alt.add(e, Repetitions.once);
        alt.add(right, Repetitions.once);
        p.add(alt);
        //p.realize();
        return e;
    }

    public static void main(String[] args) {
        String inputFile = null;
        if ( args.length>0 ) inputFile = args[0];
        InputStream is = System.in;
        if ( inputFile!=null ) {
            try {
                is = new FileInputStream(inputFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ANTLRInputStream input = null;
        try {
            input = new ANTLRInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ANTLRv4Lexer lexer = new ANTLRv4Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ANTLRv4Parser parser = new ANTLRv4Parser(tokens);
        ANTLRv4Parser.GrammarSpecContext tree = parser.grammarSpec(); // parse a compilationUnit
        MonkeyListener extractor = new MonkeyListener();
        ParseTreeWalker.DEFAULT.walk(extractor, tree);
        /*System.out.println(extractor.parserRules.size());
        try {
            List<ParserRule>  rulesP = ParseManager.createParserRules(extractor.parserRules);
            List<LexerRule>  rulesL = ParseManager.createLexerRules(extractor.lexerRules);
            System.out.println(rulesL.get(0).toString());
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            test2(extractor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test2(MonkeyListener extractor) throws Exception {
        Nonterminal start = initGram2(extractor);
        start.setChildsTree();
        start.clearVisitetTree();
        start.updateParentsTree();
        start.clearVisitetTree();
        start.realizeTree();
        start.clearVisitetTree();
        //todo: move to manager
        NontermNode node = new NontermNode(start);
        node.buildTree(0);
        List<PumpRule> pumps = new ArrayList<>();
        start.checkNotCovered(pumps);
        start.clearVisitetTree();
        pumps = makeUnique(pumps);
        RuleManager.expandPumps(pumps);
        node.print();
    }

    //@todo do managera
    private static List<PumpRule> makeUnique(List<PumpRule> pumps) {
        Collections.sort(pumps);
        List<PumpRule> unique = new ArrayList<>();
        PumpRule prev = null;
        for (int i=0; i<pumps.size(); i++) {
            PumpRule elem = pumps.get(i);
            if (i == 0 || elem.compareTo(prev) != 0)
            {
                prev = elem;
                unique.add(elem);
            } else {
                unique.get(unique.size()-1).count++;
            }
        }
        return unique;
    }

}
