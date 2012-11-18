package net.sf.pipitrace.core.events;

import java.util.List;

import net.sf.commonstringutil.StringUtil;
import net.sf.pipitrace.core.ftrace.TracerType;
import net.sf.pipitrace.core.model.TracePrefix;
import net.sf.pipitrace.core.model.TraceSuffix;

public class SchedEvent {

	public static final TracerType type = TracerType.SCHED_SWITCH;
	
	TracePrefix prefObj;
	
	TraceSuffix suffObj;
	
	public SchedEvent(String line) {
		
		super();
		
		String aStr = StringUtil.replaceLast(line, "==> ", "");
		
		List<String> list = StringUtil.splitAsList(aStr, "sched_switch: ");
		
		String prefStr = list.get(0);
		String suffStr = list.get(1);
		
		prefObj = TracePrefix.create(prefStr);
		suffObj = TraceSuffix.create(suffStr);
		
	}

	public TracePrefix getPrefObj() {
		return prefObj;
	}

	public TraceSuffix getSuffObj() {
		return suffObj;
	}

}
