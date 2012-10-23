package ca.eepp.quatre.java.javeltrace.trace.reader.ex;


public class NoMorePacketsException extends ReaderException {
    private static final long serialVersionUID = 751621908623008407L;
    
    public NoMorePacketsException() {
        super();
    }
    public NoMorePacketsException(String message) {
        super(message);
    }
    public NoMorePacketsException(Exception e) {
        super(e);
    }
}
