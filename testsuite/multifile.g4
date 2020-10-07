grammar multifile;
grammarDecl
   : grammarType Identifier ';';
   

grammarType
   : ('lexer' 'grammar' | 'parser' 'grammar' | 'grammar');


Identifier: [A-Za-z]+;

Whitespace: [ \t]+ -> skip;