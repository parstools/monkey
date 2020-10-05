grammar exprws;
expression:
	multiplicativeExpression (
		(Plus | Minus) multiplicativeExpression
	)*;


multiplicativeExpression:
	primaryExpression (
		(Star | Div ) primaryExpression
	)*;


primaryExpression:
	Literal
	| LParen expression RParen
	;

Literal: NONZERODIGIT DIGIT*;
fragment DIGIT: [09];
fragment NONZERODIGIT: [19];
Plus: '+';
Minus: '-';
Star:  '*';
Div:  '/';
LParen: '(';
RParen: ')';
Whitespace: [ \t]+ -> skip;
Newline: ('\r' '\n'? | '\n') -> skip;
BlockComment: '/*' .*? '*/' -> skip;
LineComment: '//' ~ [\r\n]* -> skip;