package ca.eepp.quatre.java.javeltrace.trace.reader.ex;

public class BinaryCTFReaderException extends RuntimeException {
    private static final long serialVersionUID = 7758525382369502889L;
    
    public BinaryCTFReaderException() {
        super();
    }
    public BinaryCTFReaderException(String message) {
        super(message);
    }
    public BinaryCTFReaderException(Exception e) {
        super(e);
    }
}
