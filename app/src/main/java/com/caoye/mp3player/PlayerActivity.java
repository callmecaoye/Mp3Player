package com.caoye.mp3player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.caoye.model.Mp3Info;
import com.caoye.mp3player.service.PlayerService;

public class PlayerActivity extends AppCompatActivity {
    ImageButton playButton;
    ImageButton pauseButton;
    ImageButton stopButton;
    private TextView lrcTextView = null;

    Mp3Info info = null;

    private IntentFilter filter = null;
    private BroadcastReceiver receiver = null;

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new LrcMessageBroadcastReceiver();
        registerReceiver(receiver, getIntentFilter());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        playButton = (ImageButton) findViewById(R.id.play);
        pauseButton = (ImageButton) findViewById(R.id.pause);
        stopButton = (ImageButton) findViewById(R.id.stop);
        playButton.setOnClickListener(new PlayButtonListener());
        pauseButton.setOnClickListener(new PauseButtonListener());
        stopButton.setOnClickListener(new StopButtonListener());
        lrcTextView = (TextView) findViewById(R.id.lrcTextView);

        Intent intent = getIntent();
        info = (Mp3Info) intent.getSerializableExtra("mp3Info");
    }

    class PlayButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(PlayerActivity.this, PlayerService.class);
            intent.putExtra("mp3Info", info);
            intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
            startService(intent);
        }
    }

    class PauseButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(PlayerActivity.this, PlayerService.class);
            intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
            startService(intent);
        }
    }

    class StopButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(PlayerActivity.this, PlayerService.class);
            intent.putExtra("MSG", AppConstant.PlayerMsg.STOP_MSG);
            startService(intent);
        }
    }

    private IntentFilter getIntentFilter() {
        if (filter == null) {
            filter = new IntentFilter();
            filter.addAction(AppConstant.LRC_MESSAGE_ACTION);
        }
        return filter;
    }

    class LrcMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String lrcMessage = intent.getStringExtra("lrcMessage");
            lrcTextView.setText(lrcMessage);
        }
    }
}
