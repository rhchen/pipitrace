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

import java.util.LinkedList;
import java.util.List;

/**
 * A CTF enumeration declaration.
 * <p>
 * Enumerations are integers mapped to a string label. A label may represent
 * one integer or a range of integers. This is implemented internally using
 * a special table class.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class EnumDeclaration implements IDeclaration {
    private final EnumTable table = new EnumTable();
    private IntegerDeclaration containerType = null;

    /**
     * Builds an enumeration declaration.
     * 
     * @param containerType Underlying integer type
     */
    public EnumDeclaration(IntegerDeclaration containerType) {
        this.containerType = containerType;
    }

    public IntegerDeclaration getContainerType() {
        return this.containerType;
    }

    @Override
    public long getAlignment() {
        return this.getContainerType().getAlignment();
    }
    
    @Override
    public EnumDefinition createDefinition(ScopeNode parentNode, String name) {
        // Create a child scope node
        ScopeNode node = parentNode.addChild(name);
        
        // Create definition
        EnumDefinition def = new EnumDefinition(this, node);
        
        // Associate the scope node with the definition
        node.setDef(def);
        
        return def;
    }
    
    @Override
    public EnumDefinition createDefinition() {
        return new EnumDefinition(this);
    }

    /**
     * Adds a range of values to a label.
     * 
     * @param low   Lower bound (inclusive)
     * @param high  Upper bound (inclusive)
     * @param label Label
     * @return      True if added, false if range intersects with a registered range
     */
    public boolean add(long low, long high, String label) {
        return this.table.add(low, high, label);
    }

    /**
     * Queries the enumeration for a label.
     * <p>
     * In other words: value to label.
     * 
     * @param value Integer value
     * @return      Linked label or null if none
     */
    public String query(long value) {
        return this.table.query(value);
    }

    /*
     * Maps integer range -> string. A simple list for now, but feel free to
     * optimize it. Babeltrace suggests an interval tree.
     */
    static private class EnumTable {

        List<Range> ranges = new LinkedList<Range>();

        public EnumTable() {
        }

        public boolean add(long low, long high, String label) {
            Range newRange = new Range(low, high, label);

            for (Range r : this.ranges) {
                if (r.intersects(newRange)) {
                    return false;
                }
            }

            this.ranges.add(newRange);

            return true;
        }

        public String query(long value) {
            for (Range r : this.ranges) {
                if (r.intersects(value)) {
                    return r.str;
                }
            }

            return null;
        }

        static private class Range {

            long low, high;
            String str;

            public Range(long low, long high, String str) {
                this.low = low;
                this.high = high;
                this.str = str;
            }

            public boolean intersects(long i) {
                return (i >= this.low) && (i <= this.high);
            }

            public boolean intersects(Range other) {
                return this.intersects(other.low)
                        || this.intersects(other.high);
            }
        }
    }

    @Override
    public String toString() {
        /* Only used for debugging */
        return "[declaration] enum[" + Integer.toHexString(hashCode()) + ']'; //$NON-NLS-1$
    }
}