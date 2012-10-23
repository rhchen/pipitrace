package ca.eepp.quatre.java.javeltrace.trace.reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.metadata.descriptor.StreamDescriptor;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.reader.ex.JSONReaderException;
import ca.eepp.quatre.java.javeltrace.trace.reader.ex.NoMoreEventsException;
import ca.eepp.quatre.java.javeltrace.trace.reader.ex.NoMorePacketsException;
import ca.eepp.quatre.java.javeltrace.trace.reader.ex.ReaderException;
import ca.eepp.quatre.java.javeltrace.trace.writer.JSONCTFWriter;
import ca.eepp.quatre.java.javeltrace.utils.TimeStampAccumulator;
import ca.eepp.quatre.java.javeltrace.utils.Utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import org.eclipse.linuxtools.ctf.core.trace.data.types.ArrayDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.Definition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.EnumDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.FloatDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.SequenceDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StringDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.VariantDefinition;

/**
 * JSON streamed reader for CTF.
 * <p>
 * This reader is fully compatible with what {@link JSONCTFWriter} produces.
 *
 * @author Philippe Proulx
 */
public class JSONCTFStreamedReader implements IStreamedReader {
    private File path;//RH Hack
    private String metadataText = null;
    private Event currentEvent = null;
    private PacketInfo currentPacket = null;
    private JsonParser jsonParser = null;
    private final TimeStampAccumulator acc = new TimeStampAccumulator();
    private HashMap<Integer, StreamInfo> streamInfos;
    private Stack<NodeReadContext> jsonStack = null;
    private TraceParameters params = null;

    private InputStream inputStream = null;//RH Hack
    /**
     * Builds a JSON streamed reader.
     *
     * @param path  Input path (file)
     */
    public JSONCTFStreamedReader(File path) {
        this.path = path;
    }

    /**
     * 
     * @param metadataPath : metadata.tsdl
     * @param inputStream : json inputstream
     */
    public JSONCTFStreamedReader(File metadataPath, InputStream inputStream) {
    	this.path = metadataPath;
        this.inputStream = inputStream;
    }
    
    /**
     * Builds a JSON streamed reader.
     *
     * @param path  Input path (file)
     */
    public JSONCTFStreamedReader(String path) {
        this(new File(path));
    }

    private void jsonParserReset(boolean closeBefore) throws IOException {
        if (closeBefore) {
            this.jsonParserClose();
        }
        /*
         * Important: use a MappingJsonFactory here, and not a simple
         * JsonFactory. Otherwise, the JSON parser won't be able to
         * read a node as a tree (readValueAsTree).
         */
        MappingJsonFactory factory = new MappingJsonFactory();
        
		if (this.inputStream != null) {

			this.jsonParser = factory.createJsonParser(this.inputStream);

		} else {

			this.jsonParser = factory.createJsonParser(this.path);

		}
		
        //this.jsonParser = factory.createJsonParser(this.path);
    }

    private void jsonParserClose() {
        if (this.jsonParser == null) {
            return;
        }
        try {
            this.jsonParser.close();
            this.jsonParser = null;
        } catch (IOException e) {
            throw new JSONReaderException("Cannot close JSON parser"); //$NON-NLS-1$
        }
    }

    private void readExternalMetadata(String filename) throws IOException {
        String path = this.path.getAbsoluteFile().getParent() + File.separator + filename;
        this.metadataText = Utils.UTF8StringFromTextFileContent(path);
    }

    private void setMetadata(String jsonMetadataString) {
        // Check whether the metadata is an external file or not
        if (jsonMetadataString.matches("^external:.*")) { //$NON-NLS-1$
            String metadataFilename = jsonMetadataString.replaceFirst("^external:", ""); //$NON-NLS-1$ //$NON-NLS-2$
            try {
                this.readExternalMetadata(metadataFilename);
            } catch (IOException e) {
            	throw new JSONReaderException("Cannot read external metadata file \"" + //$NON-NLS-1$
                        metadataFilename + 
                        "\", "+ e.getMessage()); //$NON-NLS-1$
            }
        } else {
            this.metadataText = jsonMetadataString;
        }
    }

