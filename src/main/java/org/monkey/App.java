package org.monkey;

import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.antlr.parser.antlr4.ANTLRv4Parser;
import org.antlr.parser.antlr4.ANTLRv4ParserBaseVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws Exception {
        String inputFile = null;
        if ( args.length>0 ) inputFile = args[0];
        InputStream is = System.in;
        if ( inputFile!=null ) is = new FileInputStream(inputFile);
        ANTLRInputStream input = new ANTLRInputStream(is);
        /*ANTLRv4Lexer lexer = new ANTLRv4Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ANTLRv4Parser parser = new ANTLRv4Parser(tokens);
        ANTLRv4Parser.GrammarSpecContext context = parser.grammarSpec();
        MonkeyVisitor visitor = new MonkeyVisitor();
        Object res = visitor.visitGrammarSpec(context);
        //
        parser.addParseListener(new MonkeyListener());
        ANTLRv4Parser.GrammarSpecContext context = parser.grammarSpec();*/
        ANTLRv4Lexer lexer = new ANTLRv4Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ANTLRv4Parser parser = new ANTLRv4Parser(tokens);
        ANTLRv4Parser.GrammarSpecContext tree = parser.grammarSpec(); // parse a compilationUnit
        MonkeyListener extractor = new MonkeyListener();
        ParseTreeWalker.DEFAULT.walk(extractor, tree);
        System.out.println(extractor.parserRules.size());
    }
}
