import java.util.ArrayList;

public class BlockStmt extends Stmt {
    public final ArrayList<Stmt> statements;

    public BlockStmt(ArrayList<Stmt> statements){
        this.statements = statements;
    }
    
}
