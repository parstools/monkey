grammar complex;

elem: 
     El5+? | El2 El3 | (El1 () El2*? (El3?|(El5 El6 (El8|El9)? El7)|))* | | '123';

Whitespace: [ \t]+ -> skip;

El1: '1';
El2: '2';
El3: '3';
El4: '4';
El5: '5';
El6: '6';
El7: '7';
El8: '8';
El9: '9';