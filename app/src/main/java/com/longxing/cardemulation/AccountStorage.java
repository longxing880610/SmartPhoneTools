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

import android.content.Context;

import com.longxing.file.SystemSetting;

/**
 * Utility class for persisting account numbers to disk.
 * <p>
 * <p>The default SharedPreferences instance is used as the backing storage. Values are cached
 * in memory for performance.
 * <p>
 * <p>This class is thread-safe.
 */
public class AccountStorage {
    private static final String PREF_ACCOUNT_NUMBER = "userName";
    //private static final String TAG = "MyLog/AccountStorage/";
    private static String sAccount = null;
    private static final Object sAccountLock = new Object();

    /**
     * @param c context
     * @param s account
     */
    public static void SetAccount(Context c, String s) {

        SystemSetting.SaveCfg(c, PREF_ACCOUNT_NUMBER, s);
        sAccount = s;
    }

    public static String GetAccount(Context c) {
        synchronized (sAccountLock) {
            if (sAccount == null) {
                sAccount = SystemSetting.GetCfg(c, PREF_ACCOUNT_NUMBER);
            }
            return sAccount;
        }
    }
}
