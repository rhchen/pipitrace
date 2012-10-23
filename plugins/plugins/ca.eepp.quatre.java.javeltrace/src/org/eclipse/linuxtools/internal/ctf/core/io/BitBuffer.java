/*******************************************************************************.
 * Copyright (c) 2011-2012 Ericsson, Ecole Polytechnique de Montreal and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Matthew Khouzam - Initial Design and implementation
 * Contributors: Francis Giraldeau - Initial API and implementation
 * Contributors: Philippe Proulx - Some refinement and optimization
 *******************************************************************************/

package org.eclipse.linuxtools.internal.ctf.core.io;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A buffer with reads/writes at bit level.
 * <p>
 * This bit buffer is capable of accessing fields with bit offsets. Its interface
 * is analogous to the {@link ByteBuffer} one.
 */
public class BitBuffer {
    /** 8 bits to a char */
    public static final int BIT_CHAR = 8;

    /** 16 bits to a short */
    public static final int BIT_SHORT = 16;

    /** 32 bits to an int */
    public static final int BIT_INT = 32;

    /** 32 bits to a float */
    public static final int BIT_FLOAT = 32;

    /** 64 bits to a long */
    public static final int BIT_LONG = 64;

    private ByteBuffer buf;
    private int pos;
    private ByteOrder byteOrder;

