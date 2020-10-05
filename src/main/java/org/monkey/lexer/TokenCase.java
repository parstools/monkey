package org.monkey.lexer;

import org.monkey.tree.RuleManager;

import java.util.ArrayList;
import java.util.List;

public class TokenCase extends Repetitive {
    protected List<Repetitive> list = new ArrayList<>();

    public void add(TokenPart alt) {
        list.add(alt);
    }
    public void add(TokenSimple alt) {
        list.add(alt);
    }

    TokenCase(TokenSimple simple) {add(simple);}
    TokenCase(TokenPart part) {add(part);}

    @Override
    public Type getType() {
        return Type.tokenCase;
    }

    public String toString() {
        if (list.isEmpty()) return "";
        int index = RuleManager.generator.nextInt(list.size());
        Repetitive alt = list.get(index);
        return alt.toString();
    }
}
