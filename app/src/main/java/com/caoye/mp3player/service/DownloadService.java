 package com.caoye.mp3player.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.caoye.download.HttpDownloader;
import com.caoye.model.Mp3Info;
import com.caoye.mp3player.AppConstant;

 public class DownloadService extends Service {
    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Mp3Info info = (Mp3Info) intent.getSerializableExtra("mp3Info");
        DownloadThread downloadThread = new DownloadThread(info);
        Thread thread = new Thread(downloadThread);
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

     class DownloadThread implements Runnable{
         private Mp3Info info = null;
         public DownloadThread(Mp3Info info) {
             this.info = info;
         }
         @Override
         public void run() {
             HttpDownloader downloader = new HttpDownloader();
             String mp3Url = AppConstant.URL.BASE_URL + info.getMp3Name();
             int result = downloader.downFile(mp3Url, AppConstant.MP3_FOLDER, info.getMp3Name());
             String resultMessage = null;
             if (result == -1) resultMessage = "MP3下载失败";
             else if (result == 0) resultMessage = "MP3文件已存在";
             else if (result == 1) resultMessage = "MP3文件下载成功";

             String lrcUrl = AppConstant.URL.BASE_URL + info.getLrcName();
             result = downloader.downFile(lrcUrl, AppConstant.LYRICS_FOLDER, info.getLrcName());
             resultMessage = null;
             if (result == -1) resultMessage = "LRC下载失败";
             else if (result == 0) resultMessage = "LRC文件已存在";
             else if (result == 1) resultMessage = "LRC文件下载成功";
         }
     }
}
