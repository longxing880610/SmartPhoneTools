/*
 * Copyright (c) 2017. ZhangLong
 */

package javacard.framework;

/**
 * Created by Zhang Long on 2017/6/29.
 *
 * iso exception
 */
public class ISOException extends Exception {

    private static final long serialVersionUID = 1000000;

    private short m_reasonVal = 0;

    private static ISOException s_ownObj = new ISOException();

    public short GetReason() {
        return s_ownObj.m_reasonVal;
    }

    public static void throwIt(short reasonVal) throws ISOException {
        s_ownObj.m_reasonVal = reasonVal;
        throw s_ownObj;
    }
}
