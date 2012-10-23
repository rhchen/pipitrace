package ca.eepp.quatre.java.javeltrace.trace.output.ex;

import ca.eepp.quatre.java.javeltrace.trace.ex.TraceException;


public class TraceOutputException extends TraceException {
    private static final long serialVersionUID = -2032799051541757150L;

    public TraceOutputException() {
        super();
    }
    public TraceOutputException(String message) {
        super(message);
    }
    public TraceOutputException(Exception e) {
        super(e);
    }
}
