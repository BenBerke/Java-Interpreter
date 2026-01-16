public class Token{
    TokenType type;
    Object literal;
    int line;
    String lexeme;

    public Token(TokenType type, String lexeme, Object literal, int line){
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }
}