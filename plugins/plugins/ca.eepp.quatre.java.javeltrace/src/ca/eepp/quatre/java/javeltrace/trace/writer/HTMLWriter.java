package ca.eepp.quatre.java.javeltrace.trace.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteOrder;
import java.util.ArrayList;

import org.eclipse.linuxtools.ctf.core.trace.data.types.ArrayDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.EnumDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.FloatDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.SequenceDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StringDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.VariantDefinition;

import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.writer.ex.WriterException;

/**
 * HTML writer.
 * <p>
 * This writer produces an HTML file per packet. All written events are
 * printed as nodes within the packet node. The writer doesn't include external
 * styling and scripting files; it only outputs the valid markup.
 * <p>
 * An optional index file can be produced, including links to all packets pages and showing
 * their begin/end time stamps.
 * <p>
 * The output is only intended for trace visualization purposes and cannot be
 * read back to a CTF model. It should be used on small traces since the plain
 * text markup tends to grow very rapidly as events are added.
 *
 * @author Philippe Proulx
 */
public class HTMLWriter implements IWriter {
    private OutputStreamWriter writer = null;
    private final String dir;
    private boolean withinTextArray = false;
    private boolean withinIntArray = false;
    private ArrayList<PacketFileInfo> packetFiles = new ArrayList<PacketFileInfo>();
    private TraceParameters params;
    private boolean outputMetadataText = false;
    private boolean outputIndex = true;
    private boolean includeJquery = true;
    private String styleFileName = "style.css"; //$NON-NLS-1$
    private String scriptFileName = "master.js"; //$NON-NLS-1$
    private String packetFileNamePrefix = "packet"; //$NON-NLS-1$
    private String indexFileName = "index.htm"; //$NON-NLS-1$

    /**
     * Builds an HTML writer.
     *
     * @param dir   Output directory path
     */
    public HTMLWriter(String dir) {
        this.dir = dir;
    }

    /**
     * Builds an HTML writer.
     *
     * @param path  Output directory path
     */
    public HTMLWriter(File path) {
        this.dir = path.getAbsolutePath();
    }

    /**
     * Sets whether or not to output the metadata text.
     *
     * @param val   True to output the metadata text
     */
    public void setOutputMetadataText(boolean val) {
        this.outputMetadataText = val;
    }

    /**
     * Sets whether or not to create an index page.
     *
     * @param val   True to create an index page
     */
    public void setOuputIndex(boolean val) {
        this.outputIndex = val;
    }

    /**
     * Sets whether or not to include jQuery.
     * <p>
     * jQuery is a free JavaScript framework and will be included from a public
     * Google URL if needed.
     *
     * @param val   True to include jQuery
     */
    public void setIncludeJquery(boolean val) {
        this.includeJquery = val;
    }

    /**
     * Sets the external CSS stylesheet file name.
     *
     * @param name  Stylesheet file name
     */
    public void setStyleFileName(String name) {
        this.styleFileName = name;
    }

    /**
     * Sets the external JavaScript script file name.
     *
     * @param name  Script file name
     */
    public void setScriptFileName(String name) {
        this.scriptFileName = name;
    }

    /**
     * Sets the packets pages file name prefix.
     *
     * @param prefix    File name prefix
     */
    public void setPacketFileNamePrefix(String prefix) {
        this.packetFileNamePrefix = prefix;
    }

    /**
     * Sets the index file name.
     *
     * @param name  Index file name (with extension)
     */
    public void setIndexFileName(String name) {
        this.indexFileName = name;
    }

