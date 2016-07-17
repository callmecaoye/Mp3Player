package com.caoye.mp3player.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.caoye.lrc.LrcProcessor;
import com.caoye.model.Mp3Info;
import com.caoye.mp3player.AppConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by admin on 7/15/16.
 */
public class PlayerService extends Service
{
    MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;
    private boolean isPause = false;
    private boolean isReleased = false;

    private ArrayList<Queue> list = new ArrayList<>();

    private Handler handler = new Handler();
    private UpdateTimeCallback updateTimeCallback = null;
    private long start = 0;
    private long nextTimeMill = 0;
    private long currentTimeMill = 0;
    private long pauseTimeMill = 0;
    private String message = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Mp3Info info = (Mp3Info) intent.getSerializableExtra("mp3Info");
        int MSG = intent.getIntExtra("MSG", 0);
        if (info != null) {
            if (MSG == AppConstant.PlayerMsg.PLAY_MSG) {
                play(info);
            }
        } else {
            if (MSG == AppConstant.PlayerMsg.PAUSE_MSG) {
                pause();
            } else if (MSG == AppConstant.PlayerMsg.STOP_MSG) {
                stop();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void play(Mp3Info info) {
        stop();

        String path = AppConstant.SDCARD_ROOT + AppConstant.MP3_FOLDER + info.getMp3Name();

        mediaPlayer = MediaPlayer.create(this, Uri.parse("file://" + path));
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

        prepareLrc(info.getLrcName());
        handler.postDelayed(updateTimeCallback, 5);
        start = System.currentTimeMillis();

        isPlaying = true;
        isReleased = false;
    }

    private void pause() {
        if (mediaPlayer != null) {
            if (!isReleased) {
                if (isPlaying) { // pause current playing
                    mediaPlayer.pause();

                    handler.removeCallbacks(updateTimeCallback);
                    pauseTimeMill = System.currentTimeMillis();

                    isPause = true;
                } else { // restore to play, recalculate the start timeMill
                    mediaPlayer.start();

                    handler.postDelayed(updateTimeCallback, 5);
                    start = System.currentTimeMillis() - pauseTimeMill + start;

                    isPause = false;
                }
                isPlaying = isPlaying ? false : true;
            }
        }
    }

    private void stop() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                if (!isReleased) {
                    mediaPlayer.stop();
                    mediaPlayer.release();

                    handler.removeCallbacks(updateTimeCallback);

                    isReleased = true;
                }
                isPlaying = false;
            }
        }
    }

    /**
     * Process data from lrc file
     * @param lrcName
     */
    private void prepareLrc(String lrcName) {
        try {
            FileInputStream input = new FileInputStream(AppConstant.SDCARD_ROOT +
                    "Lyrics" + File.separator+ lrcName);
            LrcProcessor processor = new LrcProcessor();
            list = processor.process(input);

            updateTimeCallback = new UpdateTimeCallback(list);
            start = 0;
            currentTimeMill = 0;
            nextTimeMill = 0;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    class UpdateTimeCallback implements Runnable {
        Queue times = null;
        Queue messages = null;
        public UpdateTimeCallback(ArrayList<Queue> list) {
            times = list.get(0);
            messages = list.get(1);
        }

        @Override
        public void run() {
            // start playing mp3 till now, in ms
            long offset = System.currentTimeMillis() - start;

            if (currentTimeMill == 0) {
                nextTimeMill = (Long) times.poll();
                message = (String) messages.poll();
            }

            if (offset >= nextTimeMill) {
                Intent intent = new Intent();
                intent.setAction(AppConstant.LRC_MESSAGE_ACTION);
                intent.putExtra("lrcMessage", message);
                sendBroadcast(intent);
                nextTimeMill = (Long) times.poll();
                message = (String) messages.poll();
            }

            currentTimeMill = currentTimeMill + 10;
            //execute updateTimeCallback each 10ms
            handler.postDelayed(updateTimeCallback, 10);
        }
    }
}
