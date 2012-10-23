package ca.eepp.quatre.java.javeltrace.metadata.strings;

/**
 * Some predefined metadata field names.
 * 
 * @author Philippe Proulx
 */
@SuppressWarnings("nls")
public final class MetadataFieldNames {
    // Packet header
    public static final String PACKET_HEADER_MAGIC = "magic";
    public static final String PACKET_HEADER_UUID = "uuid";
    public static final String PACKET_HEADER_STREAM_ID = "stream_id";
    
    // Packet context
    public static final String PACKET_CONTEXT_CONTENT_SIZE = "content_size";
    public static final String PACKET_CONTEXT_PACKET_SIZE = "packet_size";
    public static final String PACKET_CONTEXT_CPU_ID = "cpu_id";
    public static final String PACKET_CONTEXT_TS_BEGIN = "timestamp_begin";
    public static final String PACKET_CONTEXT_TS_END = "timestamp_end";
    public static final String PACKET_CONTEXT_EV_DISCARDED = "events_discarded";
    
    // Event header
    public static final String EVENT_HEADER_V = "v";
    public static final String EVENT_HEADER_ID = "id";
    public static final String EVENT_HEADER_TS = "timestamp";
}