/*******************************************************************************
 * Copyright (c) 2011-2012 Ericsson, Ecole Polytechnique de Montreal and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Matthew Khouzam - Initial Design and Grammar
 * Contributors: Simon Marchi    - Initial API and implementation
 *******************************************************************************/

package ca.eepp.quatre.java.javeltrace.metadata;

import java.util.HashMap;

import org.eclipse.linuxtools.ctf.core.trace.data.types.EnumDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.StructDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.VariantDeclaration;

import ca.eepp.quatre.java.javeltrace.metadata.ex.ParsingException;

/**
 * A declaration scope.
 * <p>
 * A declaration scope keeps track of the various CTF declarations for a given
 * scope. This is used when parsing the metadata text.
 * 
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 */
public class DeclarationScope {
    private DeclarationScope parentScope = null;
    private final HashMap<String, StructDeclaration> structs = new HashMap<String, StructDeclaration>();
    private final HashMap<String, EnumDeclaration> enums = new HashMap<String, EnumDeclaration>();
    private final HashMap<String, VariantDeclaration> variants = new HashMap<String, VariantDeclaration>();
    private final HashMap<String, IDeclaration> types = new HashMap<String, IDeclaration>();

    /**
     * Creates a declaration scope with no parent.
     */
    public DeclarationScope() {
    }

    /**
     * Creates a declaration scope with the specified parent.
     *
     * @param parentScope   The parent of the newly created scope.
     */
    public DeclarationScope(DeclarationScope parentScope) {
        this.parentScope = parentScope;
    }

    /**
     * Returns the parent of the current scope.
     *
     * @return The parent scope.
     */
    public DeclarationScope getParentScope() {
        return this.parentScope;
    }
    
    /**
     * Registers a type declaration.
     *
     * @param name          The name of the type
     * @param declaration   The type declaration
     * @throws ParsingException If a type with the same name has already been defined
     */
    public void registerType(String name, IDeclaration declaration) throws ParsingException {
        // Check if the type has been defined in the current scope
        if (this.types.containsKey(name)) {
            throw new ParsingException("Type " + name //$NON-NLS-1$
                    + " has already been defined."); //$NON-NLS-1$
        }

        // Add it to the register
        this.types.put(name, declaration);
    }

    /**
     * Registers a structure declaration.
     *
     * @param name          The name of the structure
     * @param declaration   The declaration of the structure
     * @throws ParsingException If a structure with the same name has already been registered
     */
    public void registerStruct(String name, StructDeclaration declaration) throws ParsingException {
        // Check if the struct has been defined in the current scope
        if (this.structs.containsKey(name)) {
            throw new ParsingException("struct " + name //$NON-NLS-1$
                    + " has already been defined."); //$NON-NLS-1$
        }

        // Add it to the register
        this.structs.put(name, declaration);

        // It also defined a new type, so add it to the type declarations
        String struct_prefix = "struct "; //$NON-NLS-1$
        registerType(struct_prefix + name, declaration);
    }

    /**
     * Registers an enumeration declaration.
     *
     * @param name          The name of the enumeration
     * @param declaration   The declaration of the enumeration
     * @throws ParsingException If an enumeration with the same name has already been registered
     */
    public void registerEnum(String name, EnumDeclaration declaration) throws ParsingException {
        // Check if the enum has been defined in the current scope
        if (lookupEnum(name) != null) {
            throw new ParsingException("enum " + name //$NON-NLS-1$
                    + " has already been defined."); //$NON-NLS-1$
        }

        // Add it to the register
        this.enums.put(name, declaration);

        // It also defined a new type, so add it to the type declarations
        String enum_prefix = "enum "; //$NON-NLS-1$
        registerType(enum_prefix + name, declaration);
    }

    /**
     * Registers a variant declaration.
     *
     * @param name          The name of the variant
     * @param declaration   The declaration of the variant
     * @throws ParsingException If a variant with the same name has already been registered
     */
    public void registerVariant(String name, VariantDeclaration declaration) throws ParsingException {
        // Check if the variant has been defined in the current scope
        if (lookupVariant(name) != null) {
            throw new ParsingException("variant " + name //$NON-NLS-1$
                    + " has already been defined."); //$NON-NLS-1$
        }

        // Add it to the register
        this.variants.put(name, declaration);

        // It also defined a new type, so add it to the type declarations
        String variant_prefix = "variant "; //$NON-NLS-1$
        registerType(variant_prefix + name, declaration);
    }

    /**
     * Looks up a type declaration in the current scope.
     *
     * @param name  The name of the type to search for
     * @return      The type declaration, or null if no type with that name has been defined
     */
    public IDeclaration lookupType(String name) {
        return this.types.get(name);
    }

    /**
     * Looks up a type declaration in the current scope and recursively in the
     * parent scopes.
     *
     * @param name  The name of the type to search for
     * @return      The type declaration, or null if no type with that name has been defined
     */
    public IDeclaration rlookupType(String name) {
        IDeclaration declaration = lookupType(name);
        if (declaration != null) {
            return declaration;
        } else if (this.parentScope != null) {
            return this.parentScope.rlookupType(name);
        } else {
            return null;
        }
    }

    /**
     * Looks up a struct declaration.
     *
     * @param name  The name of the struct to search for
     * @return      The struct declaration, or null if no struct with that name has been defined
     */
    public StructDeclaration lookupStruct(String name) {
        return this.structs.get(name);
    }

    /**
     * Looks up a struct declaration in the current scope and recursively in the
     * parent scopes.
     *
     * @param name  The name of the struct to search for
     * @return      The struct declaration, or null if no struct with that name has been defined
     */
    public StructDeclaration rlookupStruct(String name) {
        StructDeclaration declaration = lookupStruct(name);
        if (declaration != null) {
            return declaration;
        } else if (this.parentScope != null) {
            return this.parentScope.rlookupStruct(name);
        } else {
            return null;
        }
    }

    /**
     * Looks up a enum declaration.
     *
     * @param name  The name of the enum to search for
     * @return      The enum declaration, or null if no enum with that name has been defined
     */
    public EnumDeclaration lookupEnum(String name) {
        return this.enums.get(name);
    }

    /**
     * Looks up an enum declaration in the current scope and recursively in the
     * parent scopes.
     *
     * @param name  The name of the enum to search for
     * @return      The enum declaration, or null if no enum with that name has been defined
     */
    public EnumDeclaration rlookupEnum(String name) {
        EnumDeclaration declaration = lookupEnum(name);
        if (declaration != null) {
            return declaration;
        } else if (this.parentScope != null) {
            return this.parentScope.rlookupEnum(name);
        } else {
            return null;
        }
    }

    /**
     * Looks up a variant declaration.
     *
     * @param name  The name of the variant to search for
     * @return      The variant declaration, or null if no variant with that name has been defined
     */
    public VariantDeclaration lookupVariant(String name) {
        return this.variants.get(name);
    }

    /**
     * Looks up a variant declaration in the current scope and recursively in
     * the parent scopes.
     *
     * @param name  The name of the variant to search for
     * @return      The variant declaration, or null if no variant with that name has been defined
     */
    public VariantDeclaration rlookupVariant(String name) {
        VariantDeclaration declaration = lookupVariant(name);
        if (declaration != null) {
            return declaration;
        } else if (this.parentScope != null) {
            return this.parentScope.rlookupVariant(name);
        } else {
            return null;
        }
    }
}