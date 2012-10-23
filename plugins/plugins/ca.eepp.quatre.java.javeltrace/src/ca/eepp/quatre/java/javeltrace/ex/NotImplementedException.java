package ca.eepp.quatre.java.javeltrace.ex;

public class NotImplementedException extends RuntimeException {
    private static final long serialVersionUID = -1991156335560571748L;
    
    public NotImplementedException() {
        super();
    }
    public NotImplementedException(String message) {
        super(message);
    }
    public NotImplementedException(Exception e) {
        super(e);
    }
}
