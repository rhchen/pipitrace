package ca.eepp.quatre.java.javeltrace.trace.reader;


import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.reader.ex.NoMoreEventsException;
import ca.eepp.quatre.java.javeltrace.trace.reader.ex.NoMorePacketsException;
import ca.eepp.quatre.java.javeltrace.trace.reader.ex.ReaderException;

/**
 * Interface for streaming in a trace.
 * <p>
 * This interface presents methods for reading a trace by streaming it. This
 * means it has no seeking methods and will always go forward. It is however
 * possible to seek over a complete packet since this means going forward
 * anyway.
 * <p>
 * For a random access trace reader, see {@link IRandomAccessReader}.
 * 
 * @author Philippe Proulx
 */
public interface IStreamedReader extends IReader {
    /**
     * Gets the current packet information.
     * <p>
     * The reader always "points to" a current packet. When the trace is first
     * opened, the reader points to the first packet.
     * <p>
     * If there's no more packet, the method returns null.
     * 
     * @return  Current packet information or null if no more packet
     * @throws ReaderException       If there's any reading exception
     */
    public PacketInfo getCurrentPacketInfo() throws ReaderException;
    
    /**
     * Goes to the next packet.
     * <p>
     * This method skips over all the current packet's events. If the current
     * packet information is not null, it is always safe to call this one.
     * <p>
     * Packets are read in order of starting time stamp. 
     * 
     * @throws ReaderException       If there's any reading exception
     * @throws NoMorePacketsException   If the method is called when there's no more packet
     */
    public void nextPacket() throws ReaderException, NoMorePacketsException;
    
    /**
     * Gets the current packet event.
     * <p>
     * The reader always "points to" a current event. When the trace is first
     * opened, the reader points to the first event of the first packet.
     * <p>
     * If there's no more event within the current packet, the method
     * returns null. 
     * 
     * @return  Current event or null if no more event
     * @throws ReaderException   If there's any reading exception
     */
    public Event getCurrentPacketEvent() throws ReaderException;
    
    /**
     * Goes to the next packet event.
     * <p>
     * This method jumps to the next event within the current packet. If the
     * current event is not null, it is always safe to call this one.
     * <p>
     * Events are read in chronological order within the current packet. 
     * 
     * @throws ReaderException       If there's any reading exception
     * @throws NoMoreEventsException    If the method is called when there's no more event
     */
    public void nextPacketEvent() throws ReaderException, NoMoreEventsException;
}
