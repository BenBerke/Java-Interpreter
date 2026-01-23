import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();
    public final Environment enclosing;

    public Environment() { this.enclosing = null; }
    public Environment(Environment enclosing) { this.enclosing = enclosing; }

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(String name) {
        if (values.containsKey(name)) return values.get(name);
        if (enclosing != null) return enclosing.get(name);
        throw new RuntimeException("Undefined variable: " + name);
    }

    public void assign(String name, Object value) {
        if (values.containsKey(name)) {
            values.put(name, value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new RuntimeException("Undefined variable: " + name);
    }

    public void debugPrintFunctions() {
       // for (Map.Entry<String, Object> entry : values.entrySet()) 
        //if (entry.getValue() instanceof FunctionValue) System.out.println(" - " + entry.getKey());
    }

}
