package com.group1.playmusic.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group1.playmusic.ConnectRequest;
import com.group1.playmusic.MainActivity;
import com.group1.playmusic.R;
import com.group1.playmusic.adapter_list_view.AdapterListSong;
import com.group1.playmusic.object.Song;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentListSong extends Fragment {

    Context context;
    RecyclerView rcyDS;
    public AdapterListSong adapterListSong;
    public ArrayList<Song> listSong ;

    public Activity activity;
    public FragmentListSong() {
    }

    public FragmentListSong(Context context,Activity activity) {
        this.context = context;
        this.activity =activity;
    }

    public FragmentListSong(Context context, AdapterListSong adapterListSong, ArrayList<Song> listSong) {
        this.context = context;
        this.adapterListSong = adapterListSong;
        this.listSong = listSong;
    }

    public FragmentListSong(int contentLayoutId, Context context, AdapterListSong adapterListSong, ArrayList<Song> listSong) {
        super(contentLayoutId);
        this.context = context;
        this.adapterListSong = adapterListSong;
        this.listSong = listSong;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_list_music,null);
        Log.d("TAG", "onCreateView: okkktesst");
        init(view);
        return view;
    }
    public View findIDFromView(View view,int id){
        return view.findViewById(id);
    }
    private void init(View view){
        rcyDS = (RecyclerView) findIDFromView(view,R.id.rcyDS);
        listSong = new ArrayList<>();
        getListZingChart();
        MainActivity.song = listSong;
        adapterListSong = new AdapterListSong(context,listSong,activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcyDS.setAdapter(adapterListSong);
        rcyDS.setLayoutManager(linearLayoutManager);
    }

    public void getListZingChart(){
        String sig =  "57d8e2f292d655d12bc6313fad731086bde689c985673ace39d86ac97d31784b6b85828f66823ba6ded940c94d48f8912878f905bebd51e08c787ed92a8b5791";
        String api_key = "38e8643fb0dc04e8d65b99994d3dafff";
        String ctime = "1590232816";
        int count = 100;
        String url = "https://zingmp3.vn/api/chart-realtime/get-detail?type=song&count="+count+"&ctime="+ctime+"&sig="+sig+"&api_key="+api_key;

        try{
            String data = ConnectRequest.Get(context,url);
            JSONObject jsonRoot = new JSONObject(data);
            JSONObject jsonData = jsonRoot.getJSONObject("data");
            JSONArray jsonItem = jsonData.getJSONArray("items");
            for(int i=0;i<jsonItem.length();i++){
                JSONObject jsonSong = jsonItem.getJSONObject(i);
                Song song = new Song();
                song.setId(jsonSong.getString("id"));
                song.setNameSong(jsonSong.getString("title"));
                song.setNameSinger(jsonSong.getString("artists_names"));
                song.setDuration(jsonSong.getInt("duration"));
                song.setCreated_at(jsonSong.getLong("created_at")+"");
                song.setListenCount(jsonSong.getInt("listen")+"");
                song.setPoint(jsonSong.getInt("score")+"");
                song.setImageAvatar(jsonSong.getString("thumbnail_medium"));
                listSong.add(song);
            }
        }catch (Exception e){
            Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}

