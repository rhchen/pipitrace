package ca.eepp.quatre.java.javeltrace.utils;

import java.util.Date;

import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.VariantDefinition;

import ca.eepp.quatre.java.javeltrace.metadata.descriptor.ClockDescriptor;
import ca.eepp.quatre.java.javeltrace.metadata.strings.MetadataFieldNames;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.utils.ex.TimeStampAccumulatorException;

/**
 * Time stamp accumulator/calculator.
 * <p>
 * This class accepts an initial time stamp and then finds the real current
 * time stamp as it is fed with events.
 * 
 * @author Philippe Proulx
 */
public class TimeStampAccumulator {
    private final ClockDescriptor clock;
    private long ts = 0;
    
    /**
     * Builds a time stamp accumulator.
     */
    public TimeStampAccumulator() {
        this.clock = null;
    }
    
    /**
     * Builds a time stamp accumulator using a clock.
     * <p>
     * The clock descriptor will be used to get the offset for methods
     * using it such as {@link #getOffsetTS()}.
     * 
     * @param clock Clock descriptor from which to find the offset
     */
    public TimeStampAccumulator(ClockDescriptor clock) {
        this.clock = clock; 
    }
    
    /**
     * Feeds the accumulator with an event.
     * 
     * @param ev    Event
     * @throws TimeStampAccumulatorException    If the time stamp cannot be found into the event header
     */
    public void newEvent(Event ev) throws TimeStampAccumulatorException {
        this.newEvent(ev.getHeader());        
    }
    
    /**
     * Feeds the accumulator with an event header.
     * 
     * @param evHeader  Event header
     * @throws TimeStampAccumulatorException    If the time stamp cannot be found into the event header
     */
    public void newEvent(StructDefinition evHeader) throws TimeStampAccumulatorException {
        // Search the time stamp definition
        IntegerDefinition tsDef = null;
        tsDef = evHeader.lookupInteger(MetadataFieldNames.EVENT_HEADER_TS);
        if (tsDef == null && evHeader.hasField(MetadataFieldNames.EVENT_HEADER_V)) {
            VariantDefinition vDef = evHeader.lookupVariant(MetadataFieldNames.EVENT_HEADER_V);
            if (vDef != null) {
                StructDefinition vContDef = (StructDefinition) vDef.getCurrentField();
                if (vContDef.hasField(MetadataFieldNames.EVENT_HEADER_TS)) {
                    tsDef = vContDef.lookupInteger(MetadataFieldNames.EVENT_HEADER_TS);
                }
            }
        }
        if (tsDef == null) {
            throw new TimeStampAccumulatorException("Cannot find time stamp field within event header"); //$NON-NLS-1$
        }
        
        /*
         * Hack: if time stamp is 64-bit, assume it's a full one; otherwise
         * it's a relative one. Here's a reminder of how it works in LTTng (our
         * only use case for the moment).
         * 
         * We keep record of the last full time stamp (64-bit). The last full
         * time stamp before reading any event in a packet is the packet
         * context's "timestamp_begin" field, which is indeed a 64-bit one.
         * The last full time stamp of an event with an extended header is its
         * 64-bit time stamp.
         * 
         * Now if we get a 32-bit value, mask out the 32 LSBs of the last full
         * time stamp and OR this value with the 32-bit time stamp. If this
         * new 64-bit value is smaller than the last full time stamp, assume
         * there was a wrap, so add 2^32. LTTng guarantees that a wrap only
         * occurs *once* between two events with relative time stamps.
         * 
         * FIXME: how do we know if it's absolute/relative for something else
         *        than LTTng?
         */
        if (tsDef.getDeclaration().getLength() == 64) {
            // Set last full time stamp
            this.ts = tsDef.getValue();
        } else {
            long ts = 0;
            if (tsDef.getDeclaration().getLength() == 27) {
                // Try ORing with the relative value
                ts = (this.ts & 0xfffffffff8000000L) | tsDef.getValue();
                
                // Wrap?
                if (ts < this.ts) {
                    // Add 2^27
                    ts += 0x8000000L;
                }
            } else if (tsDef.getDeclaration().getLength() == 32) {
                // Try ORing with the relative value
                ts = (this.ts & 0xffffffff00000000L) | tsDef.getValue();
                
                // Wrap?
                if (ts < this.ts) {
                    // Add 2^32
                    ts += 0x100000000L;
                }
            } else {
                throw new TimeStampAccumulatorException("Time stamp width not supported: " + tsDef.getDeclaration().getLength()); //$NON-NLS-1$
            }
            
            // Set last full time stamp
            this.ts = ts;
        }
    }
    
    /**
     * Gets the current computed full time stamp.
     * 
     * @return  Current full time stamp
     */
    public long getTS() {
        return this.ts;
    }
    
    /**
     * Sets the current full time stamp.
     * <p>
     * Further computation will be based on this one.
     * 
     * @param ts    New current full time stamp
     */
    public void setTS(long ts) {
        this.ts = ts;
    }
    
    /**
     * Gets the current computed full time stamp with offset.
     * <p>
     * Assumes a zero offset if a clock was not set.
     * 
     * @return  Current full time stamp with offset
     */
    public long getOffsetTS() {
        long off = 0;
        if (this.clock != null) {
            off = this.clock.getOffset();
        }
        
        return off + this.ts;
    }
    
    private Date dateFromTS(long ts) {
        // Seconds (double precision)
        double secs = this.secondsFromTS(ts);
        
        // Java Date's time is in ms
        double ms = secs * 1000.0;
        Date date = new Date();
        date.setTime(Math.round(ms));
        
        return date;
    }
    
    /**
     * Gets the current computed date.
     * 
     * @return  Current computed date
     */
    public Date getDate() {
        return this.dateFromTS(this.ts);
    }
    
    /**
     * Gets the current computed date with offset.
     * <p>
     * Assumes a zero offset if a clock was not set.
     * 
     * @return  Current computed date with offset
     */
    public Date getOffsetDate() {
        long off = 0;
        if (this.clock != null) {
            off = this.clock.getOffset();
        }
        
        return this.dateFromTS(this.ts + off);
    }
    
    private double secondsFromTS(long ts) {
        return (double) ts / (double) this.clock.getFreq();
    }
    
    /**
     * Gets the current computed seconds.
     *   
     * @return  Current computed seconds
     */
    public double getSeconds() {
        return this.secondsFromTS(this.ts);
    }
    
    /**
     * Gets the current computed seconds with offset.
     * <p>
     * Assumes a zero offset if a clock was not set.
     * 
     * @return  Current compute dseconds with offset
     */
    public double getOffsetSeconds() {
        long off = 0;
        if (this.clock != null) {
            off = this.clock.getOffset();
        }
        
        return this.secondsFromTS(this.ts + off);
    }
}
