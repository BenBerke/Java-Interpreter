import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<Token> tokens = new ArrayList<>();
        Path sourcePath = Path.of("src/source.txt");
        String source = Files.readString(sourcePath);
        System.out.println(source);

        int line = 1;
        for(int i = 0; i < source.length(); i++){
            char c = source.charAt(i);
            if(Character.isDigit(c)){
                StringBuilder number = new StringBuilder();
                number.append(c);
                while(i + 1 < source.length() && Character.isDigit(source.charAt(i + 1))){
                    number.append(source.charAt(i + 1));
                    i++;
                }
                double literalValue = Double.valueOf(number.toString());
                tokens.add(new Token(TokenType.NUMBER, Double.toString(literalValue), literalValue, line));
                continue;
            }
    
            switch(c){
                case '\n':
                    line++;
                    break;
                case '+':
                    tokens.add(new Token(TokenType.PLUS, "+", null, line));
                    break;
                case '-':
                    tokens.add(new Token(TokenType.MINUS, "-", null, line));
                    break;
                case '*':
                    tokens.add(new Token(TokenType.MULTIPLY, "*", null, line));
                    break;
                case '/':
                    tokens.add(new Token(TokenType.DIVIDE, "/", null, line));
                    break;
                case '(':
                    tokens.add(new Token(TokenType.LEFT_PAREN, "(", null, line));
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RIGHT_PAREN, ")", null, line));
                    break;
                default:
                    if(Character.isWhitespace(c)) continue;
                    System.out.println("Unexpected character: " + c + " at line " + line);
                    break;
            }
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        //System.out.println("Tokens:");
        //for(Token token : tokens) System.out.println(token.lexeme + " : " + token.type);

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();
        System.out.println(print(expression));
    }

    public static String print(Expr expr){
        if(expr instanceof Literal){
            return Double.toString(((Literal) expr).value);
        }
        else if(expr instanceof Binary){
            Binary binary = (Binary) expr;
            return "(" + print(binary.left) + " " + binary.operator.lexeme + " " + print(binary.right) + ")";
        }
        else if(expr instanceof Grouping){
            Grouping grouping = (Grouping) expr;
            return "(group " + print(grouping.expression) + ")";
        }
        return "";
    }
}
