/*
 * Copyright (c) 2017. ZhangLong
 */

package javacard.framework;

/**
 * Created by yuchangnet on 2017/6/29.
 */
public class JavacardException extends Exception {

    public static final short c_OutofRange = 1;

    public short m_reasonVal = 0;
    private String m_reasonStr = null;

    public String GetReason()
    {
        return m_reasonStr;
    }
    /**
     *
     * @param reasonVal
     * @param reasonStr
     */
    public JavacardException(short reasonVal, String reasonStr){
        m_reasonVal = reasonVal;
        m_reasonStr = reasonStr;
    }
}
