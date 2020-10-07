package org.monkey.lexer;

import org.antlr.v4.runtime.ParserRuleContext;

public class ParsException extends RuntimeException{
    public ParsException(String message) {
        super(message);
    }
}
