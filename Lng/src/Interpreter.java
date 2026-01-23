import java.util.ArrayList;

public class Interpreter {
    private Environment environment = new Environment();

    private void executeBlock(ArrayList<Stmt> statements, Environment newEnv) {
        Environment previous = this.environment;
        try {
            this.environment = newEnv;
            for (Stmt s : statements) execute(s);
        } finally {
            this.environment = previous;
        }
    }

    public double evaluate(Expr expr){
        if(expr instanceof Literal){
            Literal literal = (Literal) expr;
            return literal.value;
        }
        else if(expr instanceof Binary){
            Binary binary = (Binary) expr;
            double left = evaluate(binary.left);
            double right = evaluate(binary.right);
            switch(binary.operator.type){
                case PLUS: return left + right;
                case MINUS: return left - right;
                case MULTIPLY: return left * right;
                case DIVIDE: return left / right;
                case EQL_EQL: return (left == right) ? 1.0 : 0.0;
                case SML: return (left < right) ? 1.0 : 0.0;
                case SML_EQL: return (left <= right) ? 1.0 : 0.0;
                case GRTR: return (left > right) ? 1.0 : 0.0;
                case GRTR_EQL: return (left >= right) ? 1.0 : 0.0;
                case LOGICAL_AND: return (left != 0.0 && right != 0.0) ? 1.0 : 0.0;
                case LOGICAL_OR: return (left != 0.0 || right != 0.0) ? 1.0 : 0.0;
                default: throw new RuntimeException("Unknown operator " + binary.operator.type);
            }
        }
        else if(expr instanceof Grouping){
            Grouping grouping = (Grouping) expr;
            return evaluate(grouping.expression);
        }
        else if (expr instanceof Variable) {
            Variable variable = (Variable) expr;
            Object value = environment.get(variable.name.lexeme); 
            return (Double) value; 
        }
        else if (expr instanceof Call) {
            Call call = (Call) expr;

            Object value = environment.get(call.name.lexeme); 
            if (!(value instanceof FunctionValue)) {
                throw new RuntimeException("Can only call functions. '" + call.name.lexeme + "' is not a function.");
            }

            FunctionValue fn = (FunctionValue) value;

            // Create a new scope for the call, enclosed by the function’s closure
            Environment callEnv = new Environment(fn.closure);

            // (later) bind params to args here

            executeBlock(fn.declaration.body, callEnv);

            return 0.0; // no return feature yet
        }       


        throw new RuntimeException("Unknown expression type");
   }

   public void interpret(ArrayList<Stmt> statements){
        for (Stmt stmt : statements) {
            execute(stmt);
        }
    }

    public void execute(Stmt stmt){
        if(stmt instanceof PrintStmt){
            PrintStmt printStmt = (PrintStmt) stmt;
            double value = evaluate(printStmt.expression);
            System.out.println(value);
            return;
        }
        else if(stmt instanceof IfStmt){
            IfStmt ifStmt = (IfStmt) stmt;
            double conditionValue = evaluate(ifStmt.condition);
            if(conditionValue != 0){
                execute(ifStmt.thenBranch);
            }
            return;
        }
        else if(stmt instanceof VarStmt){
            VarStmt varStmt = (VarStmt) stmt;
            double value = evaluate(varStmt.initializer);
            environment.define(varStmt.name.lexeme, value);
            return;
        }
        else if (stmt instanceof AssignmentStmt) {
            AssignmentStmt a = (AssignmentStmt) stmt;
            double value = evaluate(a.value);
            environment.assign(a.name.lexeme, value);
            return;
        }
        else if(stmt instanceof ExpressionStmt){
            ExpressionStmt expressionStmt = (ExpressionStmt) stmt;
            evaluate(expressionStmt.expression);
            return;
        }
        else if (stmt instanceof BlockStmt) {
            BlockStmt b = (BlockStmt) stmt;
            executeBlock(b.statements, new Environment(environment));
            return;
        }
        else if (stmt instanceof FuncStmt) {
            FuncStmt f = (FuncStmt) stmt;
            FunctionValue fn = new FunctionValue(f, environment); 
            environment.define(f.name.lexeme, fn);
            environment.debugPrintFunctions();                
            return;
        }
        else {
            throw new RuntimeException("Unknown statement type");
        }
    }
}
