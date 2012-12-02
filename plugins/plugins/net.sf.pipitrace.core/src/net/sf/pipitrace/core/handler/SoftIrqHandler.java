package net.sf.pipitrace.core.handler;

import net.sf.pipitrace.core.events.SchedEvent;
import net.sf.pipitrace.core.events.SoftIrqEvent;
import net.sf.pipitrace.core.ftrace.TracerType;
import net.sf.pipitrace.core.model.TracePrefix;
import net.sf.pipitrace.core.model.TraceSuffix;

import com.google.common.collect.ImmutableTable;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class SoftIrqHandler extends AbsHandler{

	public SoftIrqHandler(EventBus eventBus) {
		super(eventBus);
	}

	@Subscribe
    public void recieveEvent(SoftIrqEvent event) {
       
		TracePrefix prefObj = event.getPrefObj();
		TraceSuffix suffObj = event.getSuffObj();
		
		int cpuNum = prefObj.getCpuNum();
		
		if(cpuNum + 1 > builderList.size()){
			
			for(int i=builderList.size(); i<cpuNum + 1; i++){
				
				builderList.add(ImmutableTable.<TracePrefix, TracerType, TraceSuffix>builder());
				
			}
			
		}
		
		builderList.get(cpuNum).put(prefObj, TracerType.SOFT_IRQ, suffObj);
    }
}
