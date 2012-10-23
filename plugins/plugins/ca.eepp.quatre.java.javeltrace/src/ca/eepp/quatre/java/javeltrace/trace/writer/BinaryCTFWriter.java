package ca.eepp.quatre.java.javeltrace.trace.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.eclipse.linuxtools.ctf.core.trace.data.types.ArrayDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.Definition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.EnumDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.FloatDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.SequenceDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StringDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.VariantDefinition;
import org.eclipse.linuxtools.internal.ctf.core.io.BitBuffer;

import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.writer.ex.WriterException;
import ca.eepp.quatre.java.javeltrace.utils.Utils;

/**
 * Binary CTF writer.
 * <p>
 * This writer outputs binary CTF data to the file system just as
 * <a href="http://lttng.org/">LTTng</a> does. One "channel" file per CPU
 * is created, named <code>channel<em>s</em>_<em>d</em></code>, where
 * <code><em>s</em></code> is the stream ID and <code><em>d</em></code> is the
 * CPU ID. The metadata file is packetized and created with name
 * <code>metadata</code>.
 *
 * @author Philippe Proulx
 */
public class BinaryCTFWriter implements IWriter {
    private static final int METADATA_PACKET_HEADER_SIZE = 37;
    private static final int METADATA_PACKET_MAGIC_NUMBER_BE = 0x75d11d57;

    private final File traceDir;
    private final String metadataFilename;
    private final int metadataPacketSize;
    private final HashMap<Integer, LocalStreamInfo> streams = new HashMap<Integer, LocalStreamInfo>();
    private TraceParameters params;
    private BitBuffer currentBB;

    /**
     * Creates a binary CTF writer.
     *
     * @param traceDir  Trace output directory
     */
    public BinaryCTFWriter(String traceDir) {
        this(new File(traceDir));
    }

    /**
     * Creates a binary CTF writer.
     *
     * @param traceDir  Trace output directory
     */
    public BinaryCTFWriter(File traceDir) {
        this(traceDir, "metadata", 4096); //$NON-NLS-1$
    }

    /**
     * Creates a binary CTF writer.
     *
     * @param traceDir              Trace output directory
     * @param metadataFilename      Custom metadata file name
     * @param metadataPacketSize    Custom metadata packet size (bytes)
     */
    public BinaryCTFWriter(File traceDir, String metadataFilename, int metadataPacketSize) {
        this.traceDir = traceDir;
        this.metadataFilename = metadataFilename;
        this.metadataPacketSize = metadataPacketSize;
    }

    @Override
    public void openTrace(TraceParameters params) throws WriterException {
        this.params = params;

        // If the directory doesn't exist yet, create it
        if (!this.traceDir.exists()) {
            if (!this.traceDir.mkdir()) {
                throw new WriterException("Cannot create directory \"" + //$NON-NLS-1$
                        this.traceDir.getAbsolutePath() + "\""); //$NON-NLS-1$
            }
        }

        // Write metadata
        this.writeMetadata();
    }

    @Override
    public void closeTrace() throws WriterException {
        // Nothing to do here! Streams are already closed
    }

    /**
     * Packetizes a metadata text string and writes it.
     *
     * @param metadataText      Metadata text string
     * @param path              Output file path
     * @param uuid              Trace UUID
     * @param major             Trace major number
     * @param minor             Trace minor number
     * @param packetSize        Packet size
     * @throws WriterException  If there's any writing error
     */
    public static final void metadataTextToFile(String metadataText, File path,
            UUID uuid, int major, int minor, Integer packetSize) throws WriterException {
        try {
            // Create the file channel
            FileOutputStream fos = new FileOutputStream(path);
            FileChannel fc = fos.getChannel();

            // Convert metadata text to UTF-8 bytes
            byte[] metadataBytes = Utils.UTF8bytesFromString(metadataText);

            // Split UTF-8 bytes into packets
            int nb = metadataBytes.length;
            int metadataPacketHeaderSizeBytes = BinaryCTFWriter.METADATA_PACKET_HEADER_SIZE;
            int metadataPacketSize = 4096;
            if (packetSize != null) {
                if (packetSize % 4096 != 0) {
                    throw new WriterException("Metadata packet size must be a multiple of 4096"); //$NON-NLS-1$
                }
                metadataPacketSize = packetSize;
            }
            int payloadSizeBytes = metadataPacketSize - metadataPacketHeaderSizeBytes;
            assert(payloadSizeBytes > 0);
            int k = 0;
            while (k < nb) {
                // Get the following chunk
                int m = Math.min(nb - k, payloadSizeBytes);
                byte[] curPayload = Arrays.copyOfRange(metadataBytes, k, k + m);
                k += m;

                // Write a packet header
                ByteBuffer byteBuf = ByteBuffer.allocate(BinaryCTFWriter.METADATA_PACKET_HEADER_SIZE);
                byteBuf.order(ByteOrder.nativeOrder());
                byteBuf.putInt(METADATA_PACKET_MAGIC_NUMBER_BE);            // Magic
                if (uuid != null) {                                         // UUID
                    byteBuf.put(Utils.bytesFromUUID(uuid));
                } else {
                    byteBuf.putLong(0).putLong(0).putLong(0).putLong(0);
                }
                byteBuf.putInt(0);                                          // Checksum
                byteBuf.putInt((m + BinaryCTFWriter.METADATA_PACKET_HEADER_SIZE) * 8);  // Content size
                byteBuf.putInt(metadataPacketSize * 8);                     // Packet size
                byteBuf.put((byte) 0);                  // Compression scheme
                byteBuf.put((byte) 0);                  // Encryption scheme
                byteBuf.put((byte) 0);                  // Checksum scheme
                byteBuf.put((byte) major);              // Major
                byteBuf.put((byte) minor);              // Minor
                byteBuf.rewind();
                fc.write(byteBuf);

                // Now write the text bytes
                fc.write(ByteBuffer.wrap(curPayload));
            }

            // Close the stream
            fc.close();
        } catch (IOException e) {
            throw new WriterException("Cannot write metadata file"); //$NON-NLS-1$
        }
    }

