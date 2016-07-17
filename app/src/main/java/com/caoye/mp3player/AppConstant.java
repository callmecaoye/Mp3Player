package com.caoye.mp3player;

import android.os.Environment;

import java.io.File;

/**
 * Created by admin on 7/15/16.
 */
public interface AppConstant {
    class PlayerMsg {
        public static final int PLAY_MSG = 1;
        public static final int PAUSE_MSG = 0;
        public static final int STOP_MSG = -1;
    }

    class URL {
        public static final String BASE_URL = "http://10.0.3.2:8080/mp3/";
    }

    static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().
            getAbsolutePath() + File.separator;
    static final String MP3_FOLDER = "Music" + File.separator;
    static final String LYRICS_FOLDER = "Lyrics" + File.separator;
    static final String LRC_MESSAGE_ACTION = "com.caoye.mp3player.lrcmessage.action";
}
