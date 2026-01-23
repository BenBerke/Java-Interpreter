public class ReturnSignal extends RuntimeException {
    public final Object value;
    public ReturnSignal(Object value) {
        this.value = value;
    }
}
