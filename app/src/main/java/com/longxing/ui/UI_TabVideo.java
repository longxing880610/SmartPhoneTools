package com.longxing.ui;

import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;

import com.longxing.R;
import com.longxing.peripheral.Player;

public class UI_TabVideo implements IUI_TabMain {
    private static final String TAG = "MyLog/UI_TabVideo/";

    private SurfaceView surfaceView;
    private Button btnPause, btnPlayUrl, btnStop;
    private SeekBar skbProgress;
    private Player player;
    private UI_TabLog mUI_tabLog;
    private MainActivity mMainActivity;

    private static UI_TabVideo sUiTabMusic;

    /**
     * @return owner object
     */
    public static UI_TabVideo getInstance() {
        if (sUiTabMusic == null) {
            sUiTabMusic = new UI_TabVideo();
            sUiTabMusic.mMainActivity = MainActivity.getInstance();
            sUiTabMusic.mUI_tabLog = UI_TabLog.getInstance();
        }
        return sUiTabMusic;
    }

    @Override
    public void initUI(View rootView) {

        //mIsInited = true;
        //View rootView = mMainActivity.GetLayerById(R.layout.tab_log);

        surfaceView = (SurfaceView) rootView.findViewById(R.id.surfaceView1);

        btnPlayUrl = (Button) rootView.findViewById(R.id.btnPlayUrl);
        btnPlayUrl.setOnClickListener(new ClickEvent());

        btnPause = (Button) rootView.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new ClickEvent());

        btnStop = (Button) rootView.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new ClickEvent());

        skbProgress = (SeekBar) rootView.findViewById(R.id.skbProgress);
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        player = new Player(surfaceView, skbProgress);

        displayLog("日志面板初始化完成");
    }

    @Override
    public boolean processKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void processDestroy() {
        //player.surfaceDestroyed(null);
    }

    /**
     * @param msg show message
     */
    private void displayLog(String msg) {
        if (mUI_tabLog != null) {
            mUI_tabLog.displayLog(msg);
        }
    }

    public void playUrl(String url){
        //String url = "/storage/emulated/0/DCIM/Camera/VID_20170715_200010.mp4";
        player.playUrl(url);
    }


    class ClickEvent implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            if (arg0 == btnPause) {
                player.pause();
            } else if (arg0 == btnPlayUrl) {
                String url = "/storage/emulated/0/DCIM/Camera/VID_20170715_200010.mp4";
                player.playUrl(url);
            } else if (arg0 == btnStop) {
                player.stop();
            }
        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            player.mediaPlayer.seekTo(progress);
        }


    }
}