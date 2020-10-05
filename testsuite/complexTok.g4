grammar complexTok;

start: Literal+ #pound;

Literal: Ident | Number;

Ident: [AZaz_] [AZaz_09]*;
Number: [19] [09]*;
Whitespace: [ \t]+ -> skip;

