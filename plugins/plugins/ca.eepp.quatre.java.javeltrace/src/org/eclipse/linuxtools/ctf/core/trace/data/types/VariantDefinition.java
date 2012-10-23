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
import java.util.Map;


import ca.eepp.quatre.java.javeltrace.trace.reader.IReader;
import ca.eepp.quatre.java.javeltrace.trace.writer.IWriter;

/**
 * A CTF variant definition.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class VariantDefinition extends Definition {
    private final VariantDeclaration declaration;
    private EnumDefinition tagDefinition = null;
    private HashMap<String, Definition> definitions = new HashMap<String, Definition>();
    private String currentFieldName;

    /**
     * Builds a scopeful variant definition.
     * 
     * @param declaration   Definition's declaration
     * @param node          Definition's scope node
     */
    public VariantDefinition(VariantDeclaration declaration, ScopeNode node) {
        super(node);
        this.declaration = declaration;
        
        /*
         * Find the enum definition using the scope. This, along with the
         * sequence type, is the very purpose of having scopes. We need to find
         * the last created enum definition pointed to by the variant path.
         */
        this.tagDefinition = (EnumDefinition) node.lookupPath(this.declaration.getTag()).getDef();
        
        // Create all possible definitions
        for (Map.Entry<String, IDeclaration> field : declaration.getFields().entrySet()) {
            Definition fieldDef = field.getValue().createDefinition(node, field.getKey());
            this.definitions.put(field.getKey(), fieldDef);
        }
    }
    
    /**
     * Builds a scopeless variant definition.
     * 
     * @param declaration   Definition's declaration
     */
    public VariantDefinition(VariantDeclaration declaration) {
        super((ScopeNode) null);
        this.declaration = declaration;
    }
    
    /**
     * Builds a deep copy of a variant definition.
     * 
     * @param def   Variant definition to copy
     */
    public VariantDefinition(VariantDefinition def) {
        super();
        this.declaration = def.declaration; // Ok to reference: doesn't contain data
        
        // Assign current field name
        this.currentFieldName = def.currentFieldName;
        
        // Copy tag definition
        this.tagDefinition = new EnumDefinition(def.tagDefinition);
        
        // Copy fields
        for (Map.Entry<String, Definition> ed : def.definitions.entrySet()) {
            this.definitions.put(ed.getKey(), ed.getValue().copyOf());
        }
    }
    
    @Override
    public Definition copyOf() {
        return new VariantDefinition(this);
    }
    
    public VariantDeclaration getDeclaration() {
        return this.declaration;
    }
    
    public EnumDefinition getTagDefinition() {
        return this.tagDefinition;
    }

    public void setTagDefinition(EnumDefinition tagDefinition) {
        this.tagDefinition = tagDefinition;
    }

    public HashMap<String, Definition> getDefinitions() {
        return this.definitions;
    }

    public void setDefinitions(HashMap<String, Definition> definitions) {
        this.definitions = definitions;
    }

    public void setCurrentField(String currentField) {
        this.currentFieldName = currentField;
    }
    
    @Override
    public void read(String name, IReader reader) {
        // Update
        if (this.tagDefinition != null) {
            this.currentFieldName = this.tagDefinition.getValue();
        }
        
        // Read
        reader.openVariant(this, name);
        this.getCurrentField().read(name, reader);
        reader.closeVariant(this, name);
    }
    
    @Override
    public void write(String name, IWriter writer) {
        writer.openVariant(this, name);
        this.getCurrentField().write(name, writer); // Should be enough
        writer.closeVariant(this, name);
    }

    public Definition lookupDefinition(String lookupPath) {
        return this.definitions.get(lookupPath);
    }

    public String getCurrentFieldName() {
        return this.currentFieldName;
    }

    /**
     * Gets the current field definition.
     * <p>
     * The linked enumeration's value is not read prior to getting the current
     * field. The last read field name (enumeration label) is used.
     * 
     * @return  Current field definition
     */
    public Definition getCurrentField() {
        return this.definitions.get(this.currentFieldName);
    }

    @Override
    public long getSize(long offset) {
        // Current, selected definition
        Definition field = this.getCurrentField();
        
        // Return its size
        return field.getSize(offset);
    }

    @Override
    public String toString(int level) {
        StringBuilder sb = new StringBuilder();
        sb.append("variant ("); //$NON-NLS-1$
        Definition currentField = this.getCurrentField();
        if (currentField != null) {
            sb.append("\n" + Definition.getIndentString(level + 1) + currentField.toString(level + 1) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            sb.append(Definition.getIndentString(level) + " "); //$NON-NLS-1$
        }
        sb.append(Definition.getIndentString(level) + ")"); //$NON-NLS-1$
        
        return sb.toString();
    }
}
