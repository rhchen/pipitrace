package ca.eepp.quatre.java.javeltrace.trace;

import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;

/**
 * A CTF event.
 * <p>
 * This class defines a CTF event, that is: a header, an optional per-stream
 * context, an optional per-event context and a payload.
 * <p>
 * For convenience, its name and ID are also kept in this class. The owner
 * packet is optional, but can be useful to find the stream ID, for example.
 *
 * @author Philippe Proulx
 */
public class Event {
    private String name;
    private int id;
    private int streamID;
    private int cpuID = -1;
    private StructDefinition header;
    private StructDefinition streamEventContext = null;
    private StructDefinition context = null;
    private StructDefinition payload;
    private long ts;

    /**
     * Builds an event.
     *
     * @param header    Header definition
     * @param streamEventContext    Per-stream context definition or null if none
     * @param context   Per-event context definition or null if none
     * @param payload   Payload definition
     */
    public Event(StructDefinition header, StructDefinition streamEventContext,
            StructDefinition context, StructDefinition payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Event payload cannot be null"); //$NON-NLS-1$
        }
        if (header == null) {
            throw new IllegalArgumentException("Event header cannot be null"); //$NON-NLS-1$
        }
        this.header = header;
        this.streamEventContext = streamEventContext;
        this.context = context;
        this.payload = payload;
    }

    /**
     * Builds a deep copy of an event.
     *
     * @param ev    Event to copy
     */
    public Event(Event ev) {
        // Assign name/ID
        this.name = ev.name;
        this.id = ev.id;

        // Copy structures
        this.header = new StructDefinition(ev.header);
        if (ev.streamEventContext != null) {
            this.streamEventContext = new StructDefinition(ev.streamEventContext);
        }
        if (ev.context != null) {
            this.context = new StructDefinition(ev.context);
        }
        this.payload = new StructDefinition(ev.payload);
    }

    public StructDefinition getHeader() {
        return this.header;
    }

    public void setHeader(StructDefinition header) {
        this.header = header;
    }

    public StructDefinition getStreamContext() {
        return this.streamEventContext;
    }

    public void setStreamContext(StructDefinition streamContext) {
        this.streamEventContext = streamContext;
    }

    public StructDefinition getContext() {
        return this.context;
    }

    public void setContext(StructDefinition context) {
        this.context = context;
    }

    public StructDefinition getPayload() {
        return this.payload;
    }

    public void setPayload(StructDefinition payload) {
        this.payload = payload;
    }

    /**
     * Gets the event binary size.
     * <p>
     * Parameter <code>offset</code> is the bit offset within the packet of
     * the event header. It is used to compute the real occupied binary size
     * by considering the field alignments.
     *
     * @param offset    Event offset (bits)
     * @return          Real event size (bits)
     */
    public long getSize(long offset) {
        long at = offset;

        // Header size
        at += this.header.getSize(at);

        // Per-stream event context size
        if (this.streamEventContext != null) {
            at += this.streamEventContext.getSize(at);
        }

        // Per-event event context size
        if (this.context != null) {
            at += this.context.getSize(at);
        }

        // Payload size
        at += this.payload.getSize(at);

        return at - offset;
    }

    public long getTimeStamp() {
        return this.ts;
    }

    public void setTimeStamp(long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("event (id = " + this.id + ", name = " + this.name + ") {\n  header: " + this.header.toString(1) + ","); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        if (this.streamEventContext != null) {
            sb.append("\n  perStreamContext: " + this.streamEventContext.toString(1) + ","); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (this.context != null) {
            sb.append("\n  perEventcontext: " + this.context.toString(1) + ","); //$NON-NLS-1$ //$NON-NLS-2$
        }
        sb.append("\n  payload: " + this.payload.toString(1) + "\n}"); //$NON-NLS-1$ //$NON-NLS-2$

        return sb.toString();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getStreamID() {
        return this.streamID;
    }

    public void setStreamID(int id) {
        this.streamID = id;
    }

    public int getCPUID() {
        return this.cpuID;
    }

    public void setCPUID(int cpuID) {
        this.cpuID = cpuID;
    }
}
