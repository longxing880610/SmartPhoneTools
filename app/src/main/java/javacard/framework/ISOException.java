/*
 * Copyright (c) 2017. ZhangLong
 */

package javacard.framework;

/**
 * Created by yuchangnet on 2017/6/29.
 */
public class ISOException extends Exception {
    public short m_reasonVal = 0;

    private static ISOException s_ownObj = new ISOException();

    public short GetReason() {
        return s_ownObj.m_reasonVal;
    }

    public static void throwIt(short reasonVal) throws ISOException {
        s_ownObj.m_reasonVal = reasonVal;
        throw s_ownObj;
    }
}
