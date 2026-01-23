package statements;

import expressions.Expr;

public class IfStmt extends Stmt {
    public final Expr condition;
    public final Stmt thenBranch;

    public IfStmt(Expr condition, Stmt thenBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
    }
    
}
