/*
 * Copyright (c) 2017. ZhangLong
 */

package javacard.framework;

/**
 * Created by yuchangnet on 2017/6/29.
 */
public class Util {
    /**
     * copy the array bytes
     *
     * @param src
     * @param soff
     * @param des
     * @param doff
     * @param length
     */
    public static void arrayCopyNonAtomic(byte[] src, short soff, byte[] des, short doff, short length) {
        if (src != des || soff >= doff) {   // copy from small offset
            for (short i = 0; i < length; ++i) {
                des[i + doff] = src[i + soff];
            }
        } else {   // copy from big offset
            for (short i = (short) (length - 1); i >= 0; --i) {
                des[i + doff] = src[i + soff];
            }
        }
    }

    public static void arrayFillNonAtomic(byte[] src, short offset, short length, byte fillByte) {
        for (short i = 0; i < length; ++i) {
            src[i + offset] = fillByte;
        }
    }

    public static short arrayCompare(byte[] src, short soff, byte[] des, short doff, short length) {

        for (short i = 0; i < length; ++i) {
            short tmp = (short) (des[i + doff] - src[i + soff]);
            if (tmp > 0) {
                return 1;
            } else if (tmp < 0) {
                return -1;
            }
        }
        return 0;
    }

    public static void makeShort(byte[] src, short offset, short value) {
        src[offset] = (byte) ((value >> 8) & 0x0FF);
        ++offset;
        src[offset] = (byte) ((value) & 0x0FF);
    }
}
