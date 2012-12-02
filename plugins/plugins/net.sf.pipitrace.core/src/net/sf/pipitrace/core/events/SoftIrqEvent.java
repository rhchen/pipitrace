package net.sf.pipitrace.core.events;

import java.util.List;

import net.sf.commonstringutil.StringUtil;
import net.sf.pipitrace.core.model.TracePrefix;
import net.sf.pipitrace.core.model.TraceSuffix;

public class SoftIrqEvent {

	private TracePrefix prefObj;
	
	private TraceSuffix suffObj;
	
	@SuppressWarnings("unchecked")
	public SoftIrqEvent(String line) {
		
		super();
		
		List<String> list = null;
		
		if(StringUtil.countText(line, "softirq_raise: ") > 0){
			
			list = StringUtil.splitAsList(line, "softirq_raise: ");
			
		}
		
		if(StringUtil.countText(line, "softirq_entry: ") > 0){
			
			list = StringUtil.splitAsList(line, "softirq_entry: ");
			
		}

		if(StringUtil.countText(line, "softirq_exit: ") > 0){
			
			list = StringUtil.splitAsList(line, "softirq_exit: ");
			
		}
		
		String prefStr = list.get(0);
		String suffStr = list.get(1);
		
		prefObj = TracePrefix.create(prefStr);
		suffObj = TraceSuffix._create_soft_irq(suffStr);
		
	}

	public TracePrefix getPrefObj() {
		return prefObj;
	}

	public TraceSuffix getSuffObj() {
		return suffObj;
	}
	
	
}
