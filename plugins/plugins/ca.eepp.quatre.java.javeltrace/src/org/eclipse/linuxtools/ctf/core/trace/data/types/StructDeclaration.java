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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A CTF structure declaration. 
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class StructDeclaration implements IDeclaration {
    private final HashMap<String, IDeclaration> fields = new HashMap<String, IDeclaration>();
    private final List<String> fieldsList = new LinkedList<String>();
    private long maxAlign;

    /**
     * Builds a structure declaration.
     * 
     * @param align Minimum alignment for this structure
     */
    public StructDeclaration(long align) {
        this.maxAlign = Math.max(align, 1);
    }

    public long getMaxAlign() {
        return this.maxAlign;
    }

    /**
     * Checks whether this structure has a specified field name or not.
     * 
     * @param name  Field name
     * @return      True if field exists
     */
    public boolean hasField(String name) {
        return this.fields.containsKey(name);
    }

    /**
     * Gets the hash map reference of field declarations.
     * 
     * @return  Fields hash map (name to declaration)
     */
    public HashMap<String, IDeclaration> getFields() {
        return this.fields;
    }

    /**
     * Gets the ordered list of field names.
     * 
     * @return  Ordered list of field names
     */
    public List<String> getFieldsList() {
        return this.fieldsList;
    }

    @Override
    public long getAlignment() {
        return this.maxAlign;
    }

    @Override
    public StructDefinition createDefinition(ScopeNode parentNode, String name) {
        // Create a child scope node
        ScopeNode node = parentNode.addChild(name);
        
        // Create definition
        StructDefinition def = new StructDefinition(this, node);
        
        // Associate the scope node with the definition
        node.setDef(def);
        
        return def;
    }
    
    @Override
    public StructDefinition createDefinition() {
        return new StructDefinition(this);
    }

    /**
     * Adds a field declaration to this structure.
     * 
     * @param name          Field name
     * @param declaration   Field declaration
     */
    public void addField(String name, IDeclaration declaration) {
        this.fields.put(name, declaration);
        this.fieldsList.add(name);
        this.maxAlign = Math.max(this.maxAlign, declaration.getAlignment());
    }

    @Override
    public String toString() {
        /* Only used for debugging */
        return "[declaration] struct[" + Integer.toHexString(hashCode()) + ']'; //$NON-NLS-1$
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
                + ((this.fieldsList == null) ? 0 : this.fieldsList.hashCode());
        result = (prime * result) + (int) (this.maxAlign ^ (this.maxAlign >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof StructDeclaration)) {
            return false;
        }
        StructDeclaration other = (StructDeclaration) obj;
        if (this.fieldsList == null) {
            if (other.fieldsList != null) {
                return false;
            }
        } else if (!this.fieldsList.equals(other.fieldsList)) {
            return false;
        }
        if (this.maxAlign != other.maxAlign) {
            return false;
        }
        return true;
    }
}
