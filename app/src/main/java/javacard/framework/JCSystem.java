/*
 * Copyright (c) 2017. ZhangLong
 */

package javacard.framework;

/**
 * Created by yuchangnet on 2017/6/29.
 */
public class JCSystem {
    public static final short CLEAR_ON_DESELECT = 1;

    public static byte[] makeTransientByteArray(short length, short clearOnEvent){
        return new byte[length];
    }
}
