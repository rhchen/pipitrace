package ca.eepp.quatre.java.javeltrace.trace.writer.ex;

public class WriterException extends RuntimeException {
    private static final long serialVersionUID = 5517323995587271793L;
    
    public WriterException() {
        super();
    }
    public WriterException(String message) {
        super(message);
    }
    public WriterException(Exception e) {
        super(e);
    }
}
