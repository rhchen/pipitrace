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
 *******************************************************************************/

package net.sf.pipitrace.ui.views.histogram;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * A histogram widget that displays the event distribution of a whole trace.
 * <p>
 * It also features a selected range window that can be dragged and zoomed.
 * 
 * @version 1.0
 * @author Francois Chouinard
 */
public class LoadFullTraceHistogram extends LoadHistogram implements MouseMoveListener {

    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    // Histogram colors
    private final Color fTimeRangeColor = new Color(Display.getCurrent(), 255, 128, 0);

    // ------------------------------------------------------------------------
    // Attributes
    // ------------------------------------------------------------------------

    private final LoadHistogramZoom fZoom;

    private long fRangeStartTime;
    private long fRangeDuration;

    // ------------------------------------------------------------------------
    // Construction
    // ------------------------------------------------------------------------

    /**
     * Standard Constructor.
     * 
     * @param view A reference to the parent histogram view 
     * @param parent A reference to the parent composite
     */
    public LoadFullTraceHistogram(LoadHistogramView view, Composite parent) {
        super(view, parent);
        fZoom = new LoadHistogramZoom(this, fCanvas, getStartTime(), getTimeLimit());
        fCanvas.addMouseMoveListener(this);
    }

    @Override
    public void dispose() {
        fTimeRangeColor.dispose();
        super.dispose();
    }

    // ------------------------------------------------------------------------
    // Operations
    // ------------------------------------------------------------------------

    /**
     * Sets the time range of the full histogram.  
     * 
     * @param startTime A start time
     * @param endTime A end time
     */
    public void setFullRange(long startTime, long endTime) {
        fZoom.setFullRange(startTime, endTime);
    }

    /**
     * Sets the selected time range.
     * 
     * @param startTime A start time
     * @param duration A window duration
     */
    public void setTimeRange(long startTime, long duration) {
        fRangeStartTime = startTime;
        fRangeDuration = duration;
        fZoom.setNewRange(fRangeStartTime, fRangeDuration);
        getDataModel().complete();
    }

    @Override
    public void updateTimeRange(long startTime, long endTime) {
        ((LoadHistogramView) fParentView).updateTimeRange(startTime, endTime);
    }

    // ------------------------------------------------------------------------
    // MouseListener
    // ------------------------------------------------------------------------

    private boolean fMouseDown;
    private int fStartPosition;

    @Override
    public void mouseDown(MouseEvent event) {
        fMouseDown = true;
        fStartPosition = event.x;
    }
 
    @Override
    public void mouseUp(MouseEvent event) {
        if (fMouseDown) {
            fMouseDown = false;
            // Check if mouse click without move; if so, just set the current event time
            if (event.x == fStartPosition) {
                super.mouseDown(event);
                return;
            }
 
            ((LoadHistogramView) fParentView).updateTimeRange(fRangeStartTime, fRangeStartTime + fZoom.getDuration());

        }
    }
 
    
    // ------------------------------------------------------------------------
    // MouseMoveListener
    // ------------------------------------------------------------------------

    @Override
    public void mouseMove(MouseEvent event) {

        if (fMouseDown) {
            int nbBuckets = event.x - fStartPosition;
            long delta = nbBuckets * fScaledData.fBucketDuration;
            long newStart = fZoom.getStartTime() + delta;
            if (newStart < getStartTime()) {
                newStart = getStartTime();
            }
            long newEnd = newStart + fZoom.getDuration();
            if (newEnd > getEndTime()) {
                newEnd = getEndTime();
                newStart = newEnd - fZoom.getDuration();
            }
            fRangeStartTime = newStart;
            getDataModel().complete();
        }
    }

    // ------------------------------------------------------------------------
    // PaintListener
    // ------------------------------------------------------------------------

    @Override
    public void paintControl(PaintEvent event) {
        super.paintControl(event);

        Image image = (Image) fCanvas.getData(IMAGE_KEY);
        assert image != null;

        Image rangeRectangleImage = new Image(image.getDevice(), image, SWT.IMAGE_COPY);
        GC rangeWindowGC = new GC(rangeRectangleImage);

        if ((fScaledData != null) && (fRangeStartTime != 0)) {
            drawTimeRangeWindow(rangeWindowGC, rangeRectangleImage);
        }

        // Draws the buffer image onto the canvas.
        event.gc.drawImage(rangeRectangleImage, 0, 0);

        rangeWindowGC.dispose();
        rangeRectangleImage.dispose();
    }

    private void drawTimeRangeWindow(GC imageGC, Image image) {

        // Map times to histogram coordinates
        long bucketSpan = Math.max(fScaledData.fBucketDuration,1);
        int rangeWidth = (int) (fRangeDuration / bucketSpan);

        int left = (int) ((fRangeStartTime - getDataModel().getFirstBucketTime()) / bucketSpan);
        int right = left + rangeWidth;
        int center = (left + right) / 2;
        int height = fCanvas.getSize().y;

        // Draw the selection window
        imageGC.setForeground(fTimeRangeColor);
        imageGC.setLineWidth(1);
        imageGC.setLineStyle(SWT.LINE_SOLID);
        imageGC.drawRoundRectangle(left, 0, rangeWidth, height - 1, 15, 15);

        // Fill the selection window
        imageGC.setBackground(fTimeRangeColor);
        imageGC.setAlpha(35);
        imageGC.fillRoundRectangle(left + 1, 1, rangeWidth - 1, height - 2, 15, 15);
        imageGC.setAlpha(255);

        // Draw the cross hair
        imageGC.setForeground(fTimeRangeColor);
        imageGC.setLineWidth(1);
        imageGC.setLineStyle(SWT.LINE_SOLID);

        int chHalfWidth = ((rangeWidth < 60) ? (rangeWidth * 2) / 3 : 40) / 2;
        imageGC.drawLine(center - chHalfWidth, height / 2, center + chHalfWidth, height / 2);
        imageGC.drawLine(center, (height / 2) - chHalfWidth, center, (height / 2) + chHalfWidth);
    }

}
