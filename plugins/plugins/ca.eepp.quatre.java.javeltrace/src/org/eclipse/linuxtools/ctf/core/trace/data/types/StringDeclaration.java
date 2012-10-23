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
 * A CTF string declaration.
 * <p>
 * A string is a null-terminated array of bytes.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class StringDeclaration implements IDeclaration {
    private Encoding encoding = Encoding.UTF8;
    
    /**
     * Builds an UTF-8 string declaration.
     */
    public StringDeclaration() {
    }

    /**
     * Builds a string declaration with specified encoding.
     * 
     * @param encoding  String encoding
     */
    public StringDeclaration(Encoding encoding) {
        this.encoding = encoding;
    }

    public Encoding getEncoding() {
        return this.encoding;
    }
    
    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    @Override
    public long getAlignment() {
        return 8; //FIXME: should be the elementtype.
    }
    
    @Override
    public StringDefinition createDefinition(ScopeNode parentNode, String name) {
        // Create a child scope node
        ScopeNode node = parentNode.addChild(name);
        
        // Create definition
        StringDefinition def = new StringDefinition(this, node);
        
        // Associate the scope node with the definition
        node.setDef(def);
        
        return def;
    }
    
    @Override
    public StringDefinition createDefinition() {
        return new StringDefinition(this, null);
    }

    @Override
    public String toString() {
        /* Only used for debugging */
        return "[declaration] string[" + Integer.toHexString(hashCode()) + ']'; //$NON-NLS-1$
    }
}