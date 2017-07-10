package com.longxing.ui;

import android.view.View;

/**
 * Created by Zhang Long on .
 * <p>
 * show sd files
 */

public class UI_TabSdFiles implements IUI_TabMain{
    /**
     * tag for log
     */
    private static final String TAG = "MyLog/UI_TabSdFiles";

    private static UI_TabSdFiles sUiTabLog;

    /**
     * main ui interface
     */
    private MainActivity mMainActivity;
    private UI_TabLog mUI_tabLog;


    private UI_TabSdFiles() {

    }

    /**
     * @return owner object
     */
    public static UI_TabSdFiles getInstance() {
        if (sUiTabLog == null) {
            sUiTabLog = new UI_TabSdFiles();
            sUiTabLog.mMainActivity = MainActivity.GetInstance();
            sUiTabLog.mUI_tabLog = UI_TabLog.getInstance();
        }
        return sUiTabLog;
    }

    public @Override void initUI(View rootView) {


        //final ScrollView scrollViewLog = (ScrollView) rootView.findViewById(R.id.ScrollLog);


        displayLog("SD文件管理加载完成");
    }

    /**
     * @param msg show message
     */
    public void displayLog(String msg) {
        mUI_tabLog.displayLog(msg);
    }
}
