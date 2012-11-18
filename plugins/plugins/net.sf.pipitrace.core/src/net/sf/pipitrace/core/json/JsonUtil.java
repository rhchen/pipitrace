package net.sf.pipitrace.core.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import net.sf.pipitrace.core.ftrace.TracerType;
import net.sf.pipitrace.core.model.TracePrefix;
import net.sf.pipitrace.core.model.TraceSuffix;

import com.Ostermiller.util.CircularByteBuffer;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.UnmodifiableIterator;

public final class JsonUtil {

	private static final long PACKET_HEADER_MAGIC_NUMBER = 0xc1fc1fc1L;
	
	public static void WritePacketsStart(JsonGenerator g) throws JsonGenerationException, IOException{
		
		_writePacketsStart(g);
		
	}
	
	public static void Write(JsonGenerator g, ArrayList<ImmutableTable<TracePrefix, TracerType, TraceSuffix>> tableList) throws JsonGenerationException, IOException{
		
		_write(g, tableList);
		
	}
	
	public static void WritePacketsEnd(JsonGenerator g) throws JsonGenerationException, IOException{
		
		_writePacketsEnd(g);
		
	}
	
	public static InputStream toJson(ArrayList<ImmutableTable<TracePrefix, TracerType, TraceSuffix>> tableList) throws IOException{
		
		 /*
         * Important: use a MappingJsonFactory here, and not a simple
         * JsonFactory. Otherwise, the JSON parser won't be able to
         * read a node as a tree (readValueAsTree).
         */
        MappingJsonFactory fjs = new MappingJsonFactory();
        CircularByteBuffer circularByteBuffer = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
        JsonGenerator g = fjs.createJsonGenerator(circularByteBuffer.getOutputStream(), JsonEncoding.UTF8);
        
		g.useDefaultPrettyPrinter();
		
		_writePacketsStart(g);
		
		_write(g, tableList);
		
		_writePacketsEnd(g);
		
		return circularByteBuffer.getInputStream();
		
	}
	
	public static void toJson(File file) throws IOException{
		
		MappingJsonFactory fjs = new MappingJsonFactory();
		JsonGenerator g = fjs.createJsonGenerator(file, JsonEncoding.UTF8);
        
		g.useDefaultPrettyPrinter();
	}
	
	private static void _writePacketsStart(JsonGenerator g) throws JsonGenerationException, IOException{
		
		g.writeStartObject();//{
		
		g.writeObjectField("metadata", "external:metadata.tsdl");
		
			g.writeArrayFieldStart("packets");//[
	}
	
	private static void _writePacketsEnd(JsonGenerator g) throws JsonGenerationException, IOException{
		
		 	g.writeEndArray();//]
		
		
		g.writeEndObject();//}
		
		
		g.close(); // important: will force flushing of output, close underlying output stream
	}
	
	private static void _write(JsonGenerator g, ArrayList<ImmutableTable<TracePrefix, TracerType, TraceSuffix>> tableList) throws JsonGenerationException, IOException{
		
//		g.writeStartObject();//{
//		
//			g.writeObjectField("metadata", "external:metadata.tsdl");
//			
//				g.writeArrayFieldStart("packets");//[
			
				for(int i=0; i<tableList.size(); i++){
					
						ImmutableTable<TracePrefix, TracerType, TraceSuffix> tableData = tableList.get(i);
					
						ImmutableSet<TracePrefix> rowSet =  tableData.rowKeySet();
						 
						TracePrefix[] tps =rowSet.toArray(new TracePrefix[tableData.size()]);
						
						int length = rowSet.size();
						
						int times = 1000;
						int size = length / times;
						
						for(int ii=0 ; ii<size; ii++){
							
								g.writeStartObject();//{
								
									JsonUtil._writeHeaderNode(g);
									
									int start = ii * times;
									int end   = (ii + 1) * times - 1;
									
									g.writeObjectFieldStart("context");
									
										_writeContext(g, tps[start], tps[end], times);
										
										g.writeNumberField("cpu_id", i);
									
									g.writeEndObject();
								
									g.writeArrayFieldStart("events");//[
									
										for(int iii=start; iii<=end; iii++){
											
											TracePrefix tk = tps[iii];
											TraceSuffix im = tableData.get(tk, TracerType.SCHED_SWITCH);
											
											_writeEvent(g, tk, im);							
										}
										
										//_writeEvents(g, tableData);
										
									g.writeEndArray();//]
							
								g.writeEndObject();//}				
							
						}
						
				}
				
//				g.writeEndArray();//]
//			
//			
//		g.writeEndObject();//}
//		
//		
//		g.close(); // important: will force flushing of output, close underlying output stream
	}
	
