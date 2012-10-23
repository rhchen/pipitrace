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
import java.util.List;
import java.util.Map;


import ca.eepp.quatre.java.javeltrace.trace.ex.NoSuchFieldException;
import ca.eepp.quatre.java.javeltrace.trace.reader.IReader;
import ca.eepp.quatre.java.javeltrace.trace.writer.IWriter;

/**
 * A CTF structure definition.
 *
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class StructDefinition extends Definition {
    private final StructDeclaration declaration;
    private final HashMap<String, Definition> definitions = new HashMap<String, Definition>();

    /**
     * Builds a scopeful structure definition.
     *
     * @param declaration   Definition's declaration
     * @param node          Definition's scope node
     */
    public StructDefinition(StructDeclaration declaration, ScopeNode node) {
        super(node);
        this.declaration = declaration;

        for (String fName : this.declaration.getFieldsList()) {
            IDeclaration fieldDecl = this.declaration.getFields().get(fName);
            assert (fieldDecl != null);

            Definition def = fieldDecl.createDefinition(node, fName);
            this.definitions.put(fName, def);
        }
    }

    /**
     * Builds a scopeless structure definition.
     *
     * @param declaration   Definition's declaration
     */
    public StructDefinition(StructDeclaration declaration) {
        super((ScopeNode) null);
        this.declaration = declaration;

        for (String fName : declaration.getFieldsList()) {
            IDeclaration fieldDecl = declaration.getFields().get(fName);
            assert (fieldDecl != null);

            Definition def = fieldDecl.createDefinition();
            this.definitions.put(fName, def);
        }
    }

    /**
     * Builds a deep copy of a structure definition.
     *
     * @param def   Structure definition to copy
     */
    public StructDefinition(StructDefinition def) {
        super();
        this.declaration = def.declaration; // Ok to reference: doesn't contain data

        // Copy fields
        for (Map.Entry<String, Definition> ed : def.definitions.entrySet()) {
            this.definitions.put(ed.getKey(), ed.getValue().copyOf());
        }
    }

    @Override
    public Definition copyOf() {
        return new StructDefinition(this);
    }

    public HashMap<String, Definition> getDefinitions() {
        return this.definitions;
    }

    public StructDeclaration getDeclaration() {
        return this.declaration;
    }

    @Override
    public void read(String name, IReader reader) {
        reader.openStruct(this, name);

        // Browse all fields
        final List<String> fieldList = this.declaration.getFieldsList();
        for (String fName : fieldList) {
            Definition def = this.definitions.get(fName);
            assert (def != null);
            def.read(fName, reader);
        }

        reader.closeStruct(this, name);
    }

    @Override
    public void write(String name, IWriter writer) {
        writer.openStruct(this, name);

        // Browse all fields
        final List<String> fieldList = this.declaration.getFieldsList();
        for (String fName : fieldList) {
            Definition def = this.definitions.get(fName);
            assert (def != null);
            def.write(fName, writer);
        }

        writer.closeStruct(this, name);
    }

    public Definition lookupDefinition(String lookupPath) {
        /*
         * The fields are created in order of appearance, so if a variant or
         * sequence refers to a field that is after it, the field's definition
         * will not be there yet in the hashmap.
         */
        Definition retVal = this.definitions.get(lookupPath);
        if (retVal == null) {
            retVal = this.definitions.get("_" + lookupPath); //$NON-NLS-1$
        }
        return retVal;
    }

    /**
     * Looks up an array field.
     *
     * @param name  Field name
     * @return      Field definition or null if not an array or not found
     */
    public ArrayDefinition lookupArray(String name) {
        Definition def = lookupDefinition(name);
        return (ArrayDefinition) ((def instanceof ArrayDefinition) ? def : null);
    }

    /**
     * Looks up an enumeration field.
     *
     * @param name  Field name
     * @return      Field definition or null if not an enumeration or not found
     */
    public EnumDefinition lookupEnum(String name) {
        Definition def = lookupDefinition(name);
        return (EnumDefinition) ((def instanceof EnumDefinition) ? def : null);
    }

    /**
     * Looks up an integer field.
     *
     * @param name  Field name
     * @return      Field definition or null if not an integer or not found
     */
    public IntegerDefinition lookupInteger(String name) {
        Definition def = lookupDefinition(name);
        return (IntegerDefinition) ((def instanceof IntegerDefinition) ? def
                : null);
    }

    /**
     * Looks up a sequence field.
     *
     * @param name  Field name
     * @return      Field definition or null if not a sequence or not found
     */
    public SequenceDefinition lookupSequence(String name) {
        Definition def = lookupDefinition(name);
        return (SequenceDefinition) ((def instanceof SequenceDefinition) ? def
                : null);
    }

    /**
     * Looks up a string field.
     *
     * @param name  Field name
     * @return      Field definition or null if not a string or not found
     */
    public StringDefinition lookupString(String name) {
        Definition def = lookupDefinition(name);
        return (StringDefinition) ((def instanceof StringDefinition) ? def
                : null);
    }

    /**
     * Looks up a structure field.
     *
     * @param name  Field name
     * @return      Field definition or null if not a structure or not found
     */
    public StructDefinition lookupStruct(String name) {
        Definition def = lookupDefinition(name);
        return (StructDefinition) ((def instanceof StructDefinition) ? def
                : null);
    }

    /**
     * Looks up a variant field.
     *
     * @param name  Field name
     * @return      Field definition or null if not a variant or not found
     */
    public VariantDefinition lookupVariant(String name) {
        Definition def = lookupDefinition(name);
        return (VariantDefinition) ((def instanceof VariantDefinition) ? def
                : null);
    }

    /**
     * Gets an integer field's value directly.
     * <p>
     * The field may also be an enumeration, in which case its underlying
     * integer definition's value will be returned.
     *
     * @param name  Integer/enumeration field name
     * @return      Field value
     * @throws NoSuchFieldException If field doesn't exist
     */
    public long getIntFieldValue(String name) throws NoSuchFieldException {
        IntegerDefinition fieldDef = this.lookupInteger(name);
        if (fieldDef == null) {
            EnumDefinition enumDef = this.lookupEnum(name);
            if (enumDef == null) {
                throw new NoSuchFieldException("Field \"" + name + "\" of this structure is not an integer"); //$NON-NLS-1$ //$NON-NLS-2$
            }

            return enumDef.getIntegerValue();
        }

        return fieldDef.getValue();
    }

    /**
     * Sets an integer field's value directly.
     * <p>
     * The field may also be an enumeration, in which case this method sets
     * its underlying integer definition's value.
     *
     * @param name  Integer/enumeration field name
     * @param value Field value
     * @throws NoSuchFieldException If field doesn't exist
     */
    public void setIntFieldValue(String name, long value) throws NoSuchFieldException {
        IntegerDefinition fieldDef = this.lookupInteger(name);
        if (fieldDef == null) {
            throw new NoSuchFieldException("Field \"" + name + "\" of this structure does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        fieldDef.setValue(value);
    }

    /**
     * Checks whether this structure has a field with a given name.
     *
     * @param fieldName Field name to check
     * @return          True if field exists
     */
    public boolean hasField(String fieldName) {
        return this.declaration.hasField(fieldName);
    }

    @Override
    public long getSize(long offset) {
        // Align
        long at = Definition.align(offset, this.declaration.getAlignment());

        // Browse all fields
        final List<String> fieldList = this.declaration.getFieldsList();
        for (String fName : fieldList) {
            Definition def = this.definitions.get(fName);
            assert (def != null);
            at += def.getSize(at);
        }

        return at - offset;
    }

    @Override
    public String toString(int level) {
        StringBuilder sb = new StringBuilder();

        if (this.definitions == null) {
            sb.append("struct { }"); //$NON-NLS-1$
        } else {
            if (this.definitions.size() == 0) {
                sb.append("struct { }"); //$NON-NLS-1$
            } else {
                sb.append("struct {\n"); //$NON-NLS-1$
                List<String> keys = this.declaration.getFieldsList();
                for (int i = 0; i < keys.size() - 1; ++i) {
                    String key = keys.get(i);
                    Definition def = this.definitions.get(key);
                    sb.append(Definition.getIndentString(level + 1) + key + ": "); //$NON-NLS-1$
                    sb.append(def.toString(level + 1));
                    sb.append(",\n"); //$NON-NLS-1$
                }
                sb.append(Definition.getIndentString(level + 1) + keys.get(this.definitions.size() - 1) + ": "); //$NON-NLS-1$
                sb.append(this.definitions.get(keys.get(keys.size() - 1)).toString(level + 1) + "\n"); //$NON-NLS-1$
                sb.append(Definition.getIndentString(level) + "}"); //$NON-NLS-1$
            }
        }

        return sb.toString();
    }
}
