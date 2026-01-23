import java.util.ArrayList;

public class Call extends Expr {
    public final Token name;          
    public final ArrayList<Expr> args; 

    public Call(Token name, ArrayList<Expr> args) {
        this.name = name;
        this.args = args;
    }
}
