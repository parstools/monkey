grammar complexTok2;

start: Literal+ #pound;

Literal: ([A-Za-z_] [A-Za-z_0-9]*)'s' | ([1-9] [0-9]*)SUF | 'abc';

fragment SUF: 'suf' ;
Whitespace: [ \t]+ -> skip;

