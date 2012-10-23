package ca.eepp.quatre.java.javeltrace.metadata.ex;

public class ParsingException extends RuntimeException {
    private static final long serialVersionUID = 6591427740472779041L;
    
    public ParsingException() {
        super();
    }
    public ParsingException(String message) {
        super(message);
    }
    public ParsingException(Exception e) {
        super(e);
    }
}
