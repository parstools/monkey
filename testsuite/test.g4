grammar test;
start: TEST+ EOF;

TEST:[\b\f\n\r\t\\\-\]];
//escape no \" \' \a \e \v \? plus \- \] no numbers like \0x22
//Whitespace: [ \t]+ -> skip;