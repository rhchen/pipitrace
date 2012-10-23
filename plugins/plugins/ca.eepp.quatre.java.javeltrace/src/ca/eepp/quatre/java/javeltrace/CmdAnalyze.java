package ca.eepp.quatre.java.javeltrace;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import ca.eepp.quatre.java.javeltrace.metadata.strings.MetadataFieldNames;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.input.StreamedTraceInput;
import ca.eepp.quatre.java.javeltrace.trace.input.ex.TraceInputException;
import ca.eepp.quatre.java.javeltrace.trace.reader.BinaryCTFStreamedReader;
import ca.eepp.quatre.java.javeltrace.trace.reader.IStreamedReader;
import ca.eepp.quatre.java.javeltrace.trace.reader.JSONCTFStreamedReader;
import ca.eepp.quatre.java.javeltrace.utils.TimeStampAccumulator;

public class CmdAnalyze extends Command {
    private String inputType;
    private String outputType = "human";
    private File inputPath;
    private boolean verbose;
    
    public CmdAnalyze(String[] args) {
        super(args);
    }

    @Override
    public String usage() {
        return  
            "Usage: javeltrace analyze [OPTION]... INPUTF\n\n" +
            "Analyzes a trace and shows informations.\n\n" +
            "Analyzes INPUTF (file or directory according to the input format) and outputs\n" +
            "results to standard output using a specified format.\n\n" +
            "Options:\n" +
            "  -h, --help   show this help\n" +
            "  -i TYPE      set trace input format to TYPE (required)\n" +
            "  -o TYPE      set analysis output format to TYPE (default: human)\n" +
            "  -v           output progress to standard error\n\n" + 
            "Valid trace input types:\n" +
            "  ctf          CTF v1.8                (dir)\n" +
            "  json-ctf     JSON CTF v1.8           (file)\n\n" +
            "Valid analysis output types:\n" +
            "  equal        param=value text\n" +
            "  human        human-readable text\n" +
            "  json         JSON\n";
    }

