package ca.eepp.quatre.java.javeltrace.trace.input;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.eclipse.linuxtools.ctf.parser.TSDLLexer;
import org.eclipse.linuxtools.ctf.parser.TSDLParser;
import org.eclipse.linuxtools.ctf.parser.TSDLParser.parse_return;

import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.metadata.MetadataTreeAnalyzer;
import ca.eepp.quatre.java.javeltrace.trace.ex.WrongStateException;
import ca.eepp.quatre.java.javeltrace.trace.input.ex.TraceInputException;
import ca.eepp.quatre.java.javeltrace.trace.reader.IReader;
import ca.eepp.quatre.java.javeltrace.utils.TraceParametersMTAVisitor;

/**
 * An abstract trace input.
 * <p>
 * This abstract class specifies common methods and defines common implementation
 * for all trace input types. Known concrete trace inputs are
 * {@link StreamedTraceInput} and {@link RandomAccessTraceInput}.
 * <p>
 * The input uses a {@link IReader} to support various trace formats. Concrete
 * derived class use specific reader interfaces.
 * <p>
 * An internal state is kept as methods are called. For example, the trace
 * must be opened before trying any read operation and before getting the
 * trace parameters. If an operation is asked while the object is not in the
 * appropriate state, a {@link WrongStateException} exception is thrown.
 *
 * @author Philippe Proulx
 */
public abstract class TraceInput {
    private final TraceParameters params;
    protected final IReader reader;
    protected enum State {
        UNOPENED,
        OPENED,
        CLOSED
    };
    private State state = State.UNOPENED;
    protected static final String MSG_TRACE_NOT_OPENED = "trace is not opened";  //$NON-NLS-1$

    protected TraceInput(IReader reader) {
        this.reader = reader;
        this.params = new TraceParameters();
    }

    protected void checkState(State state, String errorMsg) throws WrongStateException {
        if (this.state != state) {
            throw new WrongStateException("Wrong trace state for this operation: " + errorMsg); //$NON-NLS-1$
        }
    }

    /**
     * Checks if this input is opened.
     *
     * @return  True if opened
     */
    public boolean isOpen() {
        return this.state == State.OPENED;
    }

    /**
     * Opens this input.
     *
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceInputException   If any reading error occurs
     */
    public void open() throws WrongStateException, TraceInputException {
        // Check state
        if (this.state == State.OPENED) {
            throw new WrongStateException("Wrong trace state for this operation: cannot reopen an opened trace"); //$NON-NLS-1$
        }

        // Open the trace
        this.reader.openTrace();

        // Get the metadata text
        this.params.metadataText = this.reader.getMetadataText();

        // Parse the metadata using a trace parameters MTA visitor
        try {
            this.parseMetadata();
        } catch (Exception e) {
            throw new TraceInputException("Cannot parse the metadata: " + e.getMessage()); //$NON-NLS-1$
        }

        // Ask the reader to open the streams
        this.reader.openStreams(this.params);

        // Update the state
        this.state = State.OPENED;
    }

    /**
     * Closes this input.
     *
     * @throws WrongStateException    If operation cannot be done in current state
     * @throws TraceInputException   If any reading error occurs
     */
    public void close() throws WrongStateException, TraceInputException {
        // Check state
        this.checkState(State.OPENED, TraceInput.MSG_TRACE_NOT_OPENED);

        // Close all streams
        this.reader.closeStreams();

        // Close trace
        this.reader.closeTrace();

        // Update state
        this.state = State.CLOSED;
    }

    private void parseMetadata() throws IOException, RecognitionException {
        // Metadata text -> reader
        Reader metadataTextInput = new StringReader(this.params.metadataText);

        // Create an ANTLR stream
        ANTLRReaderStream antlrStream;
        antlrStream = new ANTLRReaderStream(metadataTextInput);

        // Create the TSDL lexer
        TSDLLexer ctfLexer = new TSDLLexer(antlrStream);
        CommonTokenStream tokens = new CommonTokenStream(ctfLexer);

        // Create the TSDL parser
        TSDLParser ctfParser = new TSDLParser(tokens, false);
        parse_return ret;
        ret = ctfParser.parse();

        // Get the AST
        CommonTree tree = (CommonTree) ret.getTree();

        // Create an MTA visitor which will fill the trace parameters
        TraceParametersMTAVisitor visitor = new TraceParametersMTAVisitor(this.params);

        // Create the MTA with this visitor
        MetadataTreeAnalyzer mta = new MetadataTreeAnalyzer(tree, visitor);

        // Analyse the metadata
        mta.analyze();
    }

    /**
     * Gets the number of streams found in the metadata.
     *
     * @return  Number of streams found in the metadata
     * @throws WrongStateException    If operation cannot be done in current state
     */
    public int getNbStreams() throws WrongStateException {
        this.checkState(State.OPENED, TraceInput.MSG_TRACE_NOT_OPENED);

        return this.params.streams.size();
    }

    /**
     * Gets all stream IDs found in the metadata.
     *
     * @return  Array of all stream IDs found in the metadata
     * @throws WrongStateException    If operation cannot be done in current state
     */
    public ArrayList<Integer> getStreamsIDs() throws WrongStateException {
        this.checkState(State.OPENED, TraceInput.MSG_TRACE_NOT_OPENED);

        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Integer key : this.params.streams.keySet()) {
            ids.add(new Integer(key));
        }

        return ids;
    }

    /**
     * Gets the trace parameters read by this input.
     *
     * @return  Trace parameters
     * @throws WrongStateException    If operation cannot be done in current state
     */
    public TraceParameters getTraceParameters() throws WrongStateException {
        this.checkState(State.OPENED, TraceInput.MSG_TRACE_NOT_OPENED);

        return this.params;
    }
}
