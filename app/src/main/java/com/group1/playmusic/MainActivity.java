package com.group1.playmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.group1.playmusic.adapter_list_view.AdapterListSong;
import com.group1.playmusic.fragment.FragmentListSong;
import com.group1.playmusic.inteface.IPlayAction;
import com.group1.playmusic.nofitication.CreateNotificationControlMusic;
import com.group1.playmusic.object.Song;
import com.group1.playmusic.service.PlayService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IPlayAction {

    public MediaPlayer mediaPlayer;
    FragmentManager fragmentManager;
    FragmentListSong fragmentListSong;
    NotificationManager notificationManager;
    public static PlayService playService;
    public static boolean isBound = false;
    public static ServiceConnection mServiceConnection ;
    public static int positonPlay = 0;
    public static ArrayList<Song> song;
    BroadcastReceiver broadcastReceiver ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void createChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CreateNotificationControlMusic.CHANNEL_ID,"ok",NotificationManager.IMPORTANCE_LOW);
            notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null){
                notificationManager.createNotificationChannel(notificationChannel);
            }


            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d("TAG", "onReceive: Error");
                    switch (intent.getStringExtra("actionname")){
                        case CreateNotificationControlMusic.ACTION_NEXT:
                            next();
                            break;
                        case CreateNotificationControlMusic.ACTION_PLAY:
                            play();
                            break;
                        case CreateNotificationControlMusic.ACTION_PRE:
                            pre();
                            break;
                    }
                }
            };
        }

    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        fragmentListSong = new FragmentListSong(MainActivity.this,MainActivity.this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            registerReceiver(broadcastReceiver,new IntentFilter("TRACK"));
        }
        mediaPlayer = new MediaPlayer();
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                PlayService.MyBinder myBinder =(PlayService.MyBinder) service;
                MainActivity.playService = myBinder.getService();
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("TAG", "onServiceDisconnected: ");
                isBound = false;
            }
        };
        translationFragment(fragmentListSong);


    }
    public void translationFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frData,fragment);
        fragmentTransaction.addToBackStack(fragment.getTag());
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void play() {
        if(!playService.mediaPlayer.isPlaying())
            CreateNotificationControlMusic.createNofitication(getApplicationContext(),MainActivity.song.get(MainActivity.positonPlay), R.drawable.ic_pause_black_24dp,1, ToolHelper.getTime(MainActivity.song.get(MainActivity.positonPlay).getDuration()));
        else
            CreateNotificationControlMusic.createNofitication(this,song.get(positonPlay),0,1,ToolHelper.getTime(MainActivity.song.get(MainActivity.positonPlay).getDuration()));
        playService.play();
    }

    @Override
    public void next() {
        positonPlay++;
        CreateNotificationControlMusic.createNofitication(getApplicationContext(),MainActivity.song.get(MainActivity.positonPlay), R.drawable.ic_pause_black_24dp,1, ToolHelper.getTime(MainActivity.song.get(MainActivity.positonPlay).getDuration()));
        playService.playNext("http://api.mp3.zing.vn/api/streaming/audio/"+song.get(positonPlay).getId()+"/320");
    }

    @Override
    public void pre() {
        positonPlay--;
        CreateNotificationControlMusic.createNofitication(getApplicationContext(),MainActivity.song.get(MainActivity.positonPlay), R.drawable.ic_pause_black_24dp,1, ToolHelper.getTime(MainActivity.song.get(MainActivity.positonPlay).getDuration()));
        playService.playNext("http://api.mp3.zing.vn/api/streaming/audio/"+song.get(positonPlay).getId()+"/320");
    }
}
