package com.group1.playmusic.nofitication;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.group1.playmusic.BroadcastRecerverCustom;
import com.group1.playmusic.MainActivity;
import com.group1.playmusic.R;
import com.group1.playmusic.ToolHelper;
import com.group1.playmusic.object.Song;
import com.group1.playmusic.service.PlayService;

import java.util.concurrent.ExecutionException;

public class CreateNotificationControlMusic {
    public static final String CHANNEL_ID = "CHANNEL_ID";
    public static final String ACTION_PRE = "CHANNEL_PRE";
    public static final String ACTION_PLAY = "CHANNEL_PLAY";
    public static final String ACTION_NEXT = "CHANNEL_NEXT";

    public static Notification notification;

    public static void createNofitication(final Context context, final Song song, int playButton, int pos, String sub){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            //Intent notificationIntent = new Intent(context, MainActivity.class);
            final MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context,"tag");
            final PendingIntent pendingIntentNext ;
            final int drw_next;
            if(pos == 0){
                pendingIntentNext = null;
                drw_next = 0;
            }else {
                Intent intentNext = new Intent(context, BroadcastRecerverCustom.class).setAction(ACTION_NEXT);
                pendingIntentNext = PendingIntent.getBroadcast(context,0,intentNext,PendingIntent.FLAG_UPDATE_CURRENT);
                drw_next = R.drawable.ic_skip_next_black_24dp;
            }

            final PendingIntent pendingIntentPlay ;
            int drw_Play;
            if(pos == 0){
                pendingIntentPlay = null;
                drw_Play = 0;
            }else {
                Intent intentPlay = new Intent(context, BroadcastRecerverCustom.class).setAction(ACTION_PLAY);
                pendingIntentPlay = PendingIntent.getBroadcast(context,0,intentPlay,PendingIntent.FLAG_UPDATE_CURRENT);
                drw_Play = R.drawable.ic_play_arrow_black_24dp;
            }
            if(playButton != 0){
                drw_Play = playButton;
            }

            final PendingIntent pendingIntentPre;
            final int drw_Pre;
            if(pos == 0){
                pendingIntentPre = null;
                drw_Pre = 0;
            }else {
                Intent intentPre = new Intent(context, BroadcastRecerverCustom.class).setAction(ACTION_PRE);
                pendingIntentPre = PendingIntent.getBroadcast(context,0,intentPre,PendingIntent.FLAG_UPDATE_CURRENT);
                drw_Pre = R.drawable.ic_skip_previous_black_24dp;
            }

            final int finalDrw_Play = drw_Play;
            try {
                notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                        .setContentText(song.getNameSong())
                        .setContentTitle(song.getNameSinger())
                        .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                        .setSubText(sub)
                        .setLargeIcon(new ToolHelper.BitmapFromUrl(context).execute(song.getImageAvatar()).get())
                        .addAction(drw_Pre,"pre",pendingIntentPre)
                        .addAction(finalDrw_Play,"play",pendingIntentPlay)
                        .addAction(drw_next,"next",pendingIntentNext)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setMediaSession(mediaSessionCompat.getSessionToken())
                                .setShowActionsInCompactView(0,1,2))
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .build();
            } catch (Exception e) {
                notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                        .setContentText(song.getNameSong())
                        .setContentTitle(song.getNameSinger())
                        .setSubText(sub)
                        .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                        .addAction(drw_Pre,"pre",pendingIntentPre)
                        .addAction(finalDrw_Play,"play",pendingIntentPlay)
                        .addAction(drw_next,"next",pendingIntentNext)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setMediaSession(mediaSessionCompat.getSessionToken())
                                .setShowActionsInCompactView(0,1,2))
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .build();
            }
            notificationManagerCompat.notify(1,notification);


        }

    }



}
