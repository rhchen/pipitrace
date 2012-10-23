package ca.eepp.quatre.java.javeltrace.metadata;

import java.nio.ByteOrder;
import java.util.UUID;

import org.antlr.runtime.tree.CommonTree;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDeclaration;

/**
 * A visitor for the metadata tree analyzer.
 * <p>
 * As the metadata AST is traversed and analyzed by {@link MetadataTreeAnalyzer}
 * (MTA) methods, its visitor (a class implementing this interface) is called at various
 * places. Some application wanting to get a high-level analysis out of a metadata
 * text only needs to implement such a visitor. 
 * 
 * @author Philippe Proulx
 */
public interface IMetadataTreeAnalyzerVisitor {
    /**
     * Adds a clock.
     * <p>
     * Many clocks may exist with different names. The MTA doesn't check for
     * multiple clocks defined with the same name.
     * 
     * @param name          Name
     * @param uuid          UUID
     * @param description   Description
     * @param freq          Frequency
     * @param offset        Offset (cycles @ clock frequency)
     */
    public void addClock(String name, UUID uuid, String description, Long freq, Long offset);
    
    /**
     * Adds an environment string property.
     * <p>
     * The MTA doesn't check for multiple properties defined with the same name.
     * 
     * @param key   Environment key
     * @param value String value
     */
    public void addEnvProperty(String key, String value);
    
    /**
     * Adds an environment long property.
     * <p>
     * The MTA doesn't check for multiple properties defined with the same name.
     * 
     * @param key   Environment key
     * @param value Long value
     */
    public void addEnvProperty(String key, Long value);
    
    /**
     * Sets the trace informations.
     * <p>
     * The MTA checks for multiple definitions of the "trace" block so this
     * method will be called only once.
     * 
     * @param major             Major number
     * @param minor             Minor number
     * @param uuid              UUID
     * @param bo                Byte order
     * @param packetHeaderDecl  Trace-wise packet header declaration
     */
    public void setTrace(Integer major, Integer minor, UUID uuid,
            ByteOrder bo, StructDeclaration packetHeaderDecl);
    
    /**
     * Adds a stream.
     * <p>
     * Many streams may exist with different IDs. The MTA doesn't check for
     * multiple streams defined with the same ID.
     * 
     * @param id                Stream ID
     * @param eventHeaderDecl   Event header declaration
     * @param eventContextDecl  Per-stream event context declaration
     * @param packetContextDecl Packet context declaration
     */
    public void addStream(Integer id, StructDeclaration eventHeaderDecl,
            StructDeclaration eventContextDecl, StructDeclaration packetContextDecl);
    
    /**
     * Adds an event.
     * <p>
     * Many events may exist with different IDs and names within a given
     * stream. The MTA doesn't check for multiple events defined with the same
     * ID/name for a given stream.
     * 
     * @param id        ID
     * @param streamID  Stream ID which the event belongs to
     * @param name      Name
     * @param context   Per-event context declaration
     * @param fields    Fields (aka payload) declaration
     * @param logLevel  Log level for this event
     */
    public void addEvent(Integer id, Integer streamID, String name, StructDeclaration context,
            StructDeclaration fields, Integer logLevel);
    
    /**
     * Returns the global byte order.
     * <p>
     * This method is called by the MTA and must return the current global
     * trace byte order.
     * 
     * @return  Global byte order
     * @see #setGlobalByteOrder(ByteOrder)
     */
    public ByteOrder getGlobalByteOrder();
    
    /**
     * Sets the global byte order.
     * <p>
     * The global byte order must be kept in order to return it to the MTA
     * using {@link #getGlobalByteOrder()}.
     */
    public void setGlobalByteOrder(ByteOrder bo);
    
    /*
     * Following methods are only called by the MTA for informative purposes. They
     * can give an insight about where the MTA is currently at and more detailed
     * information can be obtained from the passed AST node.
     */
    
    /**
     * Information: parsing the root level.
     * 
     * @param node  Root node
     */
    public void infoParsingRoot(CommonTree node);
    
    /**
     * Information: parsing an environment block.
     * 
     * @param node  Environment node
     */
    public void infoParsingEnv(CommonTree node);
    
    /**
     * Information: parsing the root level.
     * 
     * @param node  Root node
     */
    public void infoParsingClock(CommonTree node);
    
    /**
     * Information: parsing a trace block.
     * 
     * @param node  Trace node
     */
    public void infoParsingTrace(CommonTree node);
    
    /**
     * Information: parsing a stream block.
     * 
     * @param node  Stream node
     */
    public void infoParsingStream(CommonTree node);
    
    /**
     * Information: parsing an event block
     * 
     * @param node  Event node
     */
    public void infoParsingEvent(CommonTree node);
    
    /**
     * Information: parsing a type definition.
     * 
     * @param node  Type definition node
     */
    public void infoParsingTypedef(CommonTree node);
    
    /**
     * Information: parsing a type alias.
     * 
     * @param node  Type alias node
     */
    public void infoParsingTypealias(CommonTree node);
    
    /**
     * Information: parsing a floating point number declaration.
     * 
     * @param node  Floating point number declaration node
     */
    public void infoParsingFloat(CommonTree node);
    
    /**
     * Information: parsing an integer number declaration.
     * 
     * @param node  Integer number declaration node
     */
    public void infoParsingInteger(CommonTree node);
    
    /**
     * Information: parsing a string declaration.
     * 
     * @param node  String declaration node
     */
    public void infoParsingString(CommonTree node);
    
    /**
     * Information: parsing an enumeration declaration.
     * 
     * @param node  Enumeration declaration node
     */
    public void infoParsingEnum(CommonTree node);
    
    /**
     * Information: parsing a structure declaration.
     * 
     * @param node  Structure declaration node
     */
    public void infoParsingStruct(CommonTree node);
    
    /**
     * Information: parsing a variant declaration.
     * 
     * @param node  Variant declaration node
     */
    public void infoParsingVariant(CommonTree node);
    
    /**
     * Information: pushed the declaration scope.
     * 
     * @param scope Current scope (after push)
     */
    public void infoPushedScope(DeclarationScope scope);
    
    /**
     * Information: poped the declaration scope.
     * 
     * @param scope Current scope (after pop)
     */
    public void infoPopedScope(DeclarationScope scope);
}
