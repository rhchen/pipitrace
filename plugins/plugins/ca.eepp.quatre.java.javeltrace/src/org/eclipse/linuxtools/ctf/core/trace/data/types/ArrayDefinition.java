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


import ca.eepp.quatre.java.javeltrace.trace.reader.IReader;
import ca.eepp.quatre.java.javeltrace.trace.writer.IWriter;

/**
 * A CTF array definition.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
@SuppressWarnings("nls")
public class ArrayDefinition extends Definition {
    private final ArrayDeclaration declaration;
    private Definition definitions[];

    /**
     * Builds a scopeful array definition.
     * 
     * @param declaration   Definition's declaration
     * @param node          Definition's scope node
     * @param namePrefix    Name prefix
     */
    public ArrayDefinition(ArrayDeclaration declaration, ScopeNode node, String namePrefix) {
        super(node);
        this.declaration = declaration;
        this.initDefinitions(namePrefix);
    }
    
    /**
     * Builds a scopeless array definition.
     * 
     * @param declaration   Definition's declaration
     */
    public ArrayDefinition(ArrayDeclaration declaration) {
        super((ScopeNode) null);
        this.declaration = declaration;
        this.initDefinitions(null);
    }
    
    /**
     * Builds a deep copy of an array definition.
     * 
     * @param def   Array definition to copy
     */
    public ArrayDefinition(ArrayDefinition def) {
        super();
        this.declaration = def.declaration; // Ok to reference: doesn't contain data
        
        // Copy array
        this.definitions = new Definition[def.definitions.length];
        for (int i = 0; i < def.definitions.length; ++i) {
            this.definitions[i] = def.definitions[i].copyOf();
        }
    }
    
    @Override
    public Definition copyOf() {
        return new ArrayDefinition(this);
    }
    
    private void initDefinitions(String namePrefix) {
        this.definitions = new Definition[this.declaration.getLength()];

        if (this.scopeNode == null) {
            for (int i = 0; i < this.declaration.getLength(); i++) {
                this.definitions[i] = this.declaration.getElementType().createDefinition();
            }
        } else {
            for (int i = 0; i < this.declaration.getLength(); i++) {
                this.definitions[i] = this.declaration.getElementType()
                        .createDefinition(this.scopeNode, namePrefix + "[" + i + "]");
            }
        }
    }
    
    public Definition[] getDefinitions() {
        return this.definitions;
    }
    
    public void setDefinitions(Definition[] definitions) {
        this.definitions = definitions;
    }

    /**
     * Gets the element at a specified index.
     * 
     * @param i Index
     * @return  Element or null if out of range
     */
    public Definition getElem(int i) {
        if (i > this.definitions.length) {
            return null;
        }

        return this.definitions[i];
    }

    public ArrayDeclaration getDeclaration() {
        return this.declaration;
    }

    /**
     * Checks whether or not this array is a string.
     * <p>
     * Sometimes, strings are encoded as an array of 1-byte integers (each one
     * being an UTF-8/ASCII/ISO-* byte).
     *
     * @return True if this array is in fact a string
     */
    public boolean isString() {
        IntegerDeclaration elemInt;

        if (this.declaration.getElementType() instanceof IntegerDeclaration) {
            /*
             * If the first byte is a "character", we'll consider the whole
             * array a character string.
             */
            elemInt = (IntegerDeclaration) this.declaration.getElementType();
            if (elemInt.isCharacter()) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void read(String name, IReader reader) {
        reader.openArray(this, name);
        for (Definition definition : this.definitions) {
            definition.read(null, reader);
        }
        reader.closeArray(this, name);
    }
    
    @Override
    public void write(String name, IWriter writer) {
        writer.openArray(this, name);
        for (Definition definition : this.definitions) {
            definition.write(null, writer);
        }
        writer.closeArray(this, name);
    }

    @Override
    public long getSize(long offset) {
        long at = offset;
        
        for (Definition definition : this.definitions) {
            at += definition.getSize(at);
        }
        
        return at - offset;
    }
    
    /**
     * Gets the array length.
     * 
     * @return  Length of this array
     */
    public int getLength() {
        return this.declaration.getLength();
    }
    
    /**
     * Gets the equivalent array string.
     * <p>
     * If this array is a string (see {@link #isString()}), this method returns
     * said string.
     * 
     * @return  Array string
     */
    public String getString() {
        StringBuilder sb = new StringBuilder();
        
        if (this.isString()) {
            for (Definition def : this.definitions) {
                IntegerDefinition character = (IntegerDefinition) def;
    
                if (character.getValue() == 0) {
                    break;
                }
    
                sb.append((char) character.getValue());
            }
            
            return sb.toString();
        }
        
        return "";
    }

    @Override
    public String toString(int level) {
        StringBuilder sb = new StringBuilder();
        
        if (this.isString()) {
            sb.append("array (elements = \"");
            sb.append(this.getString());
            sb.append("\")");
        } else if (this.definitions == null) {
            sb.append("array [ ]");
        } else {
            if (this.definitions.length == 0) {
                sb.append("array [ ]");
            } else {
                sb.append("array [\n");
                for (int i = 0; i < (this.definitions.length - 1); i++) {
                    sb.append(Definition.getIndentString(level + 1) + i + " => ");
                    sb.append(this.definitions[i].toString(level + 2));
                    sb.append(",\n");
                }
                sb.append(Definition.getIndentString(level + 1) + (this.definitions.length - 1) + " => ");
                sb.append(this.definitions[this.definitions.length - 1].toString(level + 2) + "\n");
                sb.append(Definition.getIndentString(level) + "]");
            }
        }

        return sb.toString();
    }
}
