package com.group1.playmusic.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Song implements Serializable {
    private String nameSong;
    private String nameSinger;
    private String nameAuthor;
    private String lyric;
    private String imageAvatar;
    private String listenCount;
    private String point;
    private String created_at;
    private String id;
    private int duration;


    public Song(String nameSong, String nameSinger, String nameAuthor, String lyric, String imageAvatar, String listenCount, String point, String created_at, String id, int duration) {
        this.nameSong = nameSong;
        this.nameSinger = nameSinger;
        this.nameAuthor = nameAuthor;
        this.lyric = lyric;
        this.imageAvatar = imageAvatar;
        this.listenCount = listenCount;
        this.point = point;
        this.created_at = created_at;
        this.id = id;
        this.duration = duration;
    }

    public Song(String nameSong, String nameSinger, String imageAvatar, String id, int duration) {
        this.nameSong = nameSong;
        this.nameSinger = nameSinger;
        this.imageAvatar = imageAvatar;
        this.id = id;
        this.duration = duration;
    }

    public Song(String nameSong, String nameSinger, String imageAvatar, int duration) {
        this.nameSong = nameSong;
        this.nameSinger = nameSinger;
        this.imageAvatar = imageAvatar;
        this.duration = duration;
    }

    public Song() {
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getNameSinger() {
        return nameSinger;
    }

    public void setNameSinger(String nameSinger) {
        this.nameSinger = nameSinger;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getImageAvatar() {
        return imageAvatar;
    }

    public void setImageAvatar(String imageAvatar) {
        this.imageAvatar = imageAvatar;
    }

    public String getListenCount() {
        return listenCount;
    }

    public void setListenCount(String listenCount) {
        this.listenCount = listenCount;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getSeconds(long time){
        return (int) time%60;
    }
    public int getMinute(long time){
        return  (int)(time/(60));
    }
    public int getHour(long time){
        return (int)(time/(60*60));
    }
    public String getTime(int a){
        if(a==1){
            return String.format("%02d:%02d",getMinute(duration),getSeconds(duration));
        }else {
            return String.format("%02:%02d:%02d",getHour(duration),getMinute(duration),getSeconds(duration));
        }
    }

}
