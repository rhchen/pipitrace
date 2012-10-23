package ca.eepp.quatre.java.javeltrace.trace.input;


import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.ex.WrongStateException;
import ca.eepp.quatre.java.javeltrace.trace.input.ex.TraceInputException;
import ca.eepp.quatre.java.javeltrace.trace.reader.IRandomAccessReader;

/**
 * A random access trace input.
 * <p>
 * Using this trace input, it is possible to open a trace using any
 * implementation of {@link IRandomAccessReader} and seek to a specific
 * time stamp (clock cycle).
 * <p>
 * The random access input doesn't present the notion of packets and only has
 * events access methods. The events are read in chronological order, even if
 * this means many packets are read at the same time (for different CPUs).
 * <p>
 * When the trace input is opened, it points to the first event and a call to
 * {@link #getCurrentEvent()} will return it. Then, the user must verify if
 * more events are present using {@link #hasMoreEvents()} and then seek to the
 * next one using {@link #advance()}. A random access seek is possible
 * using {@link #seek(long)}.
 * <p>
 * For this to work, the random access reader has to be able to go back and
 * forth into the trace so streaming is impossible. For a streamed input, use
 * {@link StreamedTraceInput}.
 *
 * @author Philippe Proulx
 */
public class RandomAccessTraceInput extends TraceInput {
    private final IRandomAccessReader randomReader;

    /**
     * Builds a random access trace input.
     *
     * @param reader    Random access reader to use
     */
    public RandomAccessTraceInput(IRandomAccessReader reader) {
        super(reader);
        this.randomReader = reader;
    }

    /**
     * Gets the current event.
     *
     * @return  Current event
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceInputException   If any reading error occurs
     * @see IRandomAccessReader#getCurrentEvent()
     */
    public Event getCurrentEvent() throws WrongStateException, TraceInputException {
        this.checkState(State.OPENED, TraceInput.MSG_TRACE_NOT_OPENED);

        // Return the event
        return this.randomReader.getCurrentEvent();
    }

    /**
     * Goes to the next event (trace-wise).
     *
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceInputException   If any reading error occurs
     * @see IRandomAccessReader#advance()
     */
    public void advance() throws WrongStateException, TraceInputException {
        this.checkState(State.OPENED, TraceInput.MSG_TRACE_NOT_OPENED);

        // Seek to the next event
        this.randomReader.advance();
    }

    /**
     * Seeks to the trace-wise event at a specific time stamp.
     *
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceInputException   If any reading error occurs
     * @see IRandomAccessReader#seek(long)
     */
    public void seek(long ts) throws WrongStateException, TraceInputException {
        this.checkState(State.OPENED, TraceInput.MSG_TRACE_NOT_OPENED);

        // Return the event
        this.randomReader.seek(ts);
    }
}
