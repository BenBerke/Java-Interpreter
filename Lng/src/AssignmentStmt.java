public class AssignmentStmt extends Stmt {
    final Token name;
    final Expr value;

    public AssignmentStmt(Token name, Expr value){
        this.name = name;
        this.value = value;
    }
    
}
