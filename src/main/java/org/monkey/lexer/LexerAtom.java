package org.monkey.lexer;

import org.antlr.v4.runtime.Lexer;
import org.monkey.gram.Nonterminal;
import org.monkey.pars.ParserRule;

import java.util.HashMap;

public class LexerAtom extends Repetitive {
    public String cargo;
    public RefKind kind;
    public LexerRule cargoLexerRule = null;
    public ParserRule cargoParserRule = null;

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
                s = Character.toString(realizeFragment(cargo));
            else if (kind==RefKind.TokenLiteral)
                s = removeQuotes(cargo);
            else
                s = cargo;
            result += s;
        }
        return result;
    }

    @Override
    public void updateLexerRef(HashMap<String, LexerRule> lexerMap) {
        if (kind==RefKind.TokenRef)
            if (lexerMap.containsKey(cargo))
                cargoLexerRule = lexerMap.get(cargo);
    }

    @Override
    public void updateNtRef(HashMap<String, Nonterminal> parserMap) {

    }

    private static String removeQuotes(String cargo) {
        return cargo.substring(1, cargo.length()-1);
    }

    private static char realizeFragment(String cargo) {
        if (cargo.isEmpty() || cargo.charAt(0)!='[' || cargo.charAt(cargo.length()-1)!=']')
            throw new ParseException("must be [] in range");
        int numRanges = cargo.length()/2-1;
        int index = LexerManager.generator.nextInt(numRanges);
        char from = cargo.charAt(index*2+1);
        char to = cargo.charAt(index*2+2);
        index = LexerManager.generator.nextInt(to-from+1);
        return (char)(from+index);
    }

    @Override
    public Type getType() {
        return Type.LexerAtom;
    }
}
