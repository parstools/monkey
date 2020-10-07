package org.monkey.lexer;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class ParsExceptionCtx extends RuntimeException{
    private static String moreInfo(String message, ParseTree ctx) {
        if (ctx instanceof ParserRuleContext) {
            var start = ((ParserRuleContext)ctx).start;
            return String.format("%s class=%s, in %d:%d", message, ctx.getClass().toString(),
                    start.getLine(), start.getCharPositionInLine());
        } else return message;
    }
    public ParsExceptionCtx(String message, ParseTree ctx) {
        super(moreInfo(message,ctx));
    }
}
