/*******************************************************************************
 * Copyright (c) 2011-2012 Ericsson, Ecole Polytechnique de Montreal and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Matthew Khouzam - Initial API and implementation
 *******************************************************************************/

package org.eclipse.linuxtools.ctf.core.trace.data.types;


import ca.eepp.quatre.java.javeltrace.trace.reader.IReader;
import ca.eepp.quatre.java.javeltrace.trace.writer.IWriter;

/**
 * A CTF floating point number definition.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class FloatDefinition extends Definition {
    private final FloatDeclaration declaration;
    private long raw;
    private double value;

    /**
     * Builds a new scopeful floating point number definition.
     * 
     * @param declaration   Definition's declaration
     * @param node          Definition's scope node
     */
    public FloatDefinition(FloatDeclaration declaration, ScopeNode node) {
        super(node);
        this.declaration = declaration;
    }
    
    /**
     * Builds a new scopeless floating point number definition.
     * 
     * @param declaration   Definition's declaration
     */
    public FloatDefinition(FloatDeclaration declaration) {
        super((ScopeNode) null);
        this.declaration = declaration;
    }
    
    /**
     * Builds a deep copy of a floating point number definition.
     * 
     * @param def   Floating point number definition to copy
     */
    public FloatDefinition(FloatDefinition def) {
        super();
        this.declaration = def.declaration; // Ok to reference: doesn't contain data
        
        // Assign value
        this.raw = def.raw;
        this.value = def.value;
    }
    
    @Override
    public Definition copyOf() {
        return new FloatDefinition(this);
    }
    
    public double getDoubleValue() {
        return this.value;
    }
    
    public long getRawValue() {
        return this.raw;
    }
    
    /**
     * Sets the floating point number value.
     * <p>
     * After the value is set, it is possible to get the equivalent double
     * value using {@link #getDoubleValue()}.
     * 
     * @param rawValue   Raw value (read as long)
     */
    public void setValue(long rawValue) {
        int manBits = this.declaration.getMantissa() - 1;
        int expBits = this.declaration.getExponent();
        
        long manShift = 1L << (manBits);
        long manMask = manShift - 1;
        long expMask = (1L << expBits) - 1;

        int exp = (int) ((rawValue >> (manBits)) & expMask) + 1;
        long man = (rawValue & manMask);
        double expPow = Math.pow(2.0, exp - (1 << (expBits - 1)));
        double ret = man * 1.0f;
        ret /= manShift;
        ret += 1.0;
        ret *= expPow;
        
        this.raw = rawValue;
        this.value = ret;
    }

    public FloatDeclaration getDeclaration() {
        return this.declaration;
    }

    @Override
    public void read(String name, IReader reader) {
        reader.readFloat(this, name);
    }

    @Override
    public void write(String name, IWriter writer) {
        writer.writeFloat(this, name);
    }

    @Override
    public long getSize(long offset) {
        // Align
        long at = Definition.align(offset, this.declaration.getAlignment());
        
        // Add total bits
        at += this.declaration.getTotalBits();        
        
        return at - offset;
    }

    @Override
    public String toString(int level) {
        return "float (val = " + this.value + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