    private void writeMetadata() {
        BinaryCTFWriter.metadataTextToFile(this.params.metadataText,
                new File(this.traceDir.getAbsolutePath() + File.separator + this.metadataFilename),
                this.params.uuid, this.params.major, this.params.minor, this.metadataPacketSize);
    }

    @Override
    public void openStream(int id) throws WriterException {
        /*
         * We will open the actual files as we receive packets because some
         * tracers open one file per (stream, CPU) couple and we want to mimic
         * that behaviour. So what we do here is initialize a new stream info.
         */
        this.streams.put(id, new LocalStreamInfo());
    }

    @Override
    public void closeStream(int id) throws WriterException {
        // We close all the inner SFI of the given stream
        LocalStreamInfo si = this.streams.get(id);
        for (StreamFileInfo sfi : si.perCPUSFI.values()) {
            try {
                sfi.fc.close();
            } catch (IOException e) {
                throw new WriterException("Cannot close file \"" + sfi.path.getAbsolutePath() + "\"");  //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    @Override
    public void openPacket(PacketInfo packet) throws WriterException {
        // Get the right SFI
        StreamFileInfo sfi = this.getSFIFromPacketInfo(packet);

        // Create a new bit buffer
        sfi.newBB(packet);
        this.currentBB = sfi.bb;

        /*
         * Don't write anything yet... we'll write the header + context when
         * we close the packet since we might update some fields before
         * writing it. However: position the bit buffer at the correct place
         * to begin writing events.
         */
        this.currentBB.position((int) packet.getHeaderContextSize());
    }

    @Override
    public void closePacket(PacketInfo packet) throws WriterException {
        // Get the right SFI
        StreamFileInfo sfi = this.getSFIFromPacketInfo(packet);

        // Bit buffer: back to position 0
        this.currentBB.position(0);

        // Write the header
        packet.getHeader().write(null, this);

        // Write context if any
        if (packet.getContext() != null) {
            packet.getContext().write(null, this);
        }

        // Now dump it
        sfi.dumpBB();
    }

    @Override
    public void writeEvent(Event ev) throws WriterException {
        // Header
        ev.getHeader().write(null, this);

        // Per-stream context
        if (ev.getStreamContext() != null) {
            ev.getStreamContext().write(null, this);
        }

        // Per-event context
        if (ev.getContext() != null) {
            ev.getContext().write(null, this);
        }

        // Payload
        ev.getPayload().write(null, this);
    }

    private void alignBB(IDeclaration decl) {
        long at = Definition.align(this.currentBB.position(), decl.getAlignment());
        this.currentBB.position((int) at);
    }

    @Override
    public void openStruct(StructDefinition def, String name) throws WriterException {
        // Declaration
        StructDeclaration decl = def.getDeclaration();

        // Align
        this.alignBB(decl);
    }

    @Override
    public void closeStruct(StructDefinition def, String name) throws WriterException {
        // We don't care
    }

    @Override
    public void openVariant(VariantDefinition def, String name) throws WriterException {
        // We don't care
    }

    @Override
    public void closeVariant(VariantDefinition def, String name) throws WriterException {
        // We don't care
    }

    @Override
    public void openArray(ArrayDefinition def, String name) throws WriterException {
        // We don't care
    }

    @Override
    public void closeArray(ArrayDefinition def, String name) throws WriterException {
        // We don't care
    }

    @Override
    public void openSequence(SequenceDefinition def, String name) throws WriterException {
        // We don't care
    }

    @Override
    public void closeSequence(SequenceDefinition def, String name) throws WriterException {
        // We don't care
    }

    @Override
    public void writeInteger(IntegerDefinition def, String name) throws WriterException {
        IntegerDeclaration decl = def.getDeclaration();

        // Align
        this.alignBB(def.getDeclaration());

        // Byte order
        ByteOrder bo = decl.getByteOrder();
        if (bo == null) {
            bo = this.params.byteOrder;
        }
        this.currentBB.order(bo);

        // 64-bit? We assume it's signed
        int len = decl.getLength();
        if (len == 64) {
            long low = def.getValue() & 0x00000000ffffffffL;
            long high = (def.getValue() >> 32) & 0x00000000ffffffffL;

            // According to endianness
            if (decl.getByteOrder() == ByteOrder.BIG_ENDIAN) {
                this.currentBB.putInt(32, (int) high);
                this.currentBB.putInt(32, (int) low);
            } else {
                this.currentBB.putInt(32, (int) low);
                this.currentBB.putInt(32, (int) high);
            }
        } else {
            assert(len <= 32);
            this.currentBB.putInt(len, (int) def.getValue());
        }
    }

    @Override
    public void writeFloat(FloatDefinition def, String name) throws WriterException {
        throw new WriterException("Unsupported float writing"); //$NON-NLS-1$
    }

    @Override
    public void writeEnum(EnumDefinition def, String name) throws WriterException {
        // In fact, an enum is just its underlying integer
        def.getIntegerDefinition().write(null, this);
    }

    @Override
    public void writeString(StringDefinition def, String name) throws WriterException {
        int len = def.getValue().length();
        for (int i = 0; i < len; ++i) {
            this.currentBB.putInt(8, def.getValue().charAt(i));
        }
        this.currentBB.putInt(8, 0); // NUL character
    }

    private StreamFileInfo getSFIFromPacketInfo(PacketInfo packetInfo) {
        // Do we have a stream info for that stream (let's hope so)
        if (this.streams.containsKey(packetInfo.getStreamID())) {
            LocalStreamInfo si = this.streams.get(packetInfo.getStreamID());

            // Do we have an SFI for this CPU?
            int cpuID = packetInfo.getCPUID();
            if (si.perCPUSFI.containsKey(cpuID)) {
                // Found it!
                return si.perCPUSFI.get(cpuID);
            }

            // Not yet: create it
            StreamFileInfo sfi = new StreamFileInfo();
            sfi.streamID = packetInfo.getStreamID();
            sfi.cpuID = cpuID;
            sfi.initFC();
            si.perCPUSFI.put(cpuID, sfi);

            return sfi;
        }
        throw new WriterException("Cannot find stream " + packetInfo.getStreamID()); //$NON-NLS-1$
    }

    private class LocalStreamInfo {
        public LocalStreamInfo() {
        }

        public HashMap<Integer, StreamFileInfo> perCPUSFI = new HashMap<Integer, StreamFileInfo>();
    }

    private class StreamFileInfo {
        public StreamFileInfo() {
        }

        public int streamID;
        public int cpuID;
        public FileChannel fc = null;
        public File path;
        public ByteBuffer byteBuf = null;
        public BitBuffer bb;

        public void initFC() {
            // File name
            this.path = new File(BinaryCTFWriter.this.traceDir + File.separator + "channel" + this.streamID + "_" + this.cpuID); //$NON-NLS-1$ //$NON-NLS-2$

            // Open stream
            try {
                FileOutputStream fos = new FileOutputStream(this.path);
                this.fc = fos.getChannel();
            } catch (IOException e) {
                throw new WriterException("Cannot open file \"" + this.path + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        public void newBB(PacketInfo packetInfo) {
            // Allocate a new byte buffer
            // TODO: support huge packets (no "packet_size")
            int size = (int) packetInfo.getContext().getIntFieldValue("packet_size") / 8; //$NON-NLS-1$
            this.byteBuf = ByteBuffer.allocate(size);
            this.bb = new BitBuffer(this.byteBuf);
        }

        public void dumpBB() {
            // Dump current byte buffer
            if (this.byteBuf != null) {
                try {
                    this.byteBuf.rewind();
                    this.fc.write(this.byteBuf);
                } catch (IOException e) {
                    throw new WriterException("Cannot write to file \"" + this.path + "\""); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }
    }
}
