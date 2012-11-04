package net.sf.pipitrace.ui.views.histogram;

import org.eclipse.linuxtools.tmf.ui.views.histogram.HistogramDataModel;
import org.eclipse.linuxtools.tmf.ui.views.histogram.HistogramScaledData;
import org.eclipse.linuxtools.tmf.ui.views.histogram.IHistogramDataModel;

public class LoadHistogramDataModel extends HistogramDataModel implements IHistogramDataModel{

	@Override
	public void complete() {
		// TODO Auto-generated method stub
		super.complete();
	}

	@Override
	public void countEvent(long eventCount, long timestamp) {
		// TODO Auto-generated method stub
		super.countEvent(eventCount, timestamp);
	}

	@Override
	public HistogramScaledData scaleTo(int width, int height, int barWidth) {
		// TODO Auto-generated method stub
		return super.scaleTo(width, height, barWidth);
	}

}
