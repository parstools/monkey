package org.monkey;

import org.antlr.parser.antlr4.ANTLRv4Parser;
import org.antlr.parser.antlr4.ANTLRv4ParserBaseListener;

public class MonkeyListener extends ANTLRv4ParserBaseListener {
    @Override public void enterGrammarSpec(ANTLRv4Parser.GrammarSpecContext ctx) {
        System.out.println("enterGrammarSpec");
    }
    @Override public void exitGrammarSpec(ANTLRv4Parser.GrammarSpecContext ctx) {
        System.out.println("exitGrammarSpec");
    }
}
