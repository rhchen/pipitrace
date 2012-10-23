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

/**
 * A CTF variant declaration.
 * <p>
 * A variant is a placeholder for any other real type. Which type to choose
 * depends on the dynamic value of an enumeration. 
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class VariantDeclaration implements IDeclaration {
    private String tagPath = null;
    final static private long alignment = 1;
    private final HashMap<String, IDeclaration> fields = new HashMap<String, IDeclaration>();

    /**
     * Checks whether this variant is tagged or not.
     * 
     * @return  True if tagged
     */
    public boolean isTagged() {
        return this.tagPath != null;
    }

    /**
     * Checks whether this variant has a field name.
     * 
     * @param fieldTag  Field name
     * @return          True of field name exists
     */
    public boolean hasField(String fieldTag) {
        return this.fields.containsKey(fieldTag);
    }

    public void setTag(String tag) {
        this.tagPath = tag;
    }

    public String getTag() {
        return this.tagPath;
    }

    /**
     * Gets the hash map of field declarations.
     *  
     * @return  Hash map (field name to field declaration)
     */
    public HashMap<String, IDeclaration> getFields() {
        return this.fields;
    }

    @Override
    public long getAlignment() {
        return alignment;
    }
    
    @Override
    public VariantDefinition createDefinition(ScopeNode parentNode, String name) {
        // Create a child scope node
        ScopeNode node = parentNode.addChild(name);
        
        // Create definition
        VariantDefinition def = new VariantDefinition(this, node);
        
        // Associate the scope node with the definition
        node.setDef(def);
        
        return def;
    }
    
    @Override
    public VariantDefinition createDefinition() {
        return new VariantDefinition(this);
    }

    /**
     * Adds a field declaration to this variant.
     * 
     * @param fieldTag      Field name
     * @param declaration   Field declaration
     */
    public void addField(String fieldTag, IDeclaration declaration) {
        this.fields.put(fieldTag, declaration);
    }

    @Override
    public String toString() {
        /* Only used for debugging */
        return "[declaration] variant[" + Integer.toHexString(hashCode()) + ']'; //$NON-NLS-1$
    }
}