grammar emptyAlt2;

start: c+;
alt: A| B |;
c: A alt;

A: 'a';
B: 'b';
