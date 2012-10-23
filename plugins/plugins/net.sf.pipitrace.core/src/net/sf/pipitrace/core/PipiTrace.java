package net.sf.pipitrace.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

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
import net.sf.pipitrace.core.ftrace.FtraceUtil;
import net.sf.pipitrace.core.ftrace.TracerType;
import net.sf.pipitrace.core.json.JsonUtil;
import net.sf.pipitrace.core.model.TracePrefix;
import net.sf.pipitrace.core.model.TraceSuffix;

public class PipiTrace {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 1) {
			
			System.err.println("Usage: java ... [file]");
			System.exit(1);
		
		}
		
		long start = System.currentTimeMillis();
		
		File file = new File(args[0]);

		FileInputStream fis = null;
		
		try {
			
			fis = new FileInputStream(file);
			
			FileChannel fileChannel = fis.getChannel();
			
			ArrayList<ImmutableTable<TracePrefix, TracerType, TraceSuffix>> tableList = FtraceUtil.parse(fileChannel);
			
			long delta =  System.currentTimeMillis() - start;
			System.out.println("time use read data : " + delta);
			start = System.currentTimeMillis();
			
			InputStream inputStream = JsonUtil.toJson(tableList);
			 
			delta =  System.currentTimeMillis() - start;
			System.out.println("time use to json : " + delta);
			start = System.currentTimeMillis();
			
			String out = "C:\\tmp\\javeltrace\\debug";
			String metadataPath = "metadata/metadata.tsdl";
			
//			Bundle bundle = Platform.getBundle(PipiTraceActivator.getContext().getBundle().getSymbolicName());
//	        URL fileURL = bundle.getEntry(metadataPath);
//	        File mp = new File(FileLocator.resolve(fileURL).toURI());
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