    /**
     * Creates a big-endian bit buffer.
     */
    public BitBuffer() {
        this(null, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Creates a big-endian bit buffer.
     *
     * @param buf   Underlying byte buffer to use for I/O operations
     */
    public BitBuffer(ByteBuffer buf) {
        this(buf, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Creates a bit buffer with custom byte order.
     *
     * @param buf   Underlying byte buffer to use for I/O operations
     * @param order Byte order
     */
    public BitBuffer(ByteBuffer buf, ByteOrder order) {
        setByteBuffer(buf);
        order(order);
        position(0);
    }

    /**
     * Relative <i>get</i> method for reading a 32-bit integer.
     *
     * Reads next four bytes from the current bit position according to current
     * byte order.
     *
     * @return  Value read from the buffer
     */
    public int getInt() {
        int val = getInt(BIT_INT, true);

        return val;
    }

    /**
     * Relative <i>get</i> method for reading an integer of custom width.
     * <p>
     * Reads <code>length</code> bits starting at the current position. The result is
     * signed extended if <code>signed</code> is true. The current position is
     * increased by <code>length</code> bits.
     *
     * @param length    Length to read (bits)
     * @param signed    True if signed
     * @return  Value read from the buffer
     */
    public int getInt(int length, boolean signed) {
        int val = 0;
        if (!canRead(this.pos, length)) {
            throw new BufferOverflowException();
        }
        if (length == 0) {
            return 0;
        }
        boolean gotIt = false;

        // Fall back to fast ByteBuffer reader if we want to read byte-aligned bytes
        if (this.pos % BitBuffer.BIT_CHAR == 0) {
            switch (length) {
            case BitBuffer.BIT_CHAR:
                // Byte
                if (signed) {
                    val = this.buf.get(this.pos / 8);
                } else {
                    val = ((this.buf.get(this.pos / 8)) & 0xff);
                }
                gotIt = true;
                break;

            case BitBuffer.BIT_SHORT:
                // Word
                if (signed) {
                    val = this.buf.getShort(this.pos / 8);
                } else {
                    short a = this.buf.getShort(this.pos / 8);
                    val = (a & 0xffff);
                }
                gotIt = true;
                break;

            case BitBuffer.BIT_INT:
                // Double word
                val = this.buf.getInt(this.pos / 8);
                gotIt = true;
                break;
            }
        }
        if (!gotIt) {
            // Nothing read yet: use longer methods
            if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
                val = getIntLE(this.pos, length, signed);
            } else {
                val = getIntBE(this.pos, length, signed);
            }
        }
        this.pos += length;

        return val;
    }

    private int getIntBE(int index, int length, boolean signed) {
        assert ((length > 0) && (length <= BIT_INT));
        int end = index + length;
        int startByte = index / BIT_CHAR;
        int endByte = (end + (BIT_CHAR - 1)) / BIT_CHAR;
        int currByte, lshift, cshift, mask, cmask, cache;
        int value = 0;

        currByte = startByte;
        cache = this.buf.get(currByte) & 0xFF;
        boolean isNeg = (cache & (1 << (BIT_CHAR - (index % BIT_CHAR) - 1))) != 0;
        if (signed && isNeg) {
            value = ~0;
        }
        if (startByte == (endByte - 1)) {
            cmask = cache >>> ((BIT_CHAR - (end % BIT_CHAR)) % BIT_CHAR);
            if (((length) % BIT_CHAR) > 0) {
                mask = ~((~0) << length);
                cmask &= mask;
            }
            value <<= length;
            value |= cmask;
            return value;
        }
        cshift = index % BIT_CHAR;
        if (cshift > 0) {
            mask = ~((~0) << (BIT_CHAR - cshift));
            cmask = cache & mask;
            lshift = BIT_CHAR - cshift;
            value <<= lshift;
            value |= cmask;
            // index += lshift;
            currByte++;
        }
        for (; currByte < (endByte - 1); currByte++) {
            value <<= BIT_CHAR;
            value |= this.buf.get(currByte) & 0xFF;
        }
        lshift = end % BIT_CHAR;
        if (lshift > 0) {
            mask = ~((~0) << lshift);
            cmask = this.buf.get(currByte) & 0xFF;
            cmask >>>= BIT_CHAR - lshift;
            cmask &= mask;
            value <<= lshift;
            value |= cmask;
        } else {
            value <<= BIT_CHAR;
            value |= this.buf.get(currByte) & 0xFF;
        }
        return value;
    }

    private int getIntLE(int index, int length, boolean signed) {
        assert ((length > 0) && (length <= BIT_INT));
        int end = index + length;
        int startByte = index / BIT_CHAR;
        int endByte = (end + (BIT_CHAR - 1)) / BIT_CHAR;
        int currByte, lshift, cshift, mask, cmask, cache, mod;
        int value = 0;

        currByte = endByte - 1;
        cache = this.buf.get(currByte) & 0xFF;
        mod = end % BIT_CHAR;
        lshift = (mod > 0) ? mod : BIT_CHAR;
        boolean isNeg = (cache & (1 << (lshift - 1))) != 0;
        if (signed && isNeg) {
            value = ~0;
        }
        if (startByte == (endByte - 1)) {
            cmask = cache >>> (index % BIT_CHAR);
            if (((length) % BIT_CHAR) > 0) {
                mask = ~((~0) << length);
                cmask &= mask;
            }
            value <<= length;
            value |= cmask;
            return value;
        }
        cshift = end % BIT_CHAR;
        if (cshift > 0) {
            mask = ~((~0) << cshift);
            cmask = cache & mask;
            value <<= cshift;
            value |= cmask;
            // end -= cshift;
            currByte--;
        }
        for (; currByte >= (startByte + 1); currByte--) {
            value <<= BIT_CHAR;
            value |= this.buf.get(currByte) & 0xFF;
        }
        lshift = index % BIT_CHAR;
        if (lshift > 0) {
            mask = ~((~0) << (BIT_CHAR - lshift));
            cmask = this.buf.get(currByte) & 0xFF;
            cmask >>>= lshift;
            cmask &= mask;
            value <<= (BIT_CHAR - lshift);
            value |= cmask;
        } else {
            value <<= BIT_CHAR;
            value |= this.buf.get(currByte) & 0xFF;
        }
        return value;
    }

    /**
     * Relative <i>put</i> method to write a signed 32-bit integer.
     * <p>
     * Writes four bytes starting from current bit position in the buffer
     * according to the current byte order. The current position is increased by
     * 32 bits.
     *
     * @param value Value to write
     */
    public void putInt(int value) {
        putInt(BIT_INT, value);
    }

    /**
     * Relative <i>put</i> method to write an integer of custom width.
     * <p>
     * Writes <code>length</code> lower-order bits from the provided value <code>value</code>,
     * starting from current bit position in the buffer. Sequential bytes are
     * written according to the current byte order. The sign bit is carried to
     * the MSB if the value is signed. The sign bit is included in <code>length</code>.
     * The current position is increased by <code>length</code>.
     *
     * @param length    Number of bits to write
     * @param value     Value to write
     */
    public void putInt(int length, int value) {
        putInt(this.pos, length, value);
    }

    /**
     * Absolute <i>put</i> method to write an integer of custom width.
     * <p>
     * Writes <code>length</code> lower-order bits from the provided value <code>value</code>,
     * starting from <code>index</code> bit position in the buffer. Sequential bytes are
     * written according to the current byte order. The sign bit is carried to
     * the MSB if the value is signed. The sign bit is included in <code>length</code>. The
     * current position is increased by <code>length</code>.
     *
     * @param index     Start position (bits)
     * @param value     Value to write
     * @param length    Number of bits to write
     */
    public void putInt(int index, int length, int value) {
        if (!canRead(index, length)) {
            throw new BufferOverflowException();
        }
        if (length == 0) {
            return;
        }
        if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
            putIntLE(index, length, value);
        } else {
            putIntBE(index, length, value);
        }
        this.pos += length;
    }

    private void putIntBE(int index, int length, int value) {
        assert ((length > 0) && (length <= BIT_INT));
        int end = index + length;
        int startByte = index / BIT_CHAR;
        int endByte = (end + (BIT_CHAR - 1)) / BIT_CHAR;
        int currByte, lshift, cshift, mask, cmask;
        int correctedValue = value;

        /*
         * mask v high bits. Works for unsigned and two complement signed
         * numbers which value do not overflow on length bits.
         */
        if (length < BIT_INT) {
            correctedValue &= ~(~0 << length);
        }

        /* sub byte */
        if (startByte == (endByte - 1)) {
            lshift = (BIT_CHAR - (end % BIT_CHAR)) % BIT_CHAR;
            mask = ~((~0) << lshift);
            if ((index % BIT_CHAR) > 0) {
                mask |= (~(0)) << (BIT_CHAR - (index % BIT_CHAR));
            }
            cmask = correctedValue << lshift;
            /*
             * low bits are cleared because of lshift and high bits are already
             * cleared
             */
            cmask &= ~mask;
            int b = this.buf.get(startByte) & 0xFF;
            this.buf.put(startByte, (byte) ((b & mask) | cmask));
            return;
        }

        /* head byte contains MSB */
        currByte = endByte - 1;
        cshift = end % BIT_CHAR;
        if (cshift > 0) {
            lshift = BIT_CHAR - cshift;
            mask = ~((~0) << lshift);
            cmask = correctedValue << lshift;
            cmask &= ~mask;
            int b = this.buf.get(currByte) & 0xFF;
            this.buf.put(currByte, (byte) ((b & mask) | cmask));
            correctedValue >>>= cshift;
            // end -= cshift;
            currByte--;
        }

        /* middle byte(s) */
        for (; currByte >= (startByte + 1); currByte--) {
            this.buf.put(currByte, (byte) correctedValue);
            correctedValue >>>= BIT_CHAR;
        }
        /* end byte contains LSB */
        if ((index % BIT_CHAR) > 0) {
            mask = (~0) << (BIT_CHAR - (index % BIT_CHAR));
            cmask = correctedValue & ~mask;
            int b = this.buf.get(currByte) & 0xFF;
            this.buf.put(currByte, (byte) ((b & mask) | cmask));
        } else {
            this.buf.put(currByte, (byte) correctedValue);
        }
    }

    private void putIntLE(int index, int length, int value) {
        assert ((length > 0) && (length <= BIT_INT));
        int end = index + length;
        int startByte = index / BIT_CHAR;
        int endByte = (end + (BIT_CHAR - 1)) / BIT_CHAR;
        int currByte, lshift, cshift, mask, cmask;
        int correctedValue = value;

        /*
         * mask v high bits. Works for unsigned and two complement signed
         * numbers which value do not overflow on length bits.
         */

        if (length < BIT_INT) {
            correctedValue &= ~(~0 << length);
        }

        /* sub byte */
        if (startByte == (endByte - 1)) {
            lshift = index % BIT_CHAR;
            mask = ~((~0) << lshift);
            if ((end % BIT_CHAR) > 0) {
                mask |= (~(0)) << (end % BIT_CHAR);
            }
            cmask = correctedValue << lshift;
            /*
             * low bits are cleared because of lshift and high bits are already
             * cleared
             */
            cmask &= ~mask;
            int b = this.buf.get(startByte) & 0xFF;
            this.buf.put(startByte, (byte) ((b & mask) | cmask));
            return;
        }

        /* head byte */
        currByte = startByte;
        cshift = index % BIT_CHAR;
        if (cshift > 0) {
            mask = ~((~0) << cshift);
            cmask = correctedValue << cshift;
            cmask &= ~mask;
            int b = this.buf.get(currByte) & 0xFF;
            this.buf.put(currByte, (byte) ((b & mask) | cmask));
            correctedValue >>>= BIT_CHAR - cshift;
            // index += BIT_CHAR - cshift;
            currByte++;
        }

        /* middle byte(s) */
        for (; currByte < (endByte - 1); currByte++) {
            this.buf.put(currByte, (byte) correctedValue);
            correctedValue >>>= BIT_CHAR;
        }
        /* end byte */
        if ((end % BIT_CHAR) > 0) {
            mask = (~0) << (end % BIT_CHAR);
            cmask = correctedValue & ~mask;
            int b = this.buf.get(currByte) & 0xFF;
            this.buf.put(currByte, (byte) ((b & mask) | cmask));
        } else {
            this.buf.put(currByte, (byte) correctedValue);
        }
    }
    /**
     * Checks whether this buffer can or cannot read a specific amount of bits.
     *
     * @param length    Length to read (bits)
     * @return True if buffer has enough room to read the next <code>length</code> bits
     */
    public boolean canRead(int length) {
        return canRead(this.pos, length);
    }

    /**
     * Checks whether this buffer can or cannot read a specific amount of bits (absolute).
     *
     * @param index     Position in the buffer (bits)
     * @param length    Length to read (bits)
     * @return True if buffer has enough room to read the next <code>length</code> bits from position <code>index</code>
     */
    public boolean canRead(int index, int length) {
        if (this.buf == null) {
            return false;
        }

        if ((index + length) > (this.buf.capacity() * BIT_CHAR)) {
            return false;
        }
        return true;
    }

    /**
     * Sets the byte order of the buffer.
     *
     * @param order New order
     */
    public void order(ByteOrder order) {
        this.byteOrder = order;
        if (this.buf != null) {
            this.buf.order(order);
        }
    }

    /**
     * Gets the byte order of the buffer.
     *
     * @return  Current buffer byte order
     */
    public ByteOrder order() {
        return this.byteOrder;
    }

    /**
     * Sets the position of the buffer.
     *
     * @param newPosition   New position (bits)
     */
    public void position(int newPosition) {
        this.pos = newPosition;
    }

    /**
     * Gets the position of the buffer.
     *
     * @return  Current buffer position
     */
    public int position() {
        return this.pos;
    }

    /**
     * Sets the underlying byte buffer.
     *
     * @param buf   Underlying byte buffer
     */
    public void setByteBuffer(ByteBuffer buf) {
        this.buf = buf;
        if (buf != null) {
            this.buf.order(this.byteOrder);
        }
        clear();
    }

    /**
     * Gets the underlying byte buffer.
     *
     * @return  Underlying byte buffer
     */
    public ByteBuffer getByteBuffer() {
        return this.buf;
    }

    /**
     * Clears this buffer.
     * <p>
     * This has the effect or resetting the buffer's position to 0 and making
     * everything ready to read/write.
     */
    public void clear() {
        position(0);

        if (this.buf == null) {
            return;
        }
        this.buf.clear();
    }
}
