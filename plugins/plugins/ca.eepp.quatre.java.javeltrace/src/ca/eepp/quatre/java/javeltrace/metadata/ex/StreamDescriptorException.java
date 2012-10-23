package ca.eepp.quatre.java.javeltrace.metadata.ex;

public class StreamDescriptorException extends RuntimeException {
    private static final long serialVersionUID = 1775579752659716755L;
    
    public StreamDescriptorException() {
        super();
    }
    public StreamDescriptorException(String message) {
        super(message);
    }
    public StreamDescriptorException(Exception e) {
        super(e);
    }
}
