/*
 * Copyright (c) 2017. ZhangLong
 */

package com.longxing.log;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 将Log日志写入文件中
 * <p/>
 * 使用单例模式是因为要初始化文件存放位置
 * <p/>
 * Created by waka on 2016/3/14.
 */
public class LogToSystem {
    //private static String TAG = "MyLog/LogToSystem/";

    /**
     * 初始化，须在使用之前设置，最好在Application创建时调用
     *
     * @param context
     */
    public static void init(Context context) {
        //logPath = getFilePath(context) + "/Logs";//获得文件储存路径,在后面加"/Logs"建立子文件夹
    }

    /**
     * 获得文件存储路径
     *
     * @return
     */
    private static String getFilePath(Context context) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {//如果外部储存可用
            return context.getExternalFilesDir(null).getPath();//获得外部存储路径,默认路径为 /storage/emulated/0/Android/data/com.waka.workspace.logtofile/files/Logs/log_2016-03-14_16-15-09.log
        } else {
            return context.getFilesDir().getPath();//直接存在/data/data里，非root手机是看不到的
        }
    }
/*
    private static final char VERBOSE = 'v';

    private static final char DEBUG = 'd';

    private static final char INFO = 'i';

    private static final char WARN = 'w';

    private static final char ERROR = 'e';
*/

    private static final int VERBOSE = Log.VERBOSE;

    private static final int DEBUG = Log.DEBUG;

    private static final int INFO = Log.INFO;

    private static final int WARN = Log.WARN;

    private static final int ERROR = Log.ERROR;

    private static final int ASSERT = Log.ASSERT;

    public static void v(String tag, String msg) {
        writeToSystem(VERBOSE, tag, msg);
    }

    public static void d(String tag, String msg) {
        writeToSystem(DEBUG, tag, msg);
    }

    public static void i(String tag, String msg) {
        writeToSystem(INFO, tag, msg);
    }

    public static void w(String tag, String msg) {
        writeToSystem(WARN, tag, msg);
    }

    public static void a(String tag, String msg) {
        writeToSystem(ASSERT, tag, msg);
    }

    public static void t(String tag, String msg) {
        System.out.println(tag + ":" + msg);
    }

    /**
     * write to log file when error occur
     *
     * @param tag tag of error information
     * @param msg message of error information
     */
    public static void e(String tag, String msg) {

        writeToSystem(ERROR, tag, msg);

        LogToFile.e(tag, msg);
    }

    /**
     * 将log信息写入文件中
     *
     * @param prioty
     * @param tag
     * @param msg
     */
    private static void writeToSystem(int prioty, String tag, String msg) {

        Log.println(prioty, tag, msg);

    }
}