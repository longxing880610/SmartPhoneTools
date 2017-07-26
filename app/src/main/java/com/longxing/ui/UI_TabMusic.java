package com.longxing.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.longxing.R;
import com.longxing.peripheral.MyBroadCastReceiver;
import com.longxing.peripheral.PlayingMusicServices;

/**
 * 这是一个Service生命周期及开启服务的小例子
 * 实现播放音乐功能
 */
public class UI_TabMusic implements IUI_TabMain {


    private MyBroadCastReceiver receiver;

    private static UI_TabMusic sUiTabMusic;

    private MainActivity mMainActivity;
    private UI_TabLog mUI_tabLog;

    /**
     * @return owner object
     */
    static UI_TabMusic getInstance() {
        if (sUiTabMusic == null) {
            sUiTabMusic = new UI_TabMusic();
            sUiTabMusic.mMainActivity = MainActivity.GetInstance();
            sUiTabMusic.mUI_tabLog = UI_TabLog.getInstance();
        }
        return sUiTabMusic;
    }

    public
    @Override
    void initUI(View rootView) {
        receiver = new MyBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.complete");
        mMainActivity.registerReceiver(receiver, filter);

        Button btn = (Button) rootView.findViewById(R.id.btn_startmusic);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick1(v);
            }
        });
        btn = (Button) rootView.findViewById(R.id.btn_pausemusic);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick1(v);
            }
        });

        btn = (Button) rootView.findViewById(R.id.btn_stopmusic);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick1(v);
            }
        });

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

    public void onClick1(View view) {
        switch (view.getId()) {
            //开始音乐
            case R.id.btn_startmusic:
                //启动服务，播放音乐
                Intent intent = new Intent(mMainActivity, PlayingMusicServices.class);
                intent.putExtra(PlayingMusicServices.cPARAM_TYPE, PlayingMusicServices.PLAT_MUSIC);
                intent.putExtra(PlayingMusicServices.cPARAM_FILEPATH_INT, R.raw.dingdang_shouzhangxin);
                mMainActivity.startService(intent);
                //playingmusic(PLAT_MUSIC);
                break;
            //暂停
            case R.id.btn_pausemusic:
                playingmusic(PlayingMusicServices.PAUSE_MUSIC);
                break;
            //停止
            case R.id.btn_stopmusic:
                playingmusic(PlayingMusicServices.STOP_MUSIC);
                break;
        }
    }

    private void playingmusic(int type) {
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
}
