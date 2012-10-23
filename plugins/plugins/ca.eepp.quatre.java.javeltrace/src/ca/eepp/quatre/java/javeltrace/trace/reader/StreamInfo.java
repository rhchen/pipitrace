package ca.eepp.quatre.java.javeltrace.trace.reader;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.linuxtools.ctf.core.trace.data.types.ScopeNode;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;

import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.metadata.descriptor.EventDescriptor;
import ca.eepp.quatre.java.javeltrace.metadata.descriptor.StreamDescriptor;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;

/**
 * Common information of a stream.
 * <p>
 * This abstract class exists to help the development of implementations of {@link IReader}.
 * Procedures common to all streams are already implemented here and individual readers
 * may add anything relevant to their specific needs by extending this class.
 * <p>
 * After having called {@link #init()} with valid trace parameters, a map of event
 * definitions is created. The scope tree is managed by this class.
 *
 * @author ephipro
 *
 */
public abstract class StreamInfo {
    protected final TraceParameters params;
    public final int streamID;
    public final HashMap<Integer, Event> eventsDefsMap = new HashMap<Integer, Event>();
    public StructDefinition eventHeaderDef;
    public StructDefinition eventPerStreamContextDef;
    public PacketInfo packetInfoDef = null;
    private StreamDescriptor descriptor;
    private ScopeNode scopeRoot;
    private ScopeNode scopeTracePacket;
    private ScopeNode scopeStreamPacket;
    private ScopeNode scopeStreamEvent;
    private ScopeNode scopeEvent;

    /**
     * Builds a stream info.
     *
     * @param params    Valid trace parameters
     */
    public StreamInfo(TraceParameters params, int streamID) {
        this.params = params;
        this.streamID = streamID;
        this.init();
    }

    private void init() {
        // Set descriptors
        this.descriptor = this.params.streams.get(this.streamID);

        // Initialize the scope tree
        this.initScopeTree();

        // Create the packet that will be used for this stream
        this.initPacketInfo();

        // Initialize header/context common to all events within this stream
        this.initEventHeaderContext();

        // Initialize the event definitions
        this.initEventsDefs();
    }

    private void initScopeTree() {
        // Root
        this.scopeRoot = new ScopeNode();

        // Add "stream", "trace", "env" and "event" to root
        ScopeNode streamNode = this.scopeRoot.addChild("stream"); //$NON-NLS-1$
        ScopeNode traceNode = this.scopeRoot.addChild("trace"); //$NON-NLS-1$
        this.scopeEvent = this.scopeRoot.addChild("event"); //$NON-NLS-1$
        this.scopeRoot.addChild("env"); // Useless node, but still a dynamic scope... //$NON-NLS-1$

        // Add "trace.packet"
        this.scopeTracePacket = traceNode.addChild("packet"); //$NON-NLS-1$

        // Add "stream.packet"
        this.scopeStreamPacket = streamNode.addChild("packet"); //$NON-NLS-1$

        // Add "stream.event"
        this.scopeStreamEvent = streamNode.addChild("event"); //$NON-NLS-1$
    }

    private void initEventHeaderContext() {
        // Header (parent scope node already exists)
        this.eventHeaderDef = this.descriptor.getEventHeader()
                .createDefinition(this.scopeStreamEvent, "header"); //$NON-NLS-1$

        // Context (parent scope node already exists)
        if (this.descriptor.getEventContext() != null) {
            this.eventPerStreamContextDef = this.descriptor.getEventContext()
                    .createDefinition(this.scopeStreamEvent, "context"); //$NON-NLS-1$
        }
    }

    private void initPacketInfo() {
        // Header (parent scope node already exists)
        StructDefinition packetHeader = this.descriptor.getPacketHeader()
                .createDefinition(this.scopeTracePacket, "header"); //$NON-NLS-1$

        // Context (parent scope node already exists)
        StructDefinition packetContext = this.descriptor.getPacketContext()
                .createDefinition(this.scopeStreamPacket, "context"); //$NON-NLS-1$

        // Complete packet info
        this.packetInfoDef = new PacketInfo(this.streamID, packetHeader, packetContext);
    }

    private void initEventsDefs() {
        // Browse all events descriptors and create events
        HashMap<Integer, EventDescriptor> descrMap = this.params.streams.get(this.streamID).getEventDescriptors();
        for (Map.Entry<Integer, EventDescriptor> entry : descrMap.entrySet()) {
            int id = entry.getKey();
            EventDescriptor descr = entry.getValue();

            // Update scope tree to receive new event
            this.scopeEvent.removeAllChildren();

            // Create complete definition
            Event ev = descr.createEvent(this.eventHeaderDef, this.eventPerStreamContextDef,
                    this.scopeEvent, this.scopeEvent);
            ev.setName(descr.getName());
            ev.setID(id);
            ev.setStreamID(this.streamID);

            // Add it
            this.eventsDefsMap.put(id, ev);
        }
    }
}
