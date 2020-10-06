package org.monkey.gram;

import java.util.HashMap;

public interface NtUpdatable {
    void updateNtRef(HashMap<String, Nonterminal> parserMap);
}
