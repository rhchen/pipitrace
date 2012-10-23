package ca.eepp.quatre.java.javeltrace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public final class Javeltrace {
    private static final String version = "0.21.2";
    
	public static void main(String[] args) {
        try {
            launchCommand(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
	}
	
	public static final String getResourceString(String name) {
	    String ret = "";
	    
	    String path = "/ca/eepp/quatre/java/javeltrace/resources/" + name;
	    InputStream stream = Javeltrace.class.getResourceAsStream(path);
        try {
            ret = new Scanner(stream).useDelimiter("\\A").next();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return ret;
	}
	
	public static final void stringToFile(String str, File file) {
	    try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"));
            osw.write(str);
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    
	private static void cmdVersion(boolean json) {
	    if (json) {
	        System.out.print("{\n");
	        System.out.print("  \"version\": \"" + Javeltrace.version + "\",\n");
	        System.out.print("  \"author\": {\n");
	        System.out.print("    \"name\": \"Philippe Proulx\",\n");
	        System.out.print("    \"email\": \"philippe.proulx@polymtl.ca\",\n");
	        System.out.print("    \"website\": \"http://4.eepp.ca/\"\n");
	        System.out.print("  }\n");
	        System.out.print("}\n");
	    } else {
	        System.out.print("Javeltrace v" + Javeltrace.version + "\n");
	        System.out.print("  by Philippe Proulx <philippe.proulx@polymtl.ca>\n");
	        System.out.print("  http://4.eepp.ca/\n");
	    }
	    
	    System.exit(0);
	}
	
    private static void launchCommand(String[] args) {
        // Command?
        if (args.length == 0) {
            globalUsage();
            System.exit(1);
        }
        
        // Parse command
        Command cmd = null;
        String[] rArgs = Arrays.copyOfRange(args, 1, args.length);
        if (args[0].equals("-h") || args[0].equals("--help")) {
            globalUsage();
            System.exit(0);
        } else if (args[0].equals("analyze")) {
            cmd = new CmdAnalyze(rArgs);
        } else if (args[0].equals("translate")) {
            cmd = new CmdTranslate(rArgs);
        } else if (args[0].equals("show-metadata")) {
            cmd = new CmdShowMetadata(rArgs);
        } else if (args[0].equals("version")) {
            boolean json = false;
            if (args.length > 1) {
                if (args[1].equals("--json")) {
                    json = true;
                }
            }
            cmdVersion(json);
        } else {
            System.err.print("Error: bad command name \"" + args[0] + "\".\n\n");
            globalUsage();
            System.exit(1);
        }
        
        // Need help?
        Options options = new Options();
        options.addOption("h", "help", false, "show help");
        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmdLine = parser.parse(options, rArgs);
            if (cmdLine.hasOption("h")) {
                System.out.print(cmd.usage());
                System.exit(0);
            } else {
                // Execute!
                cmd.exec();
            }
        } catch (ParseException e) {
            // We don't understand those arguments... execute!
            cmd.exec();
        }
    }
    
    private static void globalUsage() {
        System.out.print(
            "Usage: javeltrace COMMAND ...\n\n" +
            "Use\n" +
            "  javeltrace COMMAND -h\n" +
            "to get help for command COMMAND.\n\n" +
            "Commands:\n" +
            "  analyze          trace analysis\n" +
            "  show-metadata    CTF metadata text output\n" +
            "  translate        trace translation\n" +
            "  version          show version (use with --json for JSON output)\n"
        );
    }
}
