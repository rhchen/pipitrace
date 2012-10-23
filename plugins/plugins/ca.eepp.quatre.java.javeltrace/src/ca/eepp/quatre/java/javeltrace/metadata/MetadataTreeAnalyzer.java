/*******************************************************************************
 * Copyright (c) 2011-2012 Ericsson, Ecole Polytechnique de Montreal and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Matthew Khouzam - Initial Design and Grammar
 *               Simon Marchi - Most of this file
 *               Philippe Proulx - Redesign, simplification and decoupling
 *******************************************************************************/

package ca.eepp.quatre.java.javeltrace.metadata;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.antlr.runtime.tree.CommonTree;
import org.eclipse.linuxtools.ctf.core.trace.data.types.ArrayDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.Encoding;
import org.eclipse.linuxtools.ctf.core.trace.data.types.EnumDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.FloatDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.SequenceDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StringDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.VariantDeclaration;
import org.eclipse.linuxtools.ctf.parser.TSDLParser;

import ca.eepp.quatre.java.javeltrace.metadata.ex.ParsingException;
import ca.eepp.quatre.java.javeltrace.metadata.strings.MetadataStrings;
import ca.eepp.quatre.java.javeltrace.utils.Utils;

/**
 * Metadata abstract syntax tree analyzer.
 * <p> 
 * The metadata tree analyzer is what usually follows the parsing of the
 * TSDL-based CTF metadata text. The parser returns a complete AST for the given
 * metadata text. However, this tree is not that useful on its own and needs
 * further analysis. This class traverses a given tree and looks for known
 * nodes in order to notify a given visitor about its content. With only one
 * public method (except for the constructor), its use is trivial as long as
 * a useful visitor is provided.
 * 
 * @author Simon Marchi
 * @author Matthew Khouzam
 * @author Philippe Proulx
 */
@SuppressWarnings({"unchecked", "nls"})
public class MetadataTreeAnalyzer {
    private final CommonTree tree;
    private DeclarationScope scope = null;
    private final IMetadataTreeAnalyzerVisitor visitor;
    
    /**
     * Builds a metadata tree analyzer.
     * <p>
     * The visitor <code>visitor</code> will be notified about what the analyzer finds
     * (e.g. clocks, environment properties, streams, events). This visitor
     * is also notified about where the analyzer currently is doing (parsing
     * a stream node, an event node, a type declaration, pushing a declaration,
     * etc.).
     * 
     * @param tree      Previously generated metadata AST
     * @param visitor   Visitor which will be notified
     */
    public MetadataTreeAnalyzer(CommonTree tree, IMetadataTreeAnalyzerVisitor visitor) {
        if (tree == null || visitor == null) {
            throw new IllegalArgumentException("Constructor arguments cannot be null");
        }
        this.tree = tree;
        this.visitor = visitor;
    }
    
    /**
     * Starts the analysis process.
     * 
     * @throws ParsingException If there's any analysis error
     */
    public void analyze() throws ParsingException {
        this.scope = null;
        this.parseRoot(this.tree);
    }
    
    private void parseRoot(CommonTree root) throws ParsingException {
        assert(root.getType() == TSDLParser.ROOT);

        // Visiting info
        this.visitor.infoParsingRoot(root);

        /*
         * What we're going to check. Those lists are temporary buffers
         * containing nodes to visit afterwards because the order matters here. 
         */
        CommonTree traceNode = null;
        List<CommonTree> streamsList = new ArrayList<CommonTree>();
        List<CommonTree> eventsList = new ArrayList<CommonTree>();
        List<CommonTree> declList = new ArrayList<CommonTree>();
        List<CommonTree> envList = new ArrayList<CommonTree>();
        List<CommonTree> clocksList = new ArrayList<CommonTree>();
        
        // Create a new declaration scope with no parent
        this.pushScope();

        // Parse everything within the root scope
        List<CommonTree> children = root.getChildren();
        assert(children != null);
        for (CommonTree child : children) {
            switch (child.getType()) {
            case TSDLParser.DECLARATION:
                declList.add(child);
                break;
                
            case TSDLParser.TRACE:
                if (traceNode != null) {
                    MetadataTreeAnalyzer.errorWithLine("Only one \"trace\" block is allowed", child);
                }
                traceNode = child;
                break;
                
            case TSDLParser.STREAM:
                streamsList.add(child);
                break;
                
            case TSDLParser.EVENT:
                eventsList.add(child);
                break;
                
            case TSDLParser.CLOCK:
                clocksList.add(child);
                break;
                
            case TSDLParser.ENV:
                envList.add(child);
                break;
                
            default:
                MetadataTreeAnalyzer.childTypeError(child);
            }
        }

        // Environments
        for (CommonTree environment : envList) {
            this.parseEnvironment(environment);
        }
        
        // Clocks
        for (CommonTree clock : clocksList) {
            this.parseClock(clock);
        }
        
        /*
         * Here we want to get "trace.byte_order" before getting anything else
         * since this is the required global ("architecture native") endianness.
         * To do this, we go to "trace" and get the "byte_order" field value and
         * tell our visitor what it is. Then it is safe to analyze the root-level
         * declarations (which might not have any "byte_order" field, in which
         * case we use the "trace.byte_order" one), and then the "trace" node,
         * which might use previous declarations. This way of analyzing solves the
         * circular dependency problem.
         */
        if (traceNode == null) {
            throw new ParsingException("Missing \"trace\" block");
        }
        this.parseTraceByteOrder(traceNode);
        
        // Root-level declarations
        for (CommonTree decl : declList) {
            this.parseRootDeclaration(decl);
        }
        
        // Trace node
        this.parseTrace(traceNode);
        
        // Streams
        if (streamsList.size() > 0) {
            for (CommonTree stream : streamsList) {
                this.parseStream(stream);
            }
        } else {
            // No stream! Oh noes.
            throw new ParsingException("No \"stream\" specifier found at root level");
        }

        // Events
        for (CommonTree event : eventsList) {
            this.parseEvent(event);
        }
        
        // Pop scope
        this.popScope();
    }
    
    private void parseEnvironment(CommonTree environment) {        
        // Visiting info
        this.visitor.infoParsingEnv(environment);
        
        // Browse all parameters
        final List<CommonTree> children = environment.getChildren();
        for (CommonTree child : children) {
            // Environment key
            final String key = child.getChild(0).getChild(0).getChild(0).getText();
            
            // Value type and string 
            final int valueType = child.getChild(1).getChild(0).getType();
            final String valueStr = child.getChild(1).getChild(0).getChild(0).getText();
            
            // Depending on the type, add the property to our current environment
            switch (valueType) {
            case TSDLParser.UNARY_EXPRESSION_STRING_QUOTES:
                this.visitor.addEnvProperty(key, Utils.removeQuotesFromString(valueStr));
                break;
                
            case TSDLParser.UNARY_EXPRESSION_DEC:
            case TSDLParser.UNARY_EXPRESSION_HEX:
            case TSDLParser.UNARY_EXPRESSION_OCT:
                // Long.decode() understands all this
                this.visitor.addEnvProperty(key, Long.decode(valueStr));
                break;
           
            default:
                MetadataTreeAnalyzer.errorWithLine("Wrong RHS expression within an \"env\" block", child);
            }
        }
    }

    private void parseClock(CommonTree clock) {
        final List<CommonTree> children = clock.getChildren();
        
        // Visiting info
        this.visitor.infoParsingClock(clock);
        
        // Clock attributes
        String attrName = null;
        UUID attrUUID = null;
        String attrDescription = null;
        Long attrFreq = null;
        Long attrOffset = null;
        
        // Browse attributes
        for (CommonTree child : children) {
            // Key/value and type
            final String key = child.getChild(0).getChild(0).getChild(0).getText();
            final CommonTree value = (CommonTree) child.getChild(1).getChild(0).getChild(0);
            final int type = value.getType();
            
            // Integer and string values
            Long numValue = 0L;
            String strValue = null;
            
            // According to type
            switch (type) {
            case TSDLParser.INTEGER:
            case TSDLParser.DECIMAL_LITERAL:
                /*
                 * Not a pretty hack, this is to make sure that there is no number
                 * overflow due to 63 bit integers. The offset should only really
                 * be an issue in the year 2262. the tracer in C/ASM can write an offset in
                 * an unsigned 64 bit long. In java, the last bit, being set to 1 will
                 * be read as a negative number, but since it is too big a positive it will
                 * throw an exception. this will happen in 2^63 ns from 1970.
                 * Therefore 293 years from 1970.
                 */
                try {
                    numValue = Long.parseLong(value.getText());
                } catch( Exception e) {
                    numValue = 1330938566783103277L;
                }
                break;
            
            default:
                strValue = value.getText();
            }
            
            // According to key
            if (key.equals(MetadataStrings.CLOCK_NAME)) {
                if (attrName != null) {
                    MetadataTreeAnalyzer.definedMultipleTimesError("Clock", MetadataStrings.CLOCK_NAME, child);
                }
                attrName = new String(strValue);
            } else if (key.equals(MetadataStrings.CLOCK_UUID)) {
                if (attrUUID != null) {
                    MetadataTreeAnalyzer.definedMultipleTimesError("Clock", MetadataStrings.CLOCK_UUID, child);
                }
                String uuidNoQuotes = Utils.removeQuotesFromString(strValue);
                attrUUID = UUID.fromString(uuidNoQuotes);
            } else if (key.equals(MetadataStrings.CLOCK_DESC)) {
                if (attrDescription != null) {
                    MetadataTreeAnalyzer.definedMultipleTimesError("Clock", MetadataStrings.CLOCK_DESC, child);
                }
                attrDescription = Utils.removeQuotesFromString(strValue);
            } else if (key.equals(MetadataStrings.CLOCK_FREQ)) {
                if (attrFreq != null) {
                    MetadataTreeAnalyzer.definedMultipleTimesError("Clock", MetadataStrings.CLOCK_FREQ, child);
                }
                attrFreq = new Long(numValue);
            } else if (key.equals(MetadataStrings.CLOCK_OFFSET)) {
                if (attrOffset != null) {
                    MetadataTreeAnalyzer.definedMultipleTimesError("Clock", MetadataStrings.CLOCK_OFFSET, child);
                }
                attrOffset = new Long(numValue);
            } else {
                MetadataTreeAnalyzer.errorWithLine("Unknown attribute \"" + key + "\" for block \"clock\"", child);
            }
        }
        
        // TODO: more validation: do we have everything we need?
        if (attrName == null) {
            MetadataTreeAnalyzer.errorWithLine("A \"clock\" block must have a \"" + MetadataStrings.CLOCK_NAME + "\" attribute", clock);
        }
        
        // Add this clock descriptor
        this.visitor.addClock(attrName, attrUUID, attrDescription, attrFreq, attrOffset);
    }

