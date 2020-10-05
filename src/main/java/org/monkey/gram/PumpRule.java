package org.monkey.gram;

public class PumpRule implements Comparable {
    public RealizedRule r = null;
    public Serie s = null;

    public int count = 1;

    @Override
    public int compareTo(Object o) {
        PumpRule other = (PumpRule)o;
        if (r==other.r)
            return Integer.compare(s.hashCode(), other.s.hashCode());
        else
            return Integer.compare(r.hashCode(), other.r.hashCode());
    }
}
