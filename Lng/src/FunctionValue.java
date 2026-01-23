public class FunctionValue {
    final FuncStmt declaration;
    final Environment closure; 

    public FunctionValue(FuncStmt declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    public int arity() {
        return declaration.params.size();
    }
}
