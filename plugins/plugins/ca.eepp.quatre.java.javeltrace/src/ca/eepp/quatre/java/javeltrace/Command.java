package ca.eepp.quatre.java.javeltrace;

public abstract class Command {
    protected String[] args;
    
    public Command(String[] args) {
        this.args = args;
    }
    
    private static void error(String msg, int exitCode) {
        System.err.print(msg);
        System.exit(exitCode);
    }
    
    protected static void cmdlineError(String msg) {
        Command.error("Command line error: " + msg + "\n", 1);
    }
    
    protected static void fileError(String msg) {
        Command.error("File error: " + msg + "\n", 2);
    }
    
    protected static void inputError(String msg) {
        Command.error("Input error: " + msg + "\n", 3);
    }
    
    protected static void outputError(String msg) {
        Command.error("Output error: " + msg + "\n", 4);
    }
    
    protected static void unexpectedError(String msg) {
        Command.error("Unexpected error: " + msg + "\n", 5);
    }
    
    public abstract String usage();
    public abstract void exec();
}
