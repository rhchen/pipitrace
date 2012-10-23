package ca.eepp.quatre.java.javeltrace.utils;

import java.nio.ByteOrder;
import java.util.UUID;

import org.antlr.runtime.tree.CommonTree;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDeclaration;

import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.metadata.DeclarationScope;
import ca.eepp.quatre.java.javeltrace.metadata.IMetadataTreeAnalyzerVisitor;
import ca.eepp.quatre.java.javeltrace.metadata.MetadataTreeAnalyzer;
import ca.eepp.quatre.java.javeltrace.metadata.descriptor.ClockDescriptor;
import ca.eepp.quatre.java.javeltrace.metadata.descriptor.EventDescriptor;
import ca.eepp.quatre.java.javeltrace.metadata.descriptor.StreamDescriptor;
import ca.eepp.quatre.java.javeltrace.metadata.ex.ParsingException;

/**
 * MTA visitor filling trace parameters.
 * <p>
 * This is a specific MTA visitor which fills a {@link TraceParameters} object
 * with what it gets from the MTA ({@link MetadataTreeAnalyzer}).
 * <p>
 * This object also creates and adds stream descriptors to a referenced
 * hash map.
 *
 * @author Philippe Proulx
 */
public class TraceParametersMTAVisitor implements IMetadataTreeAnalyzerVisitor {
    private final TraceParameters params;
    private StructDeclaration packetHeaderDecl = null;
    private ByteOrder globalByteOrder = null;

    /**
     * Builds a trace parameters MTA visitor.
     *
     * @param params    Trace parameters to fill
     */
    public TraceParametersMTAVisitor(TraceParameters params) {
        this.params = params;
    }

    @Override
    public void addClock(String name, UUID uuid, String description, Long freq, Long offset) {
        if (this.params.clocks.containsKey(name)) {
            throw new ParsingException("Clock \"" + name + "\" defined multiple times"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        ClockDescriptor clock = new ClockDescriptor(name, uuid, description, freq, offset);
        this.params.clocks.put(name, clock);
    }

    @Override
    public void addEnvProperty(String key, String value) {
        // This will overwrite any previous value associated to that key
        this.params.env.addProperty(key, value);
    }

    @Override
    public void addEnvProperty(String key, Long value) {
        // This will overwrite any previous value associated to that key
        this.params.env.addProperty(key, value);
    }

    @Override
    public void setTrace(Integer major, Integer minor, UUID uuid, ByteOrder bo,
            StructDeclaration packetHeaderDecl) {
        this.params.major = major;
        this.params.minor = minor;
        this.params.uuid = uuid;
        this.params.byteOrder = bo;

        // Update packet header declaration for all streams
        this.packetHeaderDecl = packetHeaderDecl; // For following streams
        for (StreamDescriptor desc : this.params.streams.values()) {
            desc.setPacketHeader(packetHeaderDecl);
        }
    }

    @Override
    public void addStream(Integer id, StructDeclaration eventHeaderDecl,
            StructDeclaration eventContextDecl,
            StructDeclaration packetContextDecl) {
        Integer key = new Integer(id);
        if (this.params.streams.containsKey(key)) {
            throw new ParsingException("Stream " + id + " defined multiple times"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        StreamDescriptor stream = new StreamDescriptor(id, eventHeaderDecl,
                packetContextDecl, eventContextDecl);
        stream.setPacketHeader(this.packetHeaderDecl);
        this.params.streams.put(key, stream);
    }

    @Override
    public void addEvent(Integer id, Integer streamID, String name,
            StructDeclaration context, StructDeclaration fields, Integer logLevel) {
        Integer key = new Integer(streamID);
        if (!this.params.streams.containsKey(key)) {
            throw new ParsingException("For event \"" + name + "\" (" + id + "): stream " + id + " doesn't exist"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }

        // Get stream
        StreamDescriptor stream = this.params.streams.get(key);
        if (stream.eventExists(id)) {
            throw new ParsingException("Event \"" + name + "\" (" + id + ") defined multiple times for stream " + streamID); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        // Create event
        EventDescriptor event = new EventDescriptor(name, id, stream);
        event.setPerEventContext(context);
        event.setPayload(fields);

        // Add event to stream
        stream.addEvent(id, event);
    }

    @Override
    public ByteOrder getGlobalByteOrder() {
        return this.globalByteOrder;
    }

    @Override
    public void setGlobalByteOrder(ByteOrder bo) {
        this.globalByteOrder = bo;
    }

    @Override
    public void infoParsingRoot(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoParsingEnv(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoParsingClock(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoParsingTrace(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoParsingStream(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoParsingEvent(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoParsingTypedef(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoParsingTypealias(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoPushedScope(DeclarationScope scope) {
        // Nothing to do
    }

    @Override
    public void infoPopedScope(DeclarationScope scope) {
        // Nothing to do
    }

    @Override
    public void infoParsingFloat(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoParsingInteger(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoParsingString(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoParsingEnum(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoParsingStruct(CommonTree node) {
        // Nothing to do
    }

    @Override
    public void infoParsingVariant(CommonTree node) {
        // Nothing to do
    }
}