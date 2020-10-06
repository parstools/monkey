package org.monkey;

import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.antlr.parser.antlr4.ANTLRv4Parser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.monkey.gram.Importer;
import org.monkey.gram.Nonterminal;
import org.monkey.gram.PumpRule;
import org.monkey.lexer.LexerRule;
import org.monkey.pars.ParseManager;
import org.monkey.pars.ParserRule;
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
    static Nonterminal initGram(MonkeyListener extractor) throws Exception {
        List<LexerRule>  rulesL = ParseManager.createLexerRules(extractor.lexerRules);
        List<ParserRule>  rulesP = ParseManager.createParserRules(extractor.parserRules);
        List<Nonterminal>  rulesPI = new ArrayList<>();
        for (var pr: rulesP) {
            rulesPI.add(Importer.imp(pr));
        }
        HashMap<String, LexerRule> lexerMap = new HashMap<>();
        HashMap<String, Nonterminal> parserMap = new HashMap<>();
        for (var rule: rulesL)
            lexerMap.put(rule.name, rule);
        for (var nt: rulesPI)
            parserMap.put(nt.name, nt);
        for (var rule: rulesL) {
            rule.updateLexerRef(lexerMap);
        }
        for (var rule: rulesPI) {
            rule.updateLexerRef(lexerMap);
            rule.updateNtRef(parserMap);
        }
        return rulesPI.get(0);
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
            test(extractor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test(MonkeyListener extractor) throws Exception {
        Nonterminal start = initGram(extractor);
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
