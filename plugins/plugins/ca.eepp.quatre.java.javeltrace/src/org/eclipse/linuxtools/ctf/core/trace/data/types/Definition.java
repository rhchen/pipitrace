/*******************************************************************************
 * Copyright (c) 2011-2012 Ericsson, Ecole Polytechnique de Montreal and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Matthew Khouzam - Initial API and implementation
 * Contributors: Simon Marchi - Initial API and implementation
 *******************************************************************************/

package org.eclipse.linuxtools.ctf.core.trace.data.types;


import ca.eepp.quatre.java.javeltrace.trace.ex.NoSuchDefinitionException;
import ca.eepp.quatre.java.javeltrace.trace.reader.IReader;
import ca.eepp.quatre.java.javeltrace.trace.writer.IWriter;

/**
 * Abstract class for a CTF type definition.
 * <p>
 * The general purpose of a definition is to be read and written. It's also
 * linked to a scope node which will usually belong to a scope tree for
 * dynamic lookups.
 * <p>
 * Definitions must also implement {@link #copyOf()}, which creates a deep
 * copy of themselves.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public abstract class Definition {
    protected ScopeNode scopeNode;
    
    /**
     * Builds a scopeful definition.
     * 
     * @param scopeNode Linked scope node
     */
    public Definition(ScopeNode scopeNode) {
        this.scopeNode = scopeNode;
    }
    
    /**
     * Builds a scopeless definition.
     */
    public Definition() {
        this.scopeNode = null;
    }
    
    /**
     * Reads itself using a generic reader.
     * <p>
     * Simple types must call the appropriate reader method while compound
     * types must also call the reader for all their elements. 
     * 
     * @param name      Name of this field within its container (will be passed back to the reader)
     * @param reader    Reader to use
     */
    public abstract void read(String name, IReader reader);
    
    /**
     * Reads itself using a generic writer.
     * <p>
     * Simple types must call the appropriate writer method while compound
     * types must also call the writer for all their elements. 
     * 
     * @param name      Name of this field within its container (will be passed back to the writer)
     * @param writer    Writer to use
     */
    public abstract void write(String name, IWriter writer);
    
    /**
     * Gets the real definition binary size.
     * 
     * @param offset    Initial definition offset (bits)
     * @return          Real size (bits)
     */
    public abstract long getSize(long offset);
    
    /**
     * Gets the definition content string (debug).
     * 
     * @param level Initial indentation level
     * @return      Content string
     */
    public abstract String toString(int level);
    
    /**
     * Creates a deep copy if itself.
     * 
     * @return  Deep copy of itself
     */
    public abstract Definition copyOf();
    
    @Override
    public String toString() {
        return this.toString(0);
    }
    
    /**
     * Computes alignment.
     * 
     * @param off   Initial offset
     * @param align Alignement (power of 2)
     * @return      New offset
     */
    public static long align(long off, long align) {
        return (off + align - 1) & ~(align - 1);
    }
    
    protected static String getIndentString(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; ++i) {
            sb.append("  "); //$NON-NLS-1$
        }
        
        return sb.toString();
    }
    
    /**
     * Gets the linked scope node.
     * 
     * @return  Linked scope node or null if none
     */
    public ScopeNode getScopeNode() {
        return this.scopeNode;
    }
    
    /**
     * Sets the linked scope node.
     * 
     * @param node  Linked scope node
     */
    public void setScopeNode(ScopeNode node) {
        this.scopeNode = node;
    }
    
    protected Definition resolveDef(String path) {
        if (this.scopeNode == null) {
            // TODO: throw something bad
        }
        
        // Try to find the node
        ScopeNode node = this.scopeNode.lookupPath(path);
        
        // Should be found
        if (node == null) {
            throw new NoSuchDefinitionException("Definition @ \"" + path + "\" cannot be found"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        return node.getDef();
    }
}
