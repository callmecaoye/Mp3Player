package com.caoye.mp3player;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {
    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        TabHost.TabSpec remoteSpec = tabHost.newTabSpec("Remote");
        remoteSpec.setIndicator("Remote");
        remoteSpec.setContent(new Intent(this, RemoteMp3ListFragment.class));
        tabHost.addTab(remoteSpec, RemoteMp3ListFragment.class,null);

        TabHost.TabSpec localSpec = tabHost.newTabSpec("Local");
        localSpec.setIndicator("Local");
        localSpec.setContent(new Intent(this, LocalMp3ListFragment.class));

        tabHost.addTab(localSpec, LocalMp3ListFragment.class,null);
    }
}
