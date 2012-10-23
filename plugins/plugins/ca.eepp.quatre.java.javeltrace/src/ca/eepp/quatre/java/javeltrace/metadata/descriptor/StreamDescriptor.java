package ca.eepp.quatre.java.javeltrace.metadata.descriptor;

import java.util.HashMap;

import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDeclaration;

import ca.eepp.quatre.java.javeltrace.metadata.ex.StreamDescriptorException;

/**
 * Describes a CTF stream.
 *
 * @author Philippe Proulx
 */
public class StreamDescriptor {
    private final int id;
    private final StructDeclaration eventHeader;
    private final StructDeclaration packetContext;
    private final StructDeclaration eventContext;
    private StructDeclaration packetHeader;
    private final HashMap<Integer, EventDescriptor> events;

    /**
     * Gets all stream's events descriptors.
     *
     * @return  Hash map of IDs to events descriptors
     */
    public HashMap<Integer, EventDescriptor> getEventDescriptors() {
        return this.events;
    }

    /**
     * Builds an empty stream descriptor.
     *
     * @param id            Stream ID
     * @param eventHeader   Event header declaration
     * @param packetContext Packet context declaration or null if none
     * @param eventContext  Per-stream event context declaration or null if none
     */
    public StreamDescriptor(int id, StructDeclaration eventHeader, StructDeclaration packetContext,
            StructDeclaration eventContext) {
        this.id = id;
        this.eventHeader = eventHeader;
        this.packetContext = packetContext;
        this.eventContext = eventContext;
        this.events = new HashMap<Integer, EventDescriptor>();
    }

    /**
     * Adds an event descriptor.
     * <p>
     * If there's an existing event descriptor for the provided ID, it is
     * overwritten with the provided event descriptor.
     *
     * @param id    Event ID within this stream
     * @param ed    Event descriptor
     */
    public void addEvent(int id, EventDescriptor ed) {
        ed.setHeader(this.eventHeader);
        ed.setPerStreamContext(this.eventContext);
        this.events.put(new Integer(id), ed);
    }

    /**
     * Checks if an event descriptor exists.
     *
     * @param id    Event ID within this stream
     * @return      True if event descriptor exists
     */
    public boolean eventExists(int id) {
        return this.events.containsKey(new Integer(id));
    }

    /**
     * Gets an event descriptor.
     *
     * @param id    Event ID within this stream
     * @return      Event descriptor
     * @throws StreamDescriptorException If no event descriptor exists for the ID
     */
    public EventDescriptor getEvent(int id) throws StreamDescriptorException {
        Integer iid = new Integer(id);
        if (!this.events.containsKey(iid)) {
            throw new StreamDescriptorException("Event descriptor does not exist for ID " + id); //$NON-NLS-1$
        }

        return this.events.get(iid);
    }

    public int getID() {
        return this.id;
    }

    /**
     * Gets the packet context declaration.
     *
     * @return  Packet context declaration or null if none
     */
    public StructDeclaration getPacketContext() {
        return this.packetContext;
    }

    public StructDeclaration getPacketHeader() {
        return this.packetHeader;
    }

    public StructDeclaration getEventHeader() {
        return this.eventHeader;
    }

    public StructDeclaration getEventContext() {
        return this.eventContext;
    }

    public void setPacketHeader(StructDeclaration packetHeader) {
        this.packetHeader = packetHeader;
    }
}
