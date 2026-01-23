package runtime;

import statements.FuncStmt;

public class FunctionValue {
    public final FuncStmt declaration;
    public final Environment closure; 

    public FunctionValue(FuncStmt declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    public int arity() {
        return declaration.params.size();
    }
}
