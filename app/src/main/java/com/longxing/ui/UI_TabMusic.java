package com.longxing.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.longxing.R;
import com.longxing.log.LogToSystem;
import com.longxing.peripheral.MyBroadCastReceiver;
import com.longxing.peripheral.PlayingMusicServices;

/**
 * 这是一个Service生命周期及开启服务的小例子
 * 实现播放音乐功能
 */
public class UI_TabMusic implements IUI_TabMain {
    private static final String TAG = "MyLog/UI_TabMusic/";

    public static final int WHAT_PLAY_MUSIC = 1;
    private static final int WHAT_SET_MUSIC_NAME = WHAT_PLAY_MUSIC + 1;
    public static final int WHAT_SET_MUSIC_PROGRESS = WHAT_SET_MUSIC_NAME + 1;
    public static final int WHAT_SET_MUSIC_PROGRESS_MAX = WHAT_SET_MUSIC_PROGRESS + 1;
    private static final String cKEY_TXT = "KEY_TXT";
    private EStatusMusic mMusicStatus = EStatusMusic.STOP;

    private MyBroadCastReceiver receiver;

    private static UI_TabMusic sUiTabMusic;

    private MainActivity mMainActivity;
    private UI_TabLog mUI_tabLog;

    private View mRootView;

    private Handler mMusicHandle;

    private volatile boolean mIsSeekBarChangingisSeekBarChanging = false;

    /**
     * @return owner object
     */
    public static UI_TabMusic getInstance() {
        if (sUiTabMusic == null) {
            sUiTabMusic = new UI_TabMusic();
            sUiTabMusic.mMainActivity = MainActivity.getInstance();
            sUiTabMusic.mUI_tabLog = UI_TabLog.getInstance();
        }
        return sUiTabMusic;
    }

    @Override
    public void initUI(View rootView) {
        mRootView = rootView;

        receiver = new MyBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.complete");
        mMainActivity.registerReceiver(receiver, filter);

        Button btn = (Button) rootView.findViewById(R.id.btn_startMusic);
        btn.setOnClickListener(new ClickMusicBtn());
        btn = (Button) rootView.findViewById(R.id.btn_stopMusic);
        btn.setOnClickListener(new ClickMusicBtn());

        TextView textView_musicName1 = (TextView) rootView.findViewById(R.id.textview_musicName);

        SeekBar seekBar_music1 = (SeekBar) rootView.findViewById(R.id.seekBar_music);
        seekBar_music1.setOnSeekBarChangeListener(new ProcMusicSeekBar());

        TextView textView_totalLen1 = (TextView) rootView.findViewById(R.id.textView_totalLen);

        TextView textView_process1 = (TextView) rootView.findViewById(R.id.textView_progress);

        mMusicHandle = new HandleMusicMsg(textView_musicName1, seekBar_music1, textView_totalLen1, textView_process1);

        displayLog("音乐面板初始化完成");
    }

    /**
     * @param msg show message
     */
    private void displayLog(String msg) {
        if (mUI_tabLog != null) {
            mUI_tabLog.displayLog(msg);
        }
    }

    private void startMusic(String path) {
        Intent intent = new Intent(mMainActivity, PlayingMusicServices.class);
        intent.putExtra(PlayingMusicServices.cPARAM_TYPE, PlayingMusicServices.PLAT_MUSIC);
        intent.putExtra(PlayingMusicServices.cPARAM_FILEPATH, path);
        mMainActivity.startService(intent);

        mMusicStatus = EStatusMusic.START;
        Button btn = (Button) mRootView.findViewById(R.id.btn_startMusic);
        btn.setText(R.string.strPause);
    }

    private void playingMusic(int type) {
        //启动服务，播放音乐
        Intent intent = new Intent(mMainActivity, PlayingMusicServices.class);
        intent.putExtra("type", type);
        intent.putExtra("path", type);
        mMainActivity.startService(intent);
    }


    @Override
    public boolean processKeyDown(int keyCode, KeyEvent event) {

        return false;
    }

    @Override
    public void processDestroy() {
        //super.onDestroy();
        mMainActivity.unregisterReceiver(receiver);
    }

    /**
     * @param what   what control
     * @param txtMsg param message
     */
    public void sendMessage(int what, Object txtMsg) {
        Bundle bundle = new Bundle();

        if (txtMsg instanceof String) {
            bundle.putString(cKEY_TXT, (String) txtMsg);
        } else if (txtMsg instanceof Integer) {
            bundle.putInt(cKEY_TXT, (int) txtMsg);
        } else {
            bundle.putString(cKEY_TXT, txtMsg.toString());
        }

        Message msg = new Message();
        msg.what = what;
        msg.setData(bundle);
        mMusicHandle.sendMessage(msg);

    }

    /**
     * 点击音乐按钮的事件处理类
     */
    private class ClickMusicBtn implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Button btn = (Button) view;
            EStatusMusic status = mMusicStatus;

