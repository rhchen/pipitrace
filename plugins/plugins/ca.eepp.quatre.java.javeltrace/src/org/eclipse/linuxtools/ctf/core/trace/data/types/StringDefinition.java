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
 * A CTF string definition.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class StringDefinition extends Definition {
    private StringDeclaration declaration;
    private StringBuilder string;

    /**
     * Builds a scopeful string definition.
     * 
     * @param declaration   Definition's declaration
     * @param node          Definition's scope node
     */
    public StringDefinition(StringDeclaration declaration, ScopeNode node) {
        super(node);

        this.declaration = declaration;

        this.string = new StringBuilder(); // TODO: support encoding
    }
    
    /**
     * Builds a deep copy of a string definition.
     * 
     * @param def   String definition to copy
     */
    public StringDefinition(StringDefinition def) {
        super();
        this.declaration = def.declaration; // Ok to reference: doesn't contain data
        
        // Copy mutable string builder
        this.string = new StringBuilder(def.string.toString());
    }
    
    @Override
    public Definition copyOf() {
        return new StringDefinition(this);
    }
    
    public StringDeclaration getDeclaration() {
        return this.declaration;
    }

    public void setDeclaration(StringDeclaration declaration) {
        this.declaration = declaration;
    }

    public StringBuilder getString() {
        return this.string;
    }

    public void setString(StringBuilder string) {
        this.string = string;
    }
    
    public void setString(String str) {
        this.string = new StringBuilder(str);
    }

    public String getValue() {
        return this.string.toString();
    }

    @Override
    public void read(String name, IReader reader) {
        reader.readString(this, name);
    }

    @Override
    public void write(String name, IWriter writer) {
        writer.writeString(this, name);
    }

    @Override
    public long getSize(long offset) {
        // Don't forget the NUL character
        return 8 * (this.string.length() + 1);
    }
    
    @Override
    public String toString(int level) {
        return "string (val = \"" + this.string.toString() + "\")"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
