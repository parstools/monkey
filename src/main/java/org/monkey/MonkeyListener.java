package org.monkey;

import org.antlr.parser.antlr4.ANTLRv4Parser;
import org.antlr.parser.antlr4.ANTLRv4ParserBaseListener;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.ArrayList;
import java.util.List;

public class MonkeyListener extends ANTLRv4ParserBaseListener {
    List<ANTLRv4Parser.ParserRuleSpecContext> parserRules = new ArrayList<>();
    List<ANTLRv4Parser.LexerRuleSpecContext> lexerRules = new ArrayList<>();

    @Override public void enterParserRuleSpec(ANTLRv4Parser.ParserRuleSpecContext ctx) {
        parserRules.add(ctx);
    }

    @Override public void enterLexerRuleSpec(ANTLRv4Parser.LexerRuleSpecContext ctx) {
        lexerRules.add(ctx);
    }
}