            switch (btn.getId()) {
                //开始音乐
                case R.id.btn_startMusic:
                    //启动服务，播放音乐
                    int playOper;
                    int btnText;
                    if (status == EStatusMusic.STOP) {
                        playOper = PlayingMusicServices.PLAT_MUSIC;
                        btnText = R.string.strPause;
                        status = EStatusMusic.START;
                    } else if (status == EStatusMusic.PAUSE) {
                        playOper = PlayingMusicServices.GOON_MUSIC;
                        btnText = R.string.strPause;
                        status = EStatusMusic.GOON;
                    } else if (status == EStatusMusic.GOON || status == EStatusMusic.START) {
                        playOper = PlayingMusicServices.PAUSE_MUSIC;
                        btnText = R.string.strGoon;
                        status = EStatusMusic.PAUSE;
                    } else {
                        playOper = PlayingMusicServices.PLAT_MUSIC;
                        btnText = R.string.strPause;
                        status = EStatusMusic.START;
                    }
                    Intent intent = new Intent(mMainActivity, PlayingMusicServices.class);
                    intent.putExtra(PlayingMusicServices.cPARAM_TYPE, playOper);
                    intent.putExtra(PlayingMusicServices.cPARAM_FILEPATH_INT, R.raw.dingdang_shouzhangxin);
                    mMainActivity.startService(intent);
                    sendMessage(WHAT_SET_MUSIC_NAME, "手掌心");
                    mMusicStatus = status;
                    btn.setText(btnText);
                    //playingMusic(PLAT_MUSIC);
                    break;
                //停止
                case R.id.btn_stopMusic:
                    if (status == EStatusMusic.STOP) {
                        return;
                    }
                    playingMusic(PlayingMusicServices.STOP_MUSIC);

                    mMusicStatus = EStatusMusic.STOP;
                    btn = (Button) mRootView.findViewById(R.id.btn_startMusic);
                    btn.setText(R.string.strStart);
                    break;
            }
        }
    }

    private class HandleMusicMsg extends Handler {

        private TextView mTextView_musicName;
        private SeekBar mSeekBar_music;
        private TextView mTextView_totalLen;
        private TextView textView_process;

        HandleMusicMsg(TextView textView, SeekBar seekBar, TextView textView1, TextView textView2) {
            mTextView_musicName = textView;
            mSeekBar_music = seekBar;
            mTextView_totalLen = textView1;
            textView_process = textView2;
        }


        @Override
        public void handleMessage(android.os.Message msg) {
            try {
                if (msg.what == WHAT_PLAY_MUSIC) {
                    Bundle bundle = msg.getData();
                    String path = bundle.getString(cKEY_TXT);
                    startMusic(path);
                    mTextView_musicName.setText(path);
                } else if (msg.what == WHAT_SET_MUSIC_NAME) {
                    Bundle bundle = msg.getData();
                    String param = bundle.getString(cKEY_TXT);

                    mTextView_musicName.setText(param);
                } else if (msg.what == WHAT_SET_MUSIC_PROGRESS) {
                    if (!mIsSeekBarChangingisSeekBarChanging) {
                        Bundle bundle = msg.getData();

                        int param = bundle.getInt(cKEY_TXT);

                        mSeekBar_music.setProgress(param);

                        int time = param / 1000;
                        textView_process.setText(time / 3600 + ":" + time / 60 + ":" + time % 60);
                        //textView_process.setText("00:00:00");

                        //LogToSystem.d(TAG + "HandleMusicMsg", "" + param + "&" + mSeekBar_music.getMax());
                        if (param >= mSeekBar_music.getMax()) {
                            // 推送的进程已经达到最大值的时候
                            Button btn = (Button) mRootView.findViewById(R.id.btn_startMusic);
                            mMusicStatus = EStatusMusic.STOP;
                            btn.setText(R.string.strStart);
                        }
                    }
                } else if (msg.what == WHAT_SET_MUSIC_PROGRESS_MAX) {
                    Bundle bundle = msg.getData();
                    int param = bundle.getInt(cKEY_TXT);

                    mSeekBar_music.setMax(param);
                    param /= 1000;
                    mTextView_totalLen.setText(param / 3600 + ":" + param / 60 + ":" + param % 60);
                    textView_process.setText(R.string.timeZero);
                } else {
                    LogToSystem.w(TAG + "HandleMusicMsg", "Nothing to do with what:" + msg.what);
                }
            } catch (Exception ex) {
                LogToSystem.e(TAG + "HandleMusicMsg", ex.getMessage());
            }
            super.handleMessage(msg);
        }
    }

    private class ProcMusicSeekBar implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //LogToSystem.d(TAG + "ProcMusicSeekBar", "onStartTrackingTouch");
            mIsSeekBarChangingisSeekBarChanging = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            //LogToSystem.d(TAG + "ProcMusicSeekBar", "" + progress);
            // 设置音乐播放器的播放进度
            Intent intent = new Intent(mMainActivity, PlayingMusicServices.class);
            intent.putExtra(PlayingMusicServices.cPARAM_TYPE, PlayingMusicServices.POSITION_MUSIC);
            intent.putExtra(PlayingMusicServices.cPARAM_POSITION, progress);
            mMainActivity.startService(intent);

            TextView textView_process1 = (TextView) mRootView.findViewById(R.id.textView_progress);
            progress /= 1000;
            textView_process1.setText(progress / 3600 + ":" + progress / 60 + ":" + progress % 60);
            //textView_process1.setText();

            mIsSeekBarChangingisSeekBarChanging = false;
        }
    }

    /**
     * 音乐播放器状态的枚举
     */
    public enum EStatusMusic {
        STOP, START, PAUSE, GOON;
    }
}

