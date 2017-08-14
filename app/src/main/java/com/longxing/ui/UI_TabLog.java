package com.longxing.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.longxing.R;
import com.longxing.cardemulation.AccountStorage;

/**
 * Created by Zhang Long on .
 * <p>
 * log show for the app
 */

public class UI_TabLog implements IUI_TabMain {
    /**
     * tag for log
     */
    private static final String TAG = "MyLog/UI_TabLog/";

    private static final String cKeyLog = "logMsg";
    private static UI_TabLog sUiTabLog;
    private Handler mEditViewHandle;
    /**
     * main ui interface
     */
    private MainActivity mMainActivity;


    private UI_TabLog() {

    }

    /**
     * @return owner object
     */
    public static UI_TabLog getInstance() {
        if (sUiTabLog == null) {
            sUiTabLog = new UI_TabLog();
            sUiTabLog.mMainActivity = MainActivity.getInstance();
        }
        return sUiTabLog;
    }

    @Override
    public void initUI(View rootView) {

        //mIsInited = true;
        //View rootView = mMainActivity.GetLayerById(R.layout.tab_log);

        final ScrollView scrollViewLog = (ScrollView) rootView.findViewById(R.id.ScrollLog);

        final TextView textView = (TextView) rootView.findViewById(R.id.data_tv);
        if (textView != null) {

            mEditViewHandle = new Handler() {
                @Override
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == 0) {
                        Bundle bundle = msg.getData();
                        textView.append(bundle.getString(cKeyLog));
                        scrollViewLog.fullScroll(ScrollView.FOCUS_DOWN);
                        scrollViewLog.clearFocus();
                        //LogToSystem.i(TAG, "mEditViewHandle: " + mTextView.getText());
                    }
                    super.handleMessage(msg);
                }
            };
        }
        String account = AccountStorage.GetAccount(mMainActivity);
        //LogToSystem.i(TAG, "AccountStorage.GetAccount(mMainActivity): " + account);
        // add listen to edit view
        EditText dataEdit = (EditText) rootView.findViewById(R.id.data_edt);
        dataEdit.setText(account);
        dataEdit.setOnEditorActionListener(new ProcEnterKeyPress());
        dataEdit.addTextChangedListener(new UI_TabLog.AccountUpdater());
        //dataEdit.clearFocus();

        //LogToSystem.d(TAG + "initUI", FileInforModel.class.getFields()[0].getName());

        scrollViewLog.requestFocus();

        displayLog("日志面板初始化完成:" + account);
    }

    @Override
    public boolean processKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void processDestroy() {

    }

    /**
     * @param msg show message
     */
    public void displayLog(String msg) {
        displayLog(msg, 1);
    }

    /**
     * @param msg  show message
     * @param line how much lines will append
     */
    private void displayLog(String msg, int line) {
        if (mEditViewHandle != null) {

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
    }

    /**
     * account update
     */
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
        }
    }


    /**
     * account update
     */
    private class ProcEnterKeyPress implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //LogToSystem.d(TAG + "onEditorAction1", "" + actionId);

            int keyCode = 0;
            if (event != null) {
                keyCode = event.getKeyCode();
            }
            Editable text = v.getEditableText();
            //LogToSystem.d(TAG + "onEditorAction", text + ":" + keyCode + "==" + KeyEvent.KEYCODE_ENTER);
            if (actionId == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER) {
                //byte[] adpu = {0x00,(byte)0xA4,0x04,0x00,0x10,(byte)0xD1,0x56,0x00,0x01,0x01,(byte)0x80,0x08,(byte)0x80,0x16,(byte)0x81,0x68,0x01,0x00,0x00,0x00,0x03};
                //byte[] adpu = {0x00,(byte)0x84,0x00,0x00,0x04};
                //byte[] adpu = {0x00,(byte)0xB0,(byte)0x84,0x00,0x00};
                //new CardService().processCommandApdu(adpu, null);
                String account = text.toString();
                boolean isYaPing = false;   // 是否匹配的是雅平
                //
                int index = account.indexOf("王雅平");
                if (index < 0) {
                    index = account.indexOf("雅平");
                    isYaPing = true;
                }
                if (index >= 0) {
                    displayLog("I love " + (isYaPing ? "王" : "") + account.substring(index));
                } else {
                    displayLog("No no no " + account);
                }
                AccountStorage.SetAccount(mMainActivity, account);
                return true;
            }
            return false;
        }
    }
}
