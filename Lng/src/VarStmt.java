public class VarStmt extends Stmt {
    final Token name;
    final Expr initializer;

    public VarStmt(Token name, Expr initializer){
        this.name = name;
        this.initializer = initializer;
    }
    
}
