package ca.eepp.quatre.java.javeltrace.trace.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Stack;

import org.eclipse.linuxtools.ctf.core.trace.data.types.ArrayDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.Definition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.EnumDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.FloatDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.SequenceDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StringDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.VariantDefinition;

import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.reader.JSONCTFStreamedReader;
import ca.eepp.quatre.java.javeltrace.trace.writer.ex.WriterException;

/**
 * A JSON writer for CTF.
 * <p>
 * Writes a valid JSON file representing a mirror of a complete CTF trace,
 * which means it is possible to go back to a CTF model using an equivalent
 * reader (see {@link JSONCTFStreamedReader}).
 *
 * @author Philippe Proulx
 */
@SuppressWarnings("nls")
public class JSONCTFWriter implements IWriter {
    private enum NodeStateType {
        OBJECT,
        ARRAY
    }
    private File path = null;
    private OutputStream stream = null;
    private TraceParameters params;
    private final boolean externalMetadataFile;
    private OutputStreamWriter output;
    private int currentIndentLevel = 0;
    private int spacersPerIndentLevel = 2;
    private String externalMetadataName = "metadata.tsdl";
    private String metadataJSON = null;
    private boolean firstPacketDone = false;
    private boolean firstEventDone = false;
    private Stack<NodeState> stateStack;
    private boolean writeExternalMetadataHeaderComment = true;
    private char indentChar = ' ';
    private boolean writeEventName = true;
    private boolean writeEventID = true;
    private boolean writeEnumLabel = false;
    private boolean writeFloatValue = false;
    private boolean pretty = true;

    /**
     * Builds a JSON writer.
     *
     * @param path  JSON file path
     * @param externalMetadataFile  External metadata file name
     */
    public JSONCTFWriter(File path, boolean externalMetadataFile) {
        this.path = path;
        this.externalMetadataFile = externalMetadataFile;
    }

    /**
     * Builds a JSON writer.
     *
     * @param path  JSON file path
     * @param externalMetadataFile  External metadata file name
     */
    public JSONCTFWriter(String path, boolean externalMetadataFile) {
        this(new File(path), externalMetadataFile);
    }

    /**
     * Builds a JSON writer using an output stream.
     * <p>
     * Using an output stream means the metadata text absolutely needs to be
     * embedded in the JSON stream since the data is not going to any
     * file system.
     *
     * @param stream    Output stream to use to print JSON data
     */
    public JSONCTFWriter(OutputStream stream) {
        this.stream = stream;
        this.externalMetadataFile = false;
    }

