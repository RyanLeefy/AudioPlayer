package com.example.administrator.audioplayer.download;



public interface DownloadTaskListener {
    void onPrepare(DownloadTask task);

    void onStart(DownloadTask task);

    void onDownloading(DownloadTask task);

    void onPause(DownloadTask task);

    void onCancel(DownloadTask task);

    void onCompleted(DownloadTask task);

    void onError(DownloadTask task, int errorCode);

    int DOWNLOAD_ERROR_FILE_NOT_FOUND = -1;
    int DOWNLOAD_ERROR_IO_ERROR = -2;

}
