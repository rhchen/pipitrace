package ca.eepp.quatre.java.javeltrace.trace.input.ex;

import ca.eepp.quatre.java.javeltrace.trace.ex.TraceException;


public class TraceInputException extends TraceException {
    private static final long serialVersionUID = -162615224379842157L;
    
    public TraceInputException() {
        super();
    }
    public TraceInputException(String message) {
        super(message);
    }
    public TraceInputException(Exception e) {
        super(e);
    }
}
