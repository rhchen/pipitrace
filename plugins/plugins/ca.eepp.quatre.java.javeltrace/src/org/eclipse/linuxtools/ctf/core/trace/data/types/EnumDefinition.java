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
 * A CTF enumeration definition.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class EnumDefinition extends Definition {
    private final EnumDeclaration declaration;
    private final IntegerDefinition integerDef;
    private String value;

    /**
     * Builds a scopeful enumeration definition.
     * 
     * @param declaration   Definition's declaration
     * @param node          Definition's scope node
     */
    public EnumDefinition(EnumDeclaration declaration, ScopeNode node) {
        super(node);
        this.declaration = declaration;

        this.integerDef = declaration.getContainerType().createDefinition(node, ""); //$NON-NLS-1$
        this.value = ((Long) this.integerDef.getValue()).toString();
    }
    
    /**
     * Builds a scopeless enumeration definition.
     * 
     * @param declaration   Definition's declaration
     */
    public EnumDefinition(EnumDeclaration declaration) {
        super((ScopeNode) null);
        this.declaration = declaration;
        this.integerDef = declaration.getContainerType().createDefinition();
        this.value = ((Long) this.integerDef.getValue()).toString();
    }

    /**
     * Builds a deep copy of an enumeration definition.
     * 
     * @param def   Enumeration definition to copy
     */
    public EnumDefinition(EnumDefinition def) {
        super();
        this.declaration = def.declaration; // Ok to reference: doesn't contain data
        
        // Copy mutable integer definition
        this.integerDef = new IntegerDefinition(def.integerDef);
        
        // Assign value
        this.value = def.value;
    }
    
    @Override
    public Definition copyOf() {
        return new EnumDefinition(this);
    }
    
    public String getValue() {
        return this.value;
    }

    /**
     * Gets the current integer value.
     * 
     * @return  Current integer value
     */
    public long getIntegerValue() {
        return this.integerDef.getValue();
    }
    
    /**
     * Gets the underlying integer definition.
     * 
     * @return  Underlying integer definition
     */
    public IntegerDefinition getIntegerDefinition() {
        return this.integerDef;
    }

    /**
     * Sets the current integer value.
     * 
     * @param value Current integer value
     */
    public void setIntegerValue(long value) {
        this.integerDef.setValue(value);
        this.value = ((Long) this.integerDef.getValue()).toString();
    }

    @Override
    public void read(String name, IReader reader) {
        reader.readEnum(this, name);
        this.value = this.declaration.query(this.getIntegerValue());
    }
    
    @Override
    public void write(String name, IWriter writer) {
        writer.writeEnum(this, name);
    }

    @Override
    public long getSize(long offset) {
        return this.integerDef.getSize(offset);
    }

    @Override
    public String toString(int level) {
        return "enum (val = " + this.integerDef.getValue() + ", label = " + this.value + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}