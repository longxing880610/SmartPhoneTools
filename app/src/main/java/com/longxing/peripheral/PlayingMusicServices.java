package com.longxing.peripheral;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.longxing.ui.UI_TabMusic;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Zhang Long on 2017/7/22.
 * <p>
 * music class
 */
public class PlayingMusicServices extends Service {

    /**
     * 规定开始音乐、暂停音乐、结束音乐的标志
     */
    public static final int PLAT_MUSIC = 1;
    public static final int PAUSE_MUSIC = PLAT_MUSIC + 1;
    public static final int STOP_MUSIC = PAUSE_MUSIC + 1;
    public static final int GOON_MUSIC = STOP_MUSIC + 1;
    public static final int POSITION_MUSIC = GOON_MUSIC + 1;

    public static final String cPARAM_TYPE = "type";
    public static final String cPARAM_FILEPATH = "path";
    public static final String cPARAM_FILEPATH_INT = "pathInt";
    public static final String cPARAM_POSITION = "position";

    //用于播放音乐等媒体资源
    private MediaPlayer mediaPlayer;
    //标志判断播放歌曲是否是停止之后重新播放，还是继续播放
    private boolean isStop = true;
    private boolean mIsLoop = true;
    private Timer timer;

    /**
     * onBind，返回一个IBinder，可以与Activity交互
     * 这是Bind Service的生命周期方法
     *
     * @param intent intent
     * @return ibind
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //在此方法中服务被创建
    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();

            //为播放器添加播放完成时的监听器
            mediaPlayer.setOnCompletionListener(new CompletionListener());
        }
    }


    /**
     * 在此方法中，可以执行相关逻辑，如耗时操作
     *
     * @param intent  :由Activity传递给service的信息，存在intent中
     * @param flags   ：规定的额外信息
     * @param startId ：开启服务时，如果有规定id，则传入startid
     * @return 返回值规定此startservice是哪种类型，粘性的还是非粘性的
     * START_STICKY:粘性的，遇到异常停止后重新启动，并且intent=null
     * START_NOT_STICKY:非粘性，遇到异常停止不会重启
     * START_REDELIVER_INTENT:粘性的，重新启动，并且将Context传递的信息intent传递
     * 此方法是唯一的可以执行很多次的方法
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final UI_TabMusic tabMusic = UI_TabMusic.getInstance();
        switch (intent.getIntExtra(cPARAM_TYPE, -1)) {
            case PLAT_MUSIC:
                //重置mediaPlayer
                mediaPlayer.reset();
                //将需要播放的资源与之绑定
                int pathInt = intent.getIntExtra(cPARAM_FILEPATH_INT, -1);
                if (pathInt != -1) { // 使用系统资源,打包在APK里面
                    mediaPlayer = MediaPlayer.create(this, pathInt);
                } else {
                    String path = intent.getStringExtra(cPARAM_FILEPATH);
                    mediaPlayer = MediaPlayer.create(this, Uri.parse(path));
                }
                //开始播放
                mediaPlayer.start();
                //是否循环播放
                mediaPlayer.setLooping(mIsLoop);
                isStop = false;

                // 设置进度条
                if (timer != null) {
                    timer.cancel();
                }
                timer = new Timer();
                tabMusic.sendMessage(UI_TabMusic.WHAT_SET_MUSIC_PROGRESS_MAX, mediaPlayer.getDuration());
                //监听播放时回调函数
                timer.schedule(new ShowProgress(), 0, 1000);
                break;
            case GOON_MUSIC:
                //播放器不为空，并且正在播放
                if (!isStop && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;
            case POSITION_MUSIC://播放器不为空，并且正在播放
                if (!isStop && mediaPlayer != null) {
                    int position = intent.getIntExtra(cPARAM_POSITION, 0);
                    mediaPlayer.seekTo(position);
                }
                break;
            case PAUSE_MUSIC:
                //播放器不为空，并且正在播放
                if (!isStop && mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            case STOP_MUSIC:
                if (mediaPlayer != null) {
                    //停止之后要开始播放音乐
                    mediaPlayer.stop();
                    isStop = true;
                    timer.cancel();
                    timer = null;
                }
                break;
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class CompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            //发送广播到MainActivity
            Intent intent = new Intent();
            intent.setAction("com.complete");
            sendBroadcast(intent);
        }
    }

    /**
     * check if the music play to end
     *
     * @param mediaPlayer media player object
     * @return if the music play to end
     */
    private boolean CheckIsMusicOver(MediaPlayer mediaPlayer) {
        int pos = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();
        return !mIsLoop && total - pos < 200;
    }

    /**
     * show progress when play music
     */
    private class ShowProgress extends TimerTask {
        @Override
        public void run() {
            final UI_TabMusic tabMusic = UI_TabMusic.getInstance();
            if (mediaPlayer.isPlaying()) {
                if (!CheckIsMusicOver(mediaPlayer)) {
                    int pos = mediaPlayer.getCurrentPosition();
                    tabMusic.sendMessage(UI_TabMusic.WHAT_SET_MUSIC_PROGRESS, pos);
                }
            } else {
                // 停止定时器任务程序
                if (CheckIsMusicOver(mediaPlayer)) {    // 允许有点偏差
                    int total = mediaPlayer.getDuration();
                    tabMusic.sendMessage(UI_TabMusic.WHAT_SET_MUSIC_PROGRESS, total);
                    this.cancel();
                }
            }
        }
    }
}