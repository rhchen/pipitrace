package ca.eepp.quatre.java.javeltrace.translation;

import java.util.ArrayList;


import ca.eepp.quatre.java.javeltrace.metadata.strings.MetadataFieldNames;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.ex.TraceException;
import ca.eepp.quatre.java.javeltrace.trace.ex.WrongStateException;
import ca.eepp.quatre.java.javeltrace.trace.input.StreamedTraceInput;
import ca.eepp.quatre.java.javeltrace.trace.output.BufferedTraceOutput;
import ca.eepp.quatre.java.javeltrace.translation.ex.TranslatorException;
import ca.eepp.quatre.java.javeltrace.utils.TimeStampAccumulator;

/**
 * Translator which can convert a trace format to another.
 * <p>
 * Using a {@link StreamedTraceInput} and a {@link BufferedTraceOutput}, this
 * object is able to translate a full trace from one format to another or to
 * translate only a specific range.
 * <p>
 * The translation process progress may be followed by providing the translator
 * an observer (see {@link ITranslatorObserver}).
 *
 * @author Philippe Proulx
 */
public class Translator {
    private final StreamedTraceInput input;
    private final BufferedTraceOutput output;
    private ITranslatorObserver observer = null;

    /**
     * Builds a translator object.
     * <p>
     * The provided input and output must be already opened.
     *
     * @param input     Trace input
     * @param output    Trace output
     */
    public Translator(StreamedTraceInput input, BufferedTraceOutput output) {
        this.input = input;
        this.output = output;
    }

    /**
     * Sets a translator observer.
     *
     * @param observer  Observer or null to disable notifications
     */
    public void setObserver(ITranslatorObserver observer) {
        this.observer = observer;
    }

    private void translate(final long tsBegin, final long tsEnd, final boolean rewritePacketBegin)
            throws WrongStateException, TraceException {
        // Verify states
        if (!this.input.isOpen()) {
            throw new TranslatorException("Input side is not be opened"); //$NON-NLS-1$
        } else if (!this.output.isOpen()) {
            throw new TranslatorException("Output side is not be opened"); //$NON-NLS-1$
        }

        // Transfer streams
        ArrayList<Integer> streamIDs = this.input.getStreamsIDs();
        for (Integer id : streamIDs) {
            this.output.createStream(id);
        }

        // Translate
        if (this.observer != null) {
            this.observer.notifyStart();
        }

        // Time stamp accumulator
        TimeStampAccumulator tsAcc = new TimeStampAccumulator();

        // Translate loop
        while (this.input.getCurrentPacketInfo() != null) {
            // Get packet info
            PacketInfo info = new PacketInfo(this.input.getCurrentPacketInfo());

            // Packet's time stamp begin/end
            long packetBegin = info.getContext().getIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_TS_BEGIN);
            long packetEnd = info.getContext().getIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_TS_END);

            /*
             * Packets are *supposed* to be received in time stamp begin order.
             * Knowing this, we can break this loop if the packet time stamp
             * begin is greater than the desired range end (IOW: all remaining
             * packets are _after_ the desired range).
             */
            if (packetBegin > tsEnd) {
                break;
            }

            /*
             * Jump over this packet if there's no intersection between the
             * desired range and the packet range.
             *
             *              ##### desired range ######
             *    //////    |                        |                  no
             *        //////+--                      |                  yes
             *              |          -----------   |                  yes
             *              |                   -----+/////             yes
             *              |                        |    ////////      no
             *          ////+------------------------+///////           yes
             *              |                        |
             *
             * More cases for intersections => check for no intersection.
             */
            boolean parsePacket = true;
            if ((packetBegin < tsBegin && packetEnd < tsBegin) || (packetBegin > tsEnd && packetEnd > tsEnd)) {
                // No intersection
                parsePacket = false;
            }

            // Parse packet?
            if (!parsePacket) {
                // No
                this.input.nextPacket();
                continue;
            }

            // Update the time stamp accumulator with this packet's time stamp begin
            tsAcc.setTS(packetBegin);

            // Create a new packet
            this.output.newPacket(info);

            // Notify
            if (this.observer != null) {
                this.observer.notifyNewPacket(info);
            }

            // Browse events
            boolean firstDone = false;
            while (this.input.getCurrentPacketEvent() != null) {
                // Get event
                Event ev = this.input.getCurrentPacketEvent();

                // Update the time stamp accumulator
                tsAcc.newEvent(ev);

                // Got it?
                if (tsAcc.getTS() >= tsBegin && tsAcc.getTS() <= tsEnd) {
                    // First event we got in this packet? Update the packet time stamp begin
                    if (!firstDone) {
                        firstDone = true;
                        if (rewritePacketBegin) {
                            info.getContext().setIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_TS_BEGIN, tsAcc.getTS());
                        }
                    }
                    this.output.writeEvent(ev);

                    // Notify
                    if (this.observer != null) {
                        this.observer.notifyNewEvent(ev);
                    }
                }
                this.input.nextPacketEvent();
            }
            this.input.nextPacket();
        }
        if (this.observer != null) {
            this.observer.notifyStop();
        }
    }

    /**
     * Translates a range between two time stamps.
     * <p>
     * All events found after or exactly at <code>tsBegin</code> and before
     * or exactly at <code>tsEnd</code> are kept for outputting. All outputted
     * events are kept within their original packets, although packets contexts
     * are modified in order to create a valid output trace.
     * <p>
     * To translate a full trace, use {@link #translate()}.
     *
     * @param tsBegin   Lower bound (inclusive)
     * @param tsEnd     Upper bound (inclusive)
     * @throws WrongStateException        If input or output cannot operate in their current state
     * @throws TraceException    If there's any reading/writing error
     */
    public void translate(final long tsBegin, final long tsEnd) throws WrongStateException, TraceException {
        this.translate(tsBegin, tsEnd, true);
    }

    /**
     * Translates a full trace.
     * <p>
     * Packets contexts are not modified here since everything is translated
     * as is.
     * <p>
     * To translate only a range, use {@link #translate(long, long)}.
     *
     * @throws WrongStateException        If input or output cannot operate in their current state
     * @throws TraceException    If there's any reading/writing error
     */
    public void translate() throws WrongStateException, TraceException {
        // Translate everything
        this.translate(Long.MIN_VALUE, Long.MAX_VALUE, false);
    }
}
