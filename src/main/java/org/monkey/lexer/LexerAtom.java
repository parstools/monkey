package org.monkey.lexer;

import org.monkey.gram.Nonterminal;
import org.monkey.gram.Updatable;
import org.monkey.pars.ParserRule;

import java.util.HashMap;

public class LexerAtom extends RepetIn implements Updatable {
    public String cargo;
    public RefKind kind;
    public LexerRule cargoLexerRule = null;
    public ParserRule cargoParserRule = null;
    public boolean invert;
    public Ranges ranges = null;

    @Override
    public String toString() {
        return cargo+suffixToString();
    }

    public String realizeString() {
        int count;
        if (rep== Repetitions.once || rep==Repetitions.maybe)
            count = 1;
        else
            count = 4;
        String result = "";
        for (int j=0; j<count; j++) {
            String s;
            if (kind==RefKind.Fragment)
                s = Character.toString(realizeFragment());
            else if (kind==RefKind.TokenLiteral)
                s = removeQuotes(cargo);
            else if (cargoLexerRule!=null)
                s = cargoLexerRule.realizeString();
            else
                s = cargo;
            result += s;
        }
        return result;
    }

    @Override
    public void updateNtRef(HashMap<String, Nonterminal> parserMap) {
        //empty
    }

    @Override
    public void updateLexerRef(HashMap<String, LexerRule> lexerMap) {
        if (kind==RefKind.TokenRef)
            if (lexerMap.containsKey(cargo))
                cargoLexerRule = lexerMap.get(cargo);
    }

    private static String removeQuotes(String cargo) {
        return cargo.substring(1, cargo.length()-1);
    }

    private char realizeFragment() {
        if (ranges==null)
            throw new ParseException("not initialized ranges");
        if (ranges.rangesFrom.length()!=ranges.rangesTo.length())
            throw new ParseException("bad ranges");
        int numRanges = ranges.rangesFrom.length();
        int index = LexerManager.generator.nextInt(numRanges);
        char from = ranges.rangesFrom.charAt(index);
        char to = ranges.rangesTo.charAt(index);
        index = LexerManager.generator.nextInt(to-from+1);
        return (char)(from+index);
    }
}
