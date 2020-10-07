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
fragment DIGIT: [0-9];
fragment NONZERODIGIT: [1-9];
Plus: '+';
Minus: '-';
Star:  '*';
Div:  '/';
LParen: '(';
RParen: ')';
Whitespace: [ \t]+ -> skip;