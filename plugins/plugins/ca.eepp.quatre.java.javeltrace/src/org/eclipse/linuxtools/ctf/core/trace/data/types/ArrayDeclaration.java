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

/**
 * A CTF array declaration.
 * <p>
 * An array has a static length.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class ArrayDeclaration implements IDeclaration {
    private final int length;
    private final IDeclaration elemType;

    /**
     * Builds an array declaration.
     * 
     * @param length    Array length
     * @param elemType  Contained element type
     */
    public ArrayDeclaration(int length, IDeclaration elemType) {
        this.length = length;
        this.elemType = elemType;
    }

    public IDeclaration getElementType() {
        return this.elemType;
    }

    public int getLength() {
        return this.length;
    }

    @Override
    public long getAlignment() {
        long retVal = this.getElementType().getAlignment();
        return retVal;
    }
    
    @Override
    public String toString() {
        // Only used for debugging
        return "[declaration] array[" + Integer.toHexString(hashCode()) + ']'; //$NON-NLS-1$
    }

    @Override
    public ArrayDefinition createDefinition(ScopeNode parentNode, String name) {
        /*
         * We don't create a scope node for the array itself since it's never
         * going to be addressed directly. Each of its elements will have the
         * same parent scope and different names according to their indexes.
         */
        ArrayDefinition def = new ArrayDefinition(this, parentNode, name);
        
        return def;
    }
    
    @Override
    public ArrayDefinition createDefinition() {
        return new ArrayDefinition(this);
    }
}
