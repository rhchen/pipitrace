package ca.eepp.quatre.java.javeltrace.trace.ex;

public class WrongStateException extends RuntimeException {
    private static final long serialVersionUID = -867008230236528517L;
    
    public WrongStateException() {
        super();
    }
    public WrongStateException(String message) {
        super(message);
    }
    public WrongStateException(Exception e) {
        super(e);
    }
}
