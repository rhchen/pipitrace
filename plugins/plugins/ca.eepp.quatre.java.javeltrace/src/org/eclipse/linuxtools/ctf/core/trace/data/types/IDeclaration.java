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
 * A CTF type declaration.
 * <p>
 * A type declaration contains informations found in the metadata text for a
 * particular type. For example, an array (see {@link ArrayDeclaration}) will
 * have a declared length but a sequence will have a declared length path
 * (see {@link SequenceDeclaration}).
 * <p>
 * The main purpose of a declaration implementation is to create/initiate a
 * type definition out of itself. This definition may be scopeless if no further
 * scope lookup is required.
 */
public interface IDeclaration {
    /**
     * Creates a new scopeful definition.
     * 
     * @param parentNode    Parent scope node
     * @param name          Name of this definition within its container
     * @return              Created definition
     */
    public Definition createDefinition(ScopeNode parentNode, String name);
    
    /**
     * Creates a new scopeless definition.
     * 
     * @return Created definition
     */
    public Definition createDefinition();
    
    /**
     * Gets the real alignment of this declaration.
     * 
     * @return  Real alignment (bits)
     */
    public long getAlignment();
}