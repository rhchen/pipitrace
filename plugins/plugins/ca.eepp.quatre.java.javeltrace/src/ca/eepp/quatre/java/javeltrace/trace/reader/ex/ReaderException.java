package ca.eepp.quatre.java.javeltrace.trace.reader.ex;

public class ReaderException extends RuntimeException {
    private static final long serialVersionUID = -218957492465530139L;
    
    public ReaderException() {
        super();
    }
    public ReaderException(String message) {
        super(message);
    }
    public ReaderException(Exception e) {
        super(e);
    }
}
