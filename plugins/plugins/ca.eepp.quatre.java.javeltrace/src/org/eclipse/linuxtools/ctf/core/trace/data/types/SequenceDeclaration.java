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
 * A CTF sequence declaration.
 * <p>
 * A sequence has a path where its definition can dynamically find
 * its length at read time. 
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class SequenceDeclaration implements IDeclaration {
    private final IDeclaration elemType;
    private final String lengthPath;

    /**
     * Builds a sequence declaration.
     * 
     * @param elemType      Contained element type
     * @param lengthPath    Length path
     */
    public SequenceDeclaration(IDeclaration elemType, String lengthPath) {
        this.elemType = elemType;
        this.lengthPath = lengthPath;
    }

    public IDeclaration getElementType() {
        return this.elemType;
    }

    @Override
    public long getAlignment() {
        return getElementType().getAlignment();
    }
    
    @Override
    public SequenceDefinition createDefinition(ScopeNode parentNode, String name) {
        /*
         * We don't create a scope node for the sequence itself since it's never
         * going to be addressed directly. Each of its elements will have the
         * same parent scope and different names according to their indexes.
         */
        SequenceDefinition def = new SequenceDefinition(this, parentNode, name);
        
        return def;
    }
    
    @Override
    public SequenceDefinition createDefinition() {
        return new SequenceDefinition(this);
    }

    @Override
    public String toString() {
        /* Only used for debugging */
        return "[declaration] sequence[" + Integer.toHexString(hashCode()) + ']'; //$NON-NLS-1$
    }

    public String getLengthPath() {
        return this.lengthPath;
    }
}