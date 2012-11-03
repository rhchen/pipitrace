/*******************************************************************************
 * Copyright (c) 2011, 2012 Ericsson
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Francois Chouinard - Initial API and implementation
 *   Bernd Hufmann - Changed to updated histogram data model   
 *   Francois Chouinard - Moved from LTTng to TMF
 *******************************************************************************/

package net.sf.pipitrace.ui.views.histogram;

import org.eclipse.swt.widgets.Composite;

/**
 * <p>
 * A basic histogram widget that displays the event distribution of a specific time range of a trace. 
 * It has the following additional features:
 * <ul>
 * <li>zoom in: mouse wheel up (or forward)
 * <li>zoom out: mouse wheel down (or backward)
 * </ul>
 * 
 * @version 1.0
 * @author Francois Chouinard
 */
public class LoadTimeRangeHistogram extends LoadHistogram {

    // ------------------------------------------------------------------------
    // Attributes
    // ------------------------------------------------------------------------

    private LoadHistogramZoom fZoom = null;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------
    /**
     * Constructor 
     * @param view The parent histogram view
     * @param parent The parent composite
     */
    public LoadTimeRangeHistogram(LoadHistogramView view, Composite parent) {
        super(view, parent);
        fZoom = new LoadHistogramZoom(this, fCanvas, getStartTime(), getTimeLimit());
    }

    // ------------------------------------------------------------------------
    // Operations
    // ------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * @see org.eclipse.linuxtools.tmf.ui.views.histogram.Histogram#updateTimeRange(long, long)
     */
    @Override
    public void updateTimeRange(long startTime, long endTime) {
        ((LoadHistogramView) fParentView).updateTimeRange(startTime, endTime);
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.linuxtools.tmf.ui.views.histogram.Histogram#clear()
     */
    @Override
    public synchronized void clear() {
        if (fZoom != null)
            fZoom.stop();
        super.clear();
    }

    /**
     * Sets the time range of the histogram
     * @param startTime The start time 
     * @param duration The duration of the time range
     */
    public synchronized void setTimeRange(long startTime, long duration) {
        fZoom.setNewRange(startTime, duration);
    }

    /**
     * Sets the full time range of the whole trace.
     * @param startTime The start time 
     * @param endTime The end time
     */
    public void setFullRange(long startTime, long endTime) {
        long currentFirstEvent = getStartTime();
        fZoom.setFullRange((currentFirstEvent == 0) ? startTime : currentFirstEvent, endTime);
    }

}
