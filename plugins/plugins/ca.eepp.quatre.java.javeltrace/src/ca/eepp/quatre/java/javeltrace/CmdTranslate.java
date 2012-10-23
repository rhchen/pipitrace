package ca.eepp.quatre.java.javeltrace;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.input.StreamedTraceInput;
import ca.eepp.quatre.java.javeltrace.trace.input.ex.TraceInputException;
import ca.eepp.quatre.java.javeltrace.trace.output.BufferedTraceOutput;
import ca.eepp.quatre.java.javeltrace.trace.output.ex.TraceOutputException;
import ca.eepp.quatre.java.javeltrace.trace.reader.BinaryCTFStreamedReader;
import ca.eepp.quatre.java.javeltrace.trace.reader.IStreamedReader;
import ca.eepp.quatre.java.javeltrace.trace.reader.JSONCTFStreamedReader;
import ca.eepp.quatre.java.javeltrace.trace.writer.BinaryCTFWriter;
import ca.eepp.quatre.java.javeltrace.trace.writer.CyclesOnlyWriter;
import ca.eepp.quatre.java.javeltrace.trace.writer.HTMLWriter;
import ca.eepp.quatre.java.javeltrace.trace.writer.IWriter;
import ca.eepp.quatre.java.javeltrace.trace.writer.JSONCTFWriter;
import ca.eepp.quatre.java.javeltrace.translation.ITranslatorObserver;
import ca.eepp.quatre.java.javeltrace.translation.Translator;

public class CmdTranslate extends Command {
    // General
    private String inputType;
    private String outputType = "json-ctf";
    private File inputPath;
    private File outputPath = null;
    private boolean useRange = false;
    private long rangeFrom;
    private long rangeTo;
    private boolean expertMode;
    private boolean verbose ;
    
    // JSON CTF output
    private boolean embedMetadata ;
    private boolean minimalSize ;
    private boolean indentWithTabs;
    private int wpPerLevel = 2;
    private String externalMetadataFile = "metadata.tsdl";
    private boolean addEnumLabel;
    private boolean addEventName;
    private boolean addEventID;
    private boolean addFloatValue;
    
    // HTML output
    private boolean includeMetadataText;
    private boolean disableIndex;
    private boolean disableAnimation;
    private String packetPagePrefix = "packet";
    private String indexPagePrefix = "index";
    
    // Clock cycles only output
    private boolean includePacketCycles;
    private boolean excludeEventCycles;
    private String evCyclePrefix = "e";
    private String pktCyclePrefix = "p";
    private boolean withOffset;
    
    public CmdTranslate(String[] args) {
        super(args);
    }

