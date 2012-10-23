package ca.eepp.quatre.java.javeltrace.trace.output;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;

import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.metadata.strings.MetadataFieldNames;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.ex.WrongStateException;
import ca.eepp.quatre.java.javeltrace.trace.output.ex.TraceOutputException;
import ca.eepp.quatre.java.javeltrace.trace.writer.IWriter;

/**
 * A buffered trace output.
 * <p>
 * This trace output type buffers events (creates copies) as they arrive and
 * outputs them in one shot when a new packet is asked or when the output
 * is closed.
 * <p>
 * The main purpose of this is to automatically update the packet size and
 * content size fields of packets contexts so that they match the written
 * size even if it differs from the original one. For example, writing less
 * events than what the packet expects will result in a too big content size
 * value, and writing too much events will result in a too small packet size
 * value.
 *  
 * @author Philippe Proulx
 */
public class BufferedTraceOutput extends TraceOutput {
    private HashMap<Integer, StreamInfo> streamInfos = null;
    private StreamInfo currentStreamInfo = null;
    boolean shrinkPacketSize = false;
    boolean writeIfEmpty = true;
    
    /**
     * Builds a buffered trace output.
     * 
     * @param params    Filled trace parameters
     * @param writer    Writer to use
     */
    public BufferedTraceOutput(TraceParameters params, IWriter writer) {
        super(params, writer);
    }
    
    /**
     * Sets whether or not to enable packet size shrinking.
     * <p>
     * By enabling this, packets that are to be closed with a too big packet
     * size for the content they have will have their packet size field
     * updated accordingly to fit their content. 
     * 
     * @param val   True to shrink packet size
     */
    public void setShrinkPacketSize(boolean val) {
        this.shrinkPacketSize = val;
    }
    
    /**
     * Sets whether or not to write a packet even if it's empty.
     * <p>
     * "Empty" here means no events written.
     * 
     * @param val   True to write empty packets
     */
    public void setWriteIfEmpty(boolean val) {
        this.writeIfEmpty = val;
    }

    @Override
    public void openMe() throws TraceOutputException, WrongStateException {
        // Reset stream infos
        this.streamInfos = new HashMap<Integer, StreamInfo>();
    }

    @Override
    public void closeStreams() throws WrongStateException {
        // Close all streams
        for (Integer key : this.streamInfos.keySet()) {
            // Close and dump if anything left
            this.streamInfos.get(key).closeAndDumpCurrentPacket();
            
            // Close the stream
            this.getWriter().closeStream(key);
        }
    }

    @Override
    public void newPacketMe(PacketInfo packetInfo) throws WrongStateException, TraceOutputException {
        // Get stream info
        StreamInfo si = this.streamInfos.get(packetInfo.getStreamID());
        this.currentStreamInfo = si;
        
        // Close preceding packet if any
        si.closeAndDumpCurrentPacket();
        
        // Initialize the new packet for the stream
        si.initNewPacket(packetInfo);
    }

    @Override
    public void writeEventMe(Event ev) throws TraceOutputException, WrongStateException {
        // Copy event
        Event evCopy = new Event(ev);
        
        // Add it to our stream
        this.currentStreamInfo.addEvent(evCopy);
    }

    @Override
    public void createStreamMe(int id) throws WrongStateException {
        // Add to our collection and open
        this.streamInfos.put(id, new StreamInfo());
        this.getWriter().openStream(id);
    }

    @Override
    public long getStreamNbPacketsWrittenMe(int streamID) {
        return this.streamInfos.get(streamID).packetsWritten;
    }
    
    @Override
    public boolean streamExistsMe(int streamID) throws WrongStateException {
        
        return this.streamInfos.containsKey(streamID);
    }
    
    private class StreamInfo {
        public PacketInfo currentPacket = null;
        public ArrayList<Event> currentEvents = new ArrayList<Event>();
        public long currentContentSize = 0;
        public long packetsWritten = 0;
        
        public StreamInfo() {
        }
        
        private void updateCurrentPacketSizes() {
            StructDefinition packetContext = this.currentPacket.getContext();
            if (packetContext != null) {
                // Content size => header + context + events
                if (packetContext.hasField(MetadataFieldNames.PACKET_CONTEXT_CONTENT_SIZE)) {
                    packetContext.setIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_CONTENT_SIZE, this.currentContentSize);
                }
                
                // Packet size => content size + padding (up to a multiple of 32768 bits)
                long packetSize = (this.currentContentSize + 32768 - 1) & ~(32768 - 1);
                if (packetContext.hasField(MetadataFieldNames.PACKET_CONTEXT_PACKET_SIZE)) {
                    long oldPacketSize = packetContext.getIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_PACKET_SIZE);

                    /*
                     * We keep the old packet size if it's above the computed one. Like
                     * this, if someone wants to create a huge packets with few events,
                     * it's possible, but he can't create a packet with a size that's
                     * under the content size.
                     */
                    if ((oldPacketSize < packetSize) || BufferedTraceOutput.this.shrinkPacketSize) {
                        packetContext.setIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_PACKET_SIZE, packetSize);
                    }
                }
            }
        }
        
        public void initNewPacket(PacketInfo packet) {
            // Assign as current
            this.currentPacket = packet;
            
            // Initialize size
            StructDefinition packetHeader = this.currentPacket.getHeader();
            StructDefinition packetContext = this.currentPacket.getContext();
            this.currentContentSize = packetHeader.getSize(0);
            if (packetContext != null) {
                this.currentContentSize += packetContext.getSize(this.currentContentSize);
            }
        }
        
        public void addEvent(Event ev) {
            // Add event to the current events list
            this.currentEvents.add(ev);
            
            // Increase the current content size
            this.currentContentSize += ev.getSize(this.currentContentSize);
        }
        
        public void closeAndDumpCurrentPacket() {
            // Chances are there was no packet created so we check this
            if (this.currentPacket != null) {
                if (this.currentEvents.isEmpty() && !BufferedTraceOutput.this.writeIfEmpty) {
                    return;
                }
                
                // Update the packet sizes
                this.updateCurrentPacketSizes();
                
                // Open the packet
                getWriter().openPacket(this.currentPacket);
                
                // Write all events
                for (Event ev : this.currentEvents) {
                    getWriter().writeEvent(ev);
                }
                
                // Close the packet
                getWriter().closePacket(this.currentPacket);
                this.currentPacket = null;
                
                // Clear current events
                this.currentEvents.clear();
                
                // Update stats
                ++this.packetsWritten; 
            }
        }
    }
}
