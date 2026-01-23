public class ReturnStmt extends Stmt {
    public final Expr value;

    public ReturnStmt(Expr value) {
        this.value = value;
    }
}
