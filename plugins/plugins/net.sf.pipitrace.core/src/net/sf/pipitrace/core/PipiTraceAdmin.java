package net.sf.pipitrace.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
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
import ca.eepp.quatre.java.javeltrace.translation.ITranslatorObserver;
import ca.eepp.quatre.java.javeltrace.translation.Translator;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.sf.commonstringutil.StringUtil;
import net.sf.pipitrace.core.events.FlowControl;
import net.sf.pipitrace.core.events.SchedEvent;
import net.sf.pipitrace.core.ftrace.FtraceUtil;
import net.sf.pipitrace.core.ftrace.TracerType;
import net.sf.pipitrace.core.handler.SchedHandler;
import net.sf.pipitrace.core.json.JsonUtil;
import net.sf.pipitrace.core.model.TracePrefix;
import net.sf.pipitrace.core.model.TraceSuffix;

public class PipiTraceAdmin {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 1) {
			
			System.err.println("Usage: java ... [file]");
			System.exit(1);
		
		}
		
		EventBus eventBus = new EventBus();
		
		SchedHandler schedHandler = new SchedHandler(eventBus);
		
		eventBus.register(schedHandler);
		
		long start = System.currentTimeMillis();
		
		File file = new File(args[0]);

		FileInputStream fis = null;
		
		try {
			
			fis = new FileInputStream(file);
			
			FileChannel fileChannel = fis.getChannel();
			
			MappedByteBuffer mmb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

			byte[] buffer = new byte[(int) fileChannel.size()];
			
			mmb.get(buffer);

			BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer)));

			String line;
			
			int count = 0;
			
			Pattern pt_sched_switch = Pattern.compile("(?i).*sched_switch.*", Pattern.CASE_INSENSITIVE);
			
			for (line = in.readLine(); line != null; line = in.readLine()) {

				boolean st = StringUtil.startsWithIgnoreCase(line, "#");

				if (!st) {

					boolean find = StringUtil.matchRegex(".*sched_switch.*", line);
					
					//boolean find = pt_sched_switch.matcher(line).find();
					
					if(find){
						
						eventBus.post(new SchedEvent(line));
						
						count ++;
					}
				}
				
			}
			
			eventBus.post(FlowControl.READ_DONE);
			
			eventBus.unregister(schedHandler);
			
			long delta =  System.currentTimeMillis() - start;
			System.out.println("time use read data : " + delta +" count : "+ count);
			start = System.currentTimeMillis();
			

		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		
		} catch (IOException e) {
			
			e.printStackTrace();
		
		} catch (WrongStateException e) {
			
			e.printStackTrace();
		
		} finally{
			
			 if(fis != null){
				 
				 try {
					 
					fis.close();
				
				 } catch (IOException e) {
					 
					e.printStackTrace();
				 
				 }
				 
			 }
			 
		}
		   
		
	}
	
	private class TranslationObserver implements ITranslatorObserver {

		private long curPkt = 1;
		private boolean verbose;

		public TranslationObserver(boolean verbose) {
			this.verbose = verbose;
		}

		@Override
		public void notifyStart() {
			if (this.verbose) {
				System.err.println("starting translation...");
			}
		}

		@Override
		public void notifyNewPacket(PacketInfo packetInfo) {
			if (this.verbose) {
				System.err.println("writing packet #" + this.curPkt + "...");
				++this.curPkt;
			}
		}

		@Override
		public void notifyNewEvent(Event ev) {
			// Ignore events (happens too often)
		}

		@Override
		public void notifyStop() {
			if (this.verbose) {
				System.err.println("translation done");
			}
		}

	}

}
