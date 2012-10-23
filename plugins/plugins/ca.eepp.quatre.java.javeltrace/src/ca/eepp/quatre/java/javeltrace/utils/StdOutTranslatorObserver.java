package ca.eepp.quatre.java.javeltrace.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.translation.ITranslatorObserver;

/**
 * Standard output observer.
 * <p>
 * This is a simple translation observer that prints to standard output
 * information about processed packets and events. It is provided for
 * convenience and is not expected to be used in production.
 * 
 * @author Philippe Proulx
 */
public class StdOutTranslatorObserver implements ITranslatorObserver {
    private final DateFormat df = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$
    private final boolean disableEventOut;
    
    /**
     * Builds a standard output observer.
     * <p>
     * This constructor disables the events notifications.
     */
    public StdOutTranslatorObserver() {
        this(false);
    }
    
    /**
     * Builds a standard output observer.
     * 
     * @param disableEventOut   True to disable events notifications
     */
    public StdOutTranslatorObserver(boolean disableEventOut) {
        this.disableEventOut = disableEventOut;
    }
    
    private void out(String str) {
        Date d = new Date();
        System.out.println(this.df.format(d) + " " + str); //$NON-NLS-1$
    }
    
    @Override
    public void notifyStart() {
        this.out("start"); //$NON-NLS-1$
    }

    @Override
    public void notifyNewPacket(PacketInfo packetInfo) {
        this.out("new packet (stream ID = " + packetInfo.getStreamID() + ", CPU ID = " + packetInfo.getCPUID() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    @Override
    public void notifyNewEvent(Event ev) {
        if (!this.disableEventOut) {
            this.out("new event (ID = " + ev.getID() + ", name = " + ev.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
    }

    @Override
    public void notifyStop() {
        this.out("stop"); //$NON-NLS-1$
    }
}