    private void parseTraceByteOrder(CommonTree traceNode) throws ParsingException {
        assert(traceNode.getType() == TSDLParser.TRACE);
        boolean found = false;
        
        // Get children
        List<CommonTree> children = traceNode.getChildren();
        if (children == null) {
            MetadataTreeAnalyzer.errorWithLine("Empty \"trace\" block", traceNode);
        } else { 
            for (CommonTree child : children) {
                if (found) {
                    break;
                }
                
                // Byte order is one of:
                if (child.getType() == TSDLParser.CTF_EXPRESSION_TYPE || child.getType() == TSDLParser.CTF_EXPRESSION_VAL) {
                    assert(child.getChildCount() == 2);
                    
                    // Get left/right
                    CommonTree leftNode = (CommonTree) child.getChild(0);
                    assert(leftNode.getType() == TSDLParser.CTF_LEFT);
                    CommonTree rightNode = (CommonTree) child.getChild(1);
                    assert(rightNode.getType() == TSDLParser.CTF_RIGHT);
                    
                    // Get assignment key
                    List<CommonTree> leftStrings = leftNode.getChildren();
                    assert(leftStrings != null);
                    if (!MetadataTreeAnalyzer.isUnaryString(leftStrings.get(0))) {
                        MetadataTreeAnalyzer.errorWithLine("LHS of TSDL assignment must be a string", leftNode);
                    }
                    String left = MetadataTreeAnalyzer.concatenateUnaryStrings(leftStrings);
                    if (left.equals(MetadataStrings.BYTE_ORDER)) {
                        this.visitor.setGlobalByteOrder(MetadataTreeAnalyzer.getByteOrder(rightNode, false, false));
                        found = true;
                    }
                }
            }
        }
        
        // Found?
        if (!found) {
            MetadataTreeAnalyzer.errorWithLine("Cannot find \"trace.byte_order\"", traceNode);
        }
    }
    
    private void parseTrace(CommonTree traceNode) throws ParsingException {
        assert(traceNode.getType() == TSDLParser.TRACE);

        // Visiting info
        this.visitor.infoParsingTrace(traceNode);
        
        // Push current scope
        this.pushScope();

        // Parameters we want to set
        MetadataTreeAnalyzer.Trace trace = new MetadataTreeAnalyzer.Trace();
        
        // Browse content
        List<CommonTree> children = traceNode.getChildren();
        if (children == null) {
            MetadataTreeAnalyzer.errorWithLine("Empty \"trace\" block", traceNode);
        } else {
            for (CommonTree child : children) {
                switch (child.getType()) {
                case TSDLParser.TYPEALIAS:
                    this.parseTypealias(child);
                    break;
                    
                case TSDLParser.TYPEDEF:
                    this.parseTypedef(child);
                    break;
                    
                case TSDLParser.CTF_EXPRESSION_TYPE:
                case TSDLParser.CTF_EXPRESSION_VAL:
                    this.parseTraceDeclaration(child, trace);
                    break;
                    
                default:
                    MetadataTreeAnalyzer.childTypeError(child);
                    break;
                }
            }
            
            // Set trace
            this.visitor.setTrace(trace.major, trace.minor, trace.uuid, trace.bo, trace.packetHeader);
        }

        // Pop scope
        this.popScope();
    }

    private void parseTraceDeclaration(CommonTree traceDecl, MetadataTreeAnalyzer.Trace trace)
            throws ParsingException {
        assert((traceDecl.getType() == TSDLParser.CTF_EXPRESSION_TYPE) ||
                (traceDecl.getType() == TSDLParser.CTF_EXPRESSION_VAL));

        // There should be a left and right
        assert(traceDecl.getChildCount() == 2);

        // Get left/right
        CommonTree leftNode = (CommonTree) traceDecl.getChild(0);
        assert(leftNode.getType() == TSDLParser.CTF_LEFT);
        CommonTree rightNode = (CommonTree) traceDecl.getChild(1);
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);

        // Get assignment key
        List<CommonTree> leftStrings = leftNode.getChildren();
        assert(leftStrings != null);
        if (!MetadataTreeAnalyzer.isUnaryString(leftStrings.get(0))) {
            MetadataTreeAnalyzer.errorWithLine("LHS of TSDL assignment must be a string", leftNode);
        }
        String left = MetadataTreeAnalyzer.concatenateUnaryStrings(leftStrings);
        
