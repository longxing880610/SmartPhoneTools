/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.longxing.cardemulation;

import android.annotation.TargetApi;
import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Bundle;

import com.longxing.log.LogToFile;
import com.longxing.ui.UI_TabLog;

import javacard.framework.APDU;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.JavacardException;
import javacard.framework.Util;

/**
 * This is a sample APDU Service which demonstrates how to interface with the card emulation support
 * added in Android 4.4, KitKat.
 * <p/>
 * <p>This sample replies to any requests sent with the string "Hello World". In real-world
 * situations, you would need to modify this code to implement your desired communication
 * protocol.
 * <p/>
 * <p>This sample will be invoked for any terminals selecting AIDs of 0xF11111111, 0xF22222222, or
 * 0xF33333333. See src/main/res/xml/aid_list.xml for more details.
 * <p/>
 * <p class="note">Note: This is a low-level interface. Unlike the NdefMessage many developers
 * are familiar with for implementing Android Beam in apps, card emulation only provides a
 * byte-array based communication channel. It is left to developers to implement higher level
 * protocol support as needed.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class CardService extends HostApduService {
    private static final String TAG = "MyLog/CardService";

    private static final byte[] AID_Name = {0x10, (byte) 0xD1, (byte) 0x56, 0x00, 0x01, 0x01, (byte) 0x80, 0x08, (byte) 0x80, 0x16, (byte) 0x81, 0x68, 0x01, 0x00, 0x00, 0x00, 0x03};

    private static SuperCard s_supperCard = SuperCard.install(AID_Name, (byte) 0, (byte) (AID_Name.length));

    private UI_TabLog mUiTabLog = UI_TabLog.getInstance();

    public void onDeactivated(int reason) {
    }

    /**
     * This method will be called when a command APDU has been received from a remote device. A
     * response APDU can be provided directly by returning a byte-array in this method. In general
     * response APDUs must be sent as quickly as possible, given the fact that the user is likely
     * holding his device over an NFC reader when this method is called.
     * <p/>
     * <p class="note">If there are multiple services that have registered for the same AIDs in
     * their meta-data entry, you will only get called if the user has explicitly selected your
     * service, either as a default or just for the next tap.
     * <p/>
     * <p class="note">This method is running on the main thread of your application. If you
     * cannot return a response APDU immediately, return null and use the {@link
     * #sendResponseApdu(byte[])} method later.
     *
     * @param commandApdu The APDU that received from the remote device
     * @param extras      A bundle containing extra data. May be null.
     * @return a byte-array containing the response APDU, or null if no response APDU can be sent
     * at this point.
     */

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {

        //LogToSystem.i(TAG, "Received APDU: " + ByteArrayToHexString(commandApdu));
        LogToFile.i(TAG, "Received APDU: " + ByteArrayToHexString(commandApdu));
        //Main2Activity.sb.append("Received APDU: " + ByteArrayToHexString(commandApdu) + "\n");
        //Main2Activity.dataHandler.sendEmptyMessage(1);
        DisplayLog("Received APDU: " + ByteArrayToHexString(commandApdu));

        APDU apdu = new APDU();
        try {
            Util.arrayCopyNonAtomic(commandApdu, (short) 0, apdu.getBuffer(), (short) 0, (short) commandApdu.length);
            s_supperCard.preProcess(apdu);
            s_supperCard.process(apdu);
            apdu.sendSw1Sw2(ISO7816.SW_NO_ERROR);
        } catch (JavacardException e) {
            LogToFile.i("processCommandApdu JavacardException", e.GetReason());
        } catch (ISOException e) {
            // LogToFile.i("processCommandApdu ISOException", ""+e.GetReason());
            apdu.sendSw1Sw2(e.GetReason());
        } catch (Exception e) {
            // LogToFile.i("processCommandApdu ISOException", ""+e.GetReason());
            apdu.sendSw1Sw2((short) 0x0000);
        }
        byte[] resp = apdu.getResponseBuffer();
        LogToFile.i(TAG, "Resp APDU: " + ByteArrayToHexString(resp));
        // Main2Activity.sb.append("Resp APDU: " + ByteArrayToHexString(resp) + "\n");
        //Main2Activity.dataHandler.sendEmptyMessage(1);
        DisplayLog("Resp APDU: " + ByteArrayToHexString(resp));

        return resp;
    }

    /**
     * Utility method to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
            hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
        }
        return new String(hexChars);
    }

    /**
     * @param msg display message
     */
    public void DisplayLog(String msg) {
        if (mUiTabLog != null) {
            mUiTabLog.displayLog(msg);
        }
    }
}
