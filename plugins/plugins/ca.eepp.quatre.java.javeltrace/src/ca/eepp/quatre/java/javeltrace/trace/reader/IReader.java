package ca.eepp.quatre.java.javeltrace.trace.reader;

import org.eclipse.linuxtools.ctf.core.trace.data.types.ArrayDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.EnumDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.FloatDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.SequenceDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StringDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.VariantDefinition;

import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.trace.reader.ex.ReaderException;

/**
 * Interface for reading a trace.
 * <p>
 * Nothing useful can be done with this interface, but it serves as a base
 * for the two real reader interfaces: {@link IStreamedReader}, for
 * streaming, and {@link IRandomAccessReader}, for random access (seeking)
 * within a trace.
 * <p>
 * Normal sequence of execution ordered by the reader owner is: open the
 * trace ({@link #openTrace()}), ask the reader for the metadata text
 * ({@link #getMetadataText()}), open the streams ({@link #openStreams(TraceParameters)}),
 * do the specific reading process (streaming or random access), close the
 * streams ({@link #closeStreams()}) and close the trace ({@link #closeTrace()}).
 *
 * @author Philippe Proulx
 */
public interface IReader {
    /**
     * Opens the trace for reading.
     *
     * @throws ReaderException   If there's any reading error
     * @see #closeTrace()
     */
    public void openTrace() throws ReaderException;

    /**
     * Closes the trace.
     *
     * @throws ReaderException   If there's any reading error
     * @see #openTrace()
     */
    public void closeTrace() throws ReaderException;

    /**
     * Gets the TSDL metadata text string from the trace.
     * <p>
     * Every format has its way of storing the metadata text. Sometimes, it
     * is not stored as plain text directly and must be decoded/decrypted in
     * order to get it as a full string.
     *
     * @return  Metadata full text string
     * @throws ReaderException   If there's any reading error
     */
    public String getMetadataText() throws ReaderException;

    /**
     * Opens all the trace streams.
     * <p>
     * Parameter <code>params</code> contains all the streams described
     * in the metadata text returned to the reader owner by
     * {@link #getMetadataText()}. A stream is identified by a unique integer
     * and associated to a stream descriptor thanks to the hash map.
     *
     * @param params    Trace parameters (filled by caller)
     * @throws ReaderException   If there's any reading error
     * @see #closeStreams()
     */
    public void openStreams(TraceParameters params) throws ReaderException;

    /**
     * Closes all the trace streams.
     *
     * @throws ReaderException   If there's any reading error
     * @see #openStreams(TraceParameters)
     */
    public void closeStreams() throws ReaderException;

    /**
     * Opens a structure for reading.
     * <p>
     * No need to browse the actual fields of the opened structure here since
     * the reading method of <code>StructDefinition</code> takes care of this.
     * This method only means: all following calls to type reading are within
     * this structure until {@link #closeStruct(StructDefinition, String)} is called for the same
     * level.
     * <p>
     * If <code>name</code> is null, then this definition has no parent
     * container and thus is a packet header, a packet context, an event
     * header, an event context or an event payload.
     *
     * @param def   Structure definition
     * @param name  Name of this definition within its container type
     * @throws ReaderException   If there's any reading error
     */
    public void openStruct(StructDefinition def, String name) throws ReaderException;

    /**
     * Closes a structure.
     * <p>
     * If <code>name</code> is null, then this definition has no parent
     * container and thus is a packet header, a packet context, an event
     * header, an event context or an event payload.
     *
     * @param def   Structure definition
     * @param name  Name of this definition within its container type
     * @throws ReaderException   If there's any reading error
     * @see #openStruct(StructDefinition, String)
     */
    public void closeStruct(StructDefinition def, String name) throws ReaderException;

    /**
     * Opens a variant for reading.
     * <p>
     * No need to read the current field of the opened variant here since
     * the reading method of <code>VariantDefinition</code> takes care of this.
     * This method only means: the following call to a type reading is within
     * this variant (will be followed by {@link #closeVariant(VariantDefinition, String)}).
     *
     * @param def   Variant definition
     * @param name  Name of this definition within its container type
     * @throws ReaderException   If there's any reading error
     */
    public void openVariant(VariantDefinition def, String name) throws ReaderException;

    /**
     * Closes a variant.
     *
     * @param def   Variant definition
     * @param name  Name of this definition within its container type
     * @throws ReaderException   If there's any reading error
     * @see #openVariant(VariantDefinition, String)
     */
    public void closeVariant(VariantDefinition def, String name) throws ReaderException;

    /**
     * Opens an array for reading.
     * <p>
     * No need to browse the actual fields of the opened array here since
     * the reading method of <code>ArrayDefinition</code> takes care of this.
     * This method only means: all following calls to type reading are within
     * this array until {@link #closeArray(ArrayDefinition, String)} is called for the same
     * level.
     *
     * @param def   Array definition
     * @param name  Name of this definition within its container type
     * @throws ReaderException   If there's any reading error
     */
    public void openArray(ArrayDefinition def, String name) throws ReaderException;

    /**
     * Closes an array.
     *
     * @param def   Array definition
     * @param name  Name of this definition within its container type
     * @throws ReaderException   If there's any reading error
     * @see #openArray(ArrayDefinition, String)
     */
    public void closeArray(ArrayDefinition def, String name) throws ReaderException;

    /**
     * Opens a sequence for reading.
     * <p>
     * No need to browse the actual fields of the opened sequence here since
     * the reading method of <code>SequenceDefinition</code> takes care of this.
     * This method only means: all following calls to type reading are within
     * this sequence until {@link #closeSequence(SequenceDefinition, String)} is called for the same
     * level.
     *
     * @param def   Sequence definition
     * @param name  Name of this definition within its container type
     * @throws ReaderException   If there's any reading error
     */
    public void openSequence(SequenceDefinition def, String name) throws ReaderException;

    /**
     * Closes a sequence.
     *
     * @param def   Sequence definition
     * @param name  Name of this definition within its container type
     * @throws ReaderException   If there's any reading error
     * @see #openSequence(SequenceDefinition, String)
     */
    public void closeSequence(SequenceDefinition def, String name) throws ReaderException;

    /**
     * Reads a plain integer number.
     *
     * @param def   Integer definition
     * @param name  Name of this definition within its container type
     * @throws ReaderException   If there's any reading error
     */
    public void readInteger(IntegerDefinition def, String name) throws ReaderException;

    /**
     * Reads a plain floating point number.
     *
     * @param def   Floating point definition
     * @param name  Name of this definition within its container type
     * @throws ReaderException   If there's any reading error
     */
    public void readFloat(FloatDefinition def, String name) throws ReaderException;

    /**
     * Reads a plain enumeration.
     * <p>
     * The reader must take care to read the underlying integer definition
     * before returning from this call so that the label is set accordingly
     * by the enumeration definition.
     *
     * @param def   Enumeration definition
     * @param name  Name of this definition within its container type
     * @throws ReaderException   If there's any reading error
     */
    public void readEnum(EnumDefinition def, String name) throws ReaderException;

    /**
     * Reads a plain string.
     *
     * @param def   String definition
     * @param name  Name of this definition within its container type
     * @throws ReaderException   If there's any reading error
     */
    public void readString(StringDefinition def, String name) throws ReaderException;
}