    private void newPacketFile(PacketInfo packetInfo) {
        try {
            // Name: current packet files array size
            PacketFileInfo pfi = new PacketFileInfo();
            pfi.filename = this.packetFileNamePrefix + this.packetFiles.size() + ".htm"; //$NON-NLS-1$

            // Time stamps
            if (packetInfo.getContext() != null) {
                if (packetInfo.getContext().getDeclaration().hasField("timestamp_begin")) { //$NON-NLS-1$
                    pfi.timestampBegin = packetInfo.getContext().getIntFieldValue("timestamp_begin");  //$NON-NLS-1$
                }
                if (packetInfo.getContext().getDeclaration().hasField("timestamp_end")) { //$NON-NLS-1$
                    pfi.timestampEnd = packetInfo.getContext().getIntFieldValue("timestamp_end");  //$NON-NLS-1$
                }
            }

            // Add it
            this.packetFiles.add(pfi);

            // Open the file
            this.writer = new OutputStreamWriter(new FileOutputStream(new File(this.dir + File.separator + pfi.filename)), "UTF-8"); //$NON-NLS-1$

            // Write the header and all
            String title = "Packet #" + this.packetFiles.size() + " &mdash; CTF trace output"; //$NON-NLS-1$ //$NON-NLS-2$
            this.writer.write("<!doctype html>\n\t<head><title>" + title + "</title>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            this.writer.write("\t<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />\n"); //$NON-NLS-1$
            this.writer.write("\t<link rel=\"stylesheet\" type=\"text/css\" href=\"" + this.styleFileName + "\" />\n"); //$NON-NLS-1$ //$NON-NLS-2$
            if (this.includeJquery) {
                this.writer.write("\t<script type=\"text/javascript\" src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js\"></script>\n"); //$NON-NLS-1$
            }
            this.writer.write("\t<script type=\"text/javascript\" src=\"" + this.scriptFileName + "\"></script>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            this.writer.write("</head><body>\n"); //$NON-NLS-1$

            // Navigation
            if (this.packetFiles.size() > 1) {
                this.writer.write("<div class=\"nav\" id=\"nav-prev\">"); //$NON-NLS-1$
                this.writer.write("<a href=\"" + this.packetFileNamePrefix + //$NON-NLS-1$
                        (this.packetFiles.size() - 2) + ".htm\">&larr; previous packet</a>"); //$NON-NLS-1$
                this.writer.write("</div>"); //$NON-NLS-1$
            }
            this.writer.write("<div class=\"nav\" id=\"nav-next\">"); //$NON-NLS-1$
            this.writer.write("<a href=\"" + this.packetFileNamePrefix + //$NON-NLS-1$
                    this.packetFiles.size() + ".htm\">next packet &rarr;</a>"); //$NON-NLS-1$
            this.writer.write("</div>"); //$NON-NLS-1$
        } catch (IOException e) {
            throw new WriterException("File error"); //$NON-NLS-1$
        }
    }

    private void createIndex() {
        try {
            // Open the file
            OutputStreamWriter indexWr = new OutputStreamWriter(new FileOutputStream(
                    new File(this.dir + File.separator + this.indexFileName)), "UTF-8"); //$NON-NLS-1$

            // Head
            indexWr.write("<!doctype html>\n\t<head><title>CTF trace output</title>\n"); //$NON-NLS-1$
            indexWr.write("\t<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />\n"); //$NON-NLS-1$
            indexWr.write("\t<link rel=\"stylesheet\" type=\"text/css\" href=\"" + this.styleFileName + "\" />\n"); //$NON-NLS-1$ //$NON-NLS-2$
            if (this.includeJquery) {
                indexWr.write("\t<script type=\"text/javascript\" src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js\"></script>\n"); //$NON-NLS-1$
            }
            indexWr.write("\t<script type=\"text/javascript\" src=\"" + this.scriptFileName + "\"></script>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            indexWr.write("</head><body>\n"); //$NON-NLS-1$

            // Parameters
            String sfMetadataText = HtmlEntities
                    .encode(this.params.metadataText);
            String bo = this.params.byteOrder == ByteOrder.BIG_ENDIAN ? "big endian" //$NON-NLS-1$
                    : "little endian"; //$NON-NLS-1$
            indexWr.write("<div id=\"trace-parameters\">\n"); //$NON-NLS-1$
            indexWr.write("<ul id=\"trace-parameters-short\">\n"); //$NON-NLS-1$
            indexWr.write("\t<li><strong>Major</strong>: " + this.params.major + "</li>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            indexWr.write("\t<li><strong>Minor</strong>: " + this.params.minor + "</li>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            indexWr.write("\t<li><strong>UUID</strong>: <span class=\"uuid\">" + this.params.uuid + "</span></li>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            indexWr.write("\t<li><strong>Byte order</strong>: " + bo + "</li>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            indexWr.write("</ul>"); //$NON-NLS-1$
            if (this.outputMetadataText) {
                indexWr.write("<pre id=\"metadata-text\">\n" + sfMetadataText + "</pre>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            indexWr.write("</div>\n"); //$NON-NLS-1$

            // Packets
            indexWr.write("<div id=\"index\">"); //$NON-NLS-1$
            indexWr.write("<table>"); //$NON-NLS-1$
            indexWr.write("<thead><tr><th>Index</th><th>Cycle start</th><th>Cycle end</th></thead><tbody>\n"); //$NON-NLS-1$
            for (int i = 0; i < this.packetFiles.size(); ++i) {
                PacketFileInfo p = this.packetFiles.get(i);
                indexWr.write("\t<tr>"); //$NON-NLS-1$
                indexWr.write("<td><a href=\"" + p.filename + "\">#" + i + "</a></td>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                indexWr.write("<td>" + p.timestampBegin + "</td>"); //$NON-NLS-1$ //$NON-NLS-2$
                indexWr.write("<td>" + p.timestampEnd + "</td>"); //$NON-NLS-1$ //$NON-NLS-2$
                indexWr.write("</tr>\n"); //$NON-NLS-1$
            }
            indexWr.write("</tbody></table></div>\n"); //$NON-NLS-1$

            // Foot
            indexWr.write("</body>\n</html>"); //$NON-NLS-1$

            // Close
            indexWr.close();
        } catch (Exception e) {
            throw new WriterException("Unable to open HTML file"); //$NON-NLS-1$
        }
    }

    @Override
    public void openTrace(TraceParameters params) throws WriterException {
        this.params = params;
    }

    @Override
    public void closeTrace() throws WriterException {
        if (this.outputIndex) {
            this.createIndex();
        }
    }

    @Override
    public void openStream(int id) throws WriterException {
        // Does not apply
    }

    @Override
    public void closeStream(int id) throws WriterException {
        // Does not apply
    }

    @Override
    public void openPacket(PacketInfo packet) throws WriterException {
        try {
            // Open the file
            this.newPacketFile(packet);

            // Open it
            this.writer
                    .write("<div class=\"p\"><div class=\"spi\">Show/hide packet info</div>"); //$NON-NLS-1$
            this.writer.write("<div class=\"pi\">"); //$NON-NLS-1$

            // Header
            this.writer.write("<div class=\"ph\">"); //$NON-NLS-1$
            packet.getHeader().write("header", this); //$NON-NLS-1$
            this.writer.write("</div>"); //$NON-NLS-1$

            // Context
            if (packet.getContext() != null) {
                this.writer.write("<div class=\"pc\">"); //$NON-NLS-1$
                packet.getContext().write("context", this); //$NON-NLS-1$
                this.writer.write("</div>"); //$NON-NLS-1$
            }

            this.writer.write("</div>"); //$NON-NLS-1$
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    @Override
    public void closePacket(PacketInfo packet) throws WriterException {
        // Close it
        try {
            this.writer.write("</div></body></html>"); //$NON-NLS-1$
            this.writer.close();
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    @Override
    public void writeEvent(Event ev) throws WriterException {
        try {
            // Open
            this.writer.write("<div class=\"e\"><div class=\"en\">" //$NON-NLS-1$
                    + HtmlEntities.encode(ev.getName()) + "</div>"); //$NON-NLS-1$
            this.writer.write("<div class=\"ect\">"); //$NON-NLS-1$

            // Header
            this.writer.write("<div class=\"eh\">"); //$NON-NLS-1$
            ev.getHeader().write("header", this); //$NON-NLS-1$
            this.writer.write("</div>"); //$NON-NLS-1$

            // Per-stream context
            this.writer.write("<div class=\"esc\">"); //$NON-NLS-1$
            if (ev.getStreamContext() != null) {
                ev.getStreamContext().write("per-stream context", this); //$NON-NLS-1$
            }
            this.writer.write("</div>"); //$NON-NLS-1$

            // Per-event context
            this.writer.write("<div class=\"ec\">"); //$NON-NLS-1$
            if (ev.getContext() != null) {
                ev.getContext().write("per-event context", this); //$NON-NLS-1$
            }
            this.writer.write("</div>"); //$NON-NLS-1$

            // Payload
            this.writer.write("<div class=\"ep\">"); //$NON-NLS-1$
            ev.getPayload().write("payload", this); //$NON-NLS-1$
            this.writer.write("</div>"); //$NON-NLS-1$

            // Close
            this.writer.write("</div></div>"); //$NON-NLS-1$
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    private void writeNameSpan(String name) throws IOException {
        if (name != null) {
            this.writer.write("<span class=\"n\">" + HtmlEntities.encode(name) //$NON-NLS-1$
                    + "</span>"); //$NON-NLS-1$
        }
    }

    private void writeNameDiv(String name) throws IOException {
        if (name != null) {
            this.writer.write("<div class=\"n\">" + HtmlEntities.encode(name) //$NON-NLS-1$
                    + "</div>"); //$NON-NLS-1$
        }
    }

    @Override
    public void openStruct(StructDefinition def, String name)
            throws WriterException {
        try {
            // Open
            this.writer.write("<div class=\"s\">"); //$NON-NLS-1$
            this.writeNameDiv(name);
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    @Override
    public void closeStruct(StructDefinition def, String name)
            throws WriterException {
        try {
            // Close
            this.writer.write("</div>"); //$NON-NLS-1$
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    @Override
    public void openVariant(VariantDefinition def, String name)
            throws WriterException {
        try {
            // Open
            this.writer.write("<div class=\"v\">"); //$NON-NLS-1$
            this.writeNameDiv(name);
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    @Override
    public void closeVariant(VariantDefinition def, String name)
            throws WriterException {
        try {
            // Close
            this.writer.write("</div>"); //$NON-NLS-1$
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    @Override
    public void openArray(ArrayDefinition def, String name)
            throws WriterException {
        try {
            // Open
            this.writer.write("<div class=\"a\">"); //$NON-NLS-1$
            if (def.isString()) {
                this.writeNameSpan(name);
                this.withinTextArray = true;
                this.writer.write("<span class=\"q\">" //$NON-NLS-1$
                        + HtmlEntities.encode(def.getString()));
            } else if (def.getDeclaration().getElementType() instanceof IntegerDeclaration) {
                this.withinIntArray = true;
                this.writeNameDiv(name);
                this.writer.write("<div class=\"ai\">"); //$NON-NLS-1$
            }
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    @Override
    public void closeArray(ArrayDefinition def, String name)
            throws WriterException {
        this.withinTextArray = false;
        try {
            // Close
            if (this.withinIntArray) {
                this.withinIntArray = false;
                this.writer.write("</div>"); //$NON-NLS-1$
            }
            this.writer.write("</div>\n"); //$NON-NLS-1$
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }

    }

    @Override
    public void openSequence(SequenceDefinition def, String name)
            throws WriterException {
        try {
            // Open
            this.writer.write("<div class=\"sq\">"); //$NON-NLS-1$
            if (def.isString()) {
                this.writeNameSpan(name);
                this.withinTextArray = true;
                this.writer.write("<span class=\"q\">" //$NON-NLS-1$
                        + HtmlEntities.encode(def.getString()));
            } else {
                this.withinIntArray = true;
                this.writeNameDiv(name);
                this.writer.write("<div class=\"ai\">"); //$NON-NLS-1$
            }
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    @Override
    public void closeSequence(SequenceDefinition def, String name)
            throws WriterException {
        this.withinTextArray = false;
        try {
            // Close
            if (this.withinIntArray) {
                this.withinIntArray = false;
                this.writer.write("</div>"); //$NON-NLS-1$
            }
            this.writer.write("</div>"); //$NON-NLS-1$
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    @Override
    public void writeInteger(IntegerDefinition def, String name)
            throws WriterException {
        if (this.withinTextArray) {
            return;
        }
        try {
            // Simple
            if (this.withinIntArray) {
                this.writer.write("<span class=\"i\">" + def.getValue() //$NON-NLS-1$
                        + "</span> "); //$NON-NLS-1$
            } else {
                this.writer.write("<div class=\"i\">"); //$NON-NLS-1$
                this.writeNameSpan(name);
                this.writer.write(String.valueOf(def.getValue()));
                this.writer.write("</div>"); //$NON-NLS-1$
            }
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    @Override
    public void writeFloat(FloatDefinition def, String name)
            throws WriterException {
        try {
            // Simple
            this.writer.write("<div class=\"f\">"); //$NON-NLS-1$
            this.writeNameSpan(name);
            this.writer.write(String.valueOf(def.getDoubleValue()));
            this.writer.write("</div>"); //$NON-NLS-1$
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    @Override
    public void writeEnum(EnumDefinition def, String name)
            throws WriterException {
        try {
            // Simple
            this.writer.write("<div class=\"g\">"); //$NON-NLS-1$
            this.writeNameSpan(name);
            this.writer.write("<span class=\"q\">" //$NON-NLS-1$
                    + HtmlEntities.encode(def.getValue()) + "</span> (" //$NON-NLS-1$
                    + def.getIntegerValue() + ")"); //$NON-NLS-1$
            this.writer.write("</div>"); //$NON-NLS-1$
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    @Override
    public void writeString(StringDefinition def, String name)
            throws WriterException {
        try {
            // Simple
            this.writer.write("<div class=\"k\">"); //$NON-NLS-1$
            this.writeNameSpan(name);
            this.writer.write("<span class=\"q\">" //$NON-NLS-1$
                    + HtmlEntities.encode(def.getValue()) + "</span>"); //$NON-NLS-1$
            this.writer.write("</div>"); //$NON-NLS-1$
        } catch (IOException e) {
            throw new WriterException("Cannot write HTML"); //$NON-NLS-1$
        }
    }

    private class PacketFileInfo {
        public PacketFileInfo() {
        }

        public String filename;
        public long timestampBegin = -1;
        public long timestampEnd = -1;
    }
}
