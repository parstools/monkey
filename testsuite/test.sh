java org.antlr.v4.Tool test.g4 -o gen
javac gen/test*.java
cd gen
java org.antlr.v4.gui.TestRig test start ../test.test
