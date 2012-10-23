package ca.eepp.quatre.java.javeltrace;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.UUID;


import ca.eepp.quatre.java.javeltrace.metadata.descriptor.ClockDescriptor;
import ca.eepp.quatre.java.javeltrace.metadata.descriptor.EnvironmentDescriptor;
import ca.eepp.quatre.java.javeltrace.metadata.descriptor.StreamDescriptor;
import ca.eepp.quatre.java.javeltrace.trace.input.TraceInput;
import ca.eepp.quatre.java.javeltrace.trace.output.TraceOutput;

/**
 * Parameters common to all CTF traces.
 * <p>
 * These parameters are common to all traces and are often to be shared
 * between several classes. They do not contain information about packets,
 * events, declarations and so on, but only simple values.
 * <p>
 * {@link TraceInput} objects fill them while reading a trace and
 * {@link TraceOutput} objects use them for proper output.
 *
 * @author Philippe Proulx
 */
public class TraceParameters {
    /** Metadata TSDL text string */
    public String metadataText = null;

    /** Trace major number */
    public int major = -1;

    /** Trace minor number */
    public int minor = -1;

    /** Trace UUID */
    public UUID uuid = null;

    /** Trace default byte order */
    public ByteOrder byteOrder = null;

    /** Trace clock descriptors (name to clock descriptor) */
    public final HashMap<String, ClockDescriptor> clocks = new HashMap<String, ClockDescriptor>();

    /** Trace environment */
    public final EnvironmentDescriptor env = new EnvironmentDescriptor();

    /** Stream descriptors */
    public final HashMap<Integer, StreamDescriptor> streams = new HashMap<Integer, StreamDescriptor>();
}
