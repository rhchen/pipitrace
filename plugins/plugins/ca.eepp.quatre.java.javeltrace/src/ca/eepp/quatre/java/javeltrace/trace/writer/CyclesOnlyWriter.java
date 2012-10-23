package ca.eepp.quatre.java.javeltrace.trace.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.eclipse.linuxtools.ctf.core.trace.data.types.ArrayDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.EnumDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.FloatDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.SequenceDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StringDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.VariantDefinition;

import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.metadata.descriptor.ClockDescriptor;
import ca.eepp.quatre.java.javeltrace.metadata.strings.MetadataFieldNames;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.writer.ex.WriterException;
import ca.eepp.quatre.java.javeltrace.utils.TimeStampAccumulator;

/**
 * A writer that only outputs events/packets cycle numbers.
 * <p>
 * This writer can be used for debug purposes. It outputs one complete event
 * cycle per line, and optionally packets time stamp begin/end ones too.
 *
 * @author Philippe Proulx
 */
public class CyclesOnlyWriter implements IWriter {
    private TimeStampAccumulator acc = new TimeStampAccumulator();
    private File path = null;
    private OutputStream stream = null;
    private OutputStreamWriter writer;
    private String evPrefix = "e"; //$NON-NLS-1$
    private String pktPrefix = "p"; //$NON-NLS-1$
    private boolean outputPackets = false;
    private boolean outputEvents = true;
    private boolean withOffset = false;
    private long offset = 0;

    /**
     * Builds a cycles only writer.
     *
     * @param path  Output file path
     */
    public CyclesOnlyWriter(File path) {
        this.path = path;
    }

    /**
     * Builds a cycles only writer using an output stream.
     *
     * @param stream    Output stream
     */
    public CyclesOnlyWriter(OutputStream stream) {
        this.stream = stream;
    }

    /**
     * Sets the event prefix.
     *
     * @param pref  Event prefix to put before any event cycle
     */
    public void setEventPrefix(String pref) {
        this.evPrefix = pref;
    }

    /**
     * Sets the packet prefix.
     *
     * @param pref  Packet prefix to put before any packet cycle
     */
    public void setPacketPrefix(String pref) {
        this.pktPrefix = pref;
    }

    /**
     * Sets whether to output events cycles or not.
     *
     * @param val   True to output events cycles
     */
    public void setOutputEvents(boolean val) {
        this.outputEvents = val;
    }

    /**
     * Sets whether to output packets cycles or not.
     *
     * @param val   True to output packets cycles
     */
    public void setOutputPackets(boolean val) {
        this.outputPackets = val;
    }

    /**
     * Sets whether to add the offset or not to printed cycles.
     * <p>
     * The offset is found in the trace metadata by looking at the appropriate
     * clock descriptor.
     *
     * @param val   True to add the offset
     */
    public void setWithOffset(boolean val) {
        this.withOffset = val;
    }

    @Override
    public void openTrace(TraceParameters params) throws WriterException {
        // Set time stamp accumulator with proper clock
        if (this.withOffset) {
            if (params.clocks.containsKey("monotonic")) { //$NON-NLS-1$
                ClockDescriptor clock = params.clocks.get("monotonic"); //$NON-NLS-1$
                this.acc = new TimeStampAccumulator(clock);
            }
        }

        // Open a stream
        OutputStream stream = this.stream;
        if (this.stream == null) {
            try {
                stream = new FileOutputStream(this.path);
            } catch (IOException e) {
                throw new WriterException("Cannot open file \"" + this.path.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        this.writer = new OutputStreamWriter(stream, Charset.forName("UTF-8")); //$NON-NLS-1$
    }

    @Override
    public void closeTrace() throws WriterException {
        if (this.path == null) {
            try {
                this.writer.close();
            } catch (IOException e) {
                throw new WriterException("Cannot close file \"" + this.path.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    @Override
    public void openStream(int id) throws WriterException {
        // Nothing to do
    }

    @Override
    public void closeStream(int id) throws WriterException {
        // Nothing to do
    }

    @Override
    public void openPacket(PacketInfo packet) throws WriterException {
        this.acc.setTS(0);
        if (packet.getContext() != null) {
            boolean hasBegin = false;
            boolean hasEnd = false;
            long begin = 0;
            long end = 0;
            if (packet.getContext().hasField(MetadataFieldNames.PACKET_CONTEXT_TS_BEGIN)) {
                begin = packet.getContext().getIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_TS_BEGIN);
                this.acc.setTS(begin);
                hasBegin = true;
            }
            if (packet.getContext().hasField(MetadataFieldNames.PACKET_CONTEXT_TS_END)) {
                end = packet.getContext().getIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_TS_BEGIN);
                hasEnd = true;
            }
            if (this.outputPackets && hasBegin && hasEnd) {
                try {
                    this.writer.write(this.pktPrefix + (begin + this.offset) + "-" + (end + this.offset) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
                } catch (IOException e) {
                    throw new WriterException("Cannot write to file"); //$NON-NLS-1$
                }
            }
        }
    }

    @Override
    public void closePacket(PacketInfo packet) throws WriterException {
        try {
            this.writer.flush();
        } catch (IOException e) {
            throw new WriterException("Cannot flush output"); //$NON-NLS-1$
        }
    }

    @Override
    public void writeEvent(Event ev) throws WriterException {
        if (!this.outputEvents) {
            return;
        }
        this.acc.newEvent(ev);
        try {
            this.writer.write(this.evPrefix + this.acc.getOffsetTS() + "\n"); //$NON-NLS-1$
        } catch (IOException e) {
            throw new WriterException("Cannot write to file"); //$NON-NLS-1$
        }
    }

    @Override
    public void openStruct(StructDefinition def, String name) throws WriterException {
        // Nothing to do
    }

    @Override
    public void closeStruct(StructDefinition def, String name) throws WriterException {
        // Nothing to do
    }

    @Override
    public void openVariant(VariantDefinition def, String name) throws WriterException {
        // Nothing to do
    }

    @Override
    public void closeVariant(VariantDefinition def, String name) throws WriterException {
        // Nothing to do
    }

    @Override
    public void openArray(ArrayDefinition def, String name) throws WriterException {
        // Nothing to do
    }

    @Override
    public void closeArray(ArrayDefinition def, String name) throws WriterException {
        // Nothing to do
    }

    @Override
    public void openSequence(SequenceDefinition def, String name) throws WriterException {
        // Nothing to do
    }

    @Override
    public void closeSequence(SequenceDefinition def, String name) throws WriterException {
        // Nothing to do
    }

    @Override
    public void writeInteger(IntegerDefinition def, String name) throws WriterException {
        // Nothing to do
    }

    @Override
    public void writeFloat(FloatDefinition def, String name) throws WriterException {
        // Nothing to do
    }

    @Override
    public void writeEnum(EnumDefinition def, String name) throws WriterException {
        // Nothing to do
    }

    @Override
    public void writeString(StringDefinition def, String name) throws WriterException {
        // Nothing to do
    }
}
