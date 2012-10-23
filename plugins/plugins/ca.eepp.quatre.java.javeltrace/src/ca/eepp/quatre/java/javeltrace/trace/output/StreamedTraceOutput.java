package ca.eepp.quatre.java.javeltrace.trace.output;

import java.util.HashMap;


import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.ex.WrongStateException;
import ca.eepp.quatre.java.javeltrace.trace.output.ex.TraceOutputException;
import ca.eepp.quatre.java.javeltrace.trace.writer.IWriter;

/**
 * A streamed trace output.
 * <p>
 * Using this trace output, you are guaranteed that events get written by the
 * writer as soon as you ask for it. Also, packets headers and contexts should
 * not be modified by this output type.
 * 
 * @author Philippe Proulx
 */
public class StreamedTraceOutput extends TraceOutput {
    protected State state = State.UNOPENED;
    protected HashMap<Integer, StreamInfo> streamInfos = null;
    
    /**
     * Builds a streamed trace output.
     * 
     * @param params    Filled trace parameters
     * @param writer    Writer to use to write the trace
     */
    public StreamedTraceOutput(TraceParameters params, IWriter writer) {
        super(params, writer);
    }
    
    @Override
    protected void openMe() throws TraceOutputException, WrongStateException {        
        // Reset stream infos
        this.streamInfos = new HashMap<Integer, StreamInfo>();
    }
    
    @Override
    protected void closeStreams() throws WrongStateException {
        // Close all streams
        for (Integer key : this.streamInfos.keySet()) {
            // Close and dump if anything left
            this.streamInfos.get(key).closeAndDumpCurrentPacket();
            
            // Close the stream
            this.getWriter().closeStream(key);
        }
    }

    @Override
    protected void newPacketMe(PacketInfo packetInfo) throws WrongStateException, TraceOutputException {
        // Get stream info
        StreamInfo si = this.streamInfos.get(packetInfo.getStreamID());
        
        // Close preceding packet if any
        si.closeAndDumpCurrentPacket();
        
        // Open the packet
        si.currentPacket = packetInfo;
        this.getWriter().openPacket(si.currentPacket);
    }
    
    @Override
    protected void writeEventMe(Event ev) throws TraceOutputException, WrongStateException {
        // Write event to stream using our writer
        this.getWriter().writeEvent(ev);
    }

    @Override
    protected void createStreamMe(int id) throws WrongStateException {
        // Add to our collection and open
        this.streamInfos.put(id, new StreamInfo());
        this.getWriter().openStream(id);
    }
    
    @Override
    protected long getStreamNbPacketsWrittenMe(int streamID) {
        return this.streamInfos.get(streamID).packetsWritten;
    }
    
    @Override
    protected boolean streamExistsMe(int streamID) throws WrongStateException {        
        return this.streamInfos.containsKey(streamID);
    }
    
    private class StreamInfo {
        public StreamInfo() {    
        }
        
        PacketInfo currentPacket = null;
        long packetsWritten = 0;
        
        public void closeAndDumpCurrentPacket() {
            // Chances are there was no packet created so we check this
            if (this.currentPacket != null) {
                getWriter().closePacket(this.currentPacket);
                ++this.packetsWritten;
            }
        }
    }
}
