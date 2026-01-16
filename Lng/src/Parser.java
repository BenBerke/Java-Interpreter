import java.util.ArrayList;

public class Parser {
    ArrayList<Token> tokens;
    int current = 0;
    
    Parser(ArrayList<Token> tokens){
        this.tokens = tokens;
    }

    public Expr parse(){
        return expression();
    }

    Expr expression(){
        Expr expr = term();
        while(match(TokenType.PLUS, TokenType.MINUS)){
            Token operator = previous();
            Expr right = term();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    Expr term(){
        Expr expr = factor();

        while(match(TokenType.MULTIPLY, TokenType.DIVIDE)){
            Token operator = previous();
            Expr right = factor();
            expr = new Binary(expr, operator, right);
        }

        return expr;
    }

    Expr factor(){
        if(match(TokenType.NUMBER)){
            return new Literal((double)previous().literal);
        }
        if(match(TokenType.LEFT_PAREN)){
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new Grouping(expr);
        }
        throw error(peek(), "Expect expression.");
    }

    RuntimeException error(Token token, String message){
        return new RuntimeException("Parse Error at line " + token.line + ": " + message);
    }

    Token previous(){
        return tokens.get(current - 1);
    }

    Token peek(){
        return tokens.get(current);
    }

    boolean isAtEnd(){
        return peek().type == TokenType.EOF;
    }

    Token advance(){
        if(!isAtEnd()) current++;
        return previous();
    }

    boolean check(TokenType type){
        if(isAtEnd()) return false;
        return peek().type == type;
    }
    
    Token consume(TokenType type, String message){
        if(check(type)) return advance();
        throw new RuntimeException("Parse Error at line " + peek().line + ": " + message);
    }

    boolean match(TokenType... types){
        for(TokenType type : types){
            if(check(type)){
                advance();
                return true;
            }
        }
        return false;
    }

}
