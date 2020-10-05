grammar recurLoop;

initDeclaratorList
    :   initDeclarator
    |   initDeclaratorList ',' initDeclarator
    ;

initDeclarator
    :   'declarator'
    |   'declarator' '=' 'initializer'
    ;
