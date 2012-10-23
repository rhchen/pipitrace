package ca.eepp.quatre.java.javeltrace.translation.ex;

public class TranslatorException extends RuntimeException {
    private static final long serialVersionUID = 6591427740472779041L;
    
    public TranslatorException() {
        super();
    }
    public TranslatorException(String message) {
        super(message);
    }
    public TranslatorException(Exception e) {
        super(e);
    }
}
