package net.sf.pipitrace.core.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import net.sf.pipitrace.core.events.FlowControl;
import net.sf.pipitrace.core.ftrace.TracerType;
import net.sf.pipitrace.core.json.JsonUtil;
import net.sf.pipitrace.core.model.TracePrefix;
import net.sf.pipitrace.core.model.TraceSuffix;

import ca.eepp.quatre.java.javeltrace.trace.ex.TraceException;
import ca.eepp.quatre.java.javeltrace.trace.ex.WrongStateException;
import ca.eepp.quatre.java.javeltrace.trace.input.StreamedTraceInput;
import ca.eepp.quatre.java.javeltrace.trace.input.ex.TraceInputException;
import ca.eepp.quatre.java.javeltrace.trace.output.BufferedTraceOutput;
import ca.eepp.quatre.java.javeltrace.trace.output.ex.TraceOutputException;
import ca.eepp.quatre.java.javeltrace.trace.reader.IStreamedReader;
import ca.eepp.quatre.java.javeltrace.trace.reader.JSONCTFStreamedReader;
import ca.eepp.quatre.java.javeltrace.trace.writer.BinaryCTFWriter;
import ca.eepp.quatre.java.javeltrace.trace.writer.IWriter;
import ca.eepp.quatre.java.javeltrace.translation.Translator;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableTable.Builder;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public abstract class AbsHandler {

	protected EventBus eventBus;

	protected ArrayList<ImmutableTable.Builder<TracePrefix, TracerType, TraceSuffix>> builderList = Lists.newArrayList();
	
	public AbsHandler(EventBus eventBus) {
		super();
		this.eventBus = eventBus;
	}

	@Subscribe
    public void recieveFlowControl(FlowControl event) {
		
		if(event == FlowControl.READ_DONE){
			
			ArrayList<ImmutableTable<TracePrefix, TracerType, TraceSuffix>> tableList = Lists.newArrayList();
			
			Iterator<Builder<TracePrefix, TracerType, TraceSuffix>> it = builderList.iterator();
			
			while(it.hasNext()){
				
				tableList.add(it.next().build());
				
			}
			
			_toJson(tableList);
			
		}
		
		
	}
	
	private void _toJson(ArrayList<ImmutableTable<TracePrefix, TracerType, TraceSuffix>> tableList){
		
		try {
			
			long start = System.currentTimeMillis();
			
			InputStream inputStream = JsonUtil.toJson(tableList);
			 
			long delta =  System.currentTimeMillis() - start;
			System.out.println("time use to json : " + delta);
			
			start = System.currentTimeMillis();
			
			String out = "C:\\tmp\\javeltrace\\debug";
			String metadataPath = "metadata/metadata.tsdl";
			
	        File mp = new File("D:\\work\\eclipse421\\workspace\\pipitrace\\plugins\\net.sf.pipitrace.core\\metadata\\metadata.tsdl");
	        
			IStreamedReader reader =  new JSONCTFStreamedReader(mp, inputStream);
			IWriter writer = new BinaryCTFWriter(out);
			
			StreamedTraceInput input = new StreamedTraceInput(reader);
			input.open();
			
			BufferedTraceOutput output = new BufferedTraceOutput(input.getTraceParameters(), writer);
			output.setShrinkPacketSize(true);
			output.setWriteIfEmpty(false);
			output.open();
			
			// Translator
	        Translator translator = new Translator(input, output);
	        translator.translate();
	        
	        input.close();
	        output.close();
	        
	        delta =  System.currentTimeMillis() - start;
			System.out.println("time use to ctf : " + delta);
			start = System.currentTimeMillis();
		
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		
		} catch (IOException e) {
			
			e.printStackTrace();
		
		} catch (WrongStateException e) {
			
			e.printStackTrace();
		
		} catch (TraceInputException e) {
		
			e.printStackTrace();
		
		} catch (TraceOutputException e) {
			
			e.printStackTrace();
		
		} catch (TraceException e) {
			
			e.printStackTrace();
		
		} finally{
			
			
			 
		}
	}
}
