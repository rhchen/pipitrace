package ca.eepp.quatre.java.javeltrace.trace.ex;

public class ScopeNodeException extends RuntimeException {
    private static final long serialVersionUID = 479652040813790013L;
    
    public ScopeNodeException() {
        super();
    }
    public ScopeNodeException(String message) {
        super(message);
    }
    public ScopeNodeException(Exception e) {
        super(e);
    }
}
