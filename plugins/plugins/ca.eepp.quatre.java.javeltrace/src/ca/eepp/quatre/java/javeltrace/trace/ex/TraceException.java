package ca.eepp.quatre.java.javeltrace.trace.ex;

public class TraceException extends Exception {
    private static final long serialVersionUID = -2112794933092165420L;
    
    public TraceException() {
        super();
    }
    public TraceException(String message) {
        super(message);
    }
    public TraceException(Exception e) {
        super(e);
    }
}
