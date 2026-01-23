package expressions;

import tokens.Token;

public class Variable extends Expr {
    public final Token name;

    public Variable(Token name) {
        this.name = name;
    }
}

