grammar pump;

translationUnit: declarationseq? EOF;
declarationseq: declaration+;
declaration:
	  blockDeclaration
	| functionDefinition
	| templateDeclaration
	| linkageSpecification
	| namespaceDefinition;


blockDeclaration: '(' ')';
functionDefinition: 'int f()';
templateDeclaration: 'template <T>';
linkageSpecification: 'extern "C"';
namespaceDefinition: 'namespace a{}';
Whitespace: [ \t]+ -> skip;