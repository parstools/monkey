package org.monkey;

import org.antlr.parser.antlr4.ANTLRv4Parser;
import org.antlr.parser.antlr4.ANTLRv4ParserBaseListener;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.ArrayList;
import java.util.List;

public class MonkeyListener extends ANTLRv4ParserBaseListener {
    List<ANTLRv4Parser.ParserRuleSpecContext> parserRules = new ArrayList<>();

    @Override public void enterParserRuleSpec(ANTLRv4Parser.ParserRuleSpecContext ctx) {
        for (int i=0; i<ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof TerminalNodeImpl) {
                TerminalNodeImpl term = (TerminalNodeImpl)ctx.getChild(i);
                System.out.println(term.getText());
                break;
            }
        }
        parserRules.add(ctx);
    }
}
