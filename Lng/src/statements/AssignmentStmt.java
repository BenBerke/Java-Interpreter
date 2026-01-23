package statements;

import expressions.Expr;
import tokens.Token;

public class AssignmentStmt extends Stmt {
    public final Token name;
    public final Expr value;

    public AssignmentStmt(Token name, Expr value){
        this.name = name;
        this.value = value;
    }
    
}
