grammar multifile;
grammarDecl
   : grammarType Identifier ';';
   

grammarType
   : ('lexer' 'grammar' | 'parser' 'grammar' | 'grammar');


Identifier: [AZaz]+;

Whitespace: [ \t]+ -> skip;