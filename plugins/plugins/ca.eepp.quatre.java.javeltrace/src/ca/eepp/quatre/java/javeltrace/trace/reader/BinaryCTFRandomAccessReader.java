package ca.eepp.quatre.java.javeltrace.trace.reader;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Vector;

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
import ca.eepp.quatre.java.javeltrace.metadata.descriptor.StreamDescriptor;
import ca.eepp.quatre.java.javeltrace.metadata.strings.MetadataFieldNames;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.reader.ex.NoMoreEventsException;
import ca.eepp.quatre.java.javeltrace.trace.reader.ex.ReaderException;
import ca.eepp.quatre.java.javeltrace.utils.TimeStampAccumulator;

/**
 * Binary CTF streamed reader.
 * <p>
 * This reader essentially takes a directory containing the trace files as
 * an argument and does everything its owner needs to stream in the packets
 * and events.
 *
 * @author Philippe Proulx
 */
public class BinaryCTFRandomAccessReader implements IRandomAccessReader {
    private static final int METADATA_PACKET_HEADER_SIZE = 37; // Bytes
    private static final long METADATA_PACKET_MAGIC_NUMBER_LE = 0x571dd175L;
    private static final long METADATA_PACKET_MAGIC_NUMBER_BE = 0x75d11d57L;
    private static final long PACKET_HEADER_MAGIC_NUMBER = 0xc1fc1fc1L;
    private final File traceDir;
    final String metadataFilename;
    private final boolean checkMagicNumbers;
    private File[] files;
    private TraceParameters params = null;
    private BitBuffer currentBB = null;
    private final HashMap<Integer, ArrayList<StreamFileInfo>> streamsSFI = new HashMap<Integer, ArrayList<StreamFileInfo>>();
    private String metadataText = null;
    private PriorityQueue<StreamFileInfo> globalPrio = new PriorityQueue<StreamFileInfo>(10, new StreamFileInfoTimestampBeginComparator());

    /**
     * Builds a binary CTF streamed reader.
     *
     * @param traceDir  Trace directory path
     */
    public BinaryCTFRandomAccessReader(String traceDir) {
        this(new File(traceDir));
    }

    /**
     * Builds a binary CTF streamed reader.
     *
     * @param traceDir  Trace directory path
     */
    public BinaryCTFRandomAccessReader(File traceDir) {
        this(traceDir, "metadata", true); //$NON-NLS-1$
    }

    /**
     * Builds a binary CTF streamed reader with custom metadata file name.
     *
     * @param traceDir          Trace directory path
     * @param metadataFilename  Custom metadata file name
     * @param checkMagicNumbers True to verify magic numbers of files
     */
    public BinaryCTFRandomAccessReader(File traceDir, String metadataFilename,
            boolean checkMagicNumbers) {
        if (!traceDir.isDirectory()) {
            throw new ReaderException("Path must be a valid directory"); //$NON-NLS-1$
        }
        this.traceDir = traceDir;
        this.metadataFilename = metadataFilename;
        this.checkMagicNumbers = checkMagicNumbers;
    }

    @Override
    public void openTrace() throws ReaderException {
        // Get all the potential files
        this.files = this.traceDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return false;
                }
                if (pathname.isHidden()) {
                    return false;
                }
                if (pathname.getName().equals(BinaryCTFRandomAccessReader.this.metadataFilename)) {
                    return false;
                }

                return true;
            }
        });
    }

    @Override
    public void openStreams(TraceParameters params) {
        // Keep parameters
        this.params = params;

        // Open each one now
        for (StreamDescriptor descr : params.streams.values()) {
            this.openStream(descr);
        }
    }

    @Override
    public void closeTrace() throws ReaderException {
        // Nothing to do here... streams close their FC themselves
    }

    @Override
    public String getMetadataText() throws ReaderException {
        // Maybe we already have it
        if (this.metadataText != null) {
            return this.metadataText;
        }

        /*
         * This is a nice one. We know the metadata packets layout so we can
         * read them and get the text. We first create a file channel:
         */
        FileInputStream fis;
        try {
            fis = new FileInputStream(this.traceDir.getAbsolutePath() + File.separator + this.metadataFilename);
        } catch (FileNotFoundException e) {
            throw new ReaderException("Cannot open the metadata file"); //$NON-NLS-1$
        }
        FileChannel fc = fis.getChannel();

        String metadata = null;
        try {
            /*
             * By reading the very first 4 bytes, we check if the file is packet-based
             * and at the same time get its endianness.
             */
            ByteBuffer buf = ByteBuffer.allocate(4);
            fc.read(buf, 0);
            buf.order(ByteOrder.nativeOrder());
            int magic = buf.getInt(0);
            boolean isText = false;
            ByteOrder detectedBO = ByteOrder.nativeOrder();
            if (magic == BinaryCTFRandomAccessReader.METADATA_PACKET_MAGIC_NUMBER_BE) {
                // Native order since we read what we expect as big-endian
                detectedBO = ByteOrder.nativeOrder();
            } else if (magic == BinaryCTFRandomAccessReader.METADATA_PACKET_MAGIC_NUMBER_LE) {
                // Reversed order
                detectedBO = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
            } else {
                isText = true;
            }

            // Rewind the file
            fc.position(0);

            // Text/packet-based
            if (isText) {
                // Text-based
                try {
                    // Neat trick
                    metadata = new java.util.Scanner(fis).useDelimiter("\\A").next(); //$NON-NLS-1$
                } catch (java.util.NoSuchElementException e) {
                    throw new ReaderException("Empty metadata"); //$NON-NLS-1$
                }
            } else {
                // Packet-based
                StringBuilder sb = new StringBuilder();
                int fileSize = fis.available();
                int pos = 0;
                while (pos < fileSize - BinaryCTFRandomAccessReader.METADATA_PACKET_HEADER_SIZE) {
                    // Read the packet header (map it)
                    buf = ByteBuffer.allocate(BinaryCTFRandomAccessReader.METADATA_PACKET_HEADER_SIZE);
                    buf.order(detectedBO);
                    fc.read(buf, pos);

                    // Possibly verify the magic number
                    if (this.checkMagicNumbers) {
                        magic = buf.getInt(0);
                        if (magic != METADATA_PACKET_MAGIC_NUMBER_BE) {
                        	fis.close();
                            throw new ReaderException("Metadata magic number doesn't match!"); //$NON-NLS-1$
                        }
                    }

                    // Get content size (bits -> bytes)
                    int contentSize = buf.getInt(24) / 8;

                    // Get packet size (bits -> bytes)
                    int packetSize = buf.getInt(28) / 8;

                    // Payload size
                    int payloadSize = contentSize - BinaryCTFRandomAccessReader.METADATA_PACKET_HEADER_SIZE;

                    // Read the payload, convert it to a string and append it to what we have
                    buf = ByteBuffer.allocate(payloadSize);
                    fc.read(buf, pos + BinaryCTFRandomAccessReader.METADATA_PACKET_HEADER_SIZE);
                    sb.append(new String(buf.array(), "UTF-8")); //$NON-NLS-1$

                    // New position
                    pos += packetSize;
                }
                metadata = sb.toString();
            }

            // Close the file
            fis.close();
        } catch (IOException e) {
            throw new ReaderException("Cannot read the metadata file"); //$NON-NLS-1$
        }

        // Cache it
        this.metadataText = metadata;

        return metadata;
    }

    private void setCurrentBB(BitBuffer bb) {
        this.currentBB = bb;
    }

    private void setCurrentBB(FileChannel fc, long pos, long len) throws IOException {
        MappedByteBuffer byteBuf;
        byteBuf = fc.map(MapMode.READ_ONLY, pos, len);
        this.currentBB = new BitBuffer(byteBuf);
    }

    private int quickStreamIDCheck(File streamFile, StructDefinition packetHeaderDef) throws IOException {
        // Open the stream file
        FileInputStream fis;
        fis = new FileInputStream(streamFile);
        FileChannel fc = fis.getChannel();

        // Read the packet header
        this.setCurrentBB(fc, 0, packetHeaderDef.getSize(0) / 8);
        packetHeaderDef.read(null, this);

        // Verify its ID
        int id = (int) packetHeaderDef.lookupInteger(MetadataFieldNames.PACKET_HEADER_STREAM_ID).getValue();

        // If it has any magic number and we have to check that, check this here too
        if (packetHeaderDef.getDeclaration().hasField(MetadataFieldNames.PACKET_HEADER_MAGIC) && this.checkMagicNumbers) {
            if (packetHeaderDef.getIntFieldValue(MetadataFieldNames.PACKET_HEADER_MAGIC) != BinaryCTFRandomAccessReader.PACKET_HEADER_MAGIC_NUMBER) {
            	fis.close();
                throw new ReaderException("Magic number doesn't match for packet header"); //$NON-NLS-1$
            }
        }

        // Close the file
        fis.close();

        return id;
    }

	private void openStream(StreamDescriptor descr) throws ReaderException {
        // Create the packet header definition
        StructDefinition packetHeaderDef = descr.getPacketHeader().createDefinition();

        // ID
        int id = descr.getID();

        /*
         * We assume here that the stream files start with packets and that
         * the packet header contains the stream ID. We use this ID to know
         * which files map to what stream. Hence this todo:
         *
         * TODO: support streams with no ID (only one stream)
         */
        if (!packetHeaderDef.getDeclaration().hasField(MetadataFieldNames.PACKET_HEADER_STREAM_ID)) {
            throw new ReaderException("Streams without ID not supported"); //$NON-NLS-1$
        }

        // List of matching files
        ArrayList<StreamFileInfo> sfiArray = new ArrayList<StreamFileInfo>();

        // Browse all files
        for (File f : this.files) {
            // Check this file's stream ID
            try {
                // File channel
                int fileID = this.quickStreamIDCheck(f, packetHeaderDef);
                if (fileID == id) {
                    FileInputStream fis;
                    try {
                        fis = new FileInputStream(f);
                    } catch (FileNotFoundException e) {
                        throw new ReaderException("Cannot open file \"" + f.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    // Fresh stream file info
                    StreamFileInfo sfi = new StreamFileInfo(this.params, id);
                    sfi.fc = fis.getChannel();
                    sfi.fc.position(0);
                    sfi.fileSize = fis.available();
                    sfi.path = f;

                    // Do we have at least one packet in this file?
                    if (sfi.seekReadNextEvent()) {
                    	sfiArray.add(sfi);
                    }
                }
            } catch (IOException e) {
                // Ignore this file, it's probably not good at all
                continue;
            }
        }

        if (!sfiArray.isEmpty()) {
            // Add this stream to our global map (keeps all SFIs of a stream)
            this.streamsSFI.put(id, sfiArray);

            // Add them to the global priority queue
            for (StreamFileInfo s : sfiArray) {
                this.globalPrio.add(s);
            }

            // Restore current bit buffer
            this.currentBB = this.globalPrio.peek().bb;
        }
    }



    @Override
    public void closeStreams() throws ReaderException {
        // Close all streams
        for (StreamDescriptor descr : this.params.streams.values()) {
            this.closeStream(descr.getID());
        }
    }

    private void closeStream(int id) throws ReaderException {
        // Get associated SFIs
        if (!this.streamsSFI.containsKey(id)) {
            // Ignore
            return;
        }
        ArrayList<StreamFileInfo> sfi = this.streamsSFI.get(id);

        // Close all of them
        try {
            for (StreamFileInfo s : sfi) {
                s.fc.close();
            }
        } catch (IOException e) {
            throw new ReaderException("Cannot close stream file (for stream" + id + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

	@Override
	public Event getCurrentEvent() throws ReaderException {
		if (this.globalPrio.isEmpty()) {
			return null;
		}

		return this.globalPrio.peek().curEvent;
	}

	@Override
	public void advance() throws ReaderException {
		// Priority queue
        PriorityQueue<StreamFileInfo> prio = this.globalPrio;

        // Priority queue empty? No more packets
        if (prio.isEmpty()) {
            throw new NoMoreEventsException();
        }

        /*
         * Poll the current winner. Here we want to remove the item if it doesn't
         * have any more packets. If it has some, we'll add it back. This poll/add
         * strategy is important because a simple peek doesn't rebalance the heap.
         */
        StreamFileInfo winnerSFI = prio.poll();

        // Try to seek to next event
        boolean result = winnerSFI.seekReadNextEvent();

        // Did we find anything?
        if (result) {
            // And add it back to the priority queue
            prio.add(winnerSFI);
        }

        // Maybe the winner is not the same, so reset the current bit buffer
        if (!prio.isEmpty()) {
            this.setCurrentBB(prio.peek().bb);
        }
	}

	@Override
	public void seek(long ts) throws ReaderException {
	    this.globalPrio.clear();
	    for (ArrayList<StreamFileInfo> sfiArray : this.streamsSFI.values()) {
	        for (StreamFileInfo sfi : sfiArray) {
	            sfi.seek(ts);
	            if (sfi.curEvent != null) {
	                this.globalPrio.add(sfi);
	            }
	        }
	    }
	}

    private void alignBB(IDeclaration decl) {
        long at = Definition.align(this.currentBB.position(), decl.getAlignment());
        this.currentBB.position((int) at);
    }

    @Override
    public void openStruct(StructDefinition def, String name) throws ReaderException {
        // Declaration
        StructDeclaration decl = def.getDeclaration();

        // Align
        this.alignBB(decl);
    }

    @Override
    public void closeStruct(StructDefinition def, String name) throws ReaderException {
        // We don't care
    }

    @Override
    public void openVariant(VariantDefinition def, String name) throws ReaderException {
        // We don't care
    }

    @Override
    public void closeVariant(VariantDefinition def, String name) throws ReaderException {
        // We don't care
    }

    @Override
    public void openArray(ArrayDefinition def, String name) throws ReaderException {
        // Nothing to do here
    }

    @Override
    public void closeArray(ArrayDefinition def, String name) throws ReaderException {
        // Nothing to do here
    }

    @Override
    public void openSequence(SequenceDefinition def, String name) throws ReaderException {
        // Nothing to do here
    }

    @Override
    public void closeSequence(SequenceDefinition def, String name) throws ReaderException {
        // Nothing to do here
    }

    @Override
    public void readInteger(IntegerDefinition def, String name) throws ReaderException {
        // Declaration
        IntegerDeclaration decl = def.getDeclaration();

        // Align
        this.alignBB(decl);

        // Parameters
        boolean signed = decl.isSigned();
        int length = decl.getLength();
        long bits = 0;
        ByteOrder bo = decl.getByteOrder();

        // TODO: make this hack prettier
        if (bo == null) {
            bo = this.params.byteOrder;
        }

        // TODO: use the eventual getLong from BitBuffer
        final long longNegBit = 0x0000000080000000L;
        this.currentBB.order(bo);
        if (length == 64) {
            long first4 = this.currentBB.getInt(32, false);
            long last4 = this.currentBB.getInt(32, false);
            first4 = first4 & 0x00000000FFFFFFFFL;
            last4 = last4 & 0x00000000FFFFFFFFL;

            // According to endianness
            if (decl.getByteOrder() == ByteOrder.BIG_ENDIAN) {
                bits = (first4 << 32) | last4;
            } else {
                bits = (last4 << 32) | first4;
            }
        } else {
            bits = this.currentBB.getInt(length, signed);
            bits &= 0x00000000FFFFFFFFL;

            /*
             * The previous line loses sign information but is necessary, this fixes the sign
             * for 32 bit numbers. Sorry, in java all 64 bit ints are signed.
             */
            if ((longNegBit == (bits & longNegBit)) && signed) {
                bits |= 0xFFFFFFFF00000000L;
            }
        }
        def.setValue(bits);
    }

    @Override
    public void readFloat(FloatDefinition def, String name) throws ReaderException {
        int manLength = def.getDeclaration().getMantissa();
        int expLength = def.getDeclaration().getExponent();
        if ((expLength + manLength) == 32) {
            def.setValue(this.currentBB.getInt(32, false));
        } else if ((expLength + manLength) == 64) {
            long low = this.currentBB.getInt(32, false);
            low = low & 0x00000000FFFFFFFFL;
            long high = this.currentBB.getInt(32, false);
            high = high & 0x00000000FFFFFFFFL;
            long temp = (high << 32) | low;
            def.setValue(temp);
        } else {
            throw new ReaderException("Unsupported floating point number total length " + //$NON-NLS-1$
                    (manLength + expLength) + " bits"); //$NON-NLS-1$
        }
    }

    @Override
    public void readEnum(EnumDefinition def, String name) throws ReaderException {
        // In fact, an enum is just its underlying integer
        def.getIntegerDefinition().read(name, this);
    }

    @Override
    public void readString(StringDefinition def, String name) throws ReaderException {
        StringBuilder sb = new StringBuilder();

        // TODO: support encoding
        char c = (char) this.currentBB.getInt(8, false);
        while (c != 0) {
            sb.append(c);
            c = (char) this.currentBB.getInt(8, false);
        }
        def.setString(sb);
    }

    private class StreamFileInfo extends StreamInfo {
        public File path;
        public FileChannel fc;
        public BitBuffer bb;
        public long fileSize;
        public long startOffset = 0; // Bytes
        public PacketInfo curPacketInfo = null;
        public Event curEvent = null;
        private final TimeStampAccumulator acc = new TimeStampAccumulator();
        private final InMemoryStreamFileInfoPacketIndex index = new InMemoryStreamFileInfoPacketIndex();
        private int nextEntryIndex = 0;

        public StreamFileInfo(TraceParameters params, int streamID) {
            super(params, streamID);
        }

        private void setBB(FileChannel fc, long pos, long len) throws IOException {
            MappedByteBuffer byteBuf;
            byteBuf = fc.map(MapMode.READ_ONLY, pos, len);
            this.bb = new BitBuffer(byteBuf);
        }

        private boolean currentPacketHasMoreEvents() {
            /*
             * We simply verify if the current bit buffer's position exceeds the
             * current packet's content size (includes header and context).
             */
            return this.bb.position() < this.curPacketInfo.getContentSize();
        }

        private void readEvent() {
            if (!this.currentPacketHasMoreEvents()) {
                this.curEvent = null;

                return;
            }

            // Read the event header and context
            this.eventHeaderDef.read(null, BinaryCTFRandomAccessReader.this);
            if (this.eventPerStreamContextDef != null) {
                this.eventPerStreamContextDef.read(null, BinaryCTFRandomAccessReader.this);
            }

            // Get its ID
            int eventID = (int) this.eventHeaderDef.getIntFieldValue(MetadataFieldNames.EVENT_HEADER_ID);

            /*
             * TODO: is this LTTng specific? At least make it more generic, like
             * eventID == (2^eventIDLengthBits - 1).
             */
            int eventIDLengthBits = this.eventHeaderDef.lookupEnum(MetadataFieldNames.EVENT_HEADER_ID).getIntegerDefinition().getDeclaration().getLength();
            if ((eventID == 65535 && eventIDLengthBits == 16) || (eventID == 31 && eventIDLengthBits == 5)) {
                // Extended header: read ID from the variant
                // TODO: catch exceptions here if fields are not found
                StructDefinition vCurDef = (StructDefinition) this.eventHeaderDef.lookupVariant(MetadataFieldNames.EVENT_HEADER_V).getCurrentField();
                eventID = (int) vCurDef.getIntFieldValue(MetadataFieldNames.EVENT_HEADER_ID);
            }

            // Get the event definition and set its header/name/ID already
            Event ev = this.eventsDefsMap.get(eventID);

            // Per-event context if any
            if (ev.getContext() != null) {
                ev.getContext().read(null, BinaryCTFRandomAccessReader.this);
            }

            // Payload: always read
            ev.getPayload().read(null, BinaryCTFRandomAccessReader.this);

            // Current time stamp
            this.acc.newEvent(ev);
            ev.setTimeStamp(this.acc.getTS());

            // CPU ID
            ev.setCPUID(this.curPacketInfo.getCPUID());

            // Update current event
            this.curEvent = ev;
        }

        private void setCurrentDataFromIndexEntry() throws IOException {
            this.curPacketInfo = this.index.get(this.nextEntryIndex).packetInfo;
            this.startOffset = this.index.get(this.nextEntryIndex).streamFileOffsetBytes;
            this.setBB(this.fc, this.startOffset, this.curPacketInfo.getPacketSizeBytes());
            BinaryCTFRandomAccessReader.this.setCurrentBB(this.bb);

            // Bit buffer: first event position
            this.bb.position((int) this.curPacketInfo.getHeaderContextSize());
        }

        private void readPacketInfo() {
            try {
                this.curPacketInfo = null;
                this.curEvent = null;

                // Priority: read from index
                if (this.nextEntryIndex < this.index.size()) {
                    this.setCurrentDataFromIndexEntry();
                    this.seekNextPacket();

                    return;
                }

                // End of file
                if (!this.hasMorePackets()) {
                    return;
                }

                // Packet info size (safe to ask here)
                long headerContextSzBits = this.packetInfoDef.getHeaderContextSize();

                // Create/set the new bit buffer (mapped only to the header + context space)
                this.setBB(this.fc, this.startOffset, headerContextSzBits / 8);
                BinaryCTFRandomAccessReader.this.setCurrentBB(this.bb);

                // Read the header and the context if any
                this.packetInfoDef.getHeader().read(null, BinaryCTFRandomAccessReader.this);
                if (this.packetInfoDef.getContext() != null) {
                    this.packetInfoDef.getContext().read(null, BinaryCTFRandomAccessReader.this);
                }

                // Updated cached information
                this.packetInfoDef.updateCachedInfos();

                // Update the time stamp accumulator
                this.acc.setTS(this.packetInfoDef.getTimeStampBegin());

                // Create/set the new bit buffer (mapped to the whole packet to read events from it)
                this.setBB(this.fc, this.startOffset, this.packetInfoDef.getPacketSizeBytes());
                BinaryCTFRandomAccessReader.this.setCurrentBB(this.bb);

                // Bit buffer: seek to the first event position
                this.bb.position((int) this.packetInfoDef.getHeaderContextSize());

                // Add an entry to the index
                StreamFileInfoPacketIndexEntry entry = new StreamFileInfoPacketIndexEntry(this.startOffset,
                        new PacketInfo(this.packetInfoDef));
                this.index.addEntry(entry);

                // Update current packet info
                this.curPacketInfo = this.packetInfoDef;

                // Prepare for next packet
                this.seekNextPacket();
            } catch (IOException e) {
                throw new ReaderException("File error"); //$NON-NLS-1$
            }
        }

        private boolean hasMorePackets() {
            // Weird filesize (less than 4 kiB)? Probably no more packet...
            if (this.fileSize < 4096) {
                return false;
            }

            /*
             * If everything goes well, we increase the start offset when we seek
             * to the next packet (within an SFI, thus within a file). This new
             * offset will be the current start offset + the current packet size.
             * Since packets fit well into files, when we seek after the last packet
             * of the file, the start offset should be equal to the file size.
             *
             */
            return this.startOffset < this.fileSize;
        }

        private boolean seekNextPacket() {
            // If the packet size is 0, we cannot skip
            if (this.curPacketInfo.getPacketSize() == 0) {
                return false;
            }

            /*
             * Here's how we skip. We update the current packet start offset to its
             * current one + its size. We then position the file channel using this
             * new offset and set the current packet info to null because what the
             * SFI is pointing to is not read yet.
             */
            this.startOffset += (this.curPacketInfo.getPacketSizeBytes());
            ++this.nextEntryIndex;
            try {
                this.fc.position(this.startOffset);
            } catch (IOException ex) {
                throw new ReaderException("Cannot seek into file channel"); //$NON-NLS-1$
            }

            return true;
        }

        public boolean seekReadNextEvent() {
            // Detect start and end
        	if (this.curPacketInfo == null) {
        	    if (this.hasMorePackets()) {
        	        // Null and more packets => first packet to read
        	        this.readPacketInfo();
        	    } else {
        	        // Null and no more packets => end of trace reached
        	        return false;
        	    }
        	}

        	// Skip empty packets
        	while (!this.currentPacketHasMoreEvents()) {
        		if (this.hasMorePackets()) {
        		    // No more events but more packets => read next packet
        		    this.readPacketInfo();
        		} else {
        		    // No more events and no more packets => end of trace reached
        		    return false;
        		}
        	}

        	// Here we are sure that this call will read a new event and seek
        	this.readEvent();

        	return true;
        }

        public void seek(long ts) {
            // We should have at least one element in the index
            if (this.index.size() == 0) {
                return;
            }

            /*
             * If the requested time stamp is after the last indexed element, read packets
             * until we get in the range
             */
            if (ts > this.index.get(this.index.size() - 1).packetInfo.getTimeStampEnd()) {
                // First, seek at the last indexed packet
                this.nextEntryIndex = this.index.size() - 1;
                this.readPacketInfo();

                // Now reach the requested range or the end
                while (ts > this.index.get(this.index.size() - 1).packetInfo.getTimeStampEnd()) {
                    this.readPacketInfo();
                    if (this.curPacketInfo == null) {
                        return;
                    }
                }

                // Seek and read events until we find THE ONE (if any)
                if (!this.seekReadNextEvent()) {
                    return;
                }
                while (this.curEvent.getTimeStamp() < ts) {
                    this.seekReadNextEvent();
                    if (this.curEvent == null) {
                        return;
                    }
                }
            } else {
                StreamFileInfoPacketIndexEntry entry = this.index.search(ts);
                if (entry == null) {
                    return;
                }

            }
        }
    }

    private class StreamFileInfoTimestampBeginComparator implements Comparator<StreamFileInfo> {
        public StreamFileInfoTimestampBeginComparator() {
        }

        @Override
        public int compare(StreamFileInfo a, StreamFileInfo b) {
            /*
             * TODO: use unsigned comparison to avoid sign errors if needed... but an
             *       unsigned time stamp that would be different from its signed
             *       equivalent is very unlikely.
             */
            long ta = a.acc.getTS();
            long tb = b.acc.getTS();

            if (ta < tb) {
                return -1;
            } else if (ta > tb) {
                return 1;
            } else {
                /*
                 * Fall back to path names in order to get a pseudo deterministic behaviour
                 * for even time stamps.
                 */
                return a.path.getName().compareTo(b.path.getName());
            }
        }
    }

    static public class StreamFileInfoPacketIndexEntry {
        final public long streamFileOffsetBytes;
        final public PacketInfo packetInfo;
        final public HashMap<String, Object> attributes = new HashMap<String, Object>();

        public StreamFileInfoPacketIndexEntry(long streamFileOffsetBytes, PacketInfo pi) {
            this.streamFileOffsetBytes = streamFileOffsetBytes;
            this.packetInfo = pi;
        }

        public void addAttribute(String key, Object value) {
            this.attributes.put(key, value);
        }

        public Object getAttribute(String key) {
            return this.attributes.get(key);
        }
    }

    private abstract class StreamFileInfoPacketIndex {
        public abstract void addEntry(StreamFileInfoPacketIndexEntry entry) throws ReaderException;
        public StreamFileInfoPacketIndexEntry addEntry(long streamFileOffsetBytes, PacketInfo pi) {
            StreamFileInfoPacketIndexEntry entry = new StreamFileInfoPacketIndexEntry(streamFileOffsetBytes, pi);
            this.addEntry(entry);

            return entry;
        }
        public abstract StreamFileInfoPacketIndexEntry search(final long ts) throws IllegalArgumentException;
        public abstract StreamFileInfoPacketIndexEntry get(int index);
        public abstract int size();
    }

    private class InMemoryStreamFileInfoPacketIndex extends StreamFileInfoPacketIndex {
        private final Vector<StreamFileInfoPacketIndexEntry> entries = new Vector<StreamFileInfoPacketIndexEntry>();

        @Override
        public void addEntry(StreamFileInfoPacketIndexEntry entry) throws ReaderException {
            // Make sure the content size is set
            assert(entry.packetInfo.getContentSize() != PacketInfo.NO_SIZE);

            // Make sure the time stamps make sense
            if (entry.packetInfo.getTimeStampBegin() > entry.packetInfo.getTimeStampEnd()) {
                throw new ReaderException("Packet time stamp begin is after time stamp end"); //$NON-NLS-1$
            }

            // Make sure entries are added in correct order
            if (!this.entries.isEmpty()) {
                if (entry.packetInfo.getTimeStampBegin() < this.entries.lastElement().packetInfo.getTimeStampBegin()) {
                    throw new ReaderException("Packets must be added in order of time stamp begin"); //$NON-NLS-1$
                }
            }

            // Add to our entries
            this.entries.add(entry);
        }

        @Override
        public StreamFileInfoPacketIndexEntry search(long ts) throws IllegalArgumentException {
            if (this.entries.isEmpty()) {
                return null;
            } else if (ts < 0) {
                throw new IllegalArgumentException("Time stamp cannot be negative"); //$NON-NLS-1$
            } else if (ts > this.entries.lastElement().packetInfo.getTimeStampEnd()) {
                return null;
            }
            int max = this.entries.size() - 1;
            int min = 0;
            int guessI;
            StreamFileInfoPacketIndexEntry guessEntry = null;
            for (;;) {
                /*
                 * Guess in the middle of min and max. The +1 is so that in case
                 * (min + 1 == max), we choose the packet at the subscript max
                 * instead of the one at min. Otherwise, it would give an infinite
                 * loop.
                 */
                guessI = (max + min + 1) / 2;
                guessEntry = this.entries.get(guessI);

                /*
                 * If we reached the point where we focus on a single packet, our
                 * search is done.
                 */
                if (min == max) {
                    break;
                }

                if (ts < guessEntry.packetInfo.getTimeStampBegin()) {
                    /*
                     * If the time stamp is before the time stamp begin, we know that
                     * the packet to return is before the guess.
                     */
                    max = guessI - 1;
                } else if (ts >= guessEntry.packetInfo.getTimeStampBegin()) {
                    /*
                     * If the time stamp is after the timestamp begin, we know that
                     * the packet to return is after the guess or is the guess.
                     */
                    min = guessI;
                }
            }

            return this.entries.get(guessI);
        }

        @Override
        public StreamFileInfoPacketIndexEntry get(int index) {
            if (index < this.entries.size()) {
                return this.entries.get(index);
            }

            return null;
        }

        @Override
        public int size() {
            return this.entries.size();
        }
    }
}