        // According to the key
        if (left.equals(MetadataStrings.MAJOR)) {
            if (trace.major != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Trace", MetadataStrings.MAJOR, leftNode);
            }
            
            // Set major number
            trace.major = MetadataTreeAnalyzer.getMajorOrMinor(rightNode);
        } else if (left.equals(MetadataStrings.MINOR)) {
            if (trace.minor != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Trace", MetadataStrings.MINOR, leftNode);
            }
            
            // Set minor number
            trace.minor = MetadataTreeAnalyzer.getMajorOrMinor(rightNode);
        } else if (left.equals(MetadataStrings.UUID_STRING)) {
            if (trace.uuid != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Trace", MetadataStrings.UUID_STRING, leftNode);
            }
            
            // Set trace UUID
            UUID uuid = MetadataTreeAnalyzer.getUUID(rightNode);
            trace.uuid = uuid;
        } else if (left.equals(MetadataStrings.BYTE_ORDER)) {
            if (trace.bo != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Trace", MetadataStrings.BYTE_ORDER, leftNode);
            }
            
            // Set byte order
            trace.bo = MetadataTreeAnalyzer.getByteOrder(rightNode, false, false);
        } else if (left.equals(MetadataStrings.PACKET_HEADER)) {
            if (trace.packetHeader != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Trace", MetadataStrings.PACKET_HEADER, leftNode);
            }
            
            // Get the type specifier
            CommonTree typeSpecifier = (CommonTree) rightNode.getChild(0);

            // Check if it's good
            if (typeSpecifier.getType() != TSDLParser.TYPE_SPECIFIER_LIST) {
                MetadataTreeAnalyzer.errorWithLine("\"packet.header\" expects a type specifier", rightNode);
            }
            
            // Get the declaration by parsing the list
            IDeclaration packetHeaderDecl = this.parseTypeSpecifierList(typeSpecifier, null);
            
            // Make sure we get the right declaration
            if (!(packetHeaderDecl instanceof StructDeclaration)) {
                MetadataTreeAnalyzer.errorWithLine("\"packet.header\" expects a structure", rightNode);
            }

            // Set packet header declaration
            trace.packetHeader = (StructDeclaration) packetHeaderDecl;
        } else {
            MetadataTreeAnalyzer.errorWithLine("Unknown \"trace\" attribute: \"" + left + "\"", rightNode);
        }
    }

    private void parseStream(CommonTree streamNode) throws ParsingException {
        assert(streamNode.getType() == TSDLParser.STREAM);
        
        // Visiting info
        this.visitor.infoParsingStream(streamNode);

        // Push current scope
        pushScope();
        
        // What is going to be filled by the stream declaration parsing
        MetadataTreeAnalyzer.Stream stream = new MetadataTreeAnalyzer.Stream(); 

        // Parse type alias, typedef and the declaration
        List<CommonTree> children = streamNode.getChildren();
        if (children == null) {
            MetadataTreeAnalyzer.errorWithLine("Empty \"stream\" block", streamNode);
        } else { 
            for (CommonTree child : children) {
                switch (child.getType()) {
                case TSDLParser.TYPEALIAS:
                    this.parseTypealias(child);
                    break;
                    
                case TSDLParser.TYPEDEF:
                    this.parseTypedef(child);
                    break;
                    
                case TSDLParser.CTF_EXPRESSION_TYPE:
                case TSDLParser.CTF_EXPRESSION_VAL:
                    this.parseStreamDeclaration(child, stream);
                    break;
                    
                default:
                    MetadataTreeAnalyzer.childTypeError(child);
                    break;
                }
            }
            
            // Add stream
            this.visitor.addStream(stream.id, stream.eventHeader, stream.eventContext, stream.packetContext);
        }

        // Pop scope
        popScope();
    }

    private void parseStreamDeclaration(CommonTree streamDecl, MetadataTreeAnalyzer.Stream stream)
            throws ParsingException {
        assert((streamDecl.getType() == TSDLParser.CTF_EXPRESSION_TYPE) ||
                (streamDecl.getType() == TSDLParser.CTF_EXPRESSION_VAL));

        // There should be a left and right
        assert(streamDecl.getChildCount() == 2);
        
        // Get left/right
        CommonTree leftNode = (CommonTree) streamDecl.getChild(0);
        assert(leftNode.getType() == TSDLParser.CTF_LEFT);
        CommonTree rightNode = (CommonTree) streamDecl.getChild(1);
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);

        // Get key
        List<CommonTree> leftStrings = leftNode.getChildren();
        assert(leftStrings != null);
        if (!MetadataTreeAnalyzer.isUnaryString(leftStrings.get(0))) {
            MetadataTreeAnalyzer.errorWithLine("LHS of TSDL assignment must be a string", leftNode);
        }
        String left = MetadataTreeAnalyzer.concatenateUnaryStrings(leftStrings);

        // According to the key
        if (left.equals(MetadataStrings.ID)) {
            if (stream.id != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Stream", MetadataStrings.ID, leftNode);
            }
            
            // Set stream ID
            stream.id = new Integer(MetadataTreeAnalyzer.getStreamID(rightNode));
        } else if (left.equals(MetadataStrings.EVENT_HEADER)) {
            if (stream.eventHeader != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Stream", MetadataStrings.EVENT_HEADER, leftNode);
            }

            // Type specifier
            CommonTree typeSpecifier = (CommonTree) rightNode.getChild(0);
            if (typeSpecifier.getType() != TSDLParser.TYPE_SPECIFIER_LIST) {
                MetadataTreeAnalyzer.errorWithLine("\"event.header\" expects a type specifier", rightNode);
            }

            // Get the declaration
            IDeclaration decl = parseTypeSpecifierList(typeSpecifier, null);
            
            // Make sure it's the right declaration
            if (!(decl instanceof StructDeclaration)) {
                MetadataTreeAnalyzer.errorWithLine("\"event.header\" expects a structure", rightNode);
            }

            // Set event header declaration
            stream.eventHeader = (StructDeclaration) decl;
        } else if (left.equals(MetadataStrings.EVENT_CONTEXT)) {
            if (stream.eventContext != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Stream", MetadataStrings.EVENT_CONTEXT, leftNode);
            }

            // Type specifier
            CommonTree typeSpecifier = (CommonTree) rightNode.getChild(0);
            if (typeSpecifier.getType() != TSDLParser.TYPE_SPECIFIER_LIST) {
                MetadataTreeAnalyzer.errorWithLine("\"event.context\" expects a type specifier", rightNode);
            }

            // Get the declaration
            IDeclaration decl = parseTypeSpecifierList(typeSpecifier, null);
            
            // Make sure it's the right declaration
            if (!(decl instanceof StructDeclaration)) {
                MetadataTreeAnalyzer.errorWithLine("\"event.context\" expects a structure", rightNode);
            }

            // Set per-stream event context declaration
            stream.eventContext = (StructDeclaration) decl;
        } else if (left.equals(MetadataStrings.PACKET_CONTEXT)) {
            if (stream.packetContext != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Stream", MetadataStrings.PACKET_CONTEXT, leftNode);
            }

            // Type specifier
            CommonTree typeSpecifier = (CommonTree) rightNode.getChild(0);
            if (typeSpecifier.getType() != TSDLParser.TYPE_SPECIFIER_LIST) {
                MetadataTreeAnalyzer.errorWithLine("\"packet.context\" expects a type specifier", rightNode);
            }

            // Get the declaration
            IDeclaration decl = parseTypeSpecifierList(typeSpecifier, null);
            
            // Make sure it's the right declaration
            if (!(decl instanceof StructDeclaration)) {
                MetadataTreeAnalyzer.errorWithLine("\"packet.context\" expects a structure", rightNode);
            }

            // Set packet context
            stream.packetContext = (StructDeclaration) decl;
        } else {
            MetadataTreeAnalyzer.errorWithLine("Unknown \"stream\" attribute: \"" + left + "\"", leftNode);
        }
    }

    private void parseEvent(CommonTree eventNode) throws ParsingException {
        assert(eventNode.getType() == TSDLParser.EVENT);

        // Visiting info
        this.visitor.infoParsingEvent(eventNode);

        // Push current scope
        pushScope();

        // What is going to be filled by the event declaration parsing
        MetadataTreeAnalyzer.Event event = new MetadataTreeAnalyzer.Event();
        
        // Browse type alias, typedef and the declaration itself
        List<CommonTree> children = eventNode.getChildren();
        if (children == null) {
            MetadataTreeAnalyzer.errorWithLine("Empty \"event\" block", eventNode);
        } else 
        for (CommonTree child : children) {
            switch (child.getType()) {
            case TSDLParser.TYPEALIAS:
                this.parseTypealias(child);
                break;
                
            case TSDLParser.TYPEDEF:
                this.parseTypedef(child);
                break;
                
            case TSDLParser.CTF_EXPRESSION_TYPE:
            case TSDLParser.CTF_EXPRESSION_VAL:
                this.parseEventDeclaration(child, event);
                break;
                
            default:
                MetadataTreeAnalyzer.childTypeError(child);
                break;
            }
        }

        // Name must be set
        if (event.name == null) {
            MetadataTreeAnalyzer.errorWithLine("Event \"name\" not set", eventNode);
        }

        /*
         * If the event did not specify a stream, then the trace must be single
         * stream. Since we don't know anything about streams or the overall
         * trace here, we let the visitor do this verification when adding the
         * event.
         */

        // Add this event
        this.visitor.addEvent(event.id, event.streamID, event.name, event.context, event.fields, event.logLevel);

        // Pop scope
        popScope();
    }

    private void parseEventDeclaration(CommonTree eventDecl, MetadataTreeAnalyzer.Event event)
            throws ParsingException {
        assert((eventDecl.getType() == TSDLParser.CTF_EXPRESSION_TYPE) ||
                (eventDecl.getType() == TSDLParser.CTF_EXPRESSION_VAL));

        // There should be a left and right
        assert(eventDecl.getChildCount() == 2);

        // Get left/right
        CommonTree leftNode = (CommonTree) eventDecl.getChild(0);
        assert(leftNode.getType() == TSDLParser.CTF_LEFT);
        CommonTree rightNode = (CommonTree) eventDecl.getChild(1);
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);

        // Get key
        List<CommonTree> leftStrings = leftNode.getChildren();
        assert(leftStrings != null);
        if (!MetadataTreeAnalyzer.isUnaryString(leftStrings.get(0))) {
            MetadataTreeAnalyzer.errorWithLine("LHS of TSDL assignment must be a string", leftNode);
        }
        String left = MetadataTreeAnalyzer.concatenateUnaryStrings(leftStrings);
        
        // According to the key
        if (left.equals(MetadataStrings.NAME2)) {
            if (event.name != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Event", MetadataStrings.NAME2, leftNode);
            }

            // Set name
            event.name = MetadataTreeAnalyzer.getEventName(rightNode);
        } else if (left.equals(MetadataStrings.ID)) {
            if (event.id != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Event", MetadataStrings.ID, leftNode);
            }

            // Set event ID
            event.id = new Integer(MetadataTreeAnalyzer.getEventID(rightNode));
        } else if (left.equals(MetadataStrings.STREAM_ID)) {
            if (event.streamID != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Event", MetadataStrings.STREAM_ID, leftNode);
            }

            // Set stream ID
            int streamID = MetadataTreeAnalyzer.getStreamID(rightNode);
            event.streamID = new Integer(streamID);
        } else if (left.equals(MetadataStrings.CONTEXT)) {
            if (event.context != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Event", MetadataStrings.CONTEXT, leftNode);
            }

            // Type specifier
            CommonTree typeSpecifier = (CommonTree) rightNode.getChild(0);
            if (typeSpecifier.getType() != TSDLParser.TYPE_SPECIFIER_LIST) {
                MetadataTreeAnalyzer.errorWithLine("Event context expects a type specifier", rightNode);
            }

            // Get the declaration
            IDeclaration contextDecl = this.parseTypeSpecifierList(typeSpecifier, null);

            // Make sure it's the right declaration
            if (!(contextDecl instanceof StructDeclaration)) {
                MetadataTreeAnalyzer.errorWithLine("Event context expects a structure", rightNode);
            }

            // Set event context declaration
            event.context = (StructDeclaration) contextDecl;
        } else if (left.equals(MetadataStrings.FIELDS_STRING)) {
            if (event.fields != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Event", MetadataStrings.FIELDS_STRING, leftNode);
            }

            // Type specifier
            CommonTree typeSpecifier = (CommonTree) rightNode.getChild(0);
            if (typeSpecifier.getType() != TSDLParser.TYPE_SPECIFIER_LIST) {
                MetadataTreeAnalyzer.errorWithLine("Event fields expects a type specifier", rightNode);
            }

            // Get the declaration
            IDeclaration fieldsDecl = this.parseTypeSpecifierList(typeSpecifier, null);

            // Make sure it's the right declaration
            if (!(fieldsDecl instanceof StructDeclaration)) {
                MetadataTreeAnalyzer.errorWithLine("Event fields expects a structure", rightNode);
            }
            
            // Set fields declaration
            final StructDeclaration fields = (StructDeclaration) fieldsDecl;
            event.fields = fields;
        } else if (left.equals(MetadataStrings.LOGLEVEL2)) {
            if (event.logLevel != null) {
                MetadataTreeAnalyzer.definedMultipleTimesError("Event", MetadataStrings.LOGLEVEL2, leftNode);
            }
            
            // Set log level
            int logLevel = (int) MetadataTreeAnalyzer.parseUnaryInteger((CommonTree) rightNode.getChild(0));
            event.logLevel = new Integer(logLevel);
        } else {
            MetadataTreeAnalyzer.errorWithLine("Unknown \"event\" attribute: \"" + left + "\"", leftNode);
        }
    }

    private void parseRootDeclaration(CommonTree declaration)
            throws ParsingException {
        assert(declaration.getType() == TSDLParser.DECLARATION);

        // Browse typedef, type alias and type specifier list
        List<CommonTree> children = declaration.getChildren();
        assert(children != null);
        for (CommonTree child : children) {
            switch (child.getType()) {
            case TSDLParser.TYPEDEF:
                this.parseTypedef(child);
                break;
                
            case TSDLParser.TYPEALIAS:
                this.parseTypealias(child);
                break;
                
            case TSDLParser.TYPE_SPECIFIER_LIST:
                this.parseTypeSpecifierList(child, null);
                break;
                
            default:
                MetadataTreeAnalyzer.childTypeError(child);
            }
        }
    }

    private void parseTypealias(CommonTree typealias) throws ParsingException {
        assert(typealias.getType() == TSDLParser.TYPEALIAS);

        CommonTree target = null;
        CommonTree alias = null;

        // Visiting info
        this.visitor.infoParsingTypealias(typealias);
        
        // Browse the node children
        List<CommonTree> children = typealias.getChildren();
        assert(children != null);
        for (CommonTree child : children) {
            switch (child.getType()) {
            case TSDLParser.TYPEALIAS_TARGET:
                assert(target == null);
                target = child;
                break;
                
            case TSDLParser.TYPEALIAS_ALIAS:
                assert(alias == null);
                alias = child;
                break;
                
            default:
                MetadataTreeAnalyzer.childTypeError(child);
                break;
            }
        }
        assert(target != null);
        assert(alias != null);

        // Target declaration
        IDeclaration targetDeclaration = this.parseTypealiasTarget(target);
        if (targetDeclaration instanceof VariantDeclaration) {
            if (((VariantDeclaration) targetDeclaration).isTagged()) {
                MetadataTreeAnalyzer.errorWithLine("Typealias of untagged variant is not permitted", typealias);
            }
        }

        // Get the alias string
        String aliasString = MetadataTreeAnalyzer.parseTypealiasAlias(alias);

        // Register this new type to the current scope
        getCurrentScope().registerType(aliasString, targetDeclaration);
    }

    private IDeclaration parseTypealiasTarget(CommonTree target) throws ParsingException {
        assert(target.getType() == TSDLParser.TYPEALIAS_TARGET);

        // Get children
        List<CommonTree> children = target.getChildren();
        assert(children != null);

        CommonTree typeSpecifierList = null;
        CommonTree typeDeclaratorList = null;
        CommonTree typeDeclarator = null;
        StringBuilder identifierSB = new StringBuilder();

        // Browse children
        for (CommonTree child : children) {
            // According to the type
            switch (child.getType()) {
            case TSDLParser.TYPE_SPECIFIER_LIST:
                assert(typeSpecifierList == null);
                typeSpecifierList = child;
                break;
                
            case TSDLParser.TYPE_DECLARATOR_LIST:
                assert(typeDeclaratorList == null);
                typeDeclaratorList = child;
                break;
                
            default:
                MetadataTreeAnalyzer.childTypeError(child);
                break;
            }
        }

        assert(typeSpecifierList != null);

        if (typeDeclaratorList != null) {
            /*
             * Only allow one declarator, e.g. "typealias uint8_t *, ** := puint8_t;"
             * is not permitted, otherwise the new type "puint8_t" would map
             * to two different types.
             */
            if (typeDeclaratorList.getChildCount() != 1) {
                MetadataTreeAnalyzer.errorWithLine("Only one type declarator is allowed in the \"typealias\" target", target);
            }

            typeDeclarator = (CommonTree) typeDeclaratorList.getChild(0);
        }

        // Parse the target type and get the declaration
        IDeclaration targetDeclaration = this.parseTypeDeclarator(typeDeclarator,
                typeSpecifierList, identifierSB);

        /*
         * We don't allow identifier in the target, e.g. in
         * "typealias uint8_t* hello := puint8_t;", the "hello" is not
         * permitted.
         */
        if (identifierSB.length() > 0) {
            MetadataTreeAnalyzer.errorWithLine("Identifier \"" + identifierSB.toString() +
                    "\" not expected in the \"typealias\" target", target);
        }

        return targetDeclaration;
    }

    private static String parseTypealiasAlias(CommonTree alias)
            throws ParsingException {
        assert(alias.getType() == TSDLParser.TYPEALIAS_ALIAS);

        // Get children
        List<CommonTree> children = alias.getChildren();
        assert(children != null);

        CommonTree typeSpecifierList = null;
        CommonTree typeDeclaratorList = null;
        CommonTree typeDeclarator = null;
        List<CommonTree> pointers = new LinkedList<CommonTree>();

        // Browse children
        for (CommonTree child : children) {
            // According to the type
            switch (child.getType()) {
            case TSDLParser.TYPE_SPECIFIER_LIST:
                assert(typeSpecifierList == null);
                typeSpecifierList = child;
                break;
                
            case TSDLParser.TYPE_DECLARATOR_LIST:
                assert(typeDeclaratorList == null);
                typeDeclaratorList = child;
                break;
                
            default:
                MetadataTreeAnalyzer.childTypeError(child);
                break;
            }
        }

        // If there is a type declarator list, extract the pointers
        if (typeDeclaratorList != null) {
            /*
             * Only allow one declarator, e.g. "typealias uint8_t := puint8_t *, **;"
             * is not permitted.
             */
            if (typeDeclaratorList.getChildCount() != 1) {
                MetadataTreeAnalyzer.errorWithLine("Only one type declarator is allowed in the \"typealias\" alias", alias);
            }

            typeDeclarator = (CommonTree) typeDeclaratorList.getChild(0);

            // Get children
            List<CommonTree> typeDeclaratorChildren = typeDeclarator.getChildren();
            assert(typeDeclaratorChildren != null);

            // Browse children
            for (CommonTree child : typeDeclaratorChildren) {
                // According to type
                switch (child.getType()) {
                case TSDLParser.POINTER:
                    pointers.add(child);
                    break;
                    
                case TSDLParser.IDENTIFIER:
                    MetadataTreeAnalyzer.errorWithLine("Identifier \"" + child.getText() + "\" not expected in the \"typealias\" target", child);
                    break;
                    
                default:
                    MetadataTreeAnalyzer.childTypeError(child);
                    break;
                }
            }
        }

        return MetadataTreeAnalyzer.createTypeDeclarationString(typeSpecifierList, pointers);
    }

    private void parseTypedef(CommonTree typedef) throws ParsingException {
        assert(typedef.getType() == TSDLParser.TYPEDEF);

        // Visiting info
        this.visitor.infoParsingTypedef(typedef);
        
        // Get declarator and specifier
        CommonTree typeDeclaratorListNode = (CommonTree) typedef.getFirstChildWithType(TSDLParser.TYPE_DECLARATOR_LIST);
        CommonTree typeSpecifierListNode = (CommonTree) typedef.getFirstChildWithType(TSDLParser.TYPE_SPECIFIER_LIST);
        assert(typeDeclaratorListNode != null);
        assert(typeSpecifierListNode != null);

        // Get the declarator list
        List<CommonTree> typeDeclaratorList = typeDeclaratorListNode.getChildren();
        assert(typeDeclaratorList != null);

        // Browse the declarator list
        for (CommonTree typeDeclaratorNode : typeDeclaratorList) {
            StringBuilder identifierSB = new StringBuilder();

            // First parse the declaration
            IDeclaration type_declaration = this.parseTypeDeclarator(typeDeclaratorNode,
                    typeSpecifierListNode, identifierSB);

            // Ensure variant declarations are tagged
            if (type_declaration instanceof VariantDeclaration) {
                if (((VariantDeclaration) type_declaration).isTagged()) {
                    MetadataTreeAnalyzer.errorWithLine("Type alias of untagged variant is not permitted", typeDeclaratorNode);
                }
            }

            // Register new type within current scope
            this.getCurrentScope().registerType(identifierSB.toString(), type_declaration);
        }
    }

    private IDeclaration parseTypeDeclarator(CommonTree typeDeclarator,
            CommonTree typeSpecifierList, StringBuilder identifierSB) throws ParsingException {
        if (typeDeclarator != null) {
            assert(typeDeclarator.getType() == TSDLParser.TYPE_DECLARATOR);
        }
        assert(typeSpecifierList.getType() == TSDLParser.TYPE_SPECIFIER_LIST);
        
        IDeclaration declaration = null;
        List<CommonTree> children = null;
        List<CommonTree> pointers = new LinkedList<CommonTree>();
        List<CommonTree> lengths = new LinkedList<CommonTree>();
        CommonTree identifier = null;
        
        // Separate the tokens by type
        if (typeDeclarator != null) {
            children = typeDeclarator.getChildren();
            assert(children != null);
            for (CommonTree child : children) {
                // According to type
                switch (child.getType()) {
                case TSDLParser.POINTER:
                    pointers.add(child);
                    break;
                    
                case TSDLParser.IDENTIFIER:
                    assert(identifier == null);
                    identifier = child;
                    break;
                    
                case TSDLParser.LENGTH:
                    lengths.add(child);
                    break;
                    
                default:
                    MetadataTreeAnalyzer.childTypeError(child);
                    break;
                }
            }

        }

        /*
         * Parse the type specifier list, which is the "base" type. For example,
         * it would be "int" in "int a[3][len]".
         */
        declaration = this.parseTypeSpecifierList(typeSpecifierList, pointers);

        /*
         * Each length subscript means that we must create a nested array or
         * sequence. For example, "int a[3][len]" means that we have an array of 3
         * (sequences of length "len" of "int").
         */
        if (lengths.size() > 0 ) {
            // We begin at the end
            Collections.reverse(lengths);

            for (CommonTree length : lengths) {
                /*
                 * By looking at the first expression, we can determine whether
                 * it is an array or a sequence.
                 */
                List<CommonTree> lengthChildren = length.getChildren();
                assert(lengthChildren != null);

                CommonTree first = lengthChildren.get(0);
                if (MetadataTreeAnalyzer.isUnaryInteger(first)) {
                    // Array
                    int arrayLength = (int) MetadataTreeAnalyzer.parseUnaryInteger(first);
                    if (arrayLength < 1) {
                        MetadataTreeAnalyzer.errorWithLine("Array length is negative", first);
                    }

                    // Create the array declaration
                    declaration = new ArrayDeclaration(arrayLength, declaration);
                } else if (MetadataTreeAnalyzer.isUnaryString(first)) {
                    // Sequence
                    String lengthName = MetadataTreeAnalyzer.concatenateUnaryStrings(lengthChildren);

                    // Create the sequence declaration
                    declaration = new SequenceDeclaration(declaration, lengthName);
                } else {
                    // Not an array nor a sequence: error
                    MetadataTreeAnalyzer.childTypeError(first);
                }
            }
        }

        if (identifier != null) {
            identifierSB.append(identifier.getText());
        }

        return declaration;
    }

    private IDeclaration parseTypeSpecifierList(CommonTree typeSpecifierList,
            List<CommonTree> pointerList) throws ParsingException {
        assert(typeSpecifierList.getType() == TSDLParser.TYPE_SPECIFIER_LIST);
        IDeclaration declaration = null;
        
        /*
         * By looking at the first element of the type specifier list, we can
         * determine which type it belongs to.
         */
        CommonTree firstChild = (CommonTree) typeSpecifierList.getChild(0);
        assert(firstChild != null); // Grammar

        // According to type
        switch (firstChild.getType()) {
        case TSDLParser.FLOATING_POINT:
            declaration = this.parseFloat(firstChild);
            break;
            
        case TSDLParser.INTEGER:
            declaration = this.parseInteger(firstChild);
            break;
            
        case TSDLParser.STRING:
            declaration = this.parseString(firstChild);
            break;
            
        case TSDLParser.STRUCT:
            declaration = this.parseStruct(firstChild);
            break;
            
        case TSDLParser.VARIANT:
            declaration = this.parseVariant(firstChild);
            break;
            
        case TSDLParser.ENUM:
            declaration = this.parseEnum(firstChild);
            break;
            
        case TSDLParser.IDENTIFIER:
        case TSDLParser.FLOATTOK:
        case TSDLParser.INTTOK:
        case TSDLParser.LONGTOK:
        case TSDLParser.SHORTTOK:
        case TSDLParser.SIGNEDTOK:
        case TSDLParser.UNSIGNEDTOK:
        case TSDLParser.CHARTOK:
        case TSDLParser.DOUBLETOK:
        case TSDLParser.VOIDTOK:
        case TSDLParser.BOOLTOK:
        case TSDLParser.COMPLEXTOK:
        case TSDLParser.IMAGINARYTOK:
            declaration = this.parseTypeDeclaration(typeSpecifierList, pointerList);
            break;
            
        default:
            MetadataTreeAnalyzer.childTypeError(firstChild);
        }

        assert(declaration != null);
        return declaration;
    }

    private IDeclaration parseFloat(CommonTree floatingPoint) throws ParsingException {
        assert(floatingPoint.getType() == TSDLParser.INTEGER);

        // Get children
        List<CommonTree> children = floatingPoint.getChildren();

        // Visiting info
        this.visitor.infoParsingFloat(floatingPoint);
        
        // What we will return
        FloatDeclaration floatDeclaration = null;
        ByteOrder byteOrder = this.visitor.getGlobalByteOrder();
        long alignment = 0;
        int exponent = 8;
        int mantissa = 24;
        
        /*
         * If the floating point number has no attributes, then it is missing the size
         * attribute which is required.
         */
        if (children == null) {
            MetadataTreeAnalyzer.errorWithLine("Missing size attributes for type \"float\"", floatingPoint);
        } else {
            // Browse children
            for (CommonTree child : children) {
                // According to type
                switch (child.getType()) {
                case TSDLParser.CTF_EXPRESSION_VAL:
                    // An assignment expression must have 2 children: left and right
                    assert(child.getChildCount() == 2);
    
                    // Get left/right children
                    CommonTree leftNode = (CommonTree) child.getChild(0);
                    assert(leftNode.getType() == TSDLParser.CTF_LEFT);
                    CommonTree rightNode = (CommonTree) child.getChild(1);
                    assert(rightNode.getType() == TSDLParser.CTF_RIGHT);
    
                    // Check left side of expression
                    List<CommonTree> leftStrings = leftNode.getChildren();
                    assert(leftStrings != null);
                    if (!MetadataTreeAnalyzer.isUnaryString(leftStrings.get(0))) {
                        MetadataTreeAnalyzer.errorWithLine("LHS of CTF expression must be a string", leftNode);
                    }
                    String left = MetadataTreeAnalyzer.concatenateUnaryStrings(leftStrings);
    
                    // Check right side according to left side
                    if (left.equals(MetadataStrings.EXP_DIG)) {
                        // Exponent size
                        exponent = (int) MetadataTreeAnalyzer.parseUnaryInteger((CommonTree) rightNode.getChild(0));
                    } else if (left.equals(MetadataStrings.BYTE_ORDER)) {
                        // Byte order
                        byteOrder = MetadataTreeAnalyzer.getByteOrder(rightNode, true, true);
                    } else if (left.equals(MetadataStrings.MANT_DIG)) {
                        // Mantissa size
                        mantissa = (int) MetadataTreeAnalyzer.parseUnaryInteger((CommonTree) rightNode.getChild(0));
                    } else if (left.equals(MetadataStrings.ALIGN)) {
                        // Alignment
                        alignment = MetadataTreeAnalyzer.getAlignment(rightNode);
                    } else {
                        MetadataTreeAnalyzer.errorWithLine("Unknown attribute \"" + left + "\" for type \"float\"", rightNode);
                    }
                    break;
                    
                default:
                    MetadataTreeAnalyzer.childTypeError(child);
                    break;
                }
            }
            
            int size = mantissa + exponent;
            if (size == 0) {
                MetadataTreeAnalyzer.errorWithLine("Missing size attributes for type \"float\"", floatingPoint);
            }
    
            // Alignment adjustments
            if (alignment == 0) {
                if ((size % 8) == 0) {
                    alignment = 1;
                } else {
                    alignment = 8;
                }
            }
    
            // Create floating point number declaration
            floatDeclaration = new FloatDeclaration(exponent, mantissa, byteOrder, alignment);
        }

        assert(floatDeclaration != null);
        return floatDeclaration;
    }

    private IDeclaration parseTypeDeclaration(CommonTree typeSpecifierList,
            List<CommonTree> pointerList) throws ParsingException {
        // Create the string representation of the type declaration
        String typeStringRepresentation = createTypeDeclarationString(
                typeSpecifierList, pointerList);

        // Use the string representation to search the type in the current scope
        IDeclaration decl = this.getCurrentScope().rlookupType(typeStringRepresentation);

        if (decl == null) {
            MetadataTreeAnalyzer.errorWithLine("Type \"" + typeStringRepresentation + "\" has not been defined", typeSpecifierList);
        }

        return decl;
    }

    private IntegerDeclaration parseInteger(CommonTree integer)
            throws ParsingException {
        assert(integer.getType() == TSDLParser.INTEGER);

        List<CommonTree> children = integer.getChildren();

        // Visiting info
        this.visitor.infoParsingInteger(integer);
        
        // What we will return
        IntegerDeclaration integerDeclaration = null;
        boolean signed = false;
        ByteOrder byteOrder = this.visitor.getGlobalByteOrder();
        long size = 0;
        long alignment = 0;
        int base = 10;
        String clock = null;
        Encoding encoding = Encoding.NONE;
        
        /*
         * If the integer has no attributes, then it is missing the size
         * attribute which is required.
         */
        if (children == null) {
            MetadataTreeAnalyzer.errorWithLine("Missing \"size\" attribute for type \"integer\"", integer);
        } else {
            // Browse children
            for (CommonTree child : children) {
                switch (child.getType()) {
                case TSDLParser.CTF_EXPRESSION_VAL:
                    // Make sure it has left and right children
                    assert(child.getChildCount() == 2);
    
                    // Get left/right children
                    CommonTree leftNode = (CommonTree) child.getChild(0);
                    assert(leftNode.getType() == TSDLParser.CTF_LEFT);
                    CommonTree rightNode = (CommonTree) child.getChild(1);
                    assert(rightNode.getType() == TSDLParser.CTF_RIGHT);
    
                    // Check LHS of expression
                    List<CommonTree> leftStrings = leftNode.getChildren();
                    assert(leftStrings != null);
                    if (!MetadataTreeAnalyzer.isUnaryString(leftStrings.get(0))) {
                        MetadataTreeAnalyzer.errorWithLine("LHS of ctf expression must be a string", leftNode);
                    }
                    String left = MetadataTreeAnalyzer.concatenateUnaryStrings(leftStrings);
    
                    // Check RHS according to LHS
                    if (left.equals("signed")) {
                        // Sign
                        signed = MetadataTreeAnalyzer.getSigned(rightNode);
                    } else if (left.equals(MetadataStrings.BYTE_ORDER)) {
                        // Byte order
                        byteOrder = MetadataTreeAnalyzer.getByteOrder(rightNode, true, true);
                    } else if (left.equals("size")) {
                        // Size
                        size = MetadataTreeAnalyzer.getSize(rightNode);
                    } else if (left.equals(MetadataStrings.ALIGN)) {
                        // Alignment
                        alignment = MetadataTreeAnalyzer.getAlignment(rightNode);
                    } else if (left.equals("base")) {
                        // Base
                        base = MetadataTreeAnalyzer.getBase(rightNode);
                    } else if (left.equals("encoding")) {
                        // Encoding
                        encoding = MetadataTreeAnalyzer.getEncoding(rightNode);
                    } else if (left.equals("map")) {
                        // Clock mapping
                        clock = MetadataTreeAnalyzer.getClock(rightNode);
                    } else {
                        MetadataTreeAnalyzer.errorWithLine("Unknown attribute \"" + left + "\" for type \"integer\"", leftNode);
                    }
                    break;
                    
                default:
                    MetadataTreeAnalyzer.childTypeError(child);
                    break;
                }
            }
        }

        if (size == 0) {
            MetadataTreeAnalyzer.errorWithLine("Missing \"size\" attribute for type \"integer\"", integer);
        }

        // Alignment adjustments
        if (alignment == 0) {
            if ((size % 8) == 0) {
                alignment = 1;
            } else {
                alignment = 8;
            }
        }

        // Create integer number declaration
        integerDeclaration = new IntegerDeclaration((int) size, signed, base,
                byteOrder, encoding, clock, alignment);

        assert(integerDeclaration != null);
        return integerDeclaration;
    }

    private static String getClock(CommonTree rightNode) {
        return rightNode.getChild(1).getChild(0).getChild(0).getText();
    }

    private StringDeclaration parseString(CommonTree string) throws ParsingException {
        assert(string.getType() == TSDLParser.STRING);

        // Get children
        List<CommonTree> children = string.getChildren();
        StringDeclaration stringDeclaration = null;

        // Visiting info
        this.visitor.infoParsingString(string);
        
        if (children == null) {
            stringDeclaration = new StringDeclaration();
        } else {
            // Default encoding: UTF-8
            Encoding encoding = Encoding.UTF8;
            
            // Browse children
            for (CommonTree child : children) {
                // According to type
                switch (child.getType()) {
                case TSDLParser.CTF_EXPRESSION_VAL:
                    // An assignment expression must have 2 children: left and right
                    assert(child.getChildCount() == 2);

                    // Get left/right children
                    CommonTree leftNode = (CommonTree) child.getChild(0);
                    assert(leftNode.getType() == TSDLParser.CTF_LEFT);
                    CommonTree rightNode = (CommonTree) child.getChild(1);
                    assert(rightNode.getType() == TSDLParser.CTF_RIGHT);

                    // Check left side of expression
                    List<CommonTree> leftStrings = leftNode.getChildren();
                    assert(leftStrings != null);
                    if (!MetadataTreeAnalyzer.isUnaryString(leftStrings.get(0))) {
                        MetadataTreeAnalyzer.errorWithLine("LHS of CTF expression must be a string", leftNode);
                    }
                    String left = MetadataTreeAnalyzer.concatenateUnaryStrings(leftStrings);

                    // Check attributes
                    if (left.equals("encoding")) {
                        // Encoding
                        encoding = MetadataTreeAnalyzer.getEncoding(rightNode);
                    } else {
                        MetadataTreeAnalyzer.errorWithLine("Unknown attribute \"" + left + "\" for type \"string\"", leftNode);
                    }
                    break;
                    
                default:
                    MetadataTreeAnalyzer.childTypeError(child);
                    break;
                }
            }

            // Create string declaration
            stringDeclaration = new StringDeclaration(encoding);
        }

        return stringDeclaration;
    }

    private StructDeclaration parseStruct(CommonTree struct) throws ParsingException {
        assert(struct.getType() == TSDLParser.STRUCT);

        // Get children
        List<CommonTree> children = struct.getChildren();
        assert(children != null);

        // Visiting info
        this.visitor.infoParsingStruct(struct);
        
        // What we will return
        StructDeclaration structDeclaration = null;

        // Name
        String structName = null;
        boolean hasName = false;

        // Body
        CommonTree structBody = null;
        boolean hasBody = false;

        // Default alignment
        long structAlign = 0;

        // Loop on all children and identify what we have to work with
        for (CommonTree child : children) {
            // According to type
            switch (child.getType()) {
            case TSDLParser.STRUCT_NAME:
                hasName = true;

                assert(child.getChildCount() == 1);
                CommonTree structNameIdentifier = (CommonTree) child.getChild(0);

                assert(structNameIdentifier.getType() == TSDLParser.IDENTIFIER);
                structName = structNameIdentifier.getText();
                break;
                
            case TSDLParser.STRUCT_BODY:
                hasBody = true;
                structBody = child;
                break;
                
            case TSDLParser.ALIGN:
                assert(child.getChildCount() == 1);
                CommonTree structAlignExpression = (CommonTree) child.getChild(0);
                structAlign = MetadataTreeAnalyzer.getAlignment(structAlignExpression);
                break;

            default:
                MetadataTreeAnalyzer.childTypeError(child);
                break;
            }
        }

        /*
         * If a struct has just a body and no name (just like the song,
         * "A Struct With No Name" by America (sorry for that...)), it's a
         * definition of a new type, so we create the type declaration and
         * return it. We can't add it to the declaration scope since there is no
         * name, but that's what we want because it won't be possible to use it
         * again to declare another field.
         *
         * If it has just a name, we look it up in the declaration scope and
         * return the associated declaration. If it is not found in the
         * declaration scope, it means that a struct with that name has not been
         * declared, which is an error.
         *
         * If it has both, then we create the type declaration and register it
         * to the current scope.
         *
         * If it has none, then what are we doing here?
         */
        if (hasBody) {
            // If struct has a name, check if already defined in the current scope
            if (hasName && (this.getCurrentScope().lookupStruct(structName) != null)) {
                MetadataTreeAnalyzer.errorWithLine("Structure \"" + structName + "\" already defined", struct);
            }
            // Create the declaration
            structDeclaration = new StructDeclaration(structAlign);

            // Parse the body
            this.parseStructBody(structBody, structDeclaration);

            // If struct has name, add it to the current scope
            if (hasName) {
                this.getCurrentScope().registerStruct(structName, structDeclaration);
            }
        } else {
            // No body
            if (hasName) {
                // A name, no body: lookup the name in the current scope
                structDeclaration = this.getCurrentScope().rlookupStruct(structName);

                // If not found, it means that a struct with such name has not been defined
                if (structDeclaration == null) {
                    MetadataTreeAnalyzer.errorWithLine("Structure \"" + structName + "\" is not defined", struct);
                }
            } else {
                // No name, no body: we can't do anything with that
                MetadataTreeAnalyzer.errorWithLine("Structure found with no name and no body", struct);
            }
        }

        assert(structDeclaration != null);
        return structDeclaration;
    }

    
    private void parseStructBody(CommonTree structBody,
            StructDeclaration structDeclaration) throws ParsingException {
        assert(structBody.getType() == TSDLParser.STRUCT_BODY);

        List<CommonTree> structDeclarations = structBody.getChildren();

        /*
         * If structDeclaration is null, structBody has no children and the
         * struct body is empty.
         */
        if (structDeclarations != null) {
            this.pushScope();

            // Browse structure declarations
            for (CommonTree declarationNode : structDeclarations) {
                // According to type
                switch (declarationNode.getType()) {
                case TSDLParser.TYPEALIAS:
                    this.parseTypealias(declarationNode);
                    break;
                    
                case TSDLParser.TYPEDEF:
                    this.parseTypedef(declarationNode);
                    break;
                    
                case TSDLParser.SV_DECLARATION:
                    this.parseStructDeclaration(declarationNode, structDeclaration);
                    break;
                    
                default:
                    MetadataTreeAnalyzer.childTypeError(declarationNode);
                    break;
                }
            }
            this.popScope();
        }
    }

    private void parseStructDeclaration(CommonTree declaration,
            StructDeclaration struct) throws ParsingException {
        assert(declaration.getType() == TSDLParser.SV_DECLARATION);

        List<CommonTree> children = declaration.getChildren();
        assert(children != null);

        // Get the type specifier list node
        CommonTree typeSpecifierListNode = (CommonTree) declaration.getFirstChildWithType(TSDLParser.TYPE_SPECIFIER_LIST);
        assert(typeSpecifierListNode != null);

        // Get the type declarator list node
        CommonTree typeDeclaratorListNode = (CommonTree) declaration.getFirstChildWithType(TSDLParser.TYPE_DECLARATOR_LIST);
        assert(typeDeclaratorListNode != null);

        // Get the type declarator list
        List<CommonTree> typeDeclaratorList = typeDeclaratorListNode.getChildren();
        assert(typeDeclaratorList != null);

        /*
         * For each type declarator, parse the declaration and add a field to
         * the struct.
         */
        for (CommonTree typeDeclaratorNode : typeDeclaratorList) {
            assert(typeDeclaratorNode.getType() == TSDLParser.TYPE_DECLARATOR);

            StringBuilder identifierSB = new StringBuilder();

            IDeclaration decl = this.parseTypeDeclarator(typeDeclaratorNode, typeSpecifierListNode, identifierSB);
            String fieldName = identifierSB.toString();
            
            if (struct.hasField(fieldName)) {
                MetadataTreeAnalyzer.errorWithLine("Duplicate field \"" + fieldName + "\" for type \"struct\"", typeDeclaratorListNode);
            }

            // Finally, add the field (name => declaration)
            struct.addField(fieldName, decl);
        }
    }

    private EnumDeclaration parseEnum(CommonTree _enum) throws ParsingException {
        assert(_enum.getType() == TSDLParser.ENUM);

        // Get children
        List<CommonTree> children = _enum.getChildren();
        assert(children != null);
        
        // Visiting info
        this.visitor.infoParsingEnum(_enum);

        // What we will return
        EnumDeclaration enumDeclaration = null;

        // Name
        String enumName = null;

        // Body
        CommonTree enumBody = null;

        // Underlying integer declaration
        IntegerDeclaration containerTypeDeclaration = null;

        // Loop on all children and identify what we have to work with
        for (CommonTree child : children) {
            // According to type
            switch (child.getType()) {
            case TSDLParser.ENUM_NAME:
                assert(enumName == null);

                assert(child.getChildCount() == 1);
                CommonTree enumNameIdentifier = (CommonTree) child.getChild(0);

                assert(enumNameIdentifier.getType() == TSDLParser.IDENTIFIER);
                enumName = enumNameIdentifier.getText();
                break;
                
            case TSDLParser.ENUM_BODY:
                assert(enumBody == null);

                enumBody = child;
                break;

            case TSDLParser.ENUM_CONTAINER_TYPE:
                assert(containerTypeDeclaration == null);

                containerTypeDeclaration = this.parseEnumContainerType(child);
                break;

            default:
                MetadataTreeAnalyzer.childTypeError(child);
                break;
            }
        }

        /*
         * If the container type has not been defined explicitly, we assume it
         * is "int".
         */
        if (containerTypeDeclaration == null) {
            IDeclaration decl = getCurrentScope().rlookupType("int");

            if (decl == null || !(decl instanceof IntegerDeclaration)) {
                MetadataTreeAnalyzer.errorWithLine("Type \"enum\" container type is implicit but simple type \"int\" not defined", _enum);
            }

            containerTypeDeclaration = (IntegerDeclaration) decl;
        }

        /*
         * If it has a body, it's a new declaration, otherwise it's a reference
         * to an existing declaration. Same logic as struct.
         */
        if (enumBody != null) {
            // If enum has a name, check if already defined in the current scope
            if ((enumName != null) && (getCurrentScope().lookupEnum(enumName) != null)) {
                MetadataTreeAnalyzer.errorWithLine("Enumeration \"" + enumName + "\" already defined", _enum);
            }

            // Create the declaration
            enumDeclaration = new EnumDeclaration(containerTypeDeclaration);

            // Parse the body
            this.parseEnumBody(enumBody, enumDeclaration);

            // If the enum has name, add it to the current scope
            if (enumName != null) {
                this.getCurrentScope().registerEnum(enumName, enumDeclaration);
            }
        } else {
            if (enumName != null) {
                // Name, no body

                // Lookup the name in the current scope
                enumDeclaration = this.getCurrentScope().rlookupEnum(enumName);

                // If not found, it means that an enum with such name has not been defined
                if (enumDeclaration == null) {
                    MetadataTreeAnalyzer.errorWithLine("Enumeration \"" + enumName + "\" is not defined", _enum);
                }
            } else {
                // No name, no body
                MetadataTreeAnalyzer.errorWithLine("Enumeration found with no name and no body", _enum);
            }
        }

        return enumDeclaration;
    }

    private void parseEnumBody(CommonTree enumBody, EnumDeclaration enumDeclaration) throws ParsingException {
        assert(enumBody.getType() == TSDLParser.ENUM_BODY);

        // Get children
        List<CommonTree> enumerators = enumBody.getChildren();
        assert(enumerators != null); // enum body can't be empty (unlike struct)

        this.pushScope();

        /*
         * Start at -1, so that if the first enumerator has no explicit value, it
         * will choose 0.
         */
        long lastHigh = -1;
        for (CommonTree enumerator : enumerators) {
            lastHigh = MetadataTreeAnalyzer.parseEnumEnumerator(enumerator, enumDeclaration, lastHigh);
        }

        this.popScope();
    }

    private static long parseEnumEnumerator(CommonTree enumerator,
            EnumDeclaration enumDeclaration, long lastHigh) throws ParsingException {
        assert(enumerator.getType() == TSDLParser.ENUM_ENUMERATOR);

        // Get children
        List<CommonTree> children = enumerator.getChildren();
        assert(children != null);

        long low = 0, high = 0;
        boolean valueSpecified = false;
        String label = null;

        // Browse children
        for (CommonTree child : children) {
            if (MetadataTreeAnalyzer.isUnaryString(child)) {
                label = MetadataTreeAnalyzer.parseUnaryString(child);
            } else if (child.getType() == TSDLParser.ENUM_VALUE) {
                assert(child.getChildCount() == 1);
                assert(MetadataTreeAnalyzer.isUnaryInteger((CommonTree) child.getChild(0)));

                valueSpecified = true;

                low = MetadataTreeAnalyzer.parseUnaryInteger((CommonTree) child.getChild(0));
                high = low;
            } else if (child.getType() == TSDLParser.ENUM_VALUE_RANGE) {
                assert(child.getChildCount() == 2);
                assert(MetadataTreeAnalyzer.isUnaryInteger((CommonTree) child.getChild(0)));
                assert(MetadataTreeAnalyzer.isUnaryInteger((CommonTree) child.getChild(1)));

                valueSpecified = true;

                low = MetadataTreeAnalyzer.parseUnaryInteger((CommonTree) child.getChild(0));
                high = MetadataTreeAnalyzer.parseUnaryInteger((CommonTree) child.getChild(1));
            } else {
                MetadataTreeAnalyzer.childTypeError(child);
            }
        }

        assert(label != null);

        if (!valueSpecified) {
            low = lastHigh + 1;
            high = low;
        }

        if (low > high) {
            MetadataTreeAnalyzer.errorWithLine("Enumeration range lower bound greater than upper bound", enumerator);
        }

        if (!enumDeclaration.add(low, high, label)) {
            MetadataTreeAnalyzer.errorWithLine("Enumeration ranges overlap", enumerator);
        }

        return high;
    }

    private IntegerDeclaration parseEnumContainerType(
            CommonTree enumContainerType) throws ParsingException {
        assert(enumContainerType.getType() == TSDLParser.ENUM_CONTAINER_TYPE);

        // Get the child, which should be a type specifier list
        assert(enumContainerType.getChildCount() == 1);
        CommonTree typeSpecifierList = (CommonTree) enumContainerType.getChild(0);

        // Parse it and get the corresponding declaration
        IDeclaration decl = parseTypeSpecifierList(typeSpecifierList, null);

        // If is is an integer, return it, else throw an error
        if (decl instanceof IntegerDeclaration) {
            return (IntegerDeclaration) decl;
        }
        
        MetadataTreeAnalyzer.errorWithLine("Enumeration container type must be an integer", enumContainerType);
        return null;
    }

    private VariantDeclaration parseVariant(CommonTree variant) throws ParsingException {
        assert(variant.getType() == TSDLParser.VARIANT);

        // Get children
        List<CommonTree> children = variant.getChildren();
        VariantDeclaration variantDeclaration = null;
        
        // Visiting info
        this.visitor.infoParsingVariant(variant);

        boolean hasName = false;
        String variantName = null;

        boolean hasBody = false;
        CommonTree variantBody = null;
        boolean hasTag = false;
        String variantTag = null;

        // Browse children
        for (CommonTree child : children) {
            // According to type
            switch (child.getType()) {
            case TSDLParser.VARIANT_NAME:
                assert(variantName == null);

                hasName = true;
                assert(child.getChildCount() == 1);
                CommonTree variantNameIdentifier = (CommonTree) child.getChild(0);

                assert(variantNameIdentifier.getType() == TSDLParser.IDENTIFIER);
                variantName = variantNameIdentifier.getText();
                break;
                
            case TSDLParser.VARIANT_TAG:
                assert(variantTag == null);

                hasTag = true;
                assert(child.getChildCount() == 1);
                CommonTree variantTagIdentifier = (CommonTree) child.getChild(0);

                assert(variantTagIdentifier.getType() == TSDLParser.IDENTIFIER);
                variantTag = variantTagIdentifier.getText();
                break;
                
            case TSDLParser.VARIANT_BODY:
                assert(variantBody == null);

                hasBody = true;
                variantBody = child;
                break;
                
            default:
                MetadataTreeAnalyzer.childTypeError(child);
                break;
            }
        }

        if (hasBody) {
            /*
             * If variant has a name, check if already defined in the current
             * scope.
             */
            if (hasName && (this.getCurrentScope().lookupVariant(variantName) != null)) {
                MetadataTreeAnalyzer.errorWithLine("Variant \"" + variantName + "\" already defined", variant);
            }

            // Create the declaration
            variantDeclaration = new VariantDeclaration();

            // Parse the body
            this.parseVariantBody(variantBody, variantDeclaration);

            // If variant has name, add it to the current scope
            if (hasName) {
                this.getCurrentScope().registerVariant(variantName, variantDeclaration);
            }
        } else {
            if (hasName) {
                // Name, no body

                // Lookup the name in the current scope
                variantDeclaration = this.getCurrentScope().rlookupVariant(variantName);

                /*
                 * If not found, it means that a variant with such name has not
                 * been defined.
                 */
                if (variantDeclaration == null) {
                    MetadataTreeAnalyzer.errorWithLine("Variant \"" + variantName + "\" is not defined", variant);
                }
            } else {
                // No name, no body

                // We can't do anything with that
                MetadataTreeAnalyzer.errorWithLine("Variant found with no name and no body", variant);
            }
        }

        // Set tag if has one
        if (hasTag && variantDeclaration != null) {
            variantDeclaration.setTag(variantTag);
        }

        assert(variantDeclaration != null);
        
        return variantDeclaration;
    }

    private void parseVariantBody(CommonTree variantBody,
            VariantDeclaration variantDeclaration) throws ParsingException {
        assert(variantBody.getType() == TSDLParser.VARIANT_BODY);

        // Get children
        List<CommonTree> variantDeclarations = variantBody.getChildren();
        assert(variantDeclarations != null);

        this.pushScope();

        // Browse declarations
        for (CommonTree declarationNode : variantDeclarations) {
            // According to type
            switch (declarationNode.getType()) {
            case TSDLParser.TYPEALIAS:
                this.parseTypealias(declarationNode);
                break;
                
            case TSDLParser.TYPEDEF:
                this.parseTypedef(declarationNode);
                break;
                
            case TSDLParser.SV_DECLARATION:
                this.parseVariantDeclaration(declarationNode, variantDeclaration);
                break;
                
            default:
                MetadataTreeAnalyzer.childTypeError(declarationNode);
                break;
            }
        }

        this.popScope();
    }

    private void parseVariantDeclaration(CommonTree declaration,
            VariantDeclaration variant) throws ParsingException {
        assert(declaration.getType() == TSDLParser.SV_DECLARATION);

        // Get children
        List<CommonTree> children = declaration.getChildren();
        assert(children != null);

        // Get the type specifier list node
        CommonTree typeSpecifierListNode = (CommonTree) declaration
                .getFirstChildWithType(TSDLParser.TYPE_SPECIFIER_LIST);
        assert(typeSpecifierListNode != null);

        // Get the type declarator list node
        CommonTree typeDeclaratorListNode = (CommonTree) declaration
                .getFirstChildWithType(TSDLParser.TYPE_DECLARATOR_LIST);
        assert(typeDeclaratorListNode != null);

        // Get the type declarator list
        List<CommonTree> typeDeclaratorList = typeDeclaratorListNode.getChildren();
        assert(typeDeclaratorList != null);

        /*
         * For each type declarator, parse the declaration and add a field to
         * the variant.
         */
        for (CommonTree typeDeclaratorNode : typeDeclaratorList) {
            assert(typeDeclaratorNode.getType() == TSDLParser.TYPE_DECLARATOR);

            StringBuilder identifierSB = new StringBuilder();
            
            IDeclaration decl = parseTypeDeclarator(typeDeclaratorNode,
                    typeSpecifierListNode, identifierSB);

            if (variant.hasField(identifierSB.toString())) {
                MetadataTreeAnalyzer.errorWithLine("Duplicate field \"" +
                        identifierSB.toString() + "\" for type \"variant\"", typeDeclaratorNode);
            }

            // Finally, add field
            variant.addField(identifierSB.toString(), decl);
        }
    }

    private static String createTypeDeclarationString(
            CommonTree typeSpecifierList, List<CommonTree> pointers)
            throws ParsingException {
        StringBuilder sb = new StringBuilder();

        MetadataTreeAnalyzer.createTypeSpecifierListString(typeSpecifierList, sb);
        MetadataTreeAnalyzer.createPointerListString(pointers, sb);

        return sb.toString();
    }

    private static void createTypeSpecifierListString(
            CommonTree typeSpecifierList, StringBuilder sb)
            throws ParsingException {
        assert(typeSpecifierList.getType() == TSDLParser.TYPE_SPECIFIER_LIST);

        // Get children
        List<CommonTree> children = typeSpecifierList.getChildren();
        assert(children != null);

        boolean firstItem = true;
        for (CommonTree child : children) {
            if (!firstItem) {
                sb.append(' ');
            }
            firstItem = false;

            // Append the string that represents this type specifier
            MetadataTreeAnalyzer.createTypeSpecifierString(child, sb);
        }
    }

    private static void createTypeSpecifierString(CommonTree typeSpecifier,
            StringBuilder sb) throws ParsingException {
        switch (typeSpecifier.getType()) {
        case TSDLParser.FLOATTOK:
        case TSDLParser.INTTOK:
        case TSDLParser.LONGTOK:
        case TSDLParser.SHORTTOK:
        case TSDLParser.SIGNEDTOK:
        case TSDLParser.UNSIGNEDTOK:
        case TSDLParser.CHARTOK:
        case TSDLParser.DOUBLETOK:
        case TSDLParser.VOIDTOK:
        case TSDLParser.BOOLTOK:
        case TSDLParser.COMPLEXTOK:
        case TSDLParser.IMAGINARYTOK:
        case TSDLParser.CONSTTOK:
        case TSDLParser.IDENTIFIER:
            sb.append(typeSpecifier.getText());
            break;
            
        case TSDLParser.STRUCT:
            CommonTree structName = (CommonTree) typeSpecifier.getFirstChildWithType(TSDLParser.STRUCT_NAME);
            if (structName == null) {
                MetadataTreeAnalyzer.errorWithLine("Nameless structure found", typeSpecifier);
            } else {
                CommonTree structNameIdentifier = (CommonTree) structName.getChild(0);
                assert(structNameIdentifier.getType() == TSDLParser.IDENTIFIER);
    
                sb.append(structNameIdentifier.getText());
            }
            break;

        case TSDLParser.VARIANT:
            CommonTree variantName = (CommonTree) typeSpecifier.getFirstChildWithType(TSDLParser.VARIANT_NAME);
            if (variantName == null) {
                MetadataTreeAnalyzer.errorWithLine("Nameless variant found", typeSpecifier);
            } else {
                CommonTree variantNameIdentifier = (CommonTree) variantName.getChild(0);
                assert(variantNameIdentifier.getType() == TSDLParser.IDENTIFIER);

                sb.append(variantNameIdentifier.getText());
            }
            break;

        case TSDLParser.ENUM:
            CommonTree enumName = (CommonTree) typeSpecifier.getFirstChildWithType(TSDLParser.ENUM_NAME);
            if (enumName == null) {
                MetadataTreeAnalyzer.errorWithLine("Nameless enumeration found", typeSpecifier);
            } else {
                CommonTree enumNameIdentifier = (CommonTree) enumName.getChild(0);
                assert(enumNameIdentifier.getType() == TSDLParser.IDENTIFIER);
    
                sb.append(enumNameIdentifier.getText());
            }
            break;

        case TSDLParser.FLOATING_POINT:
        case TSDLParser.INTEGER:
        case TSDLParser.STRING:
            MetadataTreeAnalyzer.errorWithLine("CTF basic type (\"integer\", \"float\" or \"string\") found at wrong place", typeSpecifier);
            break;

        default:
            MetadataTreeAnalyzer.childTypeError(typeSpecifier);
            break;
        }
    }

    private static void createPointerListString(List<CommonTree> pointerList,
            StringBuilder sb) {
        if (pointerList == null) {
            return;
        }

        // Browse the list of pointers
        for (CommonTree pointer : pointerList) {
            assert(pointer.getType() == TSDLParser.POINTER);

            sb.append(" *");
            if (pointer.getChildCount() > 0) {
                assert(pointer.getChildCount() == 1);
                CommonTree constQualifier = (CommonTree) pointer.getChild(0);
                assert(constQualifier.getType() == TSDLParser.CONSTTOK);

                sb.append(" const");
            }
        }
    }

    private static boolean isUnaryExpression(CommonTree node) {
        return MetadataTreeAnalyzer.isUnaryInteger(node) || MetadataTreeAnalyzer.isUnaryString(node);
    }

    private static boolean isUnaryString(CommonTree node) {
        return ((node.getType() == TSDLParser.UNARY_EXPRESSION_STRING) ||
                (node.getType() == TSDLParser.UNARY_EXPRESSION_STRING_QUOTES));
    }

    private static boolean isUnaryInteger(CommonTree node) {
        return ((node.getType() == TSDLParser.UNARY_EXPRESSION_DEC) ||
                (node.getType() == TSDLParser.UNARY_EXPRESSION_HEX) ||
                (node.getType() == TSDLParser.UNARY_EXPRESSION_OCT));
    }

    private static String parseUnaryString(CommonTree unaryString) {
        /*
         * It would be really nice to remove the quotes earlier, such as in the
         * parser itself.
         */
        assert(isUnaryString(unaryString));

        assert(unaryString.getChildCount() == 1);
        CommonTree value = (CommonTree) unaryString.getChild(0);
        assert(value != null);
        String strval = value.getText();

        /* Remove quotes */
        if (unaryString.getType() == TSDLParser.UNARY_EXPRESSION_STRING_QUOTES) {
            strval = strval.substring(1, strval.length() - 1);
        }

        return strval;
    }

    private static long parseUnaryInteger(CommonTree unaryInteger) {
        assert(isUnaryInteger(unaryInteger));

        assert(unaryInteger.getChildCount() >= 1);

        // Get children
        List<CommonTree> children = unaryInteger.getChildren();
        CommonTree value = children.get(0);
        String strval = value.getText();

        long intval;

        // Check format
        if (unaryInteger.getType() == TSDLParser.UNARY_EXPRESSION_DEC) {
            // Decimal
            intval = Long.parseLong(strval, 10);
        } else if (unaryInteger.getType() == TSDLParser.UNARY_EXPRESSION_HEX) {
            // Hexadecimal
            intval = Long.parseLong(strval, 0x10);
        } else {
            // Octal if none of the above
            intval = Long.parseLong(strval, 010);
        }

        // The rest of children are sign
        if ((children.size() % 2) == 0) {
            return -intval;
        }
        
        return intval;
    }

    private static int getMajorOrMinor(CommonTree rightNode)
            throws ParsingException {
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);
        assert(rightNode.getChildCount() > 0);

        CommonTree firstChild = (CommonTree) rightNode.getChild(0);

        if (MetadataTreeAnalyzer.isUnaryInteger(firstChild)) {
            if (rightNode.getChildCount() > 1) {
                MetadataTreeAnalyzer.errorWithLine("Invalid value for \"trace.major\" or \"trace.minor\"", rightNode);
            }

            long m = MetadataTreeAnalyzer.parseUnaryInteger(firstChild);

            if (m < 0) {
                MetadataTreeAnalyzer.errorWithLine("Invalid value for \"trace.major\" or \"trace.minor\"", rightNode);
            }

            return (int) m;
        }
        
        MetadataTreeAnalyzer.errorWithLine("Invalid value for \"trace.major\" or \"trace.minor\"", rightNode);
        return -1;
    }

    private static UUID getUUID(CommonTree rightNode) throws ParsingException {
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);
        assert(rightNode.getChildCount() > 0);

        CommonTree firstChild = (CommonTree) rightNode.getChild(0);

        if (MetadataTreeAnalyzer.isUnaryString(firstChild)) {
            if (rightNode.getChildCount() > 1) {
                MetadataTreeAnalyzer.errorWithLine("Invalid UUID string found", rightNode);
            }

            String uuidstr = MetadataTreeAnalyzer.parseUnaryString(firstChild);

            try {
                UUID uuid = UUID.fromString(uuidstr);
                
                return uuid;
            } catch (IllegalArgumentException e) {
                MetadataTreeAnalyzer.errorWithLine("Cannot pass UUID string", rightNode);
            }
        }
        
        MetadataTreeAnalyzer.errorWithLine("Invalid UUID string found", rightNode);
        return null;
    }

    private static boolean getSigned(CommonTree rightNode)
            throws ParsingException {
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);
        assert(rightNode.getChildCount() > 0);

        boolean ret = false;
        CommonTree firstChild = (CommonTree) rightNode.getChild(0);

        if (MetadataTreeAnalyzer.isUnaryString(firstChild)) {
            String strval = MetadataTreeAnalyzer.concatenateUnaryStrings(rightNode.getChildren());

            if (strval.equals(MetadataStrings.TRUE) || strval.equals(MetadataStrings.TRUE2)) {
                ret = true;
            } else if (strval.equals(MetadataStrings.FALSE) || strval.equals(MetadataStrings.FALSE2)) {
                ret = false;
            } else {
                MetadataTreeAnalyzer.errorWithLine("Invalid boolean value \"" +
                        firstChild.getChild(0).getText() + "\"", rightNode);
            }
        } else if (MetadataTreeAnalyzer.isUnaryInteger(firstChild)) {
            // Happens if the value is something like "1234.hello"
            if (rightNode.getChildCount() > 1) {
                MetadataTreeAnalyzer.errorWithLine("Invalid boolean value found", rightNode);
            }

            long intval = parseUnaryInteger(firstChild);

            if (intval == 1) {
                ret = true;
            } else if (intval == 0) {
                ret = false;
            } else {
                MetadataTreeAnalyzer.errorWithLine("Invalid boolean value \"" +
                        firstChild.getChild(0).getText() + "\"", rightNode);
            }
        } else {
            MetadataTreeAnalyzer.errorWithLine("Cannot parse boolean value \"" +
                    firstChild.getText() + "\"", rightNode);
        }

        return ret;
    }

    private static ByteOrder getByteOrder(CommonTree rightNode, boolean nativeAllowed, boolean networkAllowed) throws ParsingException {
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);
        assert(rightNode.getChildCount() > 0);

        CommonTree firstChild = (CommonTree) rightNode.getChild(0);

        if (MetadataTreeAnalyzer.isUnaryString(firstChild)) {
            String strval = MetadataTreeAnalyzer.concatenateUnaryStrings(rightNode.getChildren());

            if (strval.equals(MetadataStrings.LE)) {
                // Little-endian
                return ByteOrder.LITTLE_ENDIAN;
            } else if (strval.equals(MetadataStrings.BE)) {
                // Big-endian
                return ByteOrder.BIG_ENDIAN;
            } else if (strval.equals(MetadataStrings.NETWORK)) {
                if (!networkAllowed) {
                    MetadataTreeAnalyzer.errorWithLine("Cannot have byte order value \"" + strval + "\"", firstChild);
                }
                return ByteOrder.BIG_ENDIAN;
            } else if (strval.equals(MetadataStrings.NATIVE)) {
                if (!nativeAllowed) {
                    MetadataTreeAnalyzer.errorWithLine("Cannot have byte order value \"" + strval + "\"", firstChild);
                }
                // TODO: return the visitor's global byte order
            } else {
                MetadataTreeAnalyzer.errorWithLine("Invalid byte order value \"" + strval + "\" found", firstChild);
            }
        }
        
        MetadataTreeAnalyzer.errorWithLine("Invalid byte order value found", firstChild);
        return null;
    }

    private static boolean isValidAlignment(long alignment) {
        return !((alignment <= 0) || ((alignment & (alignment - 1)) != 0));
    }

    private static long getSize(CommonTree rightNode) throws ParsingException {
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);
        assert(rightNode.getChildCount() > 0);

        CommonTree firstChild = (CommonTree) rightNode.getChild(0);

        if (MetadataTreeAnalyzer.isUnaryInteger(firstChild)) {
            if (rightNode.getChildCount() > 1) {
                MetadataTreeAnalyzer.errorWithLine("Invalid size value found", rightNode);
            }

            long size = MetadataTreeAnalyzer.parseUnaryInteger(firstChild);
            if (size < 1) {
                MetadataTreeAnalyzer.errorWithLine("Invalid size value found", rightNode);
            }

            return size;
        }
        
        MetadataTreeAnalyzer.errorWithLine("Invalid size value found", rightNode);
        return -1;
    }

    private static long getAlignment(CommonTree node) throws ParsingException {
        assert(isUnaryExpression(node) || (node.getType() == TSDLParser.CTF_RIGHT));

        /*
         * If a CTF_RIGHT node was passed, call getAlignment with the first
         * child.
         */
        if (node.getType() == TSDLParser.CTF_RIGHT) {
            if (node.getChildCount() > 1) {
                MetadataTreeAnalyzer.errorWithLine("Invalid alignment value found", node);
            }

            return MetadataTreeAnalyzer.getAlignment((CommonTree) node.getChild(0));
        } else if (MetadataTreeAnalyzer.isUnaryInteger(node)) {
            long alignment = MetadataTreeAnalyzer.parseUnaryInteger(node);

            if (!MetadataTreeAnalyzer.isValidAlignment(alignment)) {
                MetadataTreeAnalyzer.errorWithLine("Invalid value for alignment: " + alignment, node);
            }

            return alignment;
        }
        
        MetadataTreeAnalyzer.errorWithLine("Invalid alignment value found", node);
        return -1;
    }

    private static int getBase(CommonTree rightNode) throws ParsingException {
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);
        assert(rightNode.getChildCount() > 0);

        CommonTree firstChild = (CommonTree) rightNode.getChild(0);

        if (MetadataTreeAnalyzer.isUnaryInteger(firstChild)) {
            if (rightNode.getChildCount() > 1) {
                MetadataTreeAnalyzer.errorWithLine("Invalid base value found", rightNode);
            }

            long intval = MetadataTreeAnalyzer.parseUnaryInteger(firstChild);
            if ((intval == 2) || (intval == 8) || (intval == 10) || (intval == 16)) {
                return (int) intval;
            }
            MetadataTreeAnalyzer.errorWithLine("Invalid base value found", rightNode);
        } else if (MetadataTreeAnalyzer.isUnaryString(firstChild)) {
            String strval = MetadataTreeAnalyzer.concatenateUnaryStrings(rightNode.getChildren());

            if (strval.equals(MetadataStrings.DECIMAL) || strval.equals(MetadataStrings.DEC) ||
                    strval.equals(MetadataStrings.DEC_CTE) || strval.equals(MetadataStrings.INT_MOD) ||
                    strval.equals(MetadataStrings.UNSIGNED_CTE)) {
                return 10;
            } else if (strval.equals(MetadataStrings.HEXADECIMAL) || strval.equals(MetadataStrings.HEX) ||
                    strval.equals(MetadataStrings.X) || strval.equals(MetadataStrings.X2) ||
                    strval.equals(MetadataStrings.POINTER)) {
                return 16;
            } else if (strval.equals(MetadataStrings.OCTAL) || strval.equals(MetadataStrings.OCT) ||
                    strval.equals(MetadataStrings.OCTAL_CTE)) {
                return 8;
            } else if (strval.equals(MetadataStrings.BINARY) || strval.equals(MetadataStrings.BIN)) {
                return 2;
            } else {
                MetadataTreeAnalyzer.errorWithLine("Invalid base value found", rightNode);
            }
        } else {
            MetadataTreeAnalyzer.errorWithLine("Invalid base value found", rightNode);
        }
        
        return -1;
    }

    private static Encoding getEncoding(CommonTree rightNode)
            throws ParsingException {
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);

        CommonTree firstChild = (CommonTree) rightNode.getChild(0);

        if (MetadataTreeAnalyzer.isUnaryString(firstChild)) {
            String strval = MetadataTreeAnalyzer.concatenateUnaryStrings(rightNode.getChildren());

            if (strval.equals(MetadataStrings.UTF8)) {
                // UTF-8
                return Encoding.UTF8;
            } else if (strval.equals(MetadataStrings.ASCII)) {
                // ASCII
                return Encoding.ASCII;
            } else if (strval.equals(MetadataStrings.NONE)) {
                // None
                return Encoding.NONE;
            } else {
                MetadataTreeAnalyzer.errorWithLine("Invalid encoding value found", rightNode);
            }
        }
        
        MetadataTreeAnalyzer.errorWithLine("Invalid encoding value found", rightNode);
        return null;
    }

    private static int getStreamID(CommonTree rightNode) throws ParsingException {
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);
        assert(rightNode.getChildCount() > 0);

        CommonTree firstChild = (CommonTree) rightNode.getChild(0);

        if (MetadataTreeAnalyzer.isUnaryInteger(firstChild)) {
            if (rightNode.getChildCount() > 1) {
                MetadataTreeAnalyzer.errorWithLine("Invalid stream ID value found", rightNode);
            }

            long intval = parseUnaryInteger(firstChild);

            return (int) intval;
        }
        
        MetadataTreeAnalyzer.errorWithLine("Invalid stream ID value found", rightNode);
        return -1;
    }

    private static String getEventName(CommonTree rightNode) throws ParsingException {
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);
        assert(rightNode.getChildCount() > 0);

        CommonTree firstChild = (CommonTree) rightNode.getChild(0);

        if (MetadataTreeAnalyzer.isUnaryString(firstChild)) {
            String str = MetadataTreeAnalyzer.concatenateUnaryStrings(rightNode.getChildren());

            return str;
        }
        
        MetadataTreeAnalyzer.errorWithLine("Invalid event name found", rightNode);
        return null;
    }

    private static int getEventID(CommonTree rightNode) throws ParsingException {
        assert(rightNode.getType() == TSDLParser.CTF_RIGHT);
        assert(rightNode.getChildCount() > 0);

        CommonTree firstChild = (CommonTree) rightNode.getChild(0);

        if (MetadataTreeAnalyzer.isUnaryInteger(firstChild)) {
            if (rightNode.getChildCount() > 1) {
                MetadataTreeAnalyzer.errorWithLine("Invalid event ID value found", rightNode);
            }

            long intval = MetadataTreeAnalyzer.parseUnaryInteger(firstChild);

            return (int) intval;
        }
        
        MetadataTreeAnalyzer.errorWithLine("Invalid event ID value found", rightNode);
        return -1;
    }

    private static String concatenateUnaryStrings(List<CommonTree> strings) {
        assert((strings != null) && (strings.size() > 0));

        StringBuilder sb = new StringBuilder();

        CommonTree first = strings.get(0);
        sb.append(MetadataTreeAnalyzer.parseUnaryString(first));

        boolean isFirst = true;

        for (CommonTree ref : strings) {
            if (isFirst) {
                isFirst = false;
                continue;
            }

            assert((ref.getType() == TSDLParser.ARROW) || (ref.getType() == TSDLParser.DOT));
            assert(ref.getChildCount() == 1);

            CommonTree id = (CommonTree) ref.getChild(0);

            if (ref.getType() == TSDLParser.ARROW) {
                sb.append("->");
            } else {
                // DOT
                sb.append('.');
            }

            sb.append(MetadataTreeAnalyzer.parseUnaryString(id));
        }

        return sb.toString();
    }

    private static void childTypeError(CommonTree child) throws ParsingException {
        CommonTree parent = (CommonTree) child.getParent();
        String error = "Parent \"" + TSDLParser.tokenNames[parent.getType()] +
                "\" cannot have a child of type \"" + TSDLParser.tokenNames[child.getType()] + "\"";
        MetadataTreeAnalyzer.errorWithLine(error, child);
    }
    
    private static void definedMultipleTimesError(String blockName, String field, CommonTree node) {
        MetadataTreeAnalyzer.errorWithLine(blockName + " \"" + field + "\" is defined multiple times", node);
    }
    
    private static void errorWithLine(String msg, CommonTree node) {
        throw new ParsingException(msg + " @ line " + node.getLine());
    }

    private void pushScope() {
        this.scope = new DeclarationScope(this.scope);
        this.visitor.infoPushedScope(this.scope);
    }
    
    private void popScope() {
        assert(this.scope != null);
        this.scope = this.scope.getParentScope();
        this.visitor.infoPopedScope(this.scope);
    }

    private DeclarationScope getCurrentScope() {
        assert(this.scope != null);
        return this.scope;
    }
    
    private class Trace {
        public Trace() {
        }
        
        public Integer major = null;
        public Integer minor = null;
        public UUID uuid = null;
        public ByteOrder bo = null;
        public StructDeclaration packetHeader = null;
    }
    
    private class Stream {
        public Stream() {
        }
        
        public Integer id = null;
        public StructDeclaration eventHeader = null;
        public StructDeclaration eventContext = null;
        public StructDeclaration packetContext = null;
    }
    
    private class Event {
        public Event() {
        }
        
        public String name = null;
        public Integer id = null;
        public Integer streamID = null;
        public StructDeclaration context = null;
        public StructDeclaration fields = null;
        public Integer logLevel = null;
    }
}