    @Override
    public String usage() {
        return  
            "Usage: javeltrace translate [OPTION]... INPUTF [OUTPUTF]\n\n" +
            "Translates a trace from one format to another.\n\n" +
            "Translates INPUTF (file or directory according to the input format) and\n" +
            "produces output OUTPUTF (file or directory according to output format). If\n" +
            "OUTPUTF is not specified, standard output is used for file formats and\n" +
            "current working directory is used for directory formats.\n\n" +
            "Options:\n" +
            "  -h, --help   show this help\n" +
            "  -i TYPE      set trace input format to TYPE (required)\n" +
            "  -o TYPE      set trace output format to TYPE (default: json-ctf)\n" +
            "  -r FROM:TO   set translation range from cycle FROM to cycle TO\n" +
            "  -r FROM:     set translation range from cycle FROM to end of trace\n" +
            "  -r :TO       set translation range from start of trace to cycle TO\n" +
            "  -v           output progress to standard error\n" + 
            "  -x           expert mode (do not resize packets, write packets if empty)\n\n" +
            "JSON CTF v1.8 output specific options:\n" +
            "  --embed-metadata             do not produce an external metadata file\n" +
            "  --minimal-size               produce a minimal size file (not pretty)\n" +
            "  --indent-with-tabs           indent with tabs (default: spaces)\n" +
            "  --wp-per-level=WP            indent with WP whitespaces per level (0 to 32)\n" +
            "                                 (default: 2)\n" +
            "  --external-metadata=FILE     set external metadata file name to FILE\n" +
            "                                 (default: \"metadata.tsdl\")\n" +
            "  --add-enum-label             add CTF enum label in addition to value\n" +
            "  --add-event-name             add event name to event node\n" +
            "  --add-event-id               add event real ID to event node\n" +
            "  --add-float-value            add float value to float node\n\n" +
            "HTML output specific options:\n" +
            "  --include-metadata-text      output metadata text to index page (if any)\n" +
            "  --disable-index              do not create an index page with packets links\n" +
            "  --disable-animation          disable jQuery animation (static pages)\n" +
            "  --packet-page-prefix=PREF    set packets pages prefix to PREF\n" +
            "                                 (default: \"packet\")\n" +
            "  --index-page-prefix=PREF     set index page prefix to PREF (default: \"index\")\n\n" +
            "Clock cycles only output specific options:\n" +
            "  --with-offset                apply offset to all cycles\n" +
            "  --include-packet-cycles      also output packets boundaries\n" +
            "  --exclude-event-cycles       do not output event cycles\n" +
            "  --event-cycle-prefix=PRE     set event cycle prefix to PREF (default: \"e\")\n" +
            "  --packet-cycle-prefix=PRE    set packet cycle prefix to PREF (default: \"p\")\n\n" +
            "Valid trace input types:\n" +
            "  ctf          CTF v1.8                (dir)\n" +
            "  json-ctf     JSON CTF v1.8           (file)\n\n" +
            "Valid trace output types:\n" +
            "  ctf          CTF v1.8                (dir)\n" +
            "  json-ctf     JSON CTF v1.8           (file)\n" +
            "  cycles       Clock cycles only       (file)\n" +
            "  html         HTML                    (dir)\n" +
            "  xml          XML                     (file)      -> upcoming!\n" +
            "  babel        Babeltrace-like text    (file)      -> upcoming!\n";
    }
    
    private void parseRange(String range) {
        long from = Long.MIN_VALUE;
        long to = Long.MAX_VALUE;
        
        // Check syntax
        try {
            if (range.matches("^\\d+:\\d+$")) {
                // Full range
                String[] tokens = range.split(":");
                from = Long.parseLong(tokens[0]);
                to = Long.parseLong(tokens[1]);
            } else if (range.matches("^\\d+:$")) {
                // Only from
                String[] tokens = range.split(":");
                from = Long.parseLong(tokens[0]);
            } else if (range.matches("^:\\d+$")) {
                // Only from
                String[] tokens = range.split(":");
                to = Long.parseLong(tokens[1]);
            } else {
                Command.cmdlineError("wrong range format \"" + range + "\"");
            }
        } catch (NumberFormatException e) {
            Command.cmdlineError("unable to parse range values (too long?)");
        }
        
        // Validate range
        if (to < from) {
            Command.cmdlineError("wrong range: TO must be greater or equal to FROM");
        }
        
        // Assign
        this.rangeFrom = from;
        this.rangeTo = to;
    }
    
