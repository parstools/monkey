package org.monkey.pars;

public class PumpRule1 implements Comparable {
    public RealizedRule1 r = null;
    public Pump s = null;

    public int count = 1;

    @Override
    public int compareTo(Object o) {
        PumpRule1 other = (PumpRule1)o;
        if (r==other.r)
            return Integer.compare(s.hashCode(), other.s.hashCode());
        else
            return Integer.compare(r.hashCode(), other.r.hashCode());
    }
}
