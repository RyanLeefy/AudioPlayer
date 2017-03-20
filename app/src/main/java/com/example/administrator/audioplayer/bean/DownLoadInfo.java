package com.example.administrator.audioplayer.bean;

import java.io.Serializable;

/**
 * Created by on 2017/3/1 0001.
 */

public class DownLoadInfo implements Serializable {
    public static final String KEY_DOWNLOAD_ID  = "downloadId";
    public static final String KEY_TOTALSIZE = "totalSize";
    public static final String KEY_COMPLETEDSIZE = "completedSize";
    public static final String KEY_URL = "url";
    public static final String KEY_SAVEDIRPATH = "saveDirPath";
    public static final String KEY_FILENAME = "fileName";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_DOWNLOADSTATUS = "downloadStatus";


    private String downloadId;
    private Long totalSize;
    private Long completedSize;
    private String url;
    private String saveDirPath;
    private String fileName;
    private String artist;
    private int downloadStatus;

    public DownLoadInfo() {
    }

    public DownLoadInfo(String downloadId, Long totalSize, Long completedSize,
                        String url, String saveDirPath, String fileName, String artist, int downloadStatus) {
        this.downloadId = downloadId;
        this.totalSize = totalSize;
        this.completedSize = completedSize;
        this.url = url;
        this.saveDirPath = saveDirPath;
        this.fileName = fileName;
        this.artist = artist;
        this.downloadStatus = downloadStatus;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(Long completedSize) {
        this.completedSize = completedSize;
    }

    public String getSaveDirPath() {
        return saveDirPath;
    }

    public void setSaveDirPath(String saveDirPath) {
        this.saveDirPath = saveDirPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

}
