package com.longxing.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.longxing.log.LogToSystem;

/**
 * Created by Zhang Long on 2017/7/14.
 * system setting with files
 */

public class SystemSetting {
    private static final String TAG = "MyLog/SystemSetting/";
    private static final Object sAccountLock = new Object();

    /**
     * save config
     *
     * @param c     Context
     * @param name  name of config
     * @param value value of config
     */
    public static void saveCfg(Context c, String name, String value) {
        try {
            synchronized (sAccountLock) {
                //LogToFile.i(TAG, "Setting account number: " + s);
                //LogToSystem.i(TAG, "Setting account number: " + s);
                SharedPreferences prefs;
                prefs = PreferenceManager.getDefaultSharedPreferences(c);
                if (!prefs.getString(name, "").equals(value)) {
                    LogToSystem.d(TAG, "config: " + name + "=" + value);
                    prefs.edit().putString(name, value).apply();
                } else {
                    LogToSystem.d(TAG + "saveCfg", "no need to save value(equal)");
                }
            }
        } catch (Exception ex) {
            LogToSystem.e(TAG + "saveCfg", ex.getMessage());
        }
    }

    /**
     * get config
     *
     * @param c    context
     * @param name name of config
     * @return value of config
     */
    public static String getCfg(Context c, String name) {
        try {
            synchronized (sAccountLock) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
                return prefs.getString(name, "");
            }
        } catch (Exception ex) {
            LogToSystem.e(TAG + "getCfg", ex.getMessage());
        }
        return "";
    }


    /**
     * delete config
     *
     * @param c    context
     * @param name name of config
     * @return value of config
     */
    public static boolean delCfg(Context c, String name) {
        try {
            synchronized (sAccountLock) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
                prefs.edit().remove(name).commit();
            }
        } catch (Exception ex) {
            LogToSystem.e(TAG + "delCfg", ex.getMessage());
            return false;
        }
        return true;
    }
}
