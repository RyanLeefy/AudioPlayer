package com.example.administrator.audioplayer.download;

/**
 * Created by on 2017/3/1 0001.
 */

public class DownloadStatus {
    public static final int DOWNLOAD_STATUS_INIT = -1;
    public static final int DOWNLOAD_STATUS_PREPARE = 0;
    public static final int DOWNLOAD_STATUS_START = 1;
    public static final int DOWNLOAD_STATUS_DOWNLOADING = 2;
    public static final int DOWNLOAD_STATUS_CANCEL = 3;
    public static final int DOWNLOAD_STATUS_ERROR = 4;
    public static final int DOWNLOAD_STATUS_COMPLETED = 5;
    public static final int DOWNLOAD_STATUS_PAUSE = 6;
}