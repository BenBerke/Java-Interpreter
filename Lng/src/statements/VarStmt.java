package statements;

import expressions.Expr;
import tokens.Token;

public class VarStmt extends Stmt {
    public final Token name;
    public final Expr initializer;

    public VarStmt(Token name, Expr initializer){
        this.name = name;
        this.initializer = initializer;
    }
    
}