	private static void _writeEvent(JsonGenerator g, TracePrefix tk,  TraceSuffix im) throws JsonGenerationException, IOException{
		
		String prev_comm  = im.getValue(TraceSuffix.KEY_PREV_COMM);
		String prev_pid   = im.getValue(TraceSuffix.KEY_PREV_PID);
		String prev_prio  = im.getValue(TraceSuffix.KEY_PREV_PRIO);
		String prev_state = im.getValue(TraceSuffix.KEY_PREV_STATE);
		String next_comm  = im.getValue(TraceSuffix.KEY_NEXT_COMM);
		String next_pid   = im.getValue(TraceSuffix.KEY_NEXT_PID);
		String next_prio  = im.getValue(TraceSuffix.KEY_NEXT_PRIO);
		
		int i_prev_state = 0;
		
		/**
		 * Need Fix, confirm state mapping
		 */
		if(prev_state.equalsIgnoreCase("S")) i_prev_state = 1;
		if(prev_state.equalsIgnoreCase("R")) i_prev_state = 2;
		
		g.writeStartObject();//{
		
			g.writeObjectFieldStart("header");
			
				g.writeNumberField("id", 0);
				
				g.writeObjectFieldStart("v");
					
					//g.writeNumberField("id", 0);
					g.writeNumberField("timestamp", tk.getTimeStamp());
				
				g.writeEndObject();
				
			g.writeEndObject();
			
			g.writeObjectFieldStart("payload");
			
				g.writeObjectField("_prev_comm", prev_comm);
				g.writeNumberField("_prev_tid", Integer.parseInt(prev_pid));
				g.writeNumberField("_prev_prio", Integer.parseInt(prev_prio));
				g.writeNumberField("_prev_state", i_prev_state);
				g.writeObjectField("_next_comm", next_comm);
				g.writeNumberField("_next_tid", Integer.parseInt(next_pid));
				g.writeNumberField("_next_prio", Integer.parseInt(next_prio));
				
			g.writeEndObject();
			
		g.writeEndObject();//}
		
	}

	private static void _writeEvents(JsonGenerator g, ImmutableTable<TracePrefix, TracerType, TraceSuffix> tableData) throws JsonGenerationException, IOException{
		
		UnmodifiableIterator<TracePrefix> it = tableData.rowKeySet().iterator();
		
		int ii = 0;
		
		while(it.hasNext() && ii<180000){
			
			ii++;
			
			TracePrefix tk = it.next();
			
			TraceSuffix im = tableData.get(tk, TracerType.SCHED_SWITCH);
			
			String prev_comm  = im.getValue(TraceSuffix.KEY_PREV_COMM);
			String prev_pid   = im.getValue(TraceSuffix.KEY_PREV_PID);
			String prev_prio  = im.getValue(TraceSuffix.KEY_PREV_PRIO);
			String prev_state = im.getValue(TraceSuffix.KEY_PREV_STATE);
			String next_comm  = im.getValue(TraceSuffix.KEY_NEXT_COMM);
			String next_pid   = im.getValue(TraceSuffix.KEY_NEXT_PID);
			String next_prio  = im.getValue(TraceSuffix.KEY_NEXT_PRIO);
			
			int i_prev_state = 0;
			
			/**
			 * Need Fix, confirm state mapping
			 */
			if(prev_state.equalsIgnoreCase("S")) i_prev_state = 1;
			if(prev_state.equalsIgnoreCase("R")) i_prev_state = 2;
			
			g.writeStartObject();//{
			
				g.writeObjectFieldStart("header");
				
					g.writeNumberField("id", 0);
					
					g.writeObjectFieldStart("v");
						
						//g.writeNumberField("id", 0);
						g.writeNumberField("timestamp", tk.getTimeStamp());
					
					g.writeEndObject();
					
				g.writeEndObject();
				
				g.writeObjectFieldStart("payload");
				
					g.writeObjectField("_prev_comm", prev_comm);
					g.writeNumberField("_prev_tid", Integer.parseInt(prev_pid));
					g.writeNumberField("_prev_prio", Integer.parseInt(prev_prio));
					g.writeNumberField("_prev_state", i_prev_state);
					g.writeObjectField("_next_comm", next_comm);
					g.writeNumberField("_next_tid", Integer.parseInt(next_pid));
					g.writeNumberField("_next_prio", Integer.parseInt(next_prio));
					
				g.writeEndObject();
				
			g.writeEndObject();//}
			
		}
	}
	
	private static void _writeContext(JsonGenerator g, TracePrefix start, TracePrefix end, int times) throws JsonGenerationException, IOException{
		
		long st = start.getTimeStamp();
		long et = end.getTimeStamp();
		
		long content_size = 448 * times;
		long packet_size  = 65536;
		// Packet size => content size + padding (up to a multiple of 32768 bits)
		packet_size = (content_size + 32768 - 1) & ~(32768 - 1);
		
		g.writeNumberField("timestamp_begin", st);
		g.writeNumberField("timestamp_end", et);
		g.writeNumberField("events_discarded", 0);
		g.writeNumberField("content_size", content_size);
		g.writeNumberField("packet_size", packet_size);
		
	}
	
	private static void _writeHeaderNode(JsonGenerator g) throws JsonGenerationException, IOException{
		
		g.writeObjectFieldStart("header");
		
			g.writeNumberField("magic", PACKET_HEADER_MAGIC_NUMBER);
			
			g.writeArrayFieldStart("uuid");//[
			
				g.writeNumber(33);
				g.writeNumber(58);
				g.writeNumber(5);
				g.writeNumber(10);
				g.writeNumber(13);
				g.writeNumber(150);
				g.writeNumber(56);
				g.writeNumber(64);
				g.writeNumber(153);
				g.writeNumber(176);
				g.writeNumber(178);
				g.writeNumber(181);
				g.writeNumber(47);
				g.writeNumber(189);
				g.writeNumber(194);
				g.writeNumber(119); 
			
			g.writeEndArray();//]
			
			g.writeNumberField("stream_id", 0);
			
		g.writeEndObject();
	}
}
