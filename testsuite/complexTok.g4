grammar complexTok;

start: Literal+ #pound;

Literal: Ident | Number;

Ident: [A-Za-z_] [A-Za-z_0-9]*;
Number: [1-9] [0-9]*;
Whitespace: [ \t]+ -> skip;

