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
 * A CTF sequence definition.
 *
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class SequenceDefinition extends Definition {
    private final SequenceDeclaration declaration;
    private IntegerDefinition lengthDefinition;
    private Definition definitions[];
    private int currentLength;
    private final String namePrefix;

    /**
     * Builds a scopeful sequence definition.
     *
     * @param declaration   Definition's declaration
     * @param node          Definition's scope node
     * @param namePrefix    Name prefix
     */
    public SequenceDefinition(SequenceDeclaration declaration, ScopeNode node, String namePrefix) {
        super(node);
        this.namePrefix = namePrefix;
        this.declaration = declaration;

        /*
         * Find the length definition. This is the whole point of having scope nodes. We need to
         * get the reference to the right integer definition so that once it's read, this
         * sequence will be linked to the correct value for its length. We don't initialize
         * the sequence right now; only when reading it.
         */
        this.currentLength = 0;
        this.lengthDefinition = (IntegerDefinition) node.lookupPath(this.declaration.getLengthPath(), true).getDef();
    }

    /**
     * Builds/initializes a scopeless sequence definition.
     * <p>
     * Parameter <code>lengthDef</code> must have a correct internal value since
     * this constructor initializes its internal array using it as soon as it is
     * called. This scopeless version of the constructor can be used to build
     * a synthetic sequence definition (a sequence definition that won't read
     * itself later on).
     *
     * @param declaration   Definition's declaration
     * @param lengthDef     Integer definition of length (with correct value at construction time)
     */
    public SequenceDefinition(SequenceDeclaration declaration, IntegerDefinition lengthDef) {
        super((ScopeNode) null);
        this.namePrefix = null;
        this.lengthDefinition = lengthDef;
        this.declaration = declaration;
        this.initSequence();
    }

    /**
     * Builds a scopeless sequence definition.
     *
     * @param declaration   Definition's declaration
     */
    public SequenceDefinition(SequenceDeclaration declaration) {
        super((ScopeNode) null);
        this.namePrefix = null;
        this.lengthDefinition = null;
        this.declaration = declaration;
    }

    /**
     * Builds a deep copy of a sequence definition.
     *
     * @param def   Sequence definition to copy
     */
    public SequenceDefinition(SequenceDefinition def) {
        super();
        this.declaration = def.declaration; // Ok to reference: doesn't contain data

        // Copy length definition and current length
        this.lengthDefinition = new IntegerDefinition(def.lengthDefinition);
        this.currentLength = def.currentLength;

        // Copy name prefix
        this.namePrefix = def.namePrefix;

        // Copy array
        this.definitions = new Definition[def.definitions.length];
        for (int i = 0; i < def.definitions.length; ++i) {
            this.definitions[i] = def.definitions[i].copyOf();
        }
    }

    @Override
    public Definition copyOf() {
        return new SequenceDefinition(this);
    }

    private void initSequence() {
        // Get the current length from our linked integer definition
        this.currentLength = (int) this.lengthDefinition.getValue();

        // Create the definitions
        this.definitions = new Definition [(int) this.lengthDefinition.getValue()];
        if (this.scopeNode == null) {
            for (int i = 0; i < this.currentLength; ++i) {
                this.definitions[i] = this.declaration.getElementType().createDefinition();
            }
        } else {
            for (int i = 0; i < this.currentLength; ++i) {
                this.definitions[i] = this.declaration.getElementType()
                        .createDefinition(this.scopeNode, this.namePrefix + "[" + i + "]"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    public SequenceDeclaration getDeclaration() {
        return this.declaration;
    }

    /**
     * Gets the current sequence length.
     * <p>
     * This returns the dynamic value of the integer definition serving as the
     * length definition of this sequence definition. This integer definition
     * location within the scope tree is resolved at construction time and not
     * every time this method is called.
     *
     * @return  Current sequence length
     */
    public int getLength() {
        // Length definition should have a correct value when this is called
        this.currentLength = (int) this.lengthDefinition.getValue();

        return this.currentLength;
    }

    /**
     * Gets the element at a specified index.
     *
     * @param i Index
     * @return  Element or null if out of range
     */
    public Definition getElem(int i) {
        if (i > this.definitions.length) {
            return null;
        }

        return this.definitions[i];
    }

    /**
     * Checks whether or not this sequence is a string.
     * <p>
     * Sometimes, strings are encoded as a sequence of 1-byte integers (each one
     * being an UTF-8/ASCII/ISO-* byte).
     *
     * @return True if this sequence is in fact a string
     */
    public boolean isString() {
        IntegerDeclaration elemInt;

        if (this.declaration.getElementType() instanceof IntegerDeclaration) {
            elemInt = (IntegerDeclaration) this.declaration.getElementType();
            if (elemInt.isCharacter()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the equivalent sequence string.
     * <p>
     * If this sequence is a string (see {@link #isString()}), this method returns
     * said string.
     *
     * @return  Sequence string
     */
    public String getString() {
        StringBuilder sb = new StringBuilder();

        if (this.isString()) {
            for (Definition def : this.definitions) {
                IntegerDefinition character = (IntegerDefinition) def;

                if (character.getValue() == 0) {
                    break;
                }

                sb.append((char) character.getValue());
            }

            return sb.toString();
        }

        return ""; //$NON-NLS-1$
    }

    public void setDefinitions(Definition[] defs) {
        this.definitions = defs;
    }

    @Override
    public void read(String name, IReader reader) {
        // Create definitions
        // FIXME: do *not* do this each time we read... expandSequence would
        //        be a better name
        this.initSequence();

        reader.openSequence(this, name);
        for (int i = 0; i < this.currentLength; i++) {
            this.definitions[i].read(null, reader);
        }
        reader.closeSequence(this, name);
    }

    @Override
    public void write(String name, IWriter writer) {
        writer.openSequence(this, name);
        for (int i = 0; i < this.currentLength; ++i) {
            this.definitions[i].write(null, writer);
        }
        writer.closeSequence(this, name);
    }

    @Override
    public long getSize(long offset) {
        long at = offset;

        for (int i = 0; i < this.currentLength; i++) {
            at += this.definitions[i].getSize(at);
        }

        return at - offset;
    }

    @Override
    public String toString(int level) {
        StringBuilder sb = new StringBuilder();

        if (this.isString()) {
            sb.append("seq (elements = \""); //$NON-NLS-1$
            sb.append(this.getString());
            sb.append("\")"); //$NON-NLS-1$
        } else if (this.definitions == null) {
            sb.append("seq [ ]"); //$NON-NLS-1$
        } else {
            if (this.definitions.length == 0) {
                sb.append("seq [ ]"); //$NON-NLS-1$
            } else {
                sb.append("seq [\n"); //$NON-NLS-1$
                for (int i = 0; i < (this.definitions.length - 1); i++) {
                    sb.append(Definition.getIndentString(level + 1) + i + " => "); //$NON-NLS-1$
                    sb.append(this.definitions[i].toString(level + 2));
                    sb.append(",\n"); //$NON-NLS-1$
                }
                sb.append(Definition.getIndentString(level + 1) + (this.definitions.length - 1) + " => "); //$NON-NLS-1$
                sb.append(this.definitions[this.definitions.length - 1].toString(level + 2) + "\n"); //$NON-NLS-1$
                sb.append(Definition.getIndentString(level) + "]"); //$NON-NLS-1$
            }
        }

        return sb.toString();
    }
}
