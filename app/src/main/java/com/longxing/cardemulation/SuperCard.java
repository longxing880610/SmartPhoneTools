/**
 *
 */
package com.longxing.cardemulation;


import com.longxing.log.LogToSystem;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.JCSystem;
import javacard.framework.JavacardException;
import javacard.framework.Util;

/**
 * @author Administrator
 */
public class SuperCard extends Applet {

    private byte[] m_cardInfo = null;    // 45 bytes
    private short m_areaCount = 0;
    private byte[] m_userInfo = null;    // 30 bytes
    private byte[] m_shareBytes = null;    // 45 bytes
    private short m_areaTryId = 350;
    private short m_maxAreaTryId = 1000;
    private short m_maxAreaCount = 10;
    private short m_maxPermitAreaCount = 1;
    //private byte[] m_apduBufer = new byte[10000];
    //private short m_apduLen = 0;

    private SuperCard() {
        //short pos = 0;

        m_cardInfo = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        m_userInfo = new byte[]{(byte) 0x02, (byte) 0x8D, (byte) 0x9B, (byte) 0x3B, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x99, (byte) 0x99, (byte) 0x99, (byte) 0x99, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        m_shareBytes = JCSystem.makeTransientByteArray((short) 45, JCSystem.CLEAR_ON_DESELECT);
    }

    public static SuperCard install(byte[] bArray, short bOffset, byte bLength) {
        // GP-compliant JavaCard applet registration
        try {
            SuperCard superCard = new SuperCard();
            superCard.register(bArray, (short) (bOffset + 1), bArray[bOffset]);
            return superCard;
        } catch (JavacardException e) {
            LogToSystem.e("SuperCard install", e.GetReason());
        }
        return null;
    }

    public void process(APDU apdu) throws JavacardException, ISOException {
        byte[] tmpBys = null;
        // Good practice: Return 9000 on SELECT
        if (selectingApplet()) {
            tmpBys = m_userInfo;
            apdu.setOutgoing();
            apdu.setOutgoingLength((short) (4));
            apdu.sendBytesLong(tmpBys, (short) 0, (short) (4));
            return;
        }
        short pos = 0;
        byte[] buf = apdu.getBuffer();
        short p1 = (short) (buf[ISO7816.OFFSET_P1] & 0x0FF);
        short p2 = (short) (buf[ISO7816.OFFSET_P2] & 0x0FF);
        short length = (short) (buf[ISO7816.OFFSET_LC] & 0x0FF);
        short i = 0;

        //tmpBys = m_apduBufer;
        //byte p2 = buf[ISO7816.OFFSET_P2];
        switch (buf[ISO7816.OFFSET_INS]) {
            case (byte) 0xB0:
                p1 &= 0x07F;
                switch (p1) {
                    case 0x03:
                        tmpBys = m_userInfo;
                        apdu.setOutgoing();
                        apdu.setOutgoingLength((short) (tmpBys.length - p2));
                        apdu.sendBytesLong(tmpBys, p2, (short) (tmpBys.length - p2));
                        break;
                    case 0x04:
                        pos = 0;
                        m_shareBytes[pos] = m_cardInfo[pos];
                        ++pos;

                        Util.arrayCopyNonAtomic(m_cardInfo, pos, m_shareBytes, pos, (short) (m_areaCount << 2));
                        //m_areaTryId = 350;
                        if (m_areaCount < m_maxPermitAreaCount) {
                            for (i = m_areaCount; i < m_maxAreaCount; ++i) {
                                if ((m_areaTryId + i) > m_maxAreaTryId) {
                                    m_areaTryId = 1;
                                }
                                SetsInt(m_shareBytes, (short) (1 + (i << 2)), (short) (m_areaTryId + i));
                            }
                            if (m_areaCount < m_maxAreaCount) {
                                m_areaTryId += i - m_areaCount;
                            }
                        }
                        tmpBys = m_shareBytes;
                        apdu.setOutgoing();
                        apdu.setOutgoingLength((short) (tmpBys.length - p2));
                        apdu.sendBytesLong(tmpBys, p2, (short) (tmpBys.length - p2));
                        break;
                    default:
                        ISOException.throwIt(ISO7816.SW_FILE_NOT_FOUND);
                }
                break;
            case (byte) 0x84:
                Util.arrayFillNonAtomic(buf, (short) 0, length, (byte) 0x00);
                apdu.setOutgoingAndSend((short) 0, length);
                break;
            case (byte) 0x82:
                p2 -= 2;
                if (p2 >= m_areaCount && (m_areaCount < m_maxPermitAreaCount)) {
                    Util.arrayCopyNonAtomic(m_shareBytes, (short) (1 + (p2 << 2)), m_cardInfo, (short) (1 + (m_areaCount << 2)), (short) (4));
                    ++m_areaCount;
                    m_areaTryId = 1;
                }
                break;
            case (byte) 0xB2:
                pos = 0;
                buf[pos++] = 0x01;
                buf[pos++] = 0x30;
                length = 3;
                Util.arrayFillNonAtomic(buf,  pos, length, (byte) 0x99);
                pos += length;
                length = 45;
                Util.arrayFillNonAtomic(buf, pos, length, (byte) 0xFF);
                pos += length;
                apdu.setOutgoingAndSend((short) 0, pos);
                break;
            case (byte) 0xFD:
                if (p1 != 0x00FD || p2 != 0x00FD) {
                    ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
                }
                if (m_maxPermitAreaCount < m_maxAreaCount) {
                    ++m_maxPermitAreaCount;
                }
                break;
            //case (byte)0xFE:
            //	tmpBys = m_apduBufer;
            //	apdu.setOutgoing();
            //	apdu.setOutgoingLength((short)(m_apduLen-p2));
            //	apdu.sendBytesLong(tmpBys, p2, (short)(m_apduLen));
            //	break;
            default:
                // good practice: If you don't know the INStruction, say so:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    /**
     * set short with int format to byte buffer
     * @param src src of buffer
     * @param offset offset of buffer
     * @param value value will be set
     */
    private void SetsInt(byte[] src, short offset, short value) {
        src[offset] = 0x00;
        ++offset;
        src[offset] = 0x00;
        ++offset;
        src[offset] = (byte) ((value >> 8) & 0x0FF);
        ++offset;
        src[offset] = (byte) ((value) & 0x0FF);
    }
}