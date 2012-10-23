package ca.eepp.quatre.java.javeltrace.translation;

import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;

/**
 * Observer of a translation process.
 * <p>
 * Implementors of this interface may be observers of the translation processes
 * of {@link Translator}. This can be used to provide instant feedback to users
 * about the translation progress. 
 * 
 * @author Philippe Proulx
 */
public interface ITranslatorObserver {
    /**
     * Translation starts.
     */
    public void notifyStart();
    
    /**
     * Translation processes a new packet.
     * 
     * @param packetInfo    Processed packet information
     */
    public void notifyNewPacket(PacketInfo packetInfo);
    
    /**
     * Translation processes a new event.
     * 
     * @param ev    Processed event
     */
    public void notifyNewEvent(Event ev);
    
    /**
     * Translation stops.
     */
    public void notifyStop();
}
