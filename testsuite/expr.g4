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

Literal: [19] [09]*;
Plus: '+';
Minus: '-';
Star:  '*';
Div:  '/';
LParen: '(';
RParen: ')';