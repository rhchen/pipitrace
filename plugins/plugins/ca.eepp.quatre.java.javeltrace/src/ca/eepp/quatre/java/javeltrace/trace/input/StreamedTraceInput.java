package ca.eepp.quatre.java.javeltrace.trace.input;


import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.ex.WrongStateException;
import ca.eepp.quatre.java.javeltrace.trace.input.ex.TraceInputException;
import ca.eepp.quatre.java.javeltrace.trace.reader.IStreamedReader;

/**
 * A streamed trace input.
 * <p>
 * As opposed to {@link RandomAccessTraceInput}, this streamed trace input
 * doesn't allow any seeking within a trace. However, it guarantees that the
 * trace is always read forward. This means the reader doesn't have to look at
 * a file and the trace may be infinite and coming from network.
 * 
 * @author Philippe Proulx
 */
public class StreamedTraceInput extends TraceInput {
    private final IStreamedReader seqReader;
    
    /**
     * Builds a streamed trace input.
     * 
     * @param reader    Streamed reader to use to read the trace
     */
    public StreamedTraceInput(IStreamedReader reader) {
        super(reader);
        this.seqReader = reader;
    }
    
    /**
     * Gets the current packet information.
     * 
     * @return  Current packet information or null if no more packet
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceInputException   If any reading error occurs
     * @see IStreamedReader#getCurrentPacketInfo()
     */
    public PacketInfo getCurrentPacketInfo() throws WrongStateException, TraceInputException {
        this.checkState(State.OPENED, TraceInput.MSG_TRACE_NOT_OPENED);
        
        // Return the packet info
        return this.seqReader.getCurrentPacketInfo();
    }
    
    /**
     * Goes to the next packet.
     * 
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceInputException   If any reading error occurs
     * @see IStreamedReader#nextPacket()
     */
    public void nextPacket() throws WrongStateException, TraceInputException {
        this.checkState(State.OPENED, TraceInput.MSG_TRACE_NOT_OPENED);
        
        // Seek to the next packet
        this.seqReader.nextPacket();
    }
    
    /**
     * Gets the current packet event.
     * 
     * @return  Current packet event or null if packet has no more event
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceInputException   If any reading error occurs
     * @see IStreamedReader#getCurrentPacketEvent()
     */
    public Event getCurrentPacketEvent() throws WrongStateException, TraceInputException {
        this.checkState(State.OPENED, TraceInput.MSG_TRACE_NOT_OPENED);
        
        // Return the event
        return this.seqReader.getCurrentPacketEvent();
    }
    
    /**
     * Goes to the next packet event.
     * 
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceInputException   If any reading error occurs
     * @see IStreamedReader#nextPacketEvent()
     */
    public void nextPacketEvent() throws WrongStateException, TraceInputException {
        this.checkState(State.OPENED, TraceInput.MSG_TRACE_NOT_OPENED);
        
        // Seek to the next event
        this.seqReader.nextPacketEvent();
    }
}
