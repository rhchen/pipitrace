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
 * A CTF integer number definition.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class IntegerDefinition extends Definition {
    private final IntegerDeclaration declaration;
    private long value;

    /**
     * Builds a scopeful integer definition.
     * 
     * @param declaration   Definition's declaration
     * @param node          Definition's scope node
     */
    public IntegerDefinition(IntegerDeclaration declaration, ScopeNode node) {
        super(node);
        this.declaration = declaration;
    }
    
    /**
     * Builds a scopeless integer definition. 
     * 
     * @param declaration   Definition's declaration
     */
    public IntegerDefinition(IntegerDeclaration declaration) {
        super((ScopeNode) null);
        this.declaration = declaration;
    }
    
    /**
     * Builds a deep copy of an integer definition.
     * 
     * @param def   Integer definition to copy
     */
    public IntegerDefinition(IntegerDefinition def) {
        super();
        this.declaration = def.declaration; // Ok to reference: doesn't contain data
        
        // Assign value
        this.value = def.value;
    }
    
    @Override
    public Definition copyOf() {
        return new IntegerDefinition(this);
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long val) {
        this.value = val;
    }

    public IntegerDeclaration getDeclaration() {
        return this.declaration;
    }

    @Override
    public void read(String name, IReader reader) {
        reader.readInteger(this, name);
    }
    
    @Override
    public void write(String name, IWriter writer) {
        writer.writeInteger(this, name);
    }

    @Override
    public long getSize(long offset) {
        // Align
        long at = Definition.align(offset, this.declaration.getAlignment());
        
        // Add length
        at += this.declaration.getLength();
        
        return at - offset;
    }
    
    @Override
    public String toString(int level) {
        return "int (val = " + this.value + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
