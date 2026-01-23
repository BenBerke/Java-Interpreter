package runtime;

import java.util.HashMap;
import java.util.Map;

public class ModuleValue {
    private final Map<String, Object> members = new HashMap<>();

    public void define(String name, Object value) {
        members.put(name, value);
    }

    public Object get(String name) {
        if (!members.containsKey(name)) {
            throw new RuntimeException("Unknown member '" + name + "' on module.");
        }
        return members.get(name);
    }
}
