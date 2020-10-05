grammar emptyAlt1;

start: c+;
c: A (B |);

A: 'a';
B: 'b';
