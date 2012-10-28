/**
 * 
 */
package net.sf.pipitrace.ui.views;

import org.eclipse.linuxtools.tmf.core.event.ITmfEvent;
import org.eclipse.linuxtools.tmf.core.event.ITmfTimestamp;
import org.eclipse.linuxtools.tmf.core.event.TmfTimeRange;
import org.eclipse.linuxtools.tmf.core.signal.TmfExperimentRangeUpdatedSignal;
import org.eclipse.linuxtools.tmf.core.signal.TmfExperimentSelectedSignal;
import org.eclipse.linuxtools.tmf.core.signal.TmfExperimentUpdatedSignal;
import org.eclipse.linuxtools.tmf.core.signal.TmfRangeSynchSignal;
import org.eclipse.linuxtools.tmf.core.signal.TmfSignalHandler;
import org.eclipse.linuxtools.tmf.core.signal.TmfTimeSynchSignal;
import org.eclipse.linuxtools.tmf.core.trace.TmfExperiment;
import org.eclipse.linuxtools.tmf.ui.views.TmfView;
import org.eclipse.linuxtools.tmf.ui.views.histogram.HistogramView;
import org.eclipse.swt.widgets.Composite;

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
    

}
