package org.monkey.lexer;

import org.monkey.tree.RuleManager;

public class FragmentRange {
    char from = 0;
    char to = 0;
    FragmentRange(char c) {
        from = to = c;
    }

    public FragmentRange(char from, char to) {
        this.from = from;
        this.to = to;
    }

    public char toChar() {
        int index = RuleManager.generator.nextInt(to-from+1);
        return (char)(from+index);
    }
}
