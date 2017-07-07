/*
 * Copyright (c) 2017. ZhangLong
 */

package javacard.framework;

/**
 * Created by yuchangnet on 2017/6/29.
 */
public class APDU {

    private boolean m_isOutgoing = false;
    private short m_outgoingLen = 0;
    private short m_outgoingOffset = 0;
    private byte[] m_buffer = new byte[500];
    private short m_sw1sw2 = 0x0000;

    ///////////////////////////////////////////////////////
    // property method
    public byte[] getBuffer(){
        return m_buffer;
    }


    ///////////////////////////////////////////////////////

    public void setOutgoing()
    {
        m_outgoingLen = 0;
        m_isOutgoing = true;
    }

    public void setOutgoingLength(short length){
        m_outgoingLen = length;
        m_outgoingOffset = 0;
    }

    public void setOutgoingAndSend(short offset, short length) throws JavacardException {
        Util.arrayCopyNonAtomic(m_buffer, offset, m_buffer, (short)0, length);
        m_isOutgoing = true;
        m_outgoingLen = length;
        m_outgoingOffset = length;
    }

    public void sendBytesLong(byte[] src, short offset, short length) throws JavacardException {
        if (m_outgoingOffset + length > m_outgoingLen)
        {
            throw new JavacardException(JavacardException.c_OutofRange, "发送的数据长度超出发送总长度限制");
        }
        Util.arrayCopyNonAtomic(src, offset, m_buffer, m_outgoingOffset, length);
        m_outgoingOffset += length;
    }

    public void sendSw1Sw2(short sw1sw2)
    {
        m_sw1sw2 = sw1sw2;
    }

    // property method
    public byte[] getResponseBuffer(){
        byte[] resp = new byte[m_outgoingLen+ISO7816.CONST_2];
        short offset = 0;
        Util.arrayCopyNonAtomic(m_buffer, (short)0, resp, offset, m_outgoingLen);
        offset = m_outgoingLen;
        resp[offset] = (byte)((m_sw1sw2>>8)&0x0FF);
        ++offset;
        resp[offset] = (byte)((m_sw1sw2)&0x0FF);
        return resp;
    }
}
