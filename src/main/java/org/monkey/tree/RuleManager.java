package org.monkey.tree;
/*
* zasady:
* gdy jest ? - wersja z i bez
* gdy jest + - wersja z 1 i 2
* gdy jest * - wersja z 0 i 2
* ale gdy startowy - wymagana ilość alternatyw = 1
* - nie bierze to pod uwagę tylko
* gdy jest tylko terminal - print terminal
* patrzy na nieterminale, ile mają wersji
* gdy może je powstarzać, bierze pod uwagę ich potwrxalność i ich alternatywy
* nierminala i te zgłęgione
* */

import org.monkey.gram.PumpRule;

import java.util.List;
import java.util.Random;

public class RuleManager {
    public static void expandPumps(List<PumpRule> pumps) {
        for (var pump: pumps) {
            List<Object> nodes = pump.r.nodes;
            if (nodes.size()>0)
                ((NontermNode)nodes.get(0)).expandPump(pump);
        }
    }
    /*
    static void buildNet(Nonterminal start) {
        doRecursive(start);
        clearVisited(start);
    }

    private static void clearVisited(Nonterminal start) {


    }

    private static void doRecursive(Nonterminal nt) {
    }
    */

}
