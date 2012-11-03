/**
 * 
 */
package net.sf.pipitrace.ui.views;

import org.eclipse.linuxtools.tmf.ui.views.histogram.HistogramView;

/**
 * @author Rhchen
 *
 */
public class LoadHistogramView extends HistogramView {

	// ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    /**
     *  The view ID as defined in plugin.xml
     */
    public static final String ID = "net.sf.pipitrace.ui.views.LoadHistogramView"; //$NON-NLS-1$

	public LoadHistogramView() {
		super(ID);
	}

	
}
