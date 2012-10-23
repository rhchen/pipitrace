package ca.eepp.quatre.java.javeltrace.trace.reader;


import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.reader.ex.ReaderException;

/**
 * Interface for random access to a trace.
 * <p>
 * Using this interface, it is possible to seek anywhere within an input
 * trace by providing a time stamp.
 * <p>
 * This interface doesn't know packets. It gets events in chronological order,
 * even if this means reading different packets at the same time.
 * <p>
 * For a streaming interface, see {@link IStreamedReader}.
 *
 * @author Philippe Proulx
 */
public interface IRandomAccessReader extends IReader {

    /**
     * Gets the current event.
     * <p>
     * Returns null if no more events (end of trace is reached).
     *
     * @return  Current event
     * @throws ReaderException   If there's any reading exception
     */
    public Event getCurrentEvent() throws ReaderException;

    /**
     * Seeks to the next trace-wise event.
     *
     * @throws ReaderException   If there's any reading exception
     */
    public void advance() throws ReaderException;

    /**
     * Seeks to the trace-wise event at a specific time stamp.
     * <p>
     * This returns the first event found after the given time stamp
     * <code>ts</code> (if possible). Seek to -infinity and you get the first event.
     * Seek to +infinity and you get the last event.
     *
     * @param ts    Time stamp (clock cycle, no offset)
     * @throws ReaderException   If there's any reading exception
     */
    public void seek(long ts) throws ReaderException;
}
