grammar CMinus;

program : declaration* function*
        ;

declaration
    : TYPE ID SEMI
    ;

function
    : TYPE ID LPAREN paramList? RPAREN
      OPEN_BRACE
      declaration*
      stmt*
      CLOSE_BRACE
    ;

stmt
    : assignmentStmt
    | returnStmt
    | expression SEMI
    ;

assignmentStmt
    : ID ASSIGN expression SEMI
    ;

returnStmt
    : RETURN expression SEMI
    ;

expression
    : addSubExpr
    ;

functionCall
    : ID LPAREN callList? RPAREN
    ;

term
    : INT
    | STRING_LITERAL
    | functionCall
    | LPAREN expression RPAREN
    | ID
    ;

negationExpr
    : MINUS term
    | term
    ;

multDivExpr
    : negationExpr ((STAR|DIV) negationExpr)?
    ;

addSubExpr
    : multDivExpr ((PLUS|MINUS) multDivExpr)?
    ;

paramList
    : TYPE ID (COMMA TYPE ID)*
    ;

callList
    : expression (COMMA expression)*
    ;

fragment LETTER : [a-zA-Z];
fragment ENDL : '\r\n' | '\n' ;
fragment DIGIT : [0-9] ;

TYPE : 'int' | 'string' ;
WS : (' ' | '\t' | '\n' | '\r\n')+ ->skip;
COMMENT : '//' .*? ENDL ->skip;
ID : (LETTER | '_')(LETTER | '_' | DIGIT)* ;
COMMA : ',' ;
SEMI : ';' ;
LPAREN : '(' ;
RPAREN : ')' ;
OPEN_BRACE : '{' ;
CLOSE_BRACE : '}' ;
STRING_LITERAL : '"' ~["]* '"' ;
PLUS: '+';
MINUS: '-';
STAR: '*';
DIV: '/';
ASSIGN: '=';
RETURN: 'return';

INT : '0' | [1-9] DIGIT* ;