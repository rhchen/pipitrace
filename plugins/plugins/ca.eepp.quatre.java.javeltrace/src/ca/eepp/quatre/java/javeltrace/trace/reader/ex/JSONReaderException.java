package ca.eepp.quatre.java.javeltrace.trace.reader.ex;

public class JSONReaderException extends ReaderException {
    private static final long serialVersionUID = -7908457175944495913L;
    private Integer line = null;

    public JSONReaderException() {
        super();
    }
    public JSONReaderException(String message) {
        super(message);
    }
    public JSONReaderException(String message, int line) {
        super(message);
        this.line = line;
    }
    public JSONReaderException(Exception e) {
        super(e);
    }

    public Integer getLine() {
        return this.line;
    }
}
