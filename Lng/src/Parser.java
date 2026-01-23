import java.util.ArrayList;

public class Parser {
    ArrayList<Token> tokens;
    int current = 0;
    
    Parser(ArrayList<Token> tokens){
        this.tokens = tokens;
    }

    FuncStmt functionDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect function name.");
        consume(TokenType.LEFT_PAREN, "Expect '(' after function name.");

        ArrayList<Token> params = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                params.add(consume(TokenType.IDENTIFIER, "Expect parameter name."));
            }   while (match(TokenType.COMMA));
        }

        consume(TokenType.RIGHT_PAREN, "Expect ')' after parameters.");
        consume(TokenType.LEFT_BRACE, "Expect '{' before function body.");

        ArrayList<Stmt> body = block();
        return new FuncStmt(name, params, body);
    }


    Expr expression(){
        return or();
    }

    public Expr parse(){
        return expression();
    }

    Expr or(){
        Expr expr = and();
        while(match(TokenType.LOGICAL_OR)){
            Token operator = previous();
            Expr right = and();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    Expr and(){
        Expr expr = equality();
        while(match(TokenType.LOGICAL_AND)){
            Token operator = previous();
            Expr right = equality();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    Expr addition(){
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

    Expr equality(){
        Expr expr = comparison();
        while(match(TokenType.EQL_EQL)){
            Token operator = previous();
            Expr right = comparison();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    Expr comparison(){
        Expr expr = addition();
        while(match(TokenType.SML, TokenType.SML_EQL, TokenType.GRTR, TokenType.GRTR_EQL)){
            Token operator = previous();
            Expr right = addition();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    ArrayList<Stmt> block() {
        ArrayList<Stmt> statements = new ArrayList<>();

        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(statement());
        }   

        consume(TokenType.RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }

    Stmt statement(){
        if(match(TokenType.PRINT)){
            Expr value = expression();
            consume(TokenType.SEMICOLON, "Expect ';' after value.");
            return new PrintStmt(value);
        }
        else if(match(TokenType.IF)){
            consume(TokenType.LEFT_PAREN, "Expect '(' after 'if'.");
            Expr condition = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after if condition.");
            Stmt thenBranch = statement();
            return new IfStmt(condition, thenBranch);
        }
        else if(match(TokenType.VAR)){
            Token name = consume(TokenType.IDENTIFIER, "Expect variable name.");
            consume(TokenType.EQL, "Expect '=' after variable name.");
            Expr initializer = expression();
            consume(TokenType.SEMICOLON, "Expect ';' after variable declaration.");
            return new VarStmt(name, initializer);
        }
        else if(check(TokenType.IDENTIFIER) && peekNext().type == TokenType.EQL){
            Token name = advance(); 
            consume(TokenType.EQL, "Expect '=' after variable name."); 
            Expr value = expression();
            consume(TokenType.SEMICOLON, "Expect ';' after assignment.");
            return new AssignmentStmt(name, value);
        }
        else if(match(TokenType.LEFT_BRACE)) return new BlockStmt(block());
        else if (match(TokenType.FUNCTION)) return functionDeclaration();
        Expr expr = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        return new ExpressionStmt(expr);
    }

    ArrayList<Stmt> parseStatements(){
        ArrayList<Stmt> statements = new ArrayList<>();
        while(!isAtEnd()){
            statements.add(statement());
        }
        return statements;
    }

    Expr factor(){
        if(match(TokenType.NUMBER)){
            return new Literal((double)previous().literal);
        }
        else if(match(TokenType.TRUE)) return new Literal(1.0);
        else if(match(TokenType.FALSE)) return new Literal(0.0);
        else if (match(TokenType.IDENTIFIER)) {
            Token name = previous();

            if (match(TokenType.LEFT_PAREN)) {
                ArrayList<Expr> args = new ArrayList<>();

                // (optional for later) parse arguments:
                // if (!check(TokenType.RIGHT_PAREN)) {
                //     do {
                //         args.add(expression());
                //     } while (match(TokenType.COMMA));
                // }

                consume(TokenType.RIGHT_PAREN, "Expect ')' after function call.");
                return new Call(name, args);
            }       
            return new Variable(name);
        }

        else if(match(TokenType.LEFT_PAREN)){
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

    Token peekNext(){
        if (current + 1 >= tokens.size()) return tokens.get(tokens.size() - 1); // EOF
        return tokens.get(current + 1);
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
