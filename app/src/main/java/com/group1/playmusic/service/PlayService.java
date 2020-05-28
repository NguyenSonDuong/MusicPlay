package com.group1.playmusic.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import androidx.annotation.Nullable;
import com.group1.playmusic.MainActivity;
import com.group1.playmusic.R;
import com.group1.playmusic.ToolHelper;
import com.group1.playmusic.nofitication.CreateNotificationControlMusic;

import java.io.IOException;

public class PlayService extends Service  {
    public String TAG = "TAG";
    public MediaPlayer mediaPlayer;
    public  IBinder iMyBinder;
    public Handler handler;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: Bind");
        String url = intent.getStringExtra("URL");
        mediaPlayer = new MediaPlayer();
        handler = new Handler();
        CreateNotificationControlMusic.createNofitication(getApplicationContext(),MainActivity.song.get(MainActivity.positonPlay), R.drawable.ic_pause_black_24dp,1, ToolHelper.getTime(MainActivity.song.get(MainActivity.positonPlay).getDuration()));
        playNext(url);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion: Complete");
                MainActivity.positonPlay++;
                CreateNotificationControlMusic.createNofitication(getApplicationContext(),MainActivity.song.get(MainActivity.positonPlay), R.drawable.ic_pause_black_24dp,1, ToolHelper.getTime(MainActivity.song.get(MainActivity.positonPlay).getDuration()));
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource("http://api.mp3.zing.vn/api/streaming/audio/"+MainActivity.song.get(MainActivity.positonPlay)+"/320");
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        startForeground(1, CreateNotificationControlMusic.notification);
        return iMyBinder;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        iMyBinder = new MyBinder();
    }

    public void playNext(String url){
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.reset();
        play(url);

        mediaPlayer.start();
    }

    public void loopHIHI(final int time){
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                CreateNotificationControlMusic.createNofitication(getApplicationContext(),MainActivity.song.get(MainActivity.positonPlay), R.drawable.ic_pause_black_24dp,1, ToolHelper.getTime(MainActivity.song.get(MainActivity.positonPlay).getDuration()-time));
                loopHIHI(time+1);
            }
        },1000);
    }
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    public void play() {
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }


    public class MyBinder extends Binder {

        public PlayService getService() {
            return PlayService.this;
        }
    }

    public void play(String url){

        try{
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        }catch (Exception ex){
            Log.d("TAG", "play: "+ ex.getMessage());
        }
    }
}
