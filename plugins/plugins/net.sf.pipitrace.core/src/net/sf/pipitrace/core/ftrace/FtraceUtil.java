package net.sf.pipitrace.core.ftrace;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import net.sf.commonstringutil.StringUtil;
import net.sf.pipitrace.core.model.TracePrefix;
import net.sf.pipitrace.core.model.TraceSuffix;

import com.Ostermiller.util.CircularByteBuffer;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableTable.Builder;

public class FtraceUtil {

	public static InputStream parse(FileChannel fileChannel, boolean is) throws IOException{
		
		MappedByteBuffer mmb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

		byte[] buffer = new byte[(int) fileChannel.size()];
		
		mmb.get(buffer);
		
		CircularByteBuffer circularByteBuffer = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
		
		circularByteBuffer.getOutputStream().write(buffer);
		
		return circularByteBuffer.getInputStream();
	}
	
	public static ArrayList<ImmutableTable<TracePrefix, TracerType, TraceSuffix>> parse(FileChannel fileChannel) throws IOException{
		
		MappedByteBuffer mmb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

		byte[] buffer = new byte[(int) fileChannel.size()];
		
		mmb.get(buffer);

		BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer)));

		String line;
		
		ArrayList<ImmutableTable.Builder<TracePrefix, TracerType, TraceSuffix>> builderList = Lists.newArrayList();
		
		for (line = in.readLine(); line != null; line = in.readLine()) {

			boolean st = StringUtil.startsWithIgnoreCase(line, "#");

			if (!st) {

				Pattern pt_sched_switch = Pattern.compile("(?i).*sched_switch.*", Pattern.CASE_INSENSITIVE);
				
				boolean find = pt_sched_switch.matcher(line).find();
				
				if(find){
					
					String aStr = StringUtil.replaceLast(line, "==> ", "");
					
					List<String> list = StringUtil.splitAsList(aStr, "sched_switch: ");
					
					String prefStr = list.get(0);
					String suffStr = list.get(1);
					
					TracePrefix prefObj = TracePrefix.create(prefStr);
					TraceSuffix suffObj = TraceSuffix.create(suffStr);
					
					int cpuNum = prefObj.getCpuNum();
					
					if(cpuNum + 1 > builderList.size()){
						
						for(int i=builderList.size(); i<cpuNum + 1; i++){
							
							builderList.add(ImmutableTable.<TracePrefix, TracerType, TraceSuffix>builder());
							
						}
						
					}
					
					builderList.get(cpuNum).put(prefObj, TracerType.SCHED_SWITCH, suffObj);
				}
				
			}

		}
		
		ArrayList<ImmutableTable<TracePrefix, TracerType, TraceSuffix>> tableList = Lists.newArrayList();
		
		Iterator<Builder<TracePrefix, TracerType, TraceSuffix>> it = builderList.iterator();
		
		while(it.hasNext()){
			
			tableList.add(it.next().build());
			
		}
		
		return tableList;
	}

}
