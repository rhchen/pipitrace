package ca.eepp.quatre.java.javeltrace.trace.ex;

public class NoSuchFieldException extends RuntimeException {
    private static final long serialVersionUID = -6093903763487901615L;
    
    public NoSuchFieldException() {
        super();
    }
    public NoSuchFieldException(String message) {
        super(message);
    }
    public NoSuchFieldException(Exception e) {
        super(e);
    }
}
