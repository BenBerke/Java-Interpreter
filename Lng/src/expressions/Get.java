package expressions;

import tokens.Token;

public class Get extends Expr {
    public final Expr object;
    public final Token name;

    public Get(Expr object, Token name) {
        this.object = object;
        this.name = name;
    }
}
