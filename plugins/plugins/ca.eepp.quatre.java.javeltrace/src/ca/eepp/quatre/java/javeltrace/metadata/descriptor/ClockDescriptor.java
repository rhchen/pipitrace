package ca.eepp.quatre.java.javeltrace.metadata.descriptor;

import java.util.UUID;

/**
 * Describes a CTF clock.
 * 
 * @author Philippe Proulx
 */
public final class ClockDescriptor {
    private final String name;
    private final UUID uuid;
    private final String description;
    private final Long freq;
    private final Long offset;
    
    /**
     * Builds a complete clock descriptor.
     * 
     * @param name  Name
     * @param uuid  UUID
     * @param description   Description
     * @param freq  Frequency (Hz)
     * @param offset    Offset (cycles @ frequency)
     */
    public ClockDescriptor(String name, UUID uuid, String description,
            Long freq, Long offset) {
        this.name = name;
        this.uuid = uuid;
        this.description = description;
        this.freq = freq;
        this.offset = offset;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getDescription() {
        return this.description;
    }

    public Long getFreq() {
        return new Long(this.freq);
    }

    public Long getOffset() {
        return new Long(this.offset);
    }
}
