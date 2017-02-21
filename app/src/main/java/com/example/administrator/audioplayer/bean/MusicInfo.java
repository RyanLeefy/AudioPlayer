package com.example.administrator.audioplayer.bean;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by on 2017/2/3.
 */

public class MusicInfo implements Parcelable {

    public static final String KEY_AUDIO_ID = "audioid";
    public static final String KEY_ALBUM_ID = "albumid";
    public static final String KEY_ALBUM_NAME = "albumname";
    public static final String KEY_ALBUM_DATA = "albumdata";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_MUSIC_NAME = "musicname";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_ARTIST_ID = "artist_id";
    public static final String KEY_DATA = "data";
    public static final String KEY_FOLDER = "folder";
    public static final String KEY_SIZE = "size";
    public static final String KEY_FAVORITE = "favorite";
    public static final String KEY_LRC = "lrc";
    public static final String KEY_ISLOCAL = "islocal";
    public static final String KEY_SORT = "sort";


    /**
     * 数据库中的_id
     */
    private int audioId = -1;
    private int albumId = -1;
    private String albumName;
    private String albumData;
    private int duration;
    private String musicName;
    private String artist;
    private long artistId;
    private String data;
    private String folder;
    private String lrc;
    private boolean islocal;
    private String sort;

    private int size;
    /**
     * 0表示没有收藏 1表示收藏
     */
    private int favorite = 0;
    public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>() {

        @Override
        public MusicInfo createFromParcel(Parcel source) {
            MusicInfo music = new MusicInfo();
            Bundle bundle = source.readBundle();
            music.audioId = bundle.getInt(KEY_AUDIO_ID);
            music.albumId = bundle.getInt(KEY_ALBUM_ID);
            music.albumName = bundle.getString(KEY_ALBUM_NAME);
            music.duration = bundle.getInt(KEY_DURATION);
            music.musicName = bundle.getString(KEY_MUSIC_NAME);
            music.artist = bundle.getString(KEY_ARTIST);
            music.artistId = bundle.getLong(KEY_ARTIST_ID);
            music.data = bundle.getString(KEY_DATA);
            music.folder = bundle.getString(KEY_FOLDER);
            music.albumData = bundle.getString(KEY_ALBUM_DATA);
            music.size = bundle.getInt(KEY_SIZE);
            music.lrc = bundle.getString(KEY_LRC);
            music.islocal = bundle.getBoolean(KEY_ISLOCAL);
            music.sort = bundle.getString(KEY_SORT);
            return music;
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_AUDIO_ID, audioId);
        bundle.putInt(KEY_ALBUM_ID, albumId);
        bundle.putString(KEY_ALBUM_NAME, albumName);
        bundle.putString(KEY_ALBUM_DATA, albumData);
        bundle.putInt(KEY_DURATION, duration);
        bundle.putString(KEY_MUSIC_NAME, musicName);
        bundle.putString(KEY_ARTIST, artist);
        bundle.putLong(KEY_ARTIST_ID, artistId);
        bundle.putString(KEY_DATA, data);
        bundle.putString(KEY_FOLDER, folder);
        bundle.putInt(KEY_SIZE, size);
        bundle.putString(KEY_LRC, lrc);
        bundle.putBoolean(KEY_ISLOCAL, islocal);
        bundle.putString(KEY_SORT, sort);
        dest.writeBundle(bundle);
    }


    public MusicInfo() {

    }

    public MusicInfo(MusicInfo musicInfo) {
        this.audioId = musicInfo.getAudioId();
        this.albumId = musicInfo.getAlbumId();
        this.albumName = musicInfo.getAlbumName();
        this.albumData = musicInfo.getAlbumData();
        this.duration = musicInfo.getDuration();
        this.musicName = musicInfo.getMusicName();
        this.artist = musicInfo.getArtist();
        this.artistId = musicInfo.getArtistId();
        this.data = musicInfo.getData();
        this.folder = musicInfo.getFolder();
        this.lrc = musicInfo.getLrc();
        this.islocal = musicInfo.islocal();
        this.sort = musicInfo.getSort();
        this.size = musicInfo.getSize();
        this.favorite = musicInfo.getFavorite();
    }


    public int getAudioId() {
        return audioId;
    }

    public void setAudioId(int audioid) {
        this.audioId = audioid;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumData() {
        return albumData;
    }

    public void setAlbumData(String albumData) {
        this.albumData = albumData;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public boolean islocal() {
        return islocal;
    }

    public void setIslocal(boolean islocal) {
        this.islocal = islocal;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
