package org.monkey;

import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.antlr.parser.antlr4.ANTLRv4Parser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.monkey.pars.LexerRule;
import org.monkey.pars.ParseException;
import org.monkey.pars.ParseManager;
import org.monkey.pars.ParserRule;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
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
        System.out.println(extractor.parserRules.size());
        try {
            List<ParserRule>  rulesP = ParseManager.createParserRules(extractor.parserRules);
            List<LexerRule>  rulesL = ParseManager.createLexerRules(extractor.lexerRules);
            System.out.println(rulesL.get(0).toString());
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
