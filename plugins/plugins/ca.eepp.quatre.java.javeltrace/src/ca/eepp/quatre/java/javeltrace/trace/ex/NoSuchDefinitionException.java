package ca.eepp.quatre.java.javeltrace.trace.ex;

public class NoSuchDefinitionException extends RuntimeException {
    private static final long serialVersionUID = -4443901661517198333L;
    
    public NoSuchDefinitionException() {
        super();
    }
    public NoSuchDefinitionException(String message) {
        super(message);
    }
    public NoSuchDefinitionException(Exception e) {
        super(e);
    }
}