    @Override
    public void openTrace() throws ReaderException {
        try {
            // Open the parser
            this.jsonParserReset(false);

            // We read the metadata text already
            while (this.jsonParser.getCurrentToken() != JsonToken.FIELD_NAME) {
                this.jsonParser.nextToken();
            }
            if (this.jsonParser.getCurrentToken() == null) {
                throw new JSONReaderException("Cannot find \"metadata\" node"); //$NON-NLS-1$
            }
            if ("metadata".equals(this.jsonParser.getCurrentName())) { //$NON-NLS-1$
                this.jsonParser.nextToken();
                if (this.jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                    this.setMetadata(this.jsonParser.getText());
                } else {
                    throw new JSONReaderException("\"metadata\" node must be a string"); //$NON-NLS-1$
                }
            } else {
                throw new ReaderException("Cannot find \"metadata\" node"); //$NON-NLS-1$
            }
        } catch (IOException e) {
            throw new JSONReaderException("Cannot parse JSON"); //$NON-NLS-1$
        }
    }

    @Override
    public void closeTrace() throws ReaderException {
        this.jsonParserClose();
    }

    @Override
    public String getMetadataText() throws ReaderException {
        return this.metadataText;
    }

    private void jsonReadNodeStruct(JsonNode currentNode, StructDefinition struct) {
        // Prepare the reading
        if (this.jsonStack == null) {
            this.jsonStack = new Stack<NodeReadContext>();
        }
        this.jsonStack.clear();
        this.jsonStack.push(new NodeReadContext(currentNode));

        // Read the structure
        struct.read(null, this);
    }

    private void jsonReadEvent() throws JsonParseException, IOException {
        // If we're at the end of an array here, then assume there's no more events
        if (this.jsonParser.getCurrentToken() == JsonToken.END_ARRAY) {
            this.currentEvent = null;

            return;
        }

        // Current token should be an object start
        if (this.jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            this.errorWithLine("Cannot parse event node"); //$NON-NLS-1$
        }
        this.jsonParser.nextToken();

        // Let's just read the object as a node
        JsonNode eventNode = this.jsonParser.readValueAsTree();
        this.jsonParser.nextToken();

        // Other nodes
        JsonNode headerNode = eventNode.get("header"); //$NON-NLS-1$
        if (headerNode == null) {
            this.errorWithLine("Event has no header"); //$NON-NLS-1$
        }
        JsonNode streamContextNode = eventNode.get("streamContext"); //$NON-NLS-1$
        JsonNode contextNode = eventNode.get("eventContext"); //$NON-NLS-1$
        JsonNode payloadNode = eventNode.get("payload"); //$NON-NLS-1$
        if (payloadNode == null) {
            this.errorWithLine("Event has no payload"); //$NON-NLS-1$
        }

        // Find the right SI
        StreamInfo si = this.streamInfos.get(this.currentPacket.getStreamID());

        /*
         * Find the event ID within the node (first "id" field that contains a
         * value that exists in the event definitions map.
         */
        List<JsonNode> idNodes = headerNode.findValues("id"); //$NON-NLS-1$
        Event ev = null;
        for (JsonNode idNode : idNodes) {
            int possibleID = -1;

            // Might be an enum: look at "value"
            if (idNode.isObject()) {
                JsonNode valueNode = idNode.get("value"); //$NON-NLS-1$
                if (valueNode != null) {
                    if (valueNode.isIntegralNumber()) {
                        possibleID = valueNode.intValue();
                    }
                }
            } else if (idNode.isIntegralNumber()) {
                possibleID = idNode.intValue();
            }
            if (si.eventsDefsMap.containsKey(possibleID)) {
                ev = si.eventsDefsMap.get(possibleID);
                break;
            }
        }

        // Event found?
        if (ev == null) {
            this.errorWithLine("Cannot find declaration for event"); //$NON-NLS-1$
        } else {
            // Read header
            this.jsonReadNodeStruct(headerNode, ev.getHeader());

            // Read per-stream context
            if (ev.getStreamContext() != null) {
                if (streamContextNode == null) {
                    this.errorWithLine("No per-stream event context node found"); //$NON-NLS-1$
                }
                this.jsonReadNodeStruct(streamContextNode, ev.getStreamContext());
            }

            // Read per-event context
            if (ev.getContext() != null) {
                if (contextNode == null) {
                    this.errorWithLine("No per-event event context node found"); //$NON-NLS-1$
                }
                this.jsonReadNodeStruct(streamContextNode, ev.getContext());
            }

            // Read payload
            this.jsonReadNodeStruct(payloadNode, ev.getPayload());

            // Time stamp
            this.acc.newEvent(ev);
            ev.setTimeStamp(this.acc.getTS());

            // Set it
            this.currentEvent = ev;
        }
    }

