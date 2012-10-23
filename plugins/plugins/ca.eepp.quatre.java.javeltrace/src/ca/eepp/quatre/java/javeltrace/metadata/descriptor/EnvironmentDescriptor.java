package ca.eepp.quatre.java.javeltrace.metadata.descriptor;

import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * Describes a CTF environment.
 * 
 * @author Philippe Proulx
 */
public final class EnvironmentDescriptor {
    private final HashMap<String, String> stringProperties;
    private final HashMap<String, Long> integerProperties;
    
    /**
     * Builds an empty environment descriptor.
     */
    public EnvironmentDescriptor() {
        this.stringProperties = new HashMap<String, String>();
        this.integerProperties = new HashMap<String, Long>();
    }
    
    private void removeElem(String key) {
        if (this.stringProperties.containsKey(key)) {
            this.stringProperties.remove(key);
        }
        if (this.integerProperties.containsKey(key)) {
            this.integerProperties.remove(key);
        }
    }
    
    /**
     * Adds a string property to the environment.
     * 
     * @param key   Key
     * @param value String value
     */
    public void addProperty(String key, String value) {
        this.removeElem(key);
        this.stringProperties.put(key, value);
    }
    
    /**
     * Adds a long property to the environment.
     * 
     * @param key   Key
     * @param value Long value
     */
    public void addProperty(String key, Long value) {
        this.removeElem(key);
        this.integerProperties.put(key, value);
    }
    
    /**
     * Gets a string property from the environment.
     * 
     * @param key   Lookup key
     * @return  String value
     * @throws NoSuchElementException   If provided key is not found
     */
    public String getStringProperty(String key) throws NoSuchElementException {
        if (!this.stringProperties.containsKey(key)) {
            throw new NoSuchElementException("Cannot find key \"" + key + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        return this.stringProperties.get(key);
    }
    
    /**
     * Gets a long property from the environment.
     * 
     * @param key   Lookup key
     * @return  Long value
     * @throws NoSuchElementException   If provided key is not found
     */
    public Long getIntegerProperty(String key) {
        if (!this.integerProperties.containsKey(key)) {
            throw new NoSuchElementException("Cannot find key \"" + key + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        return new Long(this.integerProperties.get(key));
    }
    
    @Override
    public String toString() {
        String r = new String();
        for (String str : this.stringProperties.keySet()) {
            r += str + " = \"" + this.stringProperties.get(str) + "\"" + System.getProperty("line.separator"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        for (String str : this.integerProperties.keySet()) {
            r += str + " = " + this.integerProperties.get(str) + System.getProperty("line.separator"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        return r;
    }
}
