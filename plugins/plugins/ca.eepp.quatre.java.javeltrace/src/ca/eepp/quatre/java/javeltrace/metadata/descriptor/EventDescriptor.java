package ca.eepp.quatre.java.javeltrace.metadata.descriptor;

import org.eclipse.linuxtools.ctf.core.trace.data.types.ScopeNode;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;

import ca.eepp.quatre.java.javeltrace.metadata.strings.MetadataStrings;
import ca.eepp.quatre.java.javeltrace.trace.Event;

/**
 * Describes a CTF event.
 *
 * @author Philippe Proulx
 */
public class EventDescriptor {
    private final String name;
    private final int id;
    private final StreamDescriptor streamDesc;
    private StructDeclaration header;
    private StructDeclaration perStreamContext = null;
    private StructDeclaration perEventContext = null;
    private StructDeclaration payload;

    /**
     * Builds an empty event descriptor.
     * <p>
     * Contents such as the header and the payload must be set afterwards.
     *
     * @param name  Event name within its stream
     * @param id    Event ID within its stream
     * @param streamDesc    Stream containing this event
     */
    public EventDescriptor(String name, int id,
            StreamDescriptor streamDesc) {
        this.name = name;
        this.id = id;
        this.streamDesc = streamDesc;
    }

    public StructDeclaration getHeader() {
        return this.header;
    }

    public void setHeader(StructDeclaration header) {
        this.header = header;
    }

    /**
     * Gets the per-stream context.
     *
     * @return  Per-stream context or null if none
     */
    public StructDeclaration getPerStreamContext() {
        return this.perStreamContext;
    }

    /**
     * Sets the per-stream context.
     *
     * @param perStreamContext  Per-stream context or null if none
     */
    public void setPerStreamContext(StructDeclaration perStreamContext) {
        this.perStreamContext = perStreamContext;
    }

    /**
     * Gets the per-event context.
     *
     * @return  Per-event context or null if none
     */
    public StructDeclaration getPerEventContext() {
        return this.perEventContext;
    }

    /**
     * Sets the per-event context.
     *
     * @param perEventContext  Per-event context or null if none
     */
    public void setPerEventContext(StructDeclaration perEventContext) {
        this.perEventContext = perEventContext;
    }

    public StructDeclaration getPayload() {
        return this.payload;
    }

    public void setPayload(StructDeclaration payload) {
        this.payload = payload;
    }

    public int getID() {
        return this.id;
    }

    /**
     * Creates an event out of the contained description.
     * <p>
     * Caller must provide parent scope nodes for two structures specific to any
     * event here (namely event context and payload) and those nodes have to exist
     * within a proper tree.
     * <p>
     * The header and per-stream context can be passed directly since they are the
     * same for all events within a stream and this enables optimization and
     * avoids replication.
     *
     * @param headerDef                     Header definition
     * @param perStreamContextDef           Per-stream context definition
     * @param perEventContextParentNode     Per-event context parent scope node
     * @param fieldsParentNode              Fields parent scope node
     * @return  Created event (ready to be read)
     */
    public Event createEvent(StructDefinition headerDef, StructDefinition perStreamContextDef,
            ScopeNode perEventContextParentNode, ScopeNode fieldsParentNode) {
        StructDefinition perEventContextDef = null;
        if (this.perEventContext != null) {
            perEventContextDef = this.perEventContext.createDefinition(perEventContextParentNode, MetadataStrings.CONTEXT);
        }
        StructDefinition fieldsDef = this.payload.createDefinition(fieldsParentNode, MetadataStrings.FIELDS);

        // Create the event
        Event ev = new Event(headerDef, perStreamContextDef, perEventContextDef, fieldsDef);

        return ev;
    }

    public StreamDescriptor getStreamDesc() {
        return this.streamDesc;
    }

    public String getName() {
        return this.name;
    }
}
