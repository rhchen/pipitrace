package ca.eepp.quatre.java.javeltrace;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import ca.eepp.quatre.java.javeltrace.trace.input.StreamedTraceInput;
import ca.eepp.quatre.java.javeltrace.trace.input.ex.TraceInputException;
import ca.eepp.quatre.java.javeltrace.trace.reader.BinaryCTFStreamedReader;
import ca.eepp.quatre.java.javeltrace.trace.reader.IStreamedReader;
import ca.eepp.quatre.java.javeltrace.trace.reader.JSONCTFStreamedReader;

public class CmdShowMetadata extends Command {
    private String inputType;
    private File inputPath;
    
    public CmdShowMetadata(String[] args) {
        super(args);
    }

    @Override
    public String usage() {
        return  
            "Usage: javeltrace show-metadata [OPTION]... INPUTF\n\n" +
            "Shows the text TSDL metadata of a trace.\n\n" +
            "Reads trace INPUTF (file or directory according to the input format) and\n" +
            "outputs its text metadata content to standard output.\n\n" +
            "Options:\n" +
            "  -h, --help   show this help\n" +
            "  -i TYPE      set trace input format to TYPE (required)\n\n" +
            "Valid trace input types:\n" +
            "  ctf          CTF v1.8                (dir)\n" +
            "  json-ctf     JSON CTF v1.8           (file)\n";
    }
    
    private void parseArgs() {
        // Options
        Options options = new Options();
        options.addOption("i", true, "trace input format");
        
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

    @Override
    public void exec() {
        // Parse arguments
        this.parseArgs();
        
        // Choose the reader
        IStreamedReader reader = null;
        if (this.inputType.equals("ctf")) {
            reader = new BinaryCTFStreamedReader(this.inputPath);
        } else if (this.inputType.equals("json-ctf")) {
            reader = new JSONCTFStreamedReader(this.inputPath);
        }
        
        // Trace input
        StreamedTraceInput input = new StreamedTraceInput(reader);
        
        try {
            // Open the input
            input.open();
            
            // Print out the metadata text
            System.out.print(input.getTraceParameters().metadataText);
            
            // Close the input
            input.close();
        } catch (TraceInputException e) {
            Command.inputError("input error: " + e.getMessage());
        } catch (Exception e) {
            Command.inputError("unexpected exception: " + e.getMessage());
        }
    }
}
