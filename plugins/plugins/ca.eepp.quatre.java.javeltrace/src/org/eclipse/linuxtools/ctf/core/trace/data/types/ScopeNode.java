package org.eclipse.linuxtools.ctf.core.trace.data.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A scope node.
 * <p>
 * Scope nodes are the building blocks of a scope tree, which is a dynamic
 * data structure for looking up real-time definitions values while reading.
 * <p>
 * This mechanism is designed for sequences (see {@link SequenceDefinition}) and
 * variants (see {@link VariantDefinition}). Sequences have a dynamic length which
 * depends on some integer value somewhere, as long as it's already read. A
 * scope tree makes it possible to find the integer definition while at the
 * sequence read time if well built. Variants act as placeholders for any other
 * real type and which type to choose depends on the value of an already read
 * enumeration at read time.
 * <p>
 * Those dynamic values can be in any parent field of an event payload, but can
 * also reside in other dynamic scopes: packet/stream/event contexts, headers,
 * etc.
 * <p>
 * Scope nodes are linked to definitions.
 * 
 * @author Philippe Proulx
 */
public class ScopeNode {
    private ScopeNode parent = null;
    private final HashMap<String, ScopeNode> children;
    private Definition def = null;
    private String name = "root"; //$NON-NLS-1$
    
    /**
     * Builds a root scope node.
     */
    public ScopeNode() {
        this.children = new HashMap<String, ScopeNode>();
    }
    
    /**
     * Adds/creates a child node with associated definition.
     * 
     * @param name  Child node name
     * @param def   Child node linked definition (may be null)
     * @return      Created child node
     */
    public ScopeNode addChildDef(String name, Definition def) {
        // If the child already exists, just overwrite it
        ScopeNode node = new ScopeNode();
        node.parent = this;
        node.name = name;
        node.def = def;
        this.children.put(name, node);
        if (def != null) {
            def.setScopeNode(node);
        }
        
        return node;
    }
    
    /**
     * Adds an existing node as a child.
     * 
     * @param name      Child node name
     * @param childNode Child node to add
     */
    public void addChildNode(String name, ScopeNode childNode) {
        childNode.parent = this;
        childNode.name = name;
        this.children.put(name, childNode);
    }
    
    /**
     * Removes all children.
     */
    public void removeAllChildren() {
        for (ScopeNode node : this.children.values()) {
            node.parent = null;
        }
        this.children.clear();
    }
    
    /**
     * Adds/creates a new child node (no linked definition).
     * 
     * @param name  Child node name
     * @return      Created child node
     */
    public ScopeNode addChild(String name) {
        return this.addChildDef(name, null);
    }
    
    /**
     * Looks up a child node by name.
     * 
     * @param name  Child node name
     * @return      Found child or null if none
     */
    public ScopeNode lookupChild(String name) {
        if (this.children.containsKey(name)) {
            return this.children.get(name);
        }

        return null;
    }
    
    /**
     * Gets the node parent.
     * 
     * @return  Node parent (null if node is a root)
     */
    public ScopeNode getParent() {
        return this.parent;
    }
    
    private ScopeNode lookupSibling(String name) {
        if (this.parent == null) {
            return null;
        }
        return this.parent.lookupChild(name);
    }
    
    private ScopeNode rlookupSibling(String name) {
        if (this.parent == null) {
            return null;
        }
        ScopeNode node = this.lookupSibling(name);
        if (node == null) {
            node = this.parent.rlookupSibling(name);
        }
        
        return node;
    }
    
    /**
     * Gets the root node of this node.
     * <p>
     * The root is the first recursive parent having a null parent.
     * 
     * @return  Root node
     */
    public ScopeNode getRoot() {
        if (this.parent == null) {
            return this;
        }
        
        return this.parent.getRoot();
    }
    
    private ScopeNode lookupPath(ArrayList<String> tokens) {
        String myChildName = tokens.get(0);
        if (tokens.size() == 1) {
            return this.lookupChild(myChildName);
        }
        tokens.remove(0);
        
        return this.lookupChild(myChildName).lookupPath(tokens);
    }
    
    /**
     * Looks up a dynamic path.
     * <p>
     * A path is recursively relative if it doesn't contain any dot (<code>.</code>)
     * and absolute if it has dots. For example, the path <code>someField</code>
     * means <code>someField</code> is a field of one of this node's ancestors,
     * while the path <code>stream.packet.context.cpu_id</code> means to start
     * at this node's root node and go to child node <code>stream</code>, then
     * its child node <code>packet</code> and so on.  
     *  
     * @param path  Path (relative or absolute)
     * @return      Found node or null if none
     */
    public ScopeNode lookupPath(String path) {
        return this.lookupPath(path, false);
    }
    
    /**
     * Looks up a dynamic path including or not children.
     * <p>
     * Same as {@link #lookupPath(String)}, but also look into child nodes
     * if asked to.
     * 
     * @param path              Path
     * @param lookupChildren    True to lookup children too
     * @return                  Found node or null if none
     */
    public ScopeNode lookupPath(String path, boolean lookupChildren) {        
        // Is this an absolute or relative path?
        if (path.contains(".")) { //$NON-NLS-1$
            // Absolute; get the root
            ScopeNode root = this.getRoot();
            
            // Build the path tokens
            ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(path.split("\\."))); //$NON-NLS-1$
            
            // Find the node
            return root.lookupPath(tokens);
        }
        if (lookupChildren) {
            ScopeNode s = this.lookupChild(path);
            if (s != null) {
                return s;
            }
        }
        
        // Relative; search recursively within siblings
        return this.rlookupSibling(path);
    }
    
    private void getAbsolutePath(StringBuilder sb) {
        sb.insert(0, this.name);
        if (this.parent != null) {
            sb.insert(0, "."); //$NON-NLS-1$
            this.getAbsolutePath(sb);
        }
    }
    
    /**
     * Gets this node's absolute path (from root node).
     * 
     * @return  Absolute path
     */
    public String getAbsolutePath() {
        // Path accumulator
        StringBuilder sb = new StringBuilder();
        
        // Build the string
        this.getAbsolutePath(sb);
        
        return sb.toString();
    }
    
    /**
     * Gets the linked definition.
     * 
     * @return  Linked definition or null if not set
     */
    public Definition getDef() {
        return this.def;
    }
    
    /**
     * Sets the linked definition.
     * 
     * @param def   Linked definition
     */
    public void setDef(Definition def) {
        this.def = def;
    }

    public String getName() {
        return this.name;
    }
    
    private void toString(int level, StringBuilder sb) {
        // Indentation
        for (int i = 0; i < level; ++i) {
            sb.append("  "); //$NON-NLS-1$
        }
        
        // Me
        sb.append(this.name);
        sb.append(System.getProperty("line.separator")); //$NON-NLS-1$
        
        // My children
        for (ScopeNode node : this.children.values()) {
            node.toString(level + 1, sb);
        }
    }
    
    @Override
    public String toString() {
        // Preorder
        StringBuilder sb = new StringBuilder();
        this.toString(0, sb);
        
        return sb.toString();
    }
}
