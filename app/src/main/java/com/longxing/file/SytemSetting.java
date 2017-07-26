package com.longxing.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.longxing.log.LogToSystem;

/**
 * Created by Zhang Long on 2017/7/14.
 */

public class SytemSetting {
    private static final String TAG = "MyLog/SytemSetting";
    private static final Object sAccountLock = new Object();

    /**
     * save config
     *
     * @param c     Context
     * @param name  name of config
     * @param value value of config
     */
    public static void SaveCfg(Context c, String name, String value) {
        synchronized (sAccountLock) {
            //LogToFile.i(TAG, "Setting account number: " + s);
            //LogToSystem.i(TAG, "Setting account number: " + s);
            SharedPreferences prefs;
            prefs = PreferenceManager.getDefaultSharedPreferences(c);
            if (!prefs.getString(name, null).equals(value)) {
                LogToSystem.d(TAG, "config: " + name + "=" + value);
                prefs.edit().putString(name, value).apply();
            }
        }
    }

    /**
     * get config
     *
     * @param c    context
     * @param name name of config
     * @return value of config
     */
    public static String GetCfg(Context c, String name) {
        synchronized (sAccountLock) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
            return prefs.getString(name, "");
        }
    }
}
