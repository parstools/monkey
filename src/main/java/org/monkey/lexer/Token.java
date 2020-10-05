package org.monkey.lexer;

public class Token extends TokenCase {
    String name;
    public Type getType() {return Type.terminal;}
    public Token(String name, TokenSimple simple) {
        super(simple);
        this.name = name;
    }

    public Token(String name, TokenPart part) {
        super(part);
        this.name = name;
    }
}
