public class Binary extends Expr {
    Expr left;
    Token operator;
    Expr right;

    Binary(Expr left, Token operator, Expr right){
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
