grammar expr;
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

Literal: [1-9] [0-9]*;
Plus: '+';
Minus: '-';
Star:  '*';
Div:  '/';
LParen: '(';
RParen: ')';