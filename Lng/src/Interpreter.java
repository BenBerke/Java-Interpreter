public class Interpreter {
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
                case PLUS:
                    return left + right;
                case MINUS:
                    return left - right;
                case MULTIPLY:
                    return left * right;
                case DIVIDE:
                    return left / right;
                default:
                    throw new RuntimeException("Unknown operator");
            }
        }
        else if(expr instanceof Grouping){
            Grouping grouping = (Grouping) expr;
            return evaluate(grouping.expression);
        }
        throw new RuntimeException("Unknown expression type");
   }
}
