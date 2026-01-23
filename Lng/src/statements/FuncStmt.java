package statements;

import java.util.ArrayList;
import tokens.Token;

public class FuncStmt extends Stmt {
    public final Token name;
    public final ArrayList<Token> params;
    public final ArrayList<Stmt> body;

    public FuncStmt(Token name, ArrayList<Token> params, ArrayList<Stmt> body){
        this.name = name;
        this.params = params;
        this.body = body;
    }
    
}
