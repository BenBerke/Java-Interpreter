import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<Token> tokens = new ArrayList<>();
        Path sourcePath = Path.of("src/source.txt");
        String source = Files.readString(sourcePath);

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
            else if(Character.isLetter(c)){
                StringBuilder identifier = new StringBuilder();
                identifier.append(c);
                while(i + 1 < source.length() && Character.isLetter(source.charAt(i + 1))){
                    identifier.append(source.charAt(i + 1));
                    i++;
                }
                switch(identifier.toString().toLowerCase()){
                    case "print": tokens.add(new Token(TokenType.PRINT, "print", null, line)); break;
                    case "if":tokens.add(new Token(TokenType.IF, "if", null, line)); break;
                    case "var": tokens.add(new Token(TokenType.VAR, "var", null, line)); break;
                    case "true": tokens.add(new Token(TokenType.TRUE, "true", 1.0, line));  break;
                    case "false": tokens.add(new Token(TokenType.FALSE, "false", 0.0, line));  break;
                    case "func": tokens.add(new Token(TokenType.FUNCTION, "function", null, line)); break;
                    case "return": tokens.add(new Token(TokenType.RETURN, "return", null, line)); break;
                    default: tokens.add(new Token(TokenType.IDENTIFIER, identifier.toString(), null, line)); break;
                }
                continue;
            }
            else 
            switch(c){
                case '\n':
                    line++;
                    break;
                case '+': tokens.add(new Token(TokenType.PLUS, "+", null, line)); break;
                case '-':tokens.add(new Token(TokenType.MINUS, "-", null, line)); break;
                case '*': tokens.add(new Token(TokenType.MULTIPLY, "*", null, line)); break;
                case '/': tokens.add(new Token(TokenType.DIVIDE, "/", null, line)); break;
                case '(': tokens.add(new Token(TokenType.LEFT_PAREN, "(", null, line)); break;
                case ')': tokens.add(new Token(TokenType.RIGHT_PAREN, ")", null, line)); break;
                case ';': tokens.add(new Token(TokenType.SEMICOLON, ";", null, line));break;
                case '=':
                    if(i + 1 < source.length() && source.charAt(i + 1) == '='){
                        tokens.add(new Token(TokenType.EQL_EQL, "==", null, line));
                        i++;
                    } else tokens.add(new Token(TokenType.EQL, "=", null, line));
                    break;
                case '<':
                    if(i + 1 < source.length() && source.charAt(i + 1) == '='){
                        tokens.add(new Token(TokenType.SML_EQL, "<=", null, line));
                        i++;
                    } else tokens.add(new Token(TokenType.SML, "<", null, line));
                    break;
                case '>':
                    if(i + 1 < source.length() && source.charAt(i + 1) == '='){
                        tokens.add(new Token(TokenType.GRTR_EQL, ">=", null, line));
                        i++;
                    } else tokens.add(new Token(TokenType.GRTR, ">", null, line));
                    break;
                case '|': tokens.add(new Token(TokenType.LOGICAL_OR, "|", null, line)); break;
                case '&': tokens.add(new Token(TokenType.LOGICAL_AND, "&", null, line)); break;
                case '!': tokens.add(new Token(TokenType.LOGICAL_NOT, "!", null, line)); break;
                case '{': tokens.add(new Token(TokenType.LEFT_BRACE, "{", null, line)); break;
                case '}': tokens.add(new Token(TokenType.RIGHT_BRACE, "}", null, line)); break;
                case ',': tokens.add(new Token(TokenType.COMMA, ",", null, line)); break;
                case '"': 
                    StringBuilder str = new StringBuilder();
                    while (i + 1 < source.length() && source.charAt(i + 1) != '"') {
                        i++;
                        char ch = source.charAt(i);
                        if (ch == '\n') line++;
                        str.append(ch);
                    }
                    if (i + 1 >= source.length()) throw new RuntimeException("Unterminated string at line " + line);
    
                    i++; // consume closing "
                    tokens.add(new Token(TokenType.STRING, str.toString(), str.toString(), line));
                break; 
                default:
                    if(Character.isWhitespace(c)) continue;
                    System.out.println("Unexpected character: " + c + " at line " + line);
                    break;
            }
        }
         tokens.add(new Token(TokenType.EOF, "", null, line));
        //  System.out.println("TOKENS:");
        //  for (Token token : tokens) {
        //      System.out.println(token.type + " '" + token.lexeme + "'");
        //  }

        Parser parser = new Parser(tokens);
        ArrayList<Stmt> program = parser.parseStatements();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);
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
