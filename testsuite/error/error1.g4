grammar error1;

seq: seq A B | seq; //The following sets of rules are mutually left-recursive [seq]

A: 'a';
B: 'b';


