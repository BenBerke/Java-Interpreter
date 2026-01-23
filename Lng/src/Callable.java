package runtime;

import java.util.ArrayList;

public interface Callable {
    int arity();
    Object call(ArrayList<Object> args);
}
