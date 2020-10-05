package org.monkey.lexer;

import org.monkey.tree.RuleManager;

import java.util.ArrayList;
import java.util.List;

public class Fragment extends Repetitive {
    List<FragmentRange> ranges = new ArrayList<>();

    public void add(FragmentRange range) {
        ranges.add(range);
    }

    @Override
    public Type getType() {
        return Type.fragment;
    }

    public String toString() {
        if (ranges.isEmpty()) return "";
        int index = RuleManager.generator.nextInt(ranges.size());
        var range = ranges.get(index);
        return Character.toString(range.toChar());
    }
}
