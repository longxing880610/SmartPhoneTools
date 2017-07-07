/*
 * Copyright (c) 2017. ZhangLong
 */

package javacard.framework;

/**
 * Created by yuchangnet on 2017/6/29.
 */
public class ISO7816 {
    public static final short CONST_0 = 0;
    public static final short CONST_1 = 1;
    public static final short CONST_2 = 2;
    ///////////////////////////////////////////////
    // offset
    public static final short OFFSET_CLA = 0;
    public static final short OFFSET_INS = 1;
    public static final short OFFSET_P1 = 2;
    public static final short OFFSET_P2 = 3;
    public static final short OFFSET_P3 = 4;
    public static final short OFFSET_LC = 4;
    public static final short OFFSET_DATA = 5;


    ////////////////////////////////////////////////////////////
    // INS
    public static final byte INS_SELECT = (byte)0xA4;

    ////////////////////////////////////////////////////////////
    // ISO
    public static final short SW_NO_ERROR = (short)0x9000;
    public static final short SW_FILE_NOT_FOUND = 0x6A82;
    public static final short SW_INCORRECT_P1P2 = 0x6A86;
    public static final short SW_INS_NOT_SUPPORTED = 0x6D00;

}
