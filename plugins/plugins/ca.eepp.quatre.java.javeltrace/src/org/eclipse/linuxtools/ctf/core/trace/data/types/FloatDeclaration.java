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

import java.nio.ByteOrder;

/**
 * A CTF floating point number declaration.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class FloatDeclaration implements IDeclaration {
    private final int mant;
    private final int exp;
    private final ByteOrder byteOrder;
    private final long alignment;

    /**
     * Builds a floating point number declaration.
     * 
     * @param exponent  Exponent size (bits)
     * @param mantissa  Mantissa size (bits)
     * @param byteOrder Byte order
     * @param alignment Alignment (bits)
     */
    public FloatDeclaration(int exponent, int mantissa, ByteOrder byteOrder, long alignment) {
        this.mant = mantissa;
        this.exp = exponent;
        this.byteOrder = byteOrder;
        this.alignment = alignment;

    }

    public int getMantissa() {
        return this.mant;
    }

    public int getExponent() {
        return this.exp;
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    @Override
    public long getAlignment() {
        return this.alignment;
    }

    @Override
    public FloatDefinition createDefinition(ScopeNode parentNode, String name) {
        // Create a child scope node
        ScopeNode node = parentNode.addChild(name);
        
        // Create definition
        FloatDefinition def = new FloatDefinition(this, node);
        
        // Associate the scope node with the definition
        node.setDef(def);
        
        return def;
    }
    
    @Override
    public FloatDefinition createDefinition() {
        return new FloatDefinition(this);
    }

    @Override
    public String toString() {
        /* Only used for debugging */
        return "[declaration] float[" + Integer.toHexString(hashCode()) + ']'; //$NON-NLS-1$
    }

    /**
     * Gets the total size (bits).
     * 
     * @return  Total size (bits)
     */
    public long getTotalBits() {
        return this.mant + this.exp;
    }
}
