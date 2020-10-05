package org.monkey.lexer;

import org.monkey.pars.Repetitions;

import java.util.ArrayList;
import java.util.List;

public class TokenPart extends Repetitive {
    public List<Repetitive> list = new ArrayList<>();
    protected List<Repetitions> reps = new ArrayList<>();

    public void add(Repetitive x, Repetitions rep) {
        list.add(x);
        reps.add(rep);
    }

    @Override
    public Type getType() {
        return Type.tokenPart;
    }

    public String toString() {
        String result = "";
        for (int i=0; i<list.size(); i++) {
            var rep = reps.get(i);
            var x = list.get(i);
            int count;
            if (rep==Repetitions.once || rep==Repetitions.maybe)
                count = 1;
            else
                count = 5;
            for (int j=0; j<count; j++)
                result += x.toString();
        }
        return result;
    }
}
