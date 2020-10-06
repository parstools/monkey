package org.monkey.lexer;

import org.monkey.gram.Nonterminal;
import org.monkey.pars.ParserRule;

import java.util.HashMap;

public abstract class Repetitive {
    public Repetitions rep;
    public abstract Type getType();// {return Type.repetitive;}

    public abstract String realizeString();

    protected String suffixToString() {
        if (rep==Repetitions.maybe)
            return "?";
        else if (rep==Repetitions.oneOrMore)
            return "+";
        else if (rep==Repetitions.zeroOrMore)
            return "*";
        else
            return "";
    }
    public abstract void updateLexerRef(HashMap<String, LexerRule> lexerMap);

//    public abstract void updateParserRef(HashMap<String, ParserRule> parserMap);

    public abstract void updateNtRef(HashMap<String, Nonterminal> parserMap);
}
