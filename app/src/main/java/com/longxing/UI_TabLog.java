package com.longxing;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.longxing.cardemulation.AccountStorage;

/**
 * Created by yuchangnet on 2017/7/6.
 */

public class UI_TabLog {
    /**
     * tag for log
     */
    private static final String TAG = "MyLog/UI_TabLog";

    private static final String cKeyLog = "logMsg";

    private Handler mEditViewHandle;
    private TextView mTextView;
    private ScrollView mScrollViewLog;

    private boolean mIsInited = false;

    private MainActivity mMainActivity;

    private static UI_TabLog sUiTablog;


    private UI_TabLog() {

    }

    /**
     * @return
     */
    public static UI_TabLog getInstance() {
        if (sUiTablog == null) {
            sUiTablog = new UI_TabLog();
            sUiTablog.mMainActivity = MainActivity.GetInstance();
        }
        return sUiTablog;
    }

    public void initUI(View rootView) {

        if (mIsInited) {   // execute only once
            return;
        }
        //mIsInited = true;
        //View rootView = mMainActivity.GetLayerById(R.layout.tab_log);

        mScrollViewLog = (ScrollView) rootView.findViewById(R.id.ScrollLog);

        TextView textView = (TextView) rootView.findViewById(R.id.data_tv);
        if (textView != null) {
            textView.setText("hello hello");

            mTextView = textView;

            mEditViewHandle = new Handler() {
                @Override
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == 0) {
                        Bundle bundle = msg.getData();
                        mTextView.append(bundle.getString(cKeyLog));
                        mScrollViewLog.fullScroll(ScrollView.FOCUS_DOWN);
                        //Log.i(TAG, "mEditViewHandle: " + mTextView.getText());
                    }
                    super.handleMessage(msg);
                }
            };

            //textView.han

        }
        String account = AccountStorage.GetAccount(mMainActivity);
        Log.i(TAG, "AccountStorage.GetAccount(mMainActivity): " + account);
        // add listen to edit view
        EditText dataEdit = (EditText) rootView.findViewById(R.id.data_edt);
        dataEdit.setText(account);
        dataEdit.addTextChangedListener(new UI_TabLog.AccountUpdater());


        displayLog("初始化完成" + account);
    }

    /**
     * @param msg
     */
    public void displayLog(String msg) {
        displayLog(msg, 1);
    }

    /**
     * @param msg
     * @param line
     */
    public void displayLog(String msg, int line) {
        StringBuilder strBuilder = new StringBuilder(msg);
        for (int i = 0; i < line; ++i) {
            strBuilder.append("\r\n");
        }
        Message msgObj = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(cKeyLog, strBuilder.toString());
        msgObj.setData(bundle);
        //msgObj.what  = 1;
        mEditViewHandle.sendMessage(msgObj);
    }


    private class AccountUpdater implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not implemented.
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Not implemented.
        }

        @Override
        public void afterTextChanged(Editable s) {
            //byte[] adpu = {0x00,(byte)0xA4,0x04,0x00,0x10,(byte)0xD1,0x56,0x00,0x01,0x01,(byte)0x80,0x08,(byte)0x80,0x16,(byte)0x81,0x68,0x01,0x00,0x00,0x00,0x03};
            //byte[] adpu = {0x00,(byte)0x84,0x00,0x00,0x04};
            //byte[] adpu = {0x00,(byte)0xB0,(byte)0x84,0x00,0x00};
            //new CardService().processCommandApdu(adpu, null);
            String account = s.toString();
            boolean isYaping = false;
            //
            int index = account.indexOf("王雅平");
            if (index < 0) {
                index = account.indexOf("雅平");
                isYaping = true;
            }
            if (index >= 0) {
                displayLog("I love " + (isYaping ? "王" : "") + account.substring(index));
            } else {
                displayLog("No no no " + account);
            }
            AccountStorage.SetAccount(mMainActivity, account);
        }
    }
}