    private void jsonReadPacketInfo() throws JsonParseException, IOException {
        // If we're at the end of an array here, then assume there's no more packet
        if (this.jsonParser.getCurrentToken() == JsonToken.END_ARRAY) {
            this.currentPacket = null;
            this.currentEvent = null;

            return;
        }

        // Current token should be an object start
        if (this.jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            this.errorWithLine("Cannot parse packet node"); //$NON-NLS-1$
        }
        this.jsonParser.nextToken();

        // Current token should be a name
        if (this.jsonParser.getCurrentToken() != JsonToken.FIELD_NAME) {
            this.errorWithLine("Cannot parse packet node"); //$NON-NLS-1$
        }
        String name = this.jsonParser.getCurrentName();

        // Current node should be "header"
        if (!"header".equals(name)) { //$NON-NLS-1$
            this.errorWithLine("Expecting \"header\" node; got \"" + name + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        }
        this.jsonParser.nextToken();
        if (this.jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            this.errorWithLine("Packet \"header\" node should be an object"); //$NON-NLS-1$
        }
        JsonNode headerNode = this.jsonParser.readValueAsTree();
        this.jsonParser.nextToken();

        // Current node will either be "context" or "events"
        if (this.jsonParser.getCurrentToken() != JsonToken.FIELD_NAME) {
            this.errorWithLine("Cannot parse packet node"); //$NON-NLS-1$
        }
        name = this.jsonParser.getCurrentName();
        JsonNode contextNode = null;
        if ("context".equals(name)) { //$NON-NLS-1$
            this.jsonParser.nextToken();
            if (this.jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
                this.errorWithLine("Packet \"context\" node should be an object"); //$NON-NLS-1$
            }
            contextNode = this.jsonParser.readValueAsTree();
            this.jsonParser.nextToken();
        }

        // Get the stream descriptor by checking at the "stream_id" node
        JsonNode streamIDNode = headerNode.get("stream_id"); //$NON-NLS-1$
        if (streamIDNode == null) {
            this.errorWithLine("Packet header with no stream ID not supported yet"); //$NON-NLS-1$
        }
        int streamID = streamIDNode.intValue();
        if (!this.params.streams.containsKey(streamID)) {
            this.errorWithLine("Cannot find declaration of stream ID " + streamID); //$NON-NLS-1$
        }
        StreamDescriptor descr = this.params.streams.get(streamID);
        StreamInfo si = this.streamInfos.get(streamID);

        // Read the header
        this.jsonReadNodeStruct(headerNode, si.packetInfoDef.getHeader());

        // Read the context (if any)
        if (contextNode != null) {
            if (descr.getPacketContext() == null) {
                this.errorWithLine("JSON has a packet context but metadata has none"); //$NON-NLS-1$
            }
            this.jsonReadNodeStruct(contextNode, si.packetInfoDef.getContext());
        }

        // Set the current packet info
        si.packetInfoDef.updateCachedInfos();
        this.acc.setTS(si.packetInfoDef.getTimeStampBegin());
        this.currentPacket = si.packetInfoDef;

        // Here we are at the "events" JSON name (we hope)
        if (this.jsonParser.getCurrentToken() != JsonToken.FIELD_NAME) {
            this.errorWithLine("Cannot parse packet node"); //$NON-NLS-1$
        }
        name = this.jsonParser.getCurrentName();
        if (!"events".equals(name)) { //$NON-NLS-1$
            this.errorWithLine("Expecting \"events\" node; got \"" + name + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        }
        this.jsonParser.nextToken();
        if (this.jsonParser.getCurrentToken() != JsonToken.START_ARRAY) {
            this.errorWithLine("Packet \"events\" node should be an array"); //$NON-NLS-1$
        }
        this.jsonParser.nextToken();

        // Ready for events! Read the first one
        this.currentEvent = null;
        this.jsonReadEvent();
    }

    private void initStreamInfos() {
        this.streamInfos = new HashMap<Integer, StreamInfo>();
        for (Integer i : this.params.streams.keySet()) {
            JSONStreamInfo si = new JSONStreamInfo(this.params, i);
            this.streamInfos.put(i, si);
        }
    }

    @Override
    public void openStreams(TraceParameters params) {
        // Keep trace parameters
        this.params = params;

        // Initialize stream infos
        this.initStreamInfos();

        // Go to the "packets" node
        try {
            while (this.jsonParser.getCurrentToken() != JsonToken.FIELD_NAME) {
                this.jsonParser.nextToken();
            }
            if (this.jsonParser.getCurrentToken() == null) {
                this.errorWithLine("Cannot find \"packets\" node"); //$NON-NLS-1$
            }
            if ("packets".equals(this.jsonParser.getCurrentName())) { //$NON-NLS-1$
                this.jsonParser.nextToken();
                if (this.jsonParser.getCurrentToken() != JsonToken.START_ARRAY) {
                    this.errorWithLine("\"packets\" node must be an array"); //$NON-NLS-1$
                }

                // Ready for the first packet node
                this.jsonParser.nextToken();

                // Read first packet info
                this.jsonReadPacketInfo();
            } else {
                this.errorWithLine("Cannot find \"packets\" node"); //$NON-NLS-1$
            }
        } catch (IOException e) {
            this.errorWithLine("Cannot parse JSON"); //$NON-NLS-1$
        }
    }

    @Override
    public void closeStreams() throws ReaderException {
        // Nothing to do here...
    }

    @Override
    public void openStruct(StructDefinition def, String name) throws ReaderException {
        if (name != null) {
            // Node read context
            NodeReadContext context = this.jsonStack.peek();

            // Object node
            JsonNode objectNode = null;

            // Into array or object?
            if (context.node.isObject()) {
                // Object
                objectNode = context.node.get(name);
            } else {
                // Array
                objectNode = context.node.get(context.arrayIndex);
                ++context.arrayIndex;
            }
            if (objectNode == null) {
                this.errorWithLine("JSON layout doesn't match metadata (cannot open structure)"); //$NON-NLS-1$
            }
            if (!objectNode.isObject()) {
                this.errorWithLine("JSON layout doesn't match metadata (cannot open structure)"); //$NON-NLS-1$
            }
            this.jsonStack.push(new NodeReadContext(objectNode));
        }
    }

    @Override
    public void closeStruct(StructDefinition def, String name) throws ReaderException {
        if (name != null) {
            this.jsonStack.pop();
        }
    }

    @Override
    public void openVariant(VariantDefinition def, String name) throws ReaderException {
        // Nothing to do here
    }

    @Override
    public void closeVariant(VariantDefinition def, String name) throws ReaderException {
        // Nothing to do here
    }

    @Override
    public void openArray(ArrayDefinition def, String name) throws ReaderException {
        if (name != null) {
            // Node read context
            NodeReadContext context = this.jsonStack.peek();

            // Object node
            JsonNode arrayNode = null;

            // Into array or object?
            if (context.node.isObject()) {
                // Object
                arrayNode = context.node.get(name);
            } else {
                // Array
                arrayNode = context.node.get(context.arrayIndex);
                ++context.arrayIndex;
            }
            if (arrayNode == null) {
                this.errorWithLine("JSON layout doesn't match metadata (cannot open array)"); //$NON-NLS-1$
            }
            boolean doNotRead = false;
            if (arrayNode.isTextual() && def.isString()) {
                // Text array
                doNotRead = true;
                String arrayText = arrayNode.textValue();

                // Get the definitions
                Definition[] textDefs = Utils.defsFromUTF8String(arrayText, def.getLength());

                // Did this work?
                if (textDefs == null) {
                    this.errorWithLine("Cannot convert from JSON string to array since JSON string is too long for associated array"); //$NON-NLS-1$
                }

                // Set
                def.setDefinitions(textDefs);
            } else if (!arrayNode.isArray()) {
                this.errorWithLine("JSON layout doesn't match metadata (cannot open array)"); //$NON-NLS-1$
            }
            this.jsonStack.push(new NodeReadContext(arrayNode, doNotRead));
        }
    }

    @Override
    public void closeArray(ArrayDefinition def, String name) throws ReaderException {
        if (name != null) {
            this.jsonStack.pop();
        }
    }

    @Override
    public void openSequence(SequenceDefinition def, String name) throws ReaderException {
        if (name != null) {
            // Node read context
            NodeReadContext context = this.jsonStack.peek();

            // Object node
            JsonNode arrayNode = null;

            // Into array or object?
            if (context.node.isObject()) {
                // Object
                arrayNode = context.node.get(name);
            } else {
                // Array
                arrayNode = context.node.get(context.arrayIndex);
                ++context.arrayIndex;
            }
            if (arrayNode == null) {
                this.errorWithLine("JSON layout doesn't match metadata (cannot open sequence)"); //$NON-NLS-1$
            }
            boolean doNotRead = false;
            if (arrayNode.isTextual() && def.isString()) {
                // Text array
                doNotRead = true;
                String arrayText = arrayNode.textValue();

                // Get the definitions
                Definition[] textDefs = Utils.defsFromUTF8String(arrayText, def.getLength());

                // Did this work?
                if (textDefs == null) {
                    this.errorWithLine("Cannot convert from JSON string to sequence since JSON string is too long for associated sequence"); //$NON-NLS-1$
                }

                // Set
                def.setDefinitions(textDefs);
            } else if (!arrayNode.isArray()) {
                this.errorWithLine("JSON layout doesn't match metadata (cannot open sequence)"); //$NON-NLS-1$
            }
            this.jsonStack.push(new NodeReadContext(arrayNode, doNotRead));
        }
    }

    @Override
    public void closeSequence(SequenceDefinition def, String name) throws ReaderException {
        if (name != null) {
            this.jsonStack.pop();
        }
    }

    @Override
    public void readInteger(IntegerDefinition def, String name) throws ReaderException {
        // Current node read context
        NodeReadContext context = this.jsonStack.peek();

        // Do not read?
        if (context.doNotRead) {
            return;
        }

        // JSON node for this integer
        JsonNode intNode = null;

        // Into array or object?
        if (context.node.isObject()) {
            // Object
            intNode = context.node.get(name);
        } else if (context.node.isArray()) {
            // Array
            intNode = context.node.get(context.arrayIndex);
            ++context.arrayIndex;
        } else {
            // Oh oh...
            this.errorWithLine("JSON layout doesn't match metadata (cannot read integer)"); //$NON-NLS-1$
        }

        // Read integer node's content
        if (intNode == null) {
            this.errorWithLine("JSON layout doesn't match metadata (cannot read integer)"); //$NON-NLS-1$
        }
        if (!intNode.isIntegralNumber()) {
            this.errorWithLine("JSON layout doesn't match metadata (cannot read integer)"); //$NON-NLS-1$
        }
        def.setValue(intNode.longValue());
    }

    @Override
    public void readFloat(FloatDefinition def, String name) throws ReaderException {
        // Current node read context
        NodeReadContext context = this.jsonStack.peek();

        // Do not read?
        if (context.doNotRead) {
            return;
        }

        // JSON node for this float
        JsonNode floatNode = null;

        // Into array or object?
        if (context.node.isObject()) {
            // Object
            floatNode = context.node.get(name);
        } else if (context.node.isArray()) {
            // Array
            floatNode = context.node.get(context.arrayIndex);
            ++context.arrayIndex;
        } else {
            // Oh oh...
            this.errorWithLine("JSON layout doesn't match metadata (cannot read floating point number)"); //$NON-NLS-1$
        }

        // Read float node's content
        if (floatNode == null) {
            this.errorWithLine("JSON layout doesn't match metadata (cannot read floating point number)"); //$NON-NLS-1$
        }
        if (floatNode.isIntegralNumber()) {
            def.setValue(floatNode.asLong());
        } else if (floatNode.isObject()) {
            if (floatNode.has("raw") && floatNode.has("value")) { //$NON-NLS-1$ //$NON-NLS-2$
                if (floatNode.get("raw").isIntegralNumber() && floatNode.get("value").isFloatingPointNumber()) { //$NON-NLS-1$ //$NON-NLS-2$
                    def.setValue(floatNode.get("raw").asLong()); //$NON-NLS-1$
                } else {
                    this.errorWithLine("JSON layout doesn't match metadata (bad floating point number node)"); //$NON-NLS-1$
                }
            } else {
                this.errorWithLine("JSON layout doesn't match metadata (bad floating point number node)"); //$NON-NLS-1$
            }
        } else {
            this.errorWithLine("JSON layout doesn't match metadata (cannot read floating point number)"); //$NON-NLS-1$
        }
    }

    @Override
    public void readEnum(EnumDefinition def, String name) throws ReaderException {
        // Current node read context
        NodeReadContext context = this.jsonStack.peek();

        // Do not read?
        if (context.doNotRead) {
            return;
        }

        // JSON node for this enum
        JsonNode enumNode = null;

        // Into array or object?
        if (context.node.isObject()) {
            // Object
            enumNode = context.node.get(name);
        } else if (context.node.isArray()) {
            // Array
            enumNode = context.node.get(context.arrayIndex);
            ++context.arrayIndex;
        } else {
            // Oh oh...
            this.errorWithLine("JSON layout doesn't match metadata (cannot read enumeration)"); //$NON-NLS-1$
        }

        // Read enum node's content
        if (enumNode == null) {
            this.errorWithLine("JSON layout doesn't match metadata (cannot read enumeration)"); //$NON-NLS-1$
        }

        /*
         * The enum node can be a simple integer or an object where the (human)
         * reader sees the label and value for convenience. We just want the
         * value here.
         */
        if (enumNode.isIntegralNumber()) {
            // Read it as an integer without changing the node context
            this.readInteger(def.getIntegerDefinition(), name);
        } else if (enumNode.isObject()) {
            // Here we expect the "label" and "value" names, but we just consider "value"
            JsonNode valueNode = enumNode.get("value"); //$NON-NLS-1$
            if (valueNode == null) {
                this.errorWithLine("Cannot read enumeration: cannot find \"value\" into object node"); //$NON-NLS-1$
            }
            if (!valueNode.isIntegralNumber()) {
                this.errorWithLine("Cannot read enumeration: \"value\" node must be an integral number"); //$NON-NLS-1$
            }

            // This is it
            def.getIntegerDefinition().setValue(valueNode.longValue());
        }
    }

    @Override
    public void readString(StringDefinition def, String name) throws ReaderException {
        // Current node read context
        NodeReadContext context = this.jsonStack.peek();

        // Do not read?
        if (context.doNotRead) {
            return;
        }

        // JSON node for this string
        JsonNode stringNode = null;

        // Into array or object?
        if (context.node.isObject()) {
            // Object
            stringNode = context.node.get(name);
        } else if (context.node.isArray()) {
            // Array
            stringNode = context.node.get(context.arrayIndex);
            ++context.arrayIndex;
        } else {
            // Oh oh...
            this.errorWithLine("JSON layout doesn't match metadata (cannot read string)"); //$NON-NLS-1$
        }

        // Read string node's content
        if (stringNode == null) {
            this.errorWithLine("JSON layout doesn't match metadata (cannot read string)"); //$NON-NLS-1$
        }
        if (!stringNode.isTextual()) {
            this.errorWithLine("JSON layout doesn't match metadata (cannot read string)"); //$NON-NLS-1$
        }
        def.setString(stringNode.textValue());
    }

    @Override
    public PacketInfo getCurrentPacketInfo() throws ReaderException {
        return this.currentPacket;
    }

    @Override
    public void nextPacket() throws ReaderException {
        // Problem if we're already at the end
        if (this.currentPacket == null) {
            throw new NoMorePacketsException();
        }

        try {
            /*
             * Following what we normally do with a reader, we're always at the
             * start of an event. Or maybe we are after the last event. In any
             * case, we simply skip blocks as long as we're not at the end of
             * an array. Then we need to consume that array end, and then an
             * object end.
             */
            while (this.jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
                this.jsonParser.skipChildren();

                // Also skip the object end
                this.jsonParser.nextToken();
            }

            // Verify that this an array end as we expect
            if (this.jsonParser.getCurrentToken() != JsonToken.END_ARRAY) {
                this.errorWithLine("Malformed \"events\" array"); //$NON-NLS-1$
            }

            // Consume the array end
            this.jsonParser.nextToken();

            // Verify that this is an object end as we expect
            if (this.jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
                this.errorWithLine("Malformed packet object"); //$NON-NLS-1$
            }

            // Consume the object end
            this.jsonParser.nextToken();

            // Now read the following packet
            this.jsonReadPacketInfo();
        } catch (IOException e) {
            this.errorWithLine("Cannot read packet in JSON file"); //$NON-NLS-1$
        }
    }

    @Override
    public Event getCurrentPacketEvent() throws ReaderException {
        return this.currentEvent;
    }

    @Override
    public void nextPacketEvent() throws ReaderException {
        // Problem if we're already at the end
        if (this.currentEvent == null) {
            throw new NoMoreEventsException();
        }

        /*
         * We're always at the start of an object here. So going forward is
         * quite straight-forward: read the event.
         */
        try {
            this.jsonReadEvent();
        } catch (IOException e) {
            this.errorWithLine("Cannot read event in JSON file"); //$NON-NLS-1$
        }
    }

    private void errorWithLine(String msg) {
        int currentLine = this.jsonParser.getCurrentLocation().getLineNr();
        throw new JSONReaderException(msg + " @ line " + currentLine, currentLine); //$NON-NLS-1$
    }

    private class NodeReadContext {
        public final JsonNode node;
        public int arrayIndex = 0;
        public final boolean doNotRead;

        public NodeReadContext(JsonNode node, boolean doNotRead) {
            this.node = node;
            this.doNotRead = doNotRead;
        }

        public NodeReadContext(JsonNode node) {
            this(node, false);
        }
    }

    private class JSONStreamInfo extends StreamInfo {
        public JSONStreamInfo(TraceParameters params, int streamID) {
            super(params, streamID);
        }
    }
}
