package ca.eepp.quatre.java.javeltrace.trace.writer;

import org.eclipse.linuxtools.ctf.core.trace.data.types.ArrayDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.EnumDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.FloatDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.SequenceDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StringDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDefinition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.VariantDefinition;

import ca.eepp.quatre.java.javeltrace.TraceParameters;
import ca.eepp.quatre.java.javeltrace.trace.Event;
import ca.eepp.quatre.java.javeltrace.trace.PacketInfo;
import ca.eepp.quatre.java.javeltrace.trace.writer.ex.WriterException;

/**
 * Interface for writing a complete trace.
 * <p>
 * Methods of this interface are called in a specific order, which is: first
 * open the trace ({@link #openTrace()}), set the trace parameters
 * ({@link #setTraceParameters(TraceParameters)}), ask to write the metadata
 * (<code>writeMetadata</code>), open the streams ({@link #openStream(int)})
 * and start the writing process. The writing process is always: open a packet
 * ({@link #openPacket(PacketInfo)}), then write all its events (no call to
 * <code>openPacket</code> in between) and then close the packet
 * ({@link #closePacket(PacketInfo)}). After this writing process, and before calling
 * {@link #closeTrace()}, streams are closed by calling
 * {@link #closeStream(int)}.
 * <p>
 * It is up to the writer to implement how an event is written. In other words,
 * type writing methods are never called by the writer owner: the writer calls
 * them on specific types. Then, some compound types have recursive calls on
 * their content (e.g. structures and arrays). It is then possible for a writer
 * to skip parts of an event in order to achieve a specific purpose. For
 * example, a writer could skip the events payloads and only dump the header
 * values to a text file.
 *
 * @author Philippe Proulx
 */
public interface IWriter {
    /**
     * Opens the trace for writing.
     * <p>
     * Some parameters can be useful for output purposes (e.g. byte order).
     * <p>
     * The writer must not modify the parameters since they are probably shared
     * by other components within the application.
     *
     * @throws WriterException   If there's any writing error
     * @see #closeTrace()
     */
    public void openTrace(TraceParameters params) throws WriterException;

    /**
     * Closes the trace.
     * <p>
     * All resources opened to write the trace must be closed after this
     * method returns.
     *
     * @throws WriterException   If there's any writing error
     * @see #openTrace()
     */
    public void closeTrace() throws WriterException;

    /**
     * Opens the given stream ID.
     *
     * @param id    Stream ID
     * @throws WriterException   If there's any wiring error
     * @see #closeStream(int)
     */
    public void openStream(int id) throws WriterException;

    /**
     * Closes the given stream ID.
     *
     * @param id    Stream ID
     * @throws WriterException   If there's any writing error
     * @see #openStream(int)
     */
    public void closeStream(int id) throws WriterException;

    /**
     * Opens a packet for writing.
     * <p>
     * The <code>packet</code> parameter serves as an identifier for the
     * packet to be written. It contains the header and context of the packet
     * to write. The stream ID of a packet can also be retrived from this
     * object.
     * <p>
     * Once this method is called, it won't be called again before the writer
     * gets a call to {@link #closePacket(PacketInfo)}, which ends the writing of the
     * packet. Between those two calls, only {@link #writeEvent(Event)} is to be
     * called by the writer owner.
     *
     * @param packet    Packet information
     * @throws WriterException   If there's any writing error
     */
    public void openPacket(PacketInfo packet) throws WriterException;

    /**
     * Closes a packet.
     *
     * @param packet    Packet information
     * @throws WriterException   If there's any writing error
     * @see #openPacket(PacketInfo)
     */
    public void closePacket(PacketInfo packet) throws WriterException;

    /**
     * Writes an event.
     *
     * @param ev        Complete event data
     * @throws WriterException   If there's any writing error
     */
    public void writeEvent(Event ev) throws WriterException;

    /**
     * Opens a structure for writing.
     * <p>
     * No need to browse the actual fields of the opened structure here since
     * the writing method of <code>StructDefinition</code> takes care of this.
     * This method only means: all following calls to type writing are within
     * this structure until {@link #closeStruct(StructDefinition, String)} is called for the same
     * level.
     * <p>
     * If <code>name</code> is null, then this definition has no parent
     * container and thus is a packet header, a packet context, an event
     * header, an event context or an event payload.
     *
     * @param def   Structure definition
     * @param name  Name of this definition within its container type
     * @throws WriterException   If there's any writing error
     */
    public void openStruct(StructDefinition def, String name) throws WriterException;

