package ca.eepp.quatre.java.javeltrace.utils.ex;

public class TimeStampAccumulatorException extends RuntimeException {
    private static final long serialVersionUID = 8219908422216843771L;
    
    public TimeStampAccumulatorException() {
        super();
    }
    public TimeStampAccumulatorException(String message) {
        super(message);
    }
    public TimeStampAccumulatorException(Exception e) {
        super(e);
    }
}
