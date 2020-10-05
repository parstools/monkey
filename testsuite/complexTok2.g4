grammar complexTok2;

start: Literal+ #pound;

Literal: ([AZaz_] [AZaz_09]*)'s' | ([19] [09]*)SUF;

fragment SUF: 'suf' ;
Whitespace: [ \t]+ -> skip;