    @SuppressWarnings("static-access")
    private void parseArgs() {
        Options options = new Options();
        
        // General options
        options.addOption("i", true, "trace input format");
        options.addOption("o", true, "trace output format");
        options.addOption("r", true, "translation range");
        options.addOption("v", false, "verbose");
        options.addOption("x", false, "expert mode");
        
        // JSON CTF output options
        options.addOption(OptionBuilder.withLongOpt("embed-metadata").create());
        options.addOption(OptionBuilder.withLongOpt("minimal-size").create());
        options.addOption(OptionBuilder.withLongOpt("indent-with-tabs").create());
        options.addOption(OptionBuilder.withLongOpt("add-enum-label").create());
        options.addOption(OptionBuilder.withLongOpt("add-event-name").create());
        options.addOption(OptionBuilder.withLongOpt("add-event-id").create());
        options.addOption(OptionBuilder.withLongOpt("add-float-value").create());
        options.addOption(OptionBuilder.withLongOpt("wp-per-level").hasArg().create());
        options.addOption(OptionBuilder.withLongOpt("external-metadata").hasArg().create());
        
        // HTML output options
        options.addOption(OptionBuilder.withLongOpt("include-metadata-text").create());
        options.addOption(OptionBuilder.withLongOpt("disable-index").create());
        options.addOption(OptionBuilder.withLongOpt("disable-animation").create());
        options.addOption(OptionBuilder.withLongOpt("packet-page-prefix").hasArg().create());
        options.addOption(OptionBuilder.withLongOpt("index-page-prefix").hasArg().create());
        
        // Clock cycles only options
        options.addOption(OptionBuilder.withLongOpt("with-offset").create());
        options.addOption(OptionBuilder.withLongOpt("include-packet-cycles").create());
        options.addOption(OptionBuilder.withLongOpt("exclude-event-cycles").create());
        options.addOption(OptionBuilder.withLongOpt("event-cycle-prefix").hasArg().create());
        options.addOption(OptionBuilder.withLongOpt("packet-cycle-prefix").hasArg().create());
        
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
                    this.cmdlineError("unknown trace input format \"" + type + "\"");
                }
            } else {
                this.cmdlineError("trace input format not specified");
            }
            
            // Trace output format
            if (cmd.hasOption("o")) {
                String type = cmd.getOptionValue("o");
                if (type.equals("ctf") || type.equals("json-ctf") || type.equals("cycles") || type.equals("html")) {
                    this.outputType = type;
                } else if (type.equals("babel") || type.equals("xml")) {
                    this.cmdlineError("trace output format \"" + type + "\" not supported yet");
                } else {
                    this.cmdlineError("unknown trace output format \"" + type + "\"");
                }
            }
            
            // Range
            if (cmd.hasOption("r")) {
                this.useRange = true;
                this.parseRange(cmd.getOptionValue("r"));
            }
            
            // Verbose
            this.verbose = cmd.hasOption("v");
            
            // Do not resize packets
            this.expertMode = cmd.hasOption("x");
            
            // JSON CTF output
            if (this.outputType.equals("json-ctf")) {
                this.embedMetadata = cmd.hasOption("embed-metadata");
                this.minimalSize = cmd.hasOption("minimal-size");
                this.indentWithTabs = cmd.hasOption("indent-with-tabs");
                this.addEnumLabel = cmd.hasOption("add-enum-label");
                this.addEventName = cmd.hasOption("add-event-name");
                this.addEventID = cmd.hasOption("add-event-id");
                this.addFloatValue = cmd.hasOption("add-float-value");
                if (cmd.hasOption("wp-per-level")) {
                    String strVal = cmd.getOptionValue("wp-per-level");
                    try {
                        this.wpPerLevel = Integer.parseInt(strVal);
                        if (this.wpPerLevel < 0 || this.wpPerLevel > 32) {
                            this.cmdlineError("value of wp-per-level option must be between 0 and 32");
                        }
                    } catch (NumberFormatException e) {
                        this.cmdlineError("cannot parse wp-per-level option (wrong value \"" + strVal + "\")");
                    }
                }
                if (cmd.hasOption("external-metadata")) {
                    this.externalMetadataFile = cmd.getOptionValue("external-metadata");
                    if (this.externalMetadataFile.contains("/")) {
                        this.cmdlineError("external metadata file must be in the same directory as the JSON file");
                    }
                    if (this.externalMetadataFile.equals(".") || this.externalMetadataFile.equals("..")) {
                        this.cmdlineError("invalid external metadata file \"" + this.externalMetadataFile + "\"");
                    }
                }
            }
            
            // HTML output
            if (this.outputType.equals("html")) {
                this.includeMetadataText = cmd.hasOption("include-metadata-text");
                this.disableAnimation = cmd.hasOption("disable-animation");
                this.disableIndex = cmd.hasOption("disable-index");
                if (cmd.hasOption("packet-page-prefix")) {
                    this.packetPagePrefix = cmd.getOptionValue("packet-page-prefix");
                }
                if (cmd.hasOption("index-page-prefix")) {
                    this.indexPagePrefix = cmd.getOptionValue("index-page-prefix");
                }
            }
            
            // Clock cycles only output
            if (this.outputType.equals("cycles")) {
                this.withOffset = cmd.hasOption("with-offset");
                this.includePacketCycles = cmd.hasOption("include-packet-cycles");
                this.excludeEventCycles = cmd.hasOption("exclude-event-cycles");
                if (cmd.hasOption("event-cycle-prefix")) {
                    this.evCyclePrefix = cmd.getOptionValue("event-cycle-prefix");
                }
                if (cmd.hasOption("packet-cycle-prefix")) {
                    this.pktCyclePrefix = cmd.getOptionValue("packet-cycle-prefix");
                }
            }
            
            // Paths
            String[] rem = cmd.getArgs();
            switch (rem.length) {
            case 0:
                this.cmdlineError("no input path provided");
                break;
                
            case 1:
                this.inputPath = new File(rem[0]);
                break;
                
            case 2:
                this.inputPath = new File(rem[0]);
                this.outputPath = new File(rem[1]);
                break;
                
            default:
                this.cmdlineError("too much path arguments provided");
            }
            
            // Input path
            if (!this.inputPath.exists()) {
                this.fileError("input path does not exist");
            }
            if (this.inputType.equals("ctf")) {
                if (!this.inputPath.isDirectory()) {
                    this.fileError("input path must be a directory with specified format");
                }
            }
            if (this.inputType.equals("json-ctf")) {
                if (!this.inputPath.isFile()) {
                    this.fileError("input path must be a file with specified format");
                }
            }
            
            // Output path
            if (this.outputPath != null) {
                if (this.outputType.equals("ctf") || this.outputType.equals("html")) {
                    if (!this.outputPath.exists()) {
                        this.fileError("output path does not exist");
                    }
                    if (!this.outputPath.isDirectory()) {
                        this.fileError("output path must be a directory with specified format");
                    }
                }
                if (this.outputType.equals("json-ctf") || this.outputType.equals("cycles")) {
                    if (this.outputPath.getParent() == null) {
                        this.fileError("wrong output path");
                    }
                    if (this.outputPath.exists() && this.outputPath.isDirectory()) {
                        this.fileError("wrong output path (pointing to an existing directory)");
                    }
                }
            }
        } catch (ParseException e) {
            this.cmdlineError("bad command line");
        }
    }

    @Override
    public void exec() {
        // Parse arguments
        this.parseArgs();
        
        // Reader
        IStreamedReader reader = null;
        if (this.inputType.equals("ctf")) {
            reader = new BinaryCTFStreamedReader(this.inputPath);
        } else if (this.inputType.equals("json-ctf")) {
            reader = new JSONCTFStreamedReader(this.inputPath);
        }
        
        // Writer
        IWriter writer = null;
        if (this.outputType.equals("ctf")) {
            File path = (this.outputPath == null) ? new File(System.getProperty("user.dir")) : this.outputPath;
            writer = new BinaryCTFWriter(path);
        } else if (this.outputType.equals("html")) {
            File path = (this.outputPath == null) ? new File(System.getProperty("user.dir")) : this.outputPath;
            HTMLWriter htmlWriter = new HTMLWriter(path);
            htmlWriter.setScriptFileName("master.js");
            htmlWriter.setStyleFileName("style.css");
            htmlWriter.setOuputIndex(!this.disableIndex);
            if (this.disableAnimation) {
                htmlWriter.setIncludeJquery(false);
            } else {
                htmlWriter.setIncludeJquery(true);
                String jsStr = Javeltrace.getResourceString("htmlJS.js");
                File jsPath = new File(path.getAbsolutePath() + File.separator + "master.js");
                Javeltrace.stringToFile(jsStr, jsPath);
            }
            String cssStr = Javeltrace.getResourceString("htmlCSS.css");
            File cssPath = new File(path.getAbsolutePath() + File.separator + "style.css");
            Javeltrace.stringToFile(cssStr, cssPath);
            htmlWriter.setIndexFileName(this.indexPagePrefix + ".htm");
            htmlWriter.setPacketFileNamePrefix(this.packetPagePrefix);
            htmlWriter.setOutputMetadataText(this.includeMetadataText);
            writer = htmlWriter;
        } else if (this.outputType.equals("json-ctf")) {
            JSONCTFWriter jsonWriter = null;
            if (this.outputPath == null) {
                jsonWriter = new JSONCTFWriter(System.out);
            } else {
                jsonWriter = new JSONCTFWriter(this.outputPath, !this.embedMetadata);
            }
            jsonWriter.setExternalMetadataName(this.externalMetadataFile);
            if (this.indentWithTabs) {
                jsonWriter.setIndentWithTabs();
            } else {
                jsonWriter.setIndentWithSpaces();
            }
            jsonWriter.setPretty(!this.minimalSize);
            jsonWriter.setSpacersPerIndentLevel(this.wpPerLevel);
            jsonWriter.setWriteEnumLabel(this.addEnumLabel);
            jsonWriter.setWriteEventName(this.addEventName);
            jsonWriter.setWriteEventID(this.addEventID);
            jsonWriter.setWriteFloatValue(this.addFloatValue);
            writer = jsonWriter;
        } else if (this.outputType.equals("cycles")) {
            CyclesOnlyWriter cyclesWriter = null;
            if (this.outputPath == null) {
                cyclesWriter = new CyclesOnlyWriter(System.out);
            } else {
                cyclesWriter = new CyclesOnlyWriter(this.outputPath);
            }
            cyclesWriter.setWithOffset(this.withOffset);
            cyclesWriter.setOutputPackets(this.includePacketCycles);
            cyclesWriter.setOutputEvents(!this.excludeEventCycles);
            cyclesWriter.setEventPrefix(this.evCyclePrefix);
            cyclesWriter.setPacketPrefix(this.pktCyclePrefix);
            writer = cyclesWriter;
        }

        try {
            // Input
            StreamedTraceInput input = new StreamedTraceInput(reader);
            if (this.verbose) {
                System.err.println("opening input trace...");
            }
            input.open();
            
            // Output
            BufferedTraceOutput output = new BufferedTraceOutput(input.getTraceParameters(), writer);
            output.setShrinkPacketSize(!this.expertMode);
            output.setWriteIfEmpty(this.expertMode);
            output.open();
            
            // Observer
            TranslationObserver observer = new TranslationObserver(this.verbose);
            
            // Translator
            Translator translator = new Translator(input, output);
            translator.setObserver(observer);
            
            // Range
            if (this.useRange) {
                translator.translate(this.rangeFrom, this.rangeTo);
            } else {
                translator.translate();
            }
            
            // Close input
            if (this.verbose) {
                System.err.println("closing input trace...");
            }
            input.close();
            
            // Close output
            if (this.verbose) {
                System.err.println("closing output trace...");
            }
            output.close();
        } catch (TraceInputException e) {
            Command.inputError(e.getMessage());
        } catch (TraceOutputException e) {
            Command.outputError(e.getMessage());
        } catch (Exception e) {
            Command.unexpectedError(e.getMessage());
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
