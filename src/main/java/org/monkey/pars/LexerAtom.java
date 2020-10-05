package org.monkey.pars;

import org.monkey.tree.RuleManager;

public class LexerAtom extends LexerElement {
    public String cargo;
    public RefKind kind;

    @Override
    public String toString() {
        return cargo+suffixToString();
    }

    public String realizeString() {
        int count;
        if (rep==Repetitions.once || rep==Repetitions.maybe)
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

    private static String removeQuotes(String cargo) {
        return cargo.substring(1, cargo.length()-1);
    }

    private static char realizeFragment(String cargo) {
        if (cargo.isEmpty() || cargo.charAt(0)!='[' || cargo.charAt(cargo.length()-1)!=']')
            throw new ParseException("must be [] in range");
        int numRanges = cargo.length()/2-1;
        int index = RuleManager.generator.nextInt(numRanges);
        char from = cargo.charAt(index*2+1);
        char to = cargo.charAt(index*2+2);
        index = RuleManager.generator.nextInt(to-from+1);
        return (char)(from+index);
    }
}
