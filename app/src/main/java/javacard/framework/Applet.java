/*
 * Copyright (c) 2017. ZhangLong
 */

package javacard.framework;

/**
 * Created by yuchangnet on 2017/6/29.
 */
public class Applet {
    /**
     * max length of AID
     */
    public static final short c_maxAidLen = 16;

    protected boolean m_isSelectingApplet = false;

    private byte[] m_aid = new byte[c_maxAidLen + ISO7816.CONST_1];

    private static final byte[] c_selHeader = {0x00, (byte) 0xA4, 0x04, 0x00};

    public void preProcess(APDU apdu) throws JavacardException, ISOException {
        byte[] buffer = apdu.getBuffer();
        short length = (short) (buffer[ISO7816.OFFSET_LC] & 0x0FF);

        m_isSelectingApplet = false;
        if (Util.arrayCompare(buffer, ISO7816.OFFSET_CLA, c_selHeader, ISO7816.CONST_0, (short) c_selHeader.length) == 0) {

            if (length == m_aid[ISO7816.CONST_0] && Util.arrayCompare(buffer, ISO7816.OFFSET_DATA, m_aid, ISO7816.CONST_1, m_aid[ISO7816.CONST_0]) == 0) {
                m_isSelectingApplet = true;
            }
        }

    }

    protected boolean selectingApplet() {
        return m_isSelectingApplet;
    }

    public void process(APDU apdu) throws JavacardException, ISOException {

    }

    /**
     * @param bArray
     * @param offset
     * @param length
     */
    protected void register(byte[] bArray, short offset, short length) throws JavacardException {
        if (length > c_maxAidLen) {
            throw new JavacardException(JavacardException.c_OutofRange, "AID的长度大于" + c_maxAidLen);
        }
        Util.arrayCopyNonAtomic(bArray, offset, m_aid, ISO7816.CONST_1, length);
        m_aid[ISO7816.CONST_0] = (byte) length;
    }
}