    /**
     * Closes a structure.
     * <p>
     * If <code>name</code> is null, then this definition has no parent
     * container and thus is a packet header, a packet context, an event
     * header, an event context or an event payload.
     *
     * @param def   Structure definition
     * @param name  Name of this definition within its container type
     * @throws WriterException   If there's any writing error
     * @see #openStruct(StructDefinition, String)
     */
    public void closeStruct(StructDefinition def, String name) throws WriterException;

    /**
     * Opens a variant for writing.
     * <p>
     * No need to manually write the current field of the opened variant here
     * since the writing method of <code>VariantDefinition</code> takes care of
     * this. This method only means: the following call to a type writing is
     * within this variant (will be followed by {@link #closeVariant(VariantDefinition, String)}).
     *
     * @param def   Variant definition
     * @param name  Name of this definition within its container type
     * @throws WriterException   If there's any writing error
     */
    public void openVariant(VariantDefinition def, String name) throws WriterException;

    /**
     * Closes a variant.
     *
     * @param def   Variant definition
     * @param name  Name of this definition within its container type
     * @throws WriterException   If there's any writing error
     * @see #openVariant(VariantDefinition, String)
     */
    public void closeVariant(VariantDefinition def, String name) throws WriterException;

    /**
     * Opens an array for writing.
     * <p>
     * No need to browse the actual definitions of the opened array here since
     * the writing method of <code>ArrayDefinition</code> takes care of this.
     * This method only means: all following calls to type writing are within
     * this array until {@link #closeArray(ArrayDefinition, String)} is called for the same
     * level.
     *
     * @param def   Array definition
     * @param name  Name of this definition within its container type
     * @throws WriterException   If there's any writing error
     */
    public void openArray(ArrayDefinition def, String name) throws WriterException;

    /**
     * Closes an array.
     *
     * @param def   Array definition
     * @param name  Name of this definition within its container type
     * @throws WriterException   If there's any writing error
     * @see #openArray(ArrayDefinition, String)
     */
    public void closeArray(ArrayDefinition def, String name) throws WriterException;

    /**
     * Opens a sequence for writing.
     * <p>
     * No need to browse the actual definitions of the opened sequence here since
     * the writing method of <code>SequenceDefinition</code> takes care of this.
     * This method only means: all following calls to type writing are within
     * this array until {@link #closeSequence(SequenceDefinition, String)} is called for the same
     * level.
     *
     * @param def   Sequence definition
     * @param name  Name of this definition within its container type
     * @throws WriterException   If there's any writing error
     */
    public void openSequence(SequenceDefinition def, String name) throws WriterException;

    /**
     * Closes a sequence.
     *
     * @param def   Sequence definition
     * @param name  Name of this definition within its container type
     * @throws WriterException   If there's any writing error
     * @see #openSequence(SequenceDefinition, String)
     */
    public void closeSequence(SequenceDefinition def, String name) throws WriterException;

    /**
     * Writes a plain integer number.
     *
     * @param def   Integer definition
     * @param name  Name of this definition within its container type
     * @throws WriterException   If there's any writing error
     */
    public void writeInteger(IntegerDefinition def, String name) throws WriterException;

    /**
     * Writes a plain floating point number.
     *
     * @param def   Floating point definition
     * @param name  Name of this definition within its container type
     * @throws WriterException   If there's any writing error
     */
    public void writeFloat(FloatDefinition def, String name) throws WriterException;

    /**
     * Writes a plain enumeration.
     * <p>
     * The writer must take care of writing what it needs from the enumeration
     * here (either the integer value, the label string, or both).
     *
     * @param def   Enumeration definition
     * @param name  Name of this definition within its container type
     * @throws WriterException   If there's any writing error
     */
    public void writeEnum(EnumDefinition def, String name) throws WriterException;

    /**
     * Writes a plain string.
     *
     * @param def   String definition
     * @param name  Name of this definition within its container type
     * @throws WriterException   If there's any writing error
     */
    public void writeString(StringDefinition def, String name) throws WriterException;
}