    /**
     * Sets how many spacers are printed per indent level.
     *
     * @param value Spacers per indent level
     * @see #setIndentWithSpaces()
     * @see #setIndentWithTabs()
     */
    public void setSpacersPerIndentLevel(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Spaces count per indent level must be positive");
        }
        this.spacersPerIndentLevel = value;
    }

    /**
     * Sets the external metadata file name.
     * <p>
     * The metadata file is in the same directory as the JSON file.
     *
     * @param filename  External metadata file name
     */
    public void setExternalMetadataName(String filename) {
        this.externalMetadataName = filename;
    }

    /**
     * Sets whether to add or not a header comment to the metadata text.
     * <p>
     * If true, the string <code>/&#42;&nbsp;CTF&nbsp;1.8&nbsp;&#42;/</code>
     * will be printed before the actual metadata text so that the metadata
     * plain text file can be recognized.
     *
     * @param write True to include the header comment
     */
    public void setWriteExternalMetadataHeaderComment(boolean write) {
        this.writeExternalMetadataHeaderComment = write;
    }

    /**
     * Indent with spaces.
     */
    public void setIndentWithSpaces() {
        this.indentChar = ' ';
    }

    /**
     * Indent with tabs.
     */
    public void setIndentWithTabs() {
        this.indentChar = '\t';
    }

    /**
     * Sets whether to pretty-print JSON or not.
     * <p>
     * Not pretty-printing means a small file size, but no human readability.
     *
     * @param value True to pretty-print
     */
    public void setPretty(boolean value) {
        this.pretty = value;
    }

    /**
     * Include event names into event nodes.
     *
     * @param value True to include event names into event nodes
     */
    public void setWriteEventName(boolean value) {
        this.writeEventName = value;
    }

    /**
     * Include event IDs into event nodes.
     *
     * @param value True to include event IDs into event nodes
     */
    public void setWriteEventID(boolean value) {
        this.writeEventID = value;
    }

    /**
     * Include enumeration labels into enumeration nodes.
     * <p>
     * Otherwise, an enumeration is simply an integer field.
     *
     * @param value True to include enumeration labels into enumeration nodes
     */
    public void setWriteEnumLabel(boolean value) {
        this.writeEnumLabel = value;
    }

    /**
     * Include floating point numbers double value into their nodes.
     * <p>
     * This is for convenience since a floating point number is written as
     * a mantissa and an exponent values.
     *
     * @param value True to include floating point numbers double value into their nodes
     */
    public void setWriteFloatValue(boolean value) {
        this.writeFloatValue = value;
    }

    @Override
    public void openTrace(TraceParameters params) throws WriterException {
        this.params = params;
        try {
            OutputStream stream = this.stream;
            if (this.path != null) {
                stream = new FileOutputStream(this.path);
            }
            this.output = new OutputStreamWriter(stream, Charset.forName("UTF-8"));
            this.stateStack = new Stack<NodeState>();

            // Write metadata
            this.writeMetadata();
        } catch (FileNotFoundException e) {
            throw new WriterException("Cannot open JSON file");
        }
    }

    @Override
    public void closeTrace() throws WriterException {
        try {
            // Close packets
            --this.currentIndentLevel;
            if (this.pretty) {
                this.output.write("\n");
            }
            this.output.write((this.pretty ? JSONCTFWriter.getIndentString(1, this.spacersPerIndentLevel, this.indentChar) : "") +
                    (this.pretty ? "]\n}" : "]}"));
            this.output.close();
        } catch (IOException e) {
            throw new WriterException("Cannot close JSON file");
        }
    }

    private void writeMetadata() throws WriterException {
        try {
            String p = this.pretty ? ": " : ":";
            if (this.externalMetadataFile) {
                File file = new File(this.path.getParent() + File.separator + this.externalMetadataName);
                OutputStreamWriter metadataWriter = new OutputStreamWriter(new FileOutputStream(file),
                        Charset.forName("UTF-8"));
                if (this.writeExternalMetadataHeaderComment) {
                    if (!this.params.metadataText.matches("^/\\* CTF 1\\.8.*")) {
                        metadataWriter.write("/* CTF 1.8 */\n\n");
                    }
                }
                metadataWriter.write(this.params.metadataText);
                metadataWriter.close();
                this.metadataJSON = JSONCTFWriter.jsonString("metadata") + p + JSONCTFWriter.jsonString("external:" + this.externalMetadataName);
            } else {
                this.metadataJSON = JSONCTFWriter.jsonString("metadata") + p + JSONCTFWriter.jsonString(this.params.metadataText);
            }
        } catch (IOException e) {
            throw new WriterException("Cannot write metadata");
        }
    }

    @Override
    public void openStream(int id) throws WriterException {
        // Nothing to do here...
    }

    @Override
    public void closeStream(int id) throws WriterException {
        // Nothing to do here...
    }

    @Override
    public void openPacket(PacketInfo packet) throws WriterException {
        try {
            String p;
            /*
             * We begin writing to the file right here since we should have already
             * received the metadata text at this step.
             */
            if (!this.firstPacketDone && this.metadataJSON != null) {
                // Metadata
                this.output.write("{");
                if (this.pretty) {
                    this.output.write("\n");
                }
                this.currentIndentLevel = 1;
                this.output.write(this.getCurrentIndentString() + this.metadataJSON + ",");
                if (this.pretty) {
                    this.output.write("\n");
                }

                // Packets
                p = this.pretty ? ": [\n" : ":[";
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString("packets") + p);
                this.currentIndentLevel = 2;
            }

            // If first packet, we don't write the preceding packet comma
            if (this.firstPacketDone) {
                this.output.write(",");
                if (this.pretty) {
                    this.output.write("\n");
                }
            }

            // First packet done?
            if (!this.firstPacketDone) {
                this.firstPacketDone = true;
            }

            // Normal packet opening routine
            this.output.write(this.getCurrentIndentString() + "{");
            if (this.pretty) {
                this.output.write("\n");
            }
            ++this.currentIndentLevel;

            // Open header (written by hand because of special fields)
            p = this.pretty ? ": {\n" : ":{";
            this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString("header") + p);
            ++this.currentIndentLevel;
            StructDefinition packetHeaderDef = packet.getHeader();
            StructDeclaration packetHeaderDecl = packetHeaderDef.getDeclaration();

            // Magic
            boolean firstDone = false;
            if (packetHeaderDecl.hasField("magic")) {
                p = this.pretty ? ": " : ":";
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString("magic") + p +
                        JSONCTFWriter.jsonDec(packetHeaderDef.lookupInteger("magic").getValue()));
                firstDone = true;
            }

            // UUID
            if (packetHeaderDecl.hasField("uuid")) {
                if (firstDone) {
                    this.output.write(",");
                    if (this.pretty) {
                        this.output.write("\n");
                    }
                }
                boolean uuidFirstDone = false;
                StringBuilder sb = new StringBuilder();
                p = this.pretty ? ", " : ",";
                for (Definition d : packetHeaderDef.lookupArray("uuid").getDefinitions()) {
                    IntegerDefinition intDef = (IntegerDefinition) d;
                    if (uuidFirstDone) {
                        sb.append(p);
                    }
                    sb.append(JSONCTFWriter.jsonDec(intDef.getValue()));
                    uuidFirstDone = true;
                }
                p = this.pretty ? ": [" : ":[";
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString("uuid") + p + sb.toString() + "]");
                firstDone = true;
            }

            // Stream ID
            if (packetHeaderDecl.hasField("stream_id")) {
                if (firstDone) {
                    this.output.write(",");
                    if (this.pretty) {
                        this.output.write("\n");
                    }
                }
                p = this.pretty ? ": " : ":";
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString("stream_id") + p + packetHeaderDef.lookupInteger("stream_id").getValue());
            }
            if (this.pretty) {
                this.output.write("\n");
            }

            // Close header
            --this.currentIndentLevel;
            this.output.write(this.getCurrentIndentString() + "},");
            if (this.pretty) {
                this.output.write("\n");
            }

            // Context
            if (packet.getContext() != null) {
                this.stateStack.push(new NodeState(NodeStateType.OBJECT, true));
                packet.getContext().write("context", this);
                this.output.write(",");
                if (this.pretty) {
                    this.output.write("\n");
                }
            }

            // Events
            p = this.pretty ? ": [\n" : ":[";
            this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString("events") + p);
            ++this.currentIndentLevel;
            this.firstEventDone = false;
        } catch (IOException e) {
            throw new WriterException("Cannot write to file");
        }
    }

    @Override
    public void closePacket(PacketInfo packet) throws WriterException {
        try {
            // Close events
            --this.currentIndentLevel;
            if (this.pretty) {
                this.output.write("\n" + this.getCurrentIndentString() + "]\n");
            } else {
                this.output.write("]");
            }

            // Close packet (no newline, next packet will take care of this)
            --this.currentIndentLevel;
            this.output.write(this.getCurrentIndentString() + "}");
        } catch (IOException e) {
            throw new WriterException("Cannot write to file");
        }
    }

    @Override
    public void writeEvent(Event ev) throws WriterException {
        String p;
        try {
            // Open event
            if (this.firstEventDone) {
                this.output.write(",");
                if (this.pretty) {
                    this.output.write("\n");
                }
            } else {
                this.firstEventDone = true;
            }
            this.output.write(this.getCurrentIndentString() + "{");
            if (this.pretty) {
                this.output.write("\n");
            }
            ++this.currentIndentLevel;

            // Informations which are also in the metadata (for convenience)
            p = this.pretty ? ": " : ":";
            if (this.writeEventName) {
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString("name") + p +
                        JSONCTFWriter.jsonString(ev.getName()) + ",");
                if (this.pretty) {
                    this.output.write("\n");
                }
            }
            if (this.writeEventID) {
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString("id") + p +
                        JSONCTFWriter.jsonDec(ev.getID()) + ",");
                if (this.pretty) {
                    this.output.write("\n");
                }
            }

            // Header
            this.stateStack.push(new NodeState(NodeStateType.OBJECT, true));
            ev.getHeader().write("header", this);

            // Per-stream context
            if (ev.getStreamContext() != null) {
                ev.getStreamContext().write("streamContext", this);
            }

            // Per-event context
            if (ev.getContext() != null) {
                ev.getContext().write("eventContext", this);
            }

            // Payload
            ev.getPayload().write("payload", this);
            this.stateStack.pop();
            if (this.pretty) {
                this.output.write("\n");
            }

            // Close event
            --this.currentIndentLevel;
            this.output.write(this.getCurrentIndentString() + "}");
        } catch (IOException e) {
            throw new WriterException("Cannot write event");
        }
    }

    private void beginElement() throws IOException {
        NodeState parentState = this.stateStack.peek();
        if (parentState.prependComma && parentState.writtenElements > 0) {
            this.output.write(",");
            if (this.pretty) {
                this.output.write("\n");
            }
        }
        ++parentState.writtenElements;
    }

    @Override
    public void openStruct(StructDefinition def, String name) throws WriterException {
        try {
            this.beginElement();
            NodeState parent = this.stateStack.peek();
            switch (parent.type) {
            case OBJECT:
                String p = this.pretty ? ": {\n" : ":{";
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString(name) + p);
                break;

            case ARRAY:
                this.output.write(this.getCurrentIndentString() + "{");
                if (this.pretty) {
                    this.output.write("\n");
                }
                break;
            }
            this.stateStack.push(new NodeState(NodeStateType.OBJECT, true, parent.doNotWrite));
            ++this.currentIndentLevel;
        } catch (IOException e) {
            throw new WriterException("Cannot write struct");
        }
    }

    @Override
    public void closeStruct(StructDefinition def, String name) throws WriterException {
        try {
            --this.currentIndentLevel;
            if (this.pretty) {
                this.output.write("\n");
            }
            this.output.write(this.getCurrentIndentString() + "}");
            this.stateStack.pop();
        } catch (IOException e) {
            throw new WriterException("Cannot write struct");
        }
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
        String p;
        try {
            this.beginElement();
            NodeState parent = this.stateStack.peek();
            this.output.write(this.getCurrentIndentString());
            if (parent.type == NodeStateType.OBJECT) {
                p = this.pretty ? ": " : ":";
                this.output.write(JSONCTFWriter.jsonString(name) + p);
            }
            if (def.isString()) {
                this.output.write(JSONCTFWriter.jsonString(def.getString()));
                this.stateStack.push(new NodeState(NodeStateType.ARRAY, true, true));
            } else {
                p = this.pretty ? " [\n" : "[";
                this.output.write(p);
                this.stateStack.push(new NodeState(NodeStateType.ARRAY, true, parent.doNotWrite));
            }
            ++this.currentIndentLevel;
        } catch (IOException e) {
            throw new WriterException("Cannot write array");
        }
    }

    @Override
    public void closeArray(ArrayDefinition def, String name) throws WriterException {
        try {
            --this.currentIndentLevel;
            NodeState state = this.stateStack.pop();
            if (!state.doNotWrite) {
                if (this.pretty) {
                    this.output.write("\n");
                }
                this.output.write(this.getCurrentIndentString() + "]");
            }
        } catch (IOException e) {
            throw new WriterException("Cannot write array");
        }
    }

    @Override
    public void openSequence(SequenceDefinition def, String name) throws WriterException {
        String p;
        try {
            this.beginElement();
            NodeState parent = this.stateStack.peek();
            this.output.write(this.getCurrentIndentString());
            if (parent.type == NodeStateType.OBJECT) {
                p = this.pretty ? ": " : ":";
                this.output.write(JSONCTFWriter.jsonString(name) + p);
            }
            if (def.isString()) {
                this.output.write(JSONCTFWriter.jsonString(def.getString()));
                this.stateStack.push(new NodeState(NodeStateType.ARRAY, true, true));
            } else {
                p = this.pretty? " [\n" : "[";
                this.output.write(p);
                this.stateStack.push(new NodeState(NodeStateType.ARRAY, true, parent.doNotWrite));
            }
            ++this.currentIndentLevel;
        } catch (IOException e) {
            throw new WriterException("Cannot write sequence");
        }
    }

    @Override
    public void closeSequence(SequenceDefinition def, String name) throws WriterException {
        try {
            --this.currentIndentLevel;
            NodeState state = this.stateStack.pop();
            if (!state.doNotWrite) {
                if (this.pretty) {
                    this.output.write("\n");
                }
                this.output.write(this.getCurrentIndentString() + "]");
            }
        } catch (IOException e) {
            throw new WriterException("Cannot write sequence");
        }
    }

    @Override
    public void writeInteger(IntegerDefinition def, String name) throws WriterException {
        NodeState parent = this.stateStack.peek();
        if (parent.doNotWrite) {
            return;
        }
        try {
            this.beginElement();
            switch (this.stateStack.peek().type) {
            case OBJECT:
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString(name) + (this.pretty ? ": " : ":") +
                        JSONCTFWriter.jsonDec(def.getValue()));
                break;

            case ARRAY:
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonDec(def.getValue()));
                break;
            }
        } catch (IOException e) {
            throw new WriterException("Cannot write integer");
        }
    }

    @Override
    public void writeFloat(FloatDefinition def, String name) throws WriterException {
        NodeState parent = this.stateStack.peek();
        if (parent.doNotWrite) {
            return;
        }
        try {
            this.beginElement();
            switch (this.stateStack.peek().type) {
            case OBJECT:
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString(name) + (this.pretty ? ": " : ":"));
                break;

            case ARRAY:
                this.output.write(this.getCurrentIndentString());
                break;
            }
            if (!this.writeFloatValue) {
                this.output.write(JSONCTFWriter.jsonDec(def.getRawValue()));
            } else {
                this.output.write("{");
                ++this.currentIndentLevel;
                if (this.pretty) {
                    this.output.write("\n");
                }
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString("raw") +
                        (this.pretty ? ": " : ":") + JSONCTFWriter.jsonDec(def.getRawValue()) + ",");
                if (this.pretty) {
                    this.output.write("\n");
                }
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString("value") +
                        (this.pretty ? ": " : ":") + JSONCTFWriter.jsonFloat(def.getDoubleValue()));
                --this.currentIndentLevel;
                if (this.pretty) {
                    this.output.write("\n");
                }
                this.output.write(this.getCurrentIndentString() + "}");
            }
        } catch (IOException e) {
            throw new WriterException("Cannot write float");
        }
    }

    @Override
    public void writeEnum(EnumDefinition def, String name) throws WriterException {
        NodeState parent = this.stateStack.peek();
        String p;
        if (parent.doNotWrite) {
            return;
        }
        if (this.writeEnumLabel) {
            try {
                this.output.write(this.getCurrentIndentString());
                switch (parent.type) {
                case OBJECT:
                    p = this.pretty ? ": {\n" : ":{";
                    this.output.write(JSONCTFWriter.jsonString(name) + p);
                    break;

                case ARRAY:
                    this.output.write("{");
                    if (this.pretty) {
                        this.output.write("\n");
                    }
                    break;
                }
                this.stateStack.push(new NodeState(NodeStateType.OBJECT, true));
                ++this.currentIndentLevel;
                p = this.pretty ? ": " : ":";
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString("label") +
                        p + JSONCTFWriter.jsonString(def.getValue()));
                ++this.stateStack.peek().writtenElements;
                this.writeInteger(def.getIntegerDefinition(), "value");
                --this.currentIndentLevel;
                this.stateStack.pop();
                if (this.pretty) {
                    this.output.write("\n");
                }
                this.output.write(this.getCurrentIndentString() + "}");
                ++parent.writtenElements;
            } catch (IOException e) {
                throw new WriterException("Cannot write enum");
            }
        } else {
            this.writeInteger(def.getIntegerDefinition(), name);
        }
    }

    @Override
    public void writeString(StringDefinition def, String name) throws WriterException {
        NodeState parent = this.stateStack.peek();
        if (parent.doNotWrite) {
            return;
        }
        try {
            this.beginElement();
            switch (this.stateStack.peek().type) {
            case OBJECT:
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString(name) + (this.pretty ? ": " : ":") +
                        JSONCTFWriter.jsonString(def.getValue()));
                break;

            case ARRAY:
                this.output.write(this.getCurrentIndentString() + JSONCTFWriter.jsonString(def.getValue()));
                break;
            }
        } catch (IOException e) {
            throw new WriterException("Cannot write string");
        }
    }

    private static String escapeString(String orig) {
        return orig.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\f", "\\f");
    }

    private static String jsonString(String str) {
        return "\"" + JSONCTFWriter.escapeString(str) + "\"";
    }

    private static String jsonDec(long val) {
        return String.valueOf(val);
    }

    private static String jsonFloat(double val) {
        return String.valueOf(val);
    }

    private static String getIndentString(int indentLevel, int spacesPerIndentLevel, char indentChar) {
        StringBuilder a = new StringBuilder();
        for (int i = 0; i < indentLevel * spacesPerIndentLevel; ++i) {
            a.append(indentChar);
        }

        return a.toString();
    }

    private String getCurrentIndentString() {
        if (!this.pretty) {
            return "";
        }
        return JSONCTFWriter.getIndentString(this.currentIndentLevel, this.spacersPerIndentLevel, this.indentChar);
    }

    private class NodeState {
        public NodeState(JSONCTFWriter.NodeStateType type, boolean prependComma) {
            this(type, prependComma, false);
        }

        public NodeState(JSONCTFWriter.NodeStateType type, boolean prependComma, boolean doNotWrite) {
            this.prependComma = prependComma;
            this.type = type;
            this.doNotWrite = doNotWrite;
        }

        public JSONCTFWriter.NodeStateType type;
        public int writtenElements = 0;
        public boolean prependComma = false;
        public boolean doNotWrite = false;
    }
}
