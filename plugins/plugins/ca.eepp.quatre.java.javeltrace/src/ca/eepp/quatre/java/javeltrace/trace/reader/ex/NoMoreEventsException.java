package ca.eepp.quatre.java.javeltrace.trace.reader.ex;


public class NoMoreEventsException extends ReaderException {
    private static final long serialVersionUID = 751621908623008407L;
    
    public NoMoreEventsException() {
        super();
    }
    public NoMoreEventsException(String message) {
        super(message);
    }
    public NoMoreEventsException(Exception e) {
        super(e);
    }
}
