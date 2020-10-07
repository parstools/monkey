package org.monkey.lexer;

import java.util.BitSet;

public class Ranges {
    public String rangesFrom = "";
    public String rangesTo = "";

    public Ranges(String rangeStr, boolean invert) throws Exception {
        parseRanges(rangeStr);
        if (invert) invertSet();
        getRanges();
    }

    private void getRanges() {
        boolean inRange = false;
        for (int i=0; i<=128; i++) {
            boolean bit = asciiSet.get(i);
            if (bit) {
                if(!inRange) {
                    rangesFrom += (char)i;
                    inRange = true;
                }
            } else{
                if(inRange) {
                    rangesTo += (char)(i-1);
                    inRange = false;
                }
            }
        }
    }

    private int pos=0;

    private static char slashed(char c, String s) throws Exception {
        switch (c) {
            case 'b': return '\b';
            case 'f': return '\f';
            case 'n': return '\n';
            case 'r': return '\r';
            case 't': return '\t';
            case '\\': return '\\';
            case '-': return '-';
            case ']': return ']';
            default: throw new ParsException("range definition "+s+ " is unknown \\"+c);
        }
    }

    private char readChar(String s) throws Exception {
        char c = s.charAt(pos);
        if (c==']') throw new ParsException("range definition "+s+ " is ] without \\");
        else if (c=='\t') throw new ParsException("range definition "+s+ " for tab use \\t");
        else if (c=='\\') {
            pos++;
            if (pos>=s.length()-1) throw new ParsException("range definition "+s+ " ends with \\");
            char nextc = s.charAt(pos);
            return slashed(nextc,s);
        }
        else if (c=='-') throw new ParsException("range definition "+s+ " is - without \\");
        else return c;
    }

    BitSet asciiSet = new BitSet();

    private void invertSet() {
        BitSet fullSet = new BitSet();
        fullSet.set(10);
        fullSet.set(13);
        fullSet.set(32,128);
        fullSet.andNot(asciiSet);
        asciiSet = fullSet;
    }

    private void parseRanges(String s) throws Exception {
        if (s.isEmpty() || s.charAt(0)!='[' || s.charAt(s.length()-1)!=']')
            throw new ParsException("Bad range definition "+s);
        pos=1;
        int from, to;
        while (pos<s.length()-1) {
            from = readChar(s);
            if (pos<s.length()-2 && s.charAt(pos+1)=='-') {
                pos+=2;
                if (pos==s.length()-1) {
                    asciiSet.set(from, from + 1);
                    to = from = '-';
                } else
                    to = readChar(s);
            } else to = from;
            pos++;
            asciiSet.set(from, to+1);
        }
    }
}

