package com.group1.playmusic.adapter_list_view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group1.playmusic.MainActivity;
import com.group1.playmusic.R;
import com.group1.playmusic.ToolHelper;
import com.group1.playmusic.nofitication.CreateNotificationControlMusic;
import com.group1.playmusic.object.Song;
import com.group1.playmusic.service.PlayService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterListSong extends RecyclerView.Adapter<AdapterListSong.SongViewHoder> {

    Context context;
    ArrayList<Song> listSong;
    Activity activity;

    public AdapterListSong(Context context, ArrayList<Song> listSong, Activity activity) {
        this.context = context;
        this.listSong = listSong;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SongViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item_list,null);
        return new SongViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongViewHoder holder, final int position) {
        final Song song = listSong.get(position);
        holder.tvNameSong.setText(song.getNameSong());
        holder.tvNameSinger.setText(song.getNameSinger());
        holder.tvTime.setText(song.getTime(1));
        Picasso.get().load(song.getImageAvatar()).into(holder.imgAvatar);
        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.positonPlay = position;
                if(MainActivity.isBound){
                    MainActivity.playService.playNext("http://api.mp3.zing.vn/api/streaming/audio/"+song.getId()+"/320");
                    CreateNotificationControlMusic.createNofitication(context,listSong.get(position),R.drawable.ic_pause_black_24dp,1, ToolHelper.getTime(listSong.get(position).getDuration()));
                }else {
                    Toast.makeText(context, "OKKKKK", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, PlayService.class);
                    intent.putExtra("URL","http://api.mp3.zing.vn/api/streaming/audio/"+song.getId()+"/320");
                    intent.putExtra("list",listSong);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        activity.bindService(intent,MainActivity.mServiceConnection,Context.BIND_AUTO_CREATE);
                    }else {
                        context.startService(intent);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }

    public class SongViewHoder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar, imgMenu;
        private TextView tvNameSong, tvNameSinger,tvTime;
        private View view;
        public SongViewHoder(@NonNull View itemView) {
            super(itemView);
            this.imgAvatar = itemView.findViewById(R.id.imgAvatar);
            this.imgMenu = itemView.findViewById(R.id.imgMenu);
            this.tvTime = itemView.findViewById(R.id.tvTime);
            this.tvNameSinger = itemView.findViewById(R.id.tvNameSinger);
            this.tvNameSong = itemView.findViewById(R.id.tvNameSong);
            this.view = itemView;
        }
    }


}