    private void parseArgs() {
        // Options
        Options options = new Options();
        options.addOption("i", true, "trace input format");
        options.addOption("o", true, "analysis output format");
        options.addOption("v", false, "verbose");
        
        // Parse
        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse(options, this.args);
            
            // Trace input format
            if (cmd.hasOption("i")) {
                String type = cmd.getOptionValue("i");
                if (type.equals("ctf") || type.equals("json-ctf")) {
                    this.inputType = type;
                } else {
                    Command.cmdlineError("unknown trace input format \"" + type + "\"");
                }
            } else {
                Command.cmdlineError("trace input format not specified");
            }
            
            // Trace output format
            if (cmd.hasOption("o")) {
                String type = cmd.getOptionValue("o");
                if (type.equals("equal") || type.equals("json") || type.equals("human")) {
                    this.outputType = type;
                } else {
                    Command.cmdlineError("unknown analysis output format \"" + type + "\"");
                }
            }
            
            // Verbose
            this.verbose = cmd.hasOption("v");
            
            // Path
            String[] rem = cmd.getArgs();
            if (rem.length != 1) {
                throw new ParseException("");
            }
            File f = new File(rem[0]);
            if (!f.exists()) {
                Command.fileError("input path does not exist");
            }
            if (this.inputType.equals("ctf")) {
                if (!f.isDirectory()) {
                    Command.fileError("input path must be a directory with specified format");
                }
            }
            if (this.inputType.equals("json-ctf")) {
                if (!f.isFile()) {
                    Command.fileError("input path must be a file with specified format");
                }
            }
            this.inputPath = f;
        } catch (ParseException e) {
            Command.cmdlineError("bad command line");
        }
    }
    
    private static void outputAsJSON(AnalysisData data) {
        System.out.print("{\n");
        System.out.print("  \"counts\": {\n");
        System.out.print("    \"streams\": " + data.nbStreams + ",\n");
        System.out.print("    \"packets\": " + data.nbPackets + ",\n");
        System.out.print("    \"events\": " + data.nbEvents + ",\n");
        System.out.print("    \"cpus\": " + data.nbCPU + ",\n");
        System.out.print("    \"lostEvents\": " + data.nbLostEvents + ",\n");
        System.out.print("    \"metadataTextLength\": " + data.nbMetadataTextChars + "\n");
        System.out.print("  },\n");
        System.out.print("  \"totalSizes\": {\n");
        System.out.print("    \"packets\": " + data.totalPacketsSizeBits + ",\n");
        System.out.print("    \"events\": " + data.totalEventsSizeBits + ",\n");
        System.out.print("    \"content\": " + data.totalContentSizeBits + ",\n");
        System.out.print("    \"wasted\": " + data.totalWastedSizeBits + "\n");
        System.out.print("  },\n");
        System.out.print("  \"minMaxSizes\": {\n");
        System.out.print("    \"packets\": {\n");
        System.out.print("      \"min\": " + data.minPacketSizeBits + ",\n");
        System.out.print("      \"max\": " + data.maxPacketSizeBits + "\n");
        System.out.print("    },\n");
        System.out.print("    \"events\": {\n");
        System.out.print("      \"min\": " + data.minEventSizeBits + ",\n");
        System.out.print("      \"max\": " + data.maxEventSizeBits + "\n");
        System.out.print("    }\n");
        System.out.print("  },\n");
        System.out.print("  \"minMaxCycles\": {\n");
        System.out.print("    \"packets\": {\n");
        System.out.print("      \"begin\": {\n");
        System.out.print("        \"min\": " + data.minPacketTSBegin + ",\n");
        System.out.print("        \"max\": " + data.maxPacketTSBegin + "\n");
        System.out.print("      },\n");
        System.out.print("      \"end\": {\n");
        System.out.print("        \"min\": " + data.minPacketTSEnd + ",\n");
        System.out.print("        \"max\": " + data.maxPacketTSEnd + "\n");
        System.out.print("      }\n");
        System.out.print("    },\n");
        System.out.print("    \"events\": {\n");
        System.out.print("      \"min\": " + data.minEventTS + ",\n");
        System.out.print("      \"max\": " + data.maxEventTS + "\n");
        System.out.print("    }\n");
        System.out.print("  },\n");
        System.out.print("  \"minMaxOffsetSeconds\": {\n");
        System.out.print("    \"events\": {\n");
        System.out.print("      \"min\": " + CmdAnalyze.niceDouble(data.minEventSeconds) + ",\n");
        System.out.print("      \"max\": " + CmdAnalyze.niceDouble(data.maxEventSeconds) + "\n");
        System.out.print("    }\n");
        System.out.print("  },\n");
        System.out.print("  \"minMaxOffsetDates\": {\n");
        System.out.print("    \"events\": {\n");
        System.out.print("      \"min\": \"" + CmdAnalyze.parsableDate(data.minEventDate) + "\",\n");
        System.out.print("      \"max\": \"" + CmdAnalyze.parsableDate(data.maxEventDate) + "\"\n");
        System.out.print("    }\n");
        System.out.print("  }\n");
        System.out.print("}\n");
    }
    
    private static String equalStr(String param, long val) {
        return param + "=" + val + "\n";
    }
    
    private static String niceDouble(double val) {
        DecimalFormat format = new DecimalFormat("#.##########");
        
        return format.format(val);
    }
    
    private static String equalStr(String param, double val) {
        return param + "=" + CmdAnalyze.niceDouble(val) + "\n";
    }
    
    private static String parsableDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
        
        return format.format(date); 
    }
    
    private static String equalStr(String param, Date val) {
        return param + "=" + CmdAnalyze.parsableDate(val) + "\n";
    }
    
    private static void outputAsEqual(AnalysisData data) {
        String out =
                CmdAnalyze.equalStr("nb_streams", data.nbStreams) +
                CmdAnalyze.equalStr("nb_packets", data.nbPackets) +
                CmdAnalyze.equalStr("nb_events", data.nbEvents) +
                CmdAnalyze.equalStr("nb_cpus", data.nbCPU) +
                CmdAnalyze.equalStr("nb_lost_events", data.nbLostEvents) +
                CmdAnalyze.equalStr("metadata_text_length", data.nbMetadataTextChars) +
                CmdAnalyze.equalStr("total_packets_size", data.totalPacketsSizeBits) +
                CmdAnalyze.equalStr("total_events_size", data.totalEventsSizeBits) +
                CmdAnalyze.equalStr("total_content_size", data.totalContentSizeBits) +
                CmdAnalyze.equalStr("total_wasted_size", data.totalWastedSizeBits) +
                CmdAnalyze.equalStr("min_packet_size", data.minPacketSizeBits) +
                CmdAnalyze.equalStr("max_packet_size", data.maxPacketSizeBits) +
                CmdAnalyze.equalStr("min_event_size", data.minEventSizeBits) +
                CmdAnalyze.equalStr("max_event_size", data.maxEventSizeBits) +
                CmdAnalyze.equalStr("min_packet_cycle_begin", data.minPacketTSBegin) +
                CmdAnalyze.equalStr("max_packet_cycle_begin", data.maxPacketTSBegin) +
                CmdAnalyze.equalStr("min_packet_cycle_end", data.minPacketTSEnd) +
                CmdAnalyze.equalStr("max_packet_cycle_end", data.maxPacketTSEnd) +
                CmdAnalyze.equalStr("min_event_cycle", data.minEventTS) +
                CmdAnalyze.equalStr("max_event_cycle", data.maxEventTS) +
                CmdAnalyze.equalStr("min_event_offset_seconds", data.minEventSeconds) +
                CmdAnalyze.equalStr("max_event_offset_seconds", data.maxEventSeconds) +
                CmdAnalyze.equalStr("min_event_offset_date", data.minEventDate) +
                CmdAnalyze.equalStr("max_event_offset_date", data.maxEventDate);
        System.out.print(out);
    }
    
    private static String humanFormat(String descr, String val) {
        return String.format("%-50s%30s\n", descr, val);
    }
    
    private static String humanFormat(String descr, long val) {
        return CmdAnalyze.humanFormat(descr, CmdAnalyze.humanFormatLong(val));
    }
    
    private static String humanFormatLong(long val) {
        DecimalFormat format = new DecimalFormat("#,###");
        
        return format.format(val);
    }
    
    private static String humanFormatDouble(double val) {
        DecimalFormat format = new DecimalFormat("#,###.##########");
        
        return format.format(val);
    }
    
    private static String humanFormat(String descr, Date val) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        return CmdAnalyze.humanFormat(descr, format.format(val));
    }
    
    private static void outputAsHuman(AnalysisData data) {
        StringBuilder sb = new StringBuilder();
        sb.append(CmdAnalyze.humanFormat("number of streams", data.nbStreams));
        sb.append(CmdAnalyze.humanFormat("number of packets", data.nbPackets));
        sb.append(CmdAnalyze.humanFormat("number of events", data.nbEvents));
        sb.append(CmdAnalyze.humanFormat("number of CPUs", data.nbCPU));
        sb.append(CmdAnalyze.humanFormat("number of lost events", data.nbLostEvents));
        sb.append(CmdAnalyze.humanFormat("metadata text length", CmdAnalyze.humanFormatLong(data.nbMetadataTextChars) + " chars."));
        sb.append("\n");
        sb.append(CmdAnalyze.humanFormat("total packets size", CmdAnalyze.humanFormatLong(data.totalPacketsSizeBits) + " b"));
        sb.append(CmdAnalyze.humanFormat("total events size", CmdAnalyze.humanFormatLong(data.totalEventsSizeBits) + " b"));
        sb.append(CmdAnalyze.humanFormat("total content size", CmdAnalyze.humanFormatLong(data.totalContentSizeBits) + " b"));
        sb.append(CmdAnalyze.humanFormat("total wasted size", CmdAnalyze.humanFormatLong(data.totalWastedSizeBits) + " b"));
        sb.append("\n");
        sb.append(CmdAnalyze.humanFormat("minimum packet size", CmdAnalyze.humanFormatLong(data.minPacketSizeBits) + " b"));
        sb.append(CmdAnalyze.humanFormat("maximum packet size", CmdAnalyze.humanFormatLong(data.maxPacketSizeBits) + " b"));
        sb.append(CmdAnalyze.humanFormat("minimum event size", CmdAnalyze.humanFormatLong(data.minEventSizeBits) + " b"));
        sb.append(CmdAnalyze.humanFormat("maximum event size", CmdAnalyze.humanFormatLong(data.maxEventSizeBits) + " b"));
        sb.append("\n");
        sb.append(CmdAnalyze.humanFormat("minimum packet cycle begin", data.minPacketTSBegin));
        sb.append(CmdAnalyze.humanFormat("maximum packet cycle begin", data.maxPacketTSBegin));
        sb.append(CmdAnalyze.humanFormat("minimum packet cycle end", data.minPacketTSEnd));
        sb.append(CmdAnalyze.humanFormat("maximum packet cycle end", data.maxPacketTSEnd));
        sb.append("\n");
        sb.append(CmdAnalyze.humanFormat("minimum event cycle", data.minEventTS));
        sb.append(CmdAnalyze.humanFormat("maximum event cycle", data.maxEventTS));
        sb.append(CmdAnalyze.humanFormat("minimum event time stamp (with offset)", CmdAnalyze.humanFormatDouble(data.minEventSeconds) + " s"));
        sb.append(CmdAnalyze.humanFormat("maximum event time stamp (with offset)", CmdAnalyze.humanFormatDouble(data.maxEventSeconds) + " s"));        sb.append(CmdAnalyze.humanFormat("minimum event date (with offset)", data.minEventDate));
        sb.append(CmdAnalyze.humanFormat("maximum event date (with offset)", data.maxEventDate));
        System.out.print(sb);
    }

    @Override
    public void exec() {
        // Parse arguments
        this.parseArgs();
        
        // Analysis data
        AnalysisData data = new AnalysisData();
        
        // Choose the reader
        IStreamedReader reader = null;
        if (this.inputType.equals("ctf")) {
            reader = new BinaryCTFStreamedReader(this.inputPath);
        } else if (this.inputType.equals("json-ctf")) {
            reader = new JSONCTFStreamedReader(this.inputPath);
        }
        
        // Trace input
        StreamedTraceInput input = new StreamedTraceInput(reader);
        
        // CPU array
        ArrayList<Integer> cpuArr = new ArrayList<Integer>();
        
        try {
            // Notify
            if (this.verbose) {
                System.err.println("opening trace \"" + this.inputPath.getAbsolutePath() + "\"...");
            }
            
            // Open the input
            input.open();

            // Time stamp accumulator
            TimeStampAccumulator tsAcc = new TimeStampAccumulator();
            if (input.getTraceParameters().clocks.containsKey("monotonic")) {
                tsAcc = new TimeStampAccumulator(input.getTraceParameters().clocks.get("monotonic"));
            }
            
            // Data (streams)
            data.nbStreams = input.getNbStreams();
            
            // Data (metadata text length)
            data.nbMetadataTextChars = input.getTraceParameters().metadataText.length();
            
            // Notify
            if (this.verbose) {
                System.err.println("browsing packets...");
            }
            
            // Browse packets
            while (input.getCurrentPacketInfo() != null) {
                // Get packet info
                PacketInfo info = input.getCurrentPacketInfo();
                                
                if (info.getContext() != null) {
                    // Data (packet size)                    
                    if (info.getContext().hasField(MetadataFieldNames.PACKET_CONTEXT_PACKET_SIZE)) {
                        long packetSize = info.getContext().getIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_PACKET_SIZE);
                        if (packetSize < data.minPacketSizeBits) {
                            data.minPacketSizeBits = packetSize;
                        }
                        if (packetSize > data.maxPacketSizeBits) {
                            data.maxPacketSizeBits = packetSize;
                        }
                        data.totalPacketsSizeBits += packetSize;
                    }
                    
                    // Data (content size)
                    if (info.getContext().hasField(MetadataFieldNames.PACKET_CONTEXT_CONTENT_SIZE)) {
                        data.totalContentSizeBits += info.getContext().getIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_CONTENT_SIZE);
                    }
                    
                    // Data (lost events)
                    if (info.getContext().hasField(MetadataFieldNames.PACKET_CONTEXT_EV_DISCARDED)) {
                        data.nbLostEvents += info.getContext().getIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_EV_DISCARDED);
                    }
                    
                    // Data (packet time stamps)
                    if (info.getContext().hasField(MetadataFieldNames.PACKET_CONTEXT_TS_BEGIN)) {
                        long ts = info.getContext().getIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_TS_BEGIN);
                        if (ts < data.minPacketTSBegin) {
                            data.minPacketTSBegin = ts;
                        }
                        if (ts > data.maxPacketTSBegin) {
                            data.maxPacketTSBegin = ts;
                        }
                        
                        // Reset time stamp accumulator
                        tsAcc.setTS(ts);
                    }
                    if (info.getContext().hasField(MetadataFieldNames.PACKET_CONTEXT_TS_END)) {
                        long ts = info.getContext().getIntFieldValue(MetadataFieldNames.PACKET_CONTEXT_TS_END);
                        if (ts < data.minPacketTSEnd) {
                            data.minPacketTSEnd = ts;
                        }
                        if (ts > data.maxPacketTSEnd) {
                            data.maxPacketTSEnd = ts;
                        }
                    }
                }
                
                // Data (number of packets)
                ++data.nbPackets;
                
                // CPU count
                if (!cpuArr.contains(info.getCPUID())) {
                    cpuArr.add(info.getCPUID());
                }
                
                // Browse packet events
                long at = 0;
                long evCount = 0;
                while (input.getCurrentPacketEvent() != null) {
                    // Get event
                    Event ev = input.getCurrentPacketEvent();
                    
                    // Update the time stamp accumulator
                    tsAcc.newEvent(ev);
                    
                    // Data (time stamps)
                    if (tsAcc.getTS() < data.minEventTS) {
                        data.minEventTS = tsAcc.getTS();
                    }
                    if (tsAcc.getTS() > data.maxEventTS) {
                        data.maxEventTS = tsAcc.getTS();
                    }
                    
                    // Data (number of events)
                    ++data.nbEvents;
                    ++evCount;
                    
                    // Event size
                    long evSize = ev.getSize(at);
                    at += evSize;
                    
                    // Data (min/max event size)
                    if (evSize < data.minEventSizeBits) {
                        data.minEventSizeBits = evSize;
                    }
                    if (evSize > data.maxEventSizeBits) {
                        data.maxEventSizeBits = evSize;
                    }
                    
                    // Next event
                    input.nextPacketEvent();
                }
                
                // Data (total events size)
                data.totalEventsSizeBits += at;
                
                // Notify if verbose
                if (this.verbose) {
                    String streamAddStr = " ";
                    if (info.getHeader().hasField(MetadataFieldNames.PACKET_HEADER_STREAM_ID)) {
                        streamAddStr = " (stream #" + info.getHeader().getIntFieldValue(MetadataFieldNames.PACKET_HEADER_STREAM_ID) + ") ";
                    }
                    System.err.println("packet #" + data.nbPackets + streamAddStr + "done with " + evCount + " events");
                }
                
                // Next packet
                input.nextPacket();
            }
            
            // Close the input
            input.close();
            
            // Data (time stamps to seconds/date)
            tsAcc.setTS(data.minEventTS);
            data.minEventSeconds = tsAcc.getOffsetSeconds();
            data.minEventDate = tsAcc.getOffsetDate();
            tsAcc.setTS(data.maxEventTS);
            data.maxEventSeconds = tsAcc.getOffsetSeconds();
            data.maxEventDate = tsAcc.getOffsetDate();
            
            // Data (total wasted size)
            data.totalWastedSizeBits = data.totalPacketsSizeBits - data.totalContentSizeBits;
            
            // Notify
            if (this.verbose) {
                System.err.println("trace closed");
            }
        } catch (TraceInputException e) {
            Command.inputError(e.getMessage());
        } catch (Exception e) {
            Command.inputError("unexpected exception: " + e.getMessage());
        }

        // Data (CPU count)
        data.nbCPU = cpuArr.size();
        
        // Present data
        if (this.outputType.equals("equal")) {
            CmdAnalyze.outputAsEqual(data);
        } else if (this.outputType.equals("json")) {
            CmdAnalyze.outputAsJSON(data);
        } else if (this.outputType.equals("human")) {
            CmdAnalyze.outputAsHuman(data);
        }
    }
    
    private class AnalysisData {
        public long nbPackets = 0;
        public long nbEvents = 0;
        public long nbStreams = 0;
        public long nbCPU = 0;
        public long nbLostEvents = 0;
        public long nbMetadataTextChars = 0;
        public long minPacketSizeBits = Long.MAX_VALUE;
        public long maxPacketSizeBits = Long.MIN_VALUE;
        public long minEventSizeBits = Long.MAX_VALUE;
        public long maxEventSizeBits = Long.MIN_VALUE;
        public long totalPacketsSizeBits = 0;
        public long totalEventsSizeBits = 0;
        public long totalContentSizeBits = 0;
        public long totalWastedSizeBits = 0;
        public long minPacketTSBegin = Long.MAX_VALUE;
        public long maxPacketTSBegin = Long.MIN_VALUE;
        public long minPacketTSEnd = Long.MAX_VALUE;
        public long maxPacketTSEnd = Long.MIN_VALUE;
        public long minEventTS = Long.MAX_VALUE;
        public long maxEventTS = Long.MIN_VALUE;
        public double minEventSeconds;
        public double maxEventSeconds;
        public Date minEventDate;
        public Date maxEventDate;
    }
}
