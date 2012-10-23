package ca.eepp.quatre.java.javeltrace.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.UUID;

import org.eclipse.linuxtools.ctf.core.trace.data.types.Definition;
import org.eclipse.linuxtools.ctf.core.trace.data.types.Encoding;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDeclaration;
import org.eclipse.linuxtools.ctf.core.trace.data.types.IntegerDefinition;

/**
 * Static utilities for various common operations.
 * <p>
 * This is a regular utilities class containing many static helper methods. It
 * also exposes useful public final static values.
 *
 * @author Matthew Khouzam
 * @author Simon Marchi
 * @author Philippe Proulx
 *
 */
public abstract class Utils {
    /** Length in bytes of a UUID value */
    public final static int UUID_LEN = 16;

    /**
     * Unsigned long comparison.
     *
     * @param a First operand.
     * @param b Second operand.
     * @return  -1 if a < b, 1 if a > b, 0 if a == b.
     */
    public static int unsignedCompare(long a, long b) {
        boolean aLeftBit = (a & (1 << (Long.SIZE - 1))) != 0;
        boolean bLeftBit = (b & (1 << (Long.SIZE - 1))) != 0;

        if (aLeftBit && !bLeftBit) {
            return 1;
        } else if (!aLeftBit && bLeftBit) {
            return -1;
        } else {
            if (a < b) {
                return -1;
            } else if (a > b) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Creates a UUID object from an array of 16 bytes.
     *
     * @param bytes Array of 16 bytes.
     * @return      A UUID object.
     */
    public static UUID makeUUID(byte bytes[]) {
        long high = 0;
        long low = 0;

        assert (bytes.length == Utils.UUID_LEN);

        for (int i = 0; i < 8; i++) {
            low = (low << 8) | (bytes[i + 8] & 0xFF);
            high = (high << 8) | (bytes[i] & 0xFF);
        }

        UUID uuid = new UUID(high, low);

        return uuid;
    }

    /**
     * Byte array from UUID object.
     * <p>
     * The array will always have a length of 16.
     *
     * @param uuid  UUID object
     * @return      Corresponding byte array
     */
    public static byte[] bytesFromUUID(UUID uuid) {
        byte[] bytes = new byte [16];

        // Get the MSB/LSB of the UUID
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();

        // Get UUID values
        for (int i = 0; i < (Utils.UUID_LEN / 2); i++) {
            bytes[i] = (byte) ((msb >>> 8 * (7 - i)) & 0xff);
        }
        for (int i = (Utils.UUID_LEN / 2); i < Utils.UUID_LEN; i++) {
            bytes[i] = (byte) ((lsb >>> 8 * (7 - i)) & 0xff);
        }

        return bytes;
    }

    /**
     * Creates a scopeless unsigned integer definition.
     *
     * @param size  Size (bits)
     * @param align Alignment (bits)
     * @param bo    Byte order
     * @param value Initial value
     * @return      Created integer definition
     */
    public static IntegerDefinition createUint(int size, int align, ByteOrder bo, long value) {
        IntegerDeclaration decl = new IntegerDeclaration(size, false, 16, bo, Encoding.NONE, "", align); //$NON-NLS-1$
        IntegerDefinition def = decl.createDefinition();
        def.setValue(value);

        return def;
    }

    /**
     * Creates a scopeless unsigned 32-bit, 8-bit aligned integer.
     *
     * @param value Initial value
     * @param bo    Byte order
     * @return      Created integer definition
     */
    public static IntegerDefinition createUint32A8(long value, ByteOrder bo) {
        return Utils.createUint(32, 8, bo, value);
    }

    /**
     * Creates a scopeless unsigned 32-bit, 32-bit aligned integer.
     *
     * @param value Initial value
     * @param bo    Byte order
     * @return      Created integer definition
     */
    public static IntegerDefinition createUint32A32(long value, ByteOrder bo) {
        return Utils.createUint(32, 32, bo, value);
    }

    /**
     * Creates a scopeless unsigned 64-bit, 8-bit aligned integer.
     *
     * @param value Initial value
     * @param bo    Byte order
     * @return      Created integer definition
     */
    public static IntegerDefinition createUint64A8(long value, ByteOrder bo) {
        return Utils.createUint(64, 8, bo, value);
    }

    /**
     * Creates a scopeless unsigned 64-bit, 64-bit aligned integer.
     *
     * @param value Initial value
     * @param bo    Byte order
     * @return      Created integer definition
     */
    public static IntegerDefinition createUint64A64(long value, ByteOrder bo) {
        return Utils.createUint(64, 64, bo, value);
    }

    /**
     * Creates a scopeless unsigned 8-bit, 8-bit aligned integer.
     *
     * @param value Initial value
     * @return      Created integer definition
     */
    public static IntegerDefinition createUint8A8(long value) {
        return Utils.createUint(8, 8, ByteOrder.BIG_ENDIAN, value);
    }

    /**
     * Synonym for {@link #createUint8A8(long)}.
     *
     * @param value Initial value
     * @return      Created integer definition
     */
    public static IntegerDefinition createUByte(long value) {
        return Utils.createUint8A8(value);
    }

    public static byte[] UTF8bytesFromString(String str) {
        byte[] utf8Bytes = null;
        utf8Bytes = str.getBytes(Charset.forName("UTF-8")); //$NON-NLS-1$

        return utf8Bytes;
    }

    /**
     * Definitions from a UTF-8 string.
     * <p>
     * This method creates an array of <code>Definition</code> from the content
     * of an UTF-8 string for a specific length. All extra bytes are set
     * to 0.
     *
     * @param str   UTF-8 string
     * @param len   Length of returned array
     * @return      Array of definitions
     */
    public static Definition[] defsFromUTF8String(String str, int len) {
        // Get UTF-8 bytes
        byte[] bytes = str.getBytes(Charset.forName("UTF-8")); //$NON-NLS-1$

        // Verify that we don't go past the required length
        if (bytes.length + 1 > len) {
            return null;
        }

        // Definition array
        Definition[] defs = new IntegerDefinition[len];

        // Browse bytes and create one 8-bit integer definition per byte
        for (int i = 0; i < bytes.length; ++i) {
            defs[i] = Utils.createUint8A8(bytes[i]);
        }

        // Add as much NUL bytes as needed
        for (int i = bytes.length; i < len; ++i) {
            defs[i] = Utils.createUint8A8(0);
        }

        return defs;
    }

    /**
     * UTF-8 string from a text file.
     *
     * @param path  Text file path
     * @return      String containing all the text file content
     * @throws IOException  If there's any reading error
     */
    public static String UTF8StringFromTextFileContent(File path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        try {
            FileChannel fc = fis.getChannel();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

            return Charset.forName("UTF-8").decode(mbb).toString(); //$NON-NLS-1$
        } finally {
            fis.close();
        }
    }

    /**
     * UTF-8 string from a text file.
     *
     * @param path  Text file path
     * @return      String containing all the text file content
     * @throws IOException  If there's any reading error
     */
    public static String UTF8StringFromTextFileContent(String path) throws IOException {
        return Utils.UTF8StringFromTextFileContent(new File(path));
    }

    /**
     * Removes surrounding quotes (<code>"</code>) from a string.
     *
     * @param orig  Original string
     * @return      String without surrounding quotes
     */
    public static String removeQuotesFromString(String orig) {
        return orig.substring(1, orig.length() - 1);
    }
}
