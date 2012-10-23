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

import java.nio.ByteOrder;

/**
 * A CTF integer declaration.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class IntegerDeclaration implements IDeclaration {
    final private int length;
    final private boolean signed;
    final private int base;
    final private ByteOrder byteOrder;
    final private Encoding encoding;
    final private long alignment;
    final private String clock;

    /**
     * Builds an integer declaration.
     * 
     * @param len       Length (bits)
     * @param signed    True if signed
     * @param base      Display radix
     * @param byteOrder Byte order
     * @param encoding  Encoding
     * @param clock     Mapped clock if any
     * @param alignment Alignment (bits)
     */
    public IntegerDeclaration(int len, boolean signed, int base,
            ByteOrder byteOrder, Encoding encoding, String clock, long alignment) {
        this.length = len;
        this.signed = signed;
        this.base = base;
        this.byteOrder = byteOrder;
        this.encoding = encoding;
        this.clock = clock;
        this.alignment = alignment;
    }
    
    public boolean isSigned() {
        return this.signed;
    }

    public int getBase() {
        return this.base;
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public Encoding getEncoding() {
        return this.encoding;
    }

   public boolean isCharacter() {
        return (this.length == 8) && (this.encoding != Encoding.NONE);
    }

    public int getLength() {
        return this.length;
    }

    public long getAlignment(){
        return this.alignment;
    }

    public String getClock(){
        return this.clock;
    }
    
    @Override
    public IntegerDefinition createDefinition(ScopeNode parentNode, String name) {
        // Create a child scope node
        ScopeNode node = parentNode.addChild(name);
        
        // Create definition
        IntegerDefinition def = new IntegerDefinition(this, node);
        
        // Associate the scope node with the definition
        node.setDef(def);
        
        return def;
    }
    
    @Override
    public IntegerDefinition createDefinition() {
        return new IntegerDefinition(this);
    }

    @Override
    public String toString() {
        /* Only used for debugging */
        return "[declaration] integer[" + Integer.toHexString(hashCode()) + ']'; //$NON-NLS-1$
    }
}
