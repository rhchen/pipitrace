package ca.eepp.quatre.java.javeltrace.trace;

import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;

import ca.eepp.quatre.java.javeltrace.metadata.strings.MetadataFieldNames;

/**
 * The information of a CTF packet.
 * <p>
 * A CTF packet contains events, but also a header and an optional context.
 * This class doesn't represent a complete packet with all its contained
 * events but only its information (header + context).
 *
 * @author Philippe Proulx
 */
public final class PacketInfo {
    /** Means no CPU ID (not found in the context or no context) */
    public static final int NO_CPU_ID = -1;
    public static final long NO_SIZE = -1;

    private final StructDefinition header;
    private final StructDefinition context;
    private final int streamID;
    private int cpuID = PacketInfo.NO_CPU_ID;
    private long tsBegin = Long.MIN_VALUE;
    private long tsEnd = Long.MAX_VALUE;
    private long packetSize = PacketInfo.NO_SIZE;
    private long packetSizeBytes = PacketInfo.NO_SIZE;
    private long contentSize = PacketInfo.NO_SIZE;
    private Long headerContextSize = null;

    /**
     * Builds a packet information.
     *
     * @param streamID  Stream unique identifier
     * @param header    Header definition
     * @param context   Context definition or null if none
     */
    public PacketInfo(int streamID, StructDefinition header,
            StructDefinition context) {
        if (header == null) {
            throw new IllegalArgumentException("Header cannot be null"); //$NON-NLS-1$
        }
        this.header = header;
        this.context = context;
        this.streamID = streamID;
    }

    /**
     * Builds a deep copy of a packet information.
     *
     * @param pi    Packet information to copy
     */
    public PacketInfo(PacketInfo pi) {
        // Assign stream ID, infos.
        this.streamID = pi.streamID;
        this.tsBegin = pi.tsBegin;
        this.cpuID = pi.cpuID;
        this.tsEnd = pi.tsEnd;
        this.packetSize = pi.packetSize;
        this.packetSizeBytes = pi.packetSizeBytes;
        this.contentSize = pi.contentSize;
        this.headerContextSize = pi.headerContextSize;

        // Copy header/context
        this.header = new StructDefinition(pi.header);
        if (pi.context != null) {
            this.context = new StructDefinition(pi.context);
        } else {
            this.context = null;
        }
    }

    /**
     * Gets the header and context binary size.
     *
     * @return  Header + context size (bits)
     */
    public long getHeaderContextSize() {
        // Cache lookup
        if (this.headerContextSize != null) {
            return this.headerContextSize;
        }

        /*
         * We assume here that the packet offset is 0. The CTF specs tell us
         * that packets will always be aligned on multiples of the source
         * architecture page size.
         */
        long at = 0;

        // Header goes first
        at += this.header.getSize(at);

        // Context (if there's any)
        if (this.context != null) {
            at += this.context.getSize(at);
        }

        /*
         * Cache the result. A packet size shouldn't change once its header/context
         * are set, unless they contain sequences or variants, but this is not common
         * for the moment so we may optimize like this. Rollback to not caching if
         * we ever get a use case with variable-size packet header/context.
         */
        this.headerContextSize = at;

        return at;
    }

    public StructDefinition getHeader() {
        return this.header;
    }

    /**
     * Gets the packet context.
     *
     * @return  Packet context or null if none
     */
    public StructDefinition getContext() {
        return this.context;
    }

    /**
     * Gets the CPU ID found in the context if any.
     *
     * @return  CPU ID or {@link #NO_CPU_ID} if not found
     */
    public int getCPUID() {
        return this.cpuID;
    }

    /**
     * Gets the time stamp begin found in the context if any.
     *
     * @return  Time stamp begin or <code>Long.MIN_VALUE</code> if not found
     */
    public long getTimeStampBegin() {
        return this.tsBegin;
    }

    /**
     * Gets the time stamp end found in the context if any.
     *
     * @return  Time stamp end or <code>Long.MAX_VALUE</code> if not found
     */
    public long getTimeStampEnd() {
        return this.tsEnd;
    }

    public long getPacketSize() {
        return this.packetSize;
    }

    public long getPacketSizeBytes() {
        return this.packetSizeBytes;
    }

    public long getContentSize() {
        return this.contentSize;
    }

    /**
     * Updates the inner informations (usually after a read).
     * <p>
     * If this is not called right after doing a read for the inner structures,
     * informations such as time stamp begin and current CPU ID won't be synced
     * with the actual values.
     */
    public void updateCachedInfos() {
        if (this.context == null) {
            this.cpuID = PacketInfo.NO_CPU_ID;
            this.tsBegin = Long.MIN_VALUE;
            this.tsEnd = Long.MAX_VALUE;
            this.packetSize = PacketInfo.NO_SIZE;
            this.contentSize = PacketInfo.NO_SIZE;
        } else {
            IntegerDefinition intDef;

            // CPU ID
            intDef = this.context.lookupInteger(MetadataFieldNames.PACKET_CONTEXT_CPU_ID);
            if (intDef == null) {
                this.cpuID = PacketInfo.NO_CPU_ID;
            } else {
                this.cpuID = (int) intDef.getValue();
            }

            // Time stamp begin
            intDef = this.context.lookupInteger(MetadataFieldNames.PACKET_CONTEXT_TS_BEGIN);
            if (intDef == null) {
                this.tsBegin = Long.MIN_VALUE;
            } else {
                this.tsBegin = intDef.getValue();
            }

            // Time stamp end
            intDef = this.context.lookupInteger(MetadataFieldNames.PACKET_CONTEXT_TS_END);
            if (intDef == null) {
                this.tsEnd = Long.MAX_VALUE;
            } else {
                this.tsEnd = intDef.getValue();
            }

            // Packet size
            intDef = this.context.lookupInteger(MetadataFieldNames.PACKET_CONTEXT_PACKET_SIZE);
            if (intDef == null) {
                this.packetSize = PacketInfo.NO_SIZE;
            } else {
                this.packetSize = intDef.getValue();
            }
            this.packetSizeBytes = this.packetSize / 8;

            // Content size
            intDef = this.context.lookupInteger(MetadataFieldNames.PACKET_CONTEXT_CONTENT_SIZE);
            if (intDef == null) {
                this.contentSize = PacketInfo.NO_SIZE;
            } else {
                this.contentSize = intDef.getValue();
            }
        }
    }

    public int getStreamID() {
        return this.streamID;
    }

    public boolean includes(long ts) {
        return ts >= this.tsBegin && ts <= this.tsEnd;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("packet (streamID = " + this.streamID + ") {\n  header: " + this.header.toString(1) + ","); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        if (this.context != null) {
            sb.append("\n  context: " + this.context.toString(1) + ","); //$NON-NLS-1$ //$NON-NLS-2$
        }
        sb.append("\n}"); //$NON-NLS-1$

        return sb.toString();
    }
}
