package core;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import expressions.Binary;
import expressions.Call;
import expressions.Expr;
import expressions.Get;
import expressions.Grouping;
import expressions.Literal;
import expressions.StringExpr;
import expressions.Unary;
import expressions.Variable;
import runtime.Environment;
import runtime.FunctionValue;
import runtime.ReturnSignal;
import statements.AssignmentStmt;
import statements.BlockStmt;
import statements.ExpressionStmt;
import statements.FuncStmt;
import statements.IfStmt;
import statements.PrintStmt;
import statements.ReturnStmt;
import statements.Stmt;
import statements.VarStmt;
import tokens.TokenType;

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

    public Object evaluate(Expr expr){
        if(expr instanceof Literal){
            Literal literal = (Literal) expr;
            return literal.value;
        }
        else if(expr instanceof Binary){
            Binary binary = (Binary) expr;
            double left = (Double) evaluate(binary.left);
            double right = (Double) evaluate(binary.right);
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
            return environment.get(variable.name.lexeme);
        }
        else if (expr instanceof Call) {
            Object callee = environment.get(call.name.lexeme);
ArrayList<Object> args = new ArrayList<>();
for (Expr a : call.args) args.add(evaluate(a));

if (callee instanceof FunctionValue) {
   ... your existing user-function call ...
} else if (callee instanceof Callable) {
   Callable c = (Callable) callee;
   if (args.size() != c.arity()) throw new RuntimeException("Wrong arity");
   return c.call(args);
} else {
   throw new RuntimeException("Not callable: " + call.name.lexeme);
}

        }

        else if (expr instanceof Unary) {
            Unary u = (Unary) expr;
            double right = (Double) evaluate(u.right);
            if (u.operator.type == TokenType.MINUS) return -right;
            throw new RuntimeException("Unknown unary operator: " + u.operator.type);
        }
        else if(expr instanceof StringExpr){
            StringExpr s = (StringExpr) expr;
            return s.value;
           // throw new RuntimeException("String evaluation not supported in numeric context.");
        }
        else if (expr instanceof Get) {
            Get g = (Get) expr;
            Object obj = evaluate(g.object);

            if (obj instanceof ModuleValue) {
                return ((ModuleValue) obj).get(g.name.lexeme);
            }
            throw new RuntimeException("Only modules have properties for now.");
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
            Object v;
            if (printStmt.expression instanceof StringExpr) v = ((StringExpr) printStmt.expression).value;
            else v = evaluate(printStmt.expression); 
            System.out.println(v);
            return;
        }
        else if(stmt instanceof IfStmt){
            IfStmt ifStmt = (IfStmt) stmt;
            double conditionValue = (Double) evaluate(ifStmt.condition);
            if(conditionValue != 0){
                execute(ifStmt.thenBranch);
            }
            return;
        }
        else if(stmt instanceof VarStmt){
            VarStmt varStmt = (VarStmt) stmt;
            Object value = evaluate(varStmt.initializer);
            environment.define(varStmt.name.lexeme, value);
            return;
        }
        else if (stmt instanceof AssignmentStmt) {
            AssignmentStmt a = (AssignmentStmt) stmt;
            Object value = evaluate(a.value);
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
        else if(stmt instanceof ReturnStmt){
            ReturnStmt r = (ReturnStmt) stmt;
            Object v = (r.value == null) ? 0.0 : evaluate(r.value);
            throw new ReturnSignal(v);
        }
        else {
            throw new RuntimeException("Unknown statement type");
        }
    }
}
