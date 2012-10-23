package ca.eepp.quatre.java.javeltrace.trace.output;


import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.ex.WrongStateException;
import ca.eepp.quatre.java.javeltrace.trace.output.ex.TraceOutputException;
import ca.eepp.quatre.java.javeltrace.trace.writer.IWriter;

/**
 * An abstract trace output.
 * <p>
 * This abstract class specifies common methods and defines common implementation
 * for all trace output types. Known concrete trace outputs are
 * {@link StreamedTraceOutput} and {@link BufferedTraceOutput}.
 * <p>
 * The input uses a {@link IWriter} to support various trace formats.
 * <p>
 * An internal state is kept as methods are called. For example, the trace
 * must be opened before trying any write operation and before creating a
 * stream. If an operation is asked while the object is not in the
 * appropriate state, a {@link WrongStateException} exception is thrown.
 *
 * @author Philippe Proulx
 */
public abstract class TraceOutput {
    protected enum State {
        UNOPENED,
        OPENED,
        CLOSED
    };
    protected State state = State.UNOPENED;
    private final IWriter writer;
    private final TraceParameters params;

    protected TraceOutput(TraceParameters params, IWriter writer) {
        this.writer = writer;
        this.params = params;
    }

    /**
     * Checks if this output is opened.
     *
     * @return  True if opened
     */
    public boolean isOpen() {
        return this.state == State.OPENED;
    }

    protected IWriter getWriter() {
        return this.writer;
    }

    protected TraceParameters getParameters() {
        return this.params;
    }

    protected void checkState(State state, String errorMsg) throws WrongStateException {
        if (this.state != state) {
            throw new WrongStateException("Wrong trace state for this operation: " + errorMsg); //$NON-NLS-1$
        }
    }

    /**
     * Opens this output.
     *
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceOutputException  If any writing error occurs
     */
    public void open() throws TraceOutputException, WrongStateException {
        // Check state
        if (this.state == State.OPENED) {
            throw new WrongStateException("Wrong trace state for this operation: cannot reopen an opened trace"); //$NON-NLS-1$
        }

        // Open the trace
        this.getWriter().openTrace(this.getParameters());

        // Update state
        this.state = State.OPENED;

        // Open specific output
        this.openMe();
    }

    /**
     * Closes this output.
     *
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceOutputException  If any writing error occurs
     */
    public void close() throws WrongStateException, TraceOutputException {
        // Check state
        this.checkState(State.OPENED, "cannot close an output trace which is not opened first"); //$NON-NLS-1$

        // Close streams
        this.closeStreams();

        // Close trace
        this.getWriter().closeTrace();

        // Update state
        this.state = State.CLOSED;
    }

    /**
     * Inserts and begins a new packet.
     * <p>
     * A call to this method means: close the preceding packet if any and
     * start a new one. Then all following events until another call to this
     * method or a call to {@link #close()} belong to this new packet.
     *
     * @param packetInfo    Packet information of the new packet
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceOutputException  If any writing error occurs
     */
    public void newPacket(PacketInfo packetInfo) throws WrongStateException, TraceOutputException {
        // Check state
        this.checkState(State.OPENED, "trace is not opened"); //$NON-NLS-1$

        // Specific new packet
        this.newPacketMe(packetInfo);
    }

    /**
     * Writes an event.
     *
     * The event will belong to the last new packet.
     *
     * @param ev    Event to write
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceOutputException  If any writing error occurs
     */
    public void writeEvent(Event ev) throws TraceOutputException, WrongStateException {
        // Check state
        this.checkState(State.OPENED, "trace is not opened"); //$NON-NLS-1$

        // Specific write event
        this.writeEventMe(ev);
    }

    /**
     * Creates a new stream with a given ID.
     *
     * @param id    Stream ID
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceOutputException  If any writing error occurs
     */
    public void createStream(int id) throws WrongStateException, TraceOutputException {
        // Check state
        this.checkState(State.OPENED, "trace is not opened"); //$NON-NLS-1$

        // Specific create stream
        this.createStreamMe(id);
    }

    /**
     * Gets the number of written packets within a stream.
     *
     * @param streamID  Stream ID
     * @return          Number of written packets within the stream
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceOutputException  If any writing error occurs
     */
    public long getStreamNbPacketsWritten(int streamID) throws TraceOutputException, WrongStateException {
        // Check state
        this.checkState(State.OPENED, "trace is not opened"); //$NON-NLS-1$

        return this.getStreamNbPacketsWrittenMe(streamID);
    }

    /**
     * Checks if a stream exists.
     *
     * @param streamID  Stream ID
     * @return          True if stream exists
     * @throws WrongStateException
     * @throws TraceOutputException
     */
    public boolean streamExists(int streamID) throws WrongStateException, TraceOutputException {
        // Specific stream exists
        return this.streamExistsMe(streamID);
    }

    protected abstract void openMe() throws TraceOutputException, WrongStateException;
    protected abstract void closeStreams() throws TraceOutputException, WrongStateException;
    protected abstract void newPacketMe(PacketInfo packetInfo) throws TraceOutputException, WrongStateException;
    protected abstract void writeEventMe(Event ev) throws TraceOutputException, WrongStateException;
    protected abstract void createStreamMe(int id) throws TraceOutputException, WrongStateException;
    protected abstract boolean streamExistsMe(int streamID) throws TraceOutputException, WrongStateException;
    protected abstract long getStreamNbPacketsWrittenMe(int streamID);
}
