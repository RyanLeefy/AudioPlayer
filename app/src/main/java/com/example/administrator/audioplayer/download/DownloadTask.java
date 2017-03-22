package com.example.administrator.audioplayer.download;

/**
 *
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.audioplayer.bean.DownLoadInfo;
import com.example.administrator.audioplayer.db.DownLoadDB;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 *
 */
public class DownloadTask implements Runnable {
    private DownLoadInfo downloadInfo;
    private DownLoadDB downLoadDB;
    private OkHttpClient client;
    private Context mContext;

    private String id;
    private long totalSize;
    private long completedSize;         //  Download section has been completed
    //    private float percent;        //  Percent Complete
    private String url;
    private String saveDirPath;
    private RandomAccessFile file;
    private int UPDATE_SIZE = 50 * 1024;    // The database is updated once every 50k
    private int downloadStatus = DownloadStatus.DOWNLOAD_STATUS_INIT;

    private String fileName;    // File name when saving
    private String artist;
    private String temp = ".temp";
    private boolean isPreparingDown;
    private String TAG = "DownloadTask";


    private List<DownloadTaskListener> listeners;

    public DownloadTask(Context context) {
        mContext = context.getApplicationContext();
        listeners = new ArrayList<>();
        downLoadDB = DownLoadDB.getInstance(context);
    }

    public DownloadTask(Context context, Builder builder) {
        //  mContext = context.getApplicationContext();
        listeners = new ArrayList<>();
        downLoadDB = DownLoadDB.getInstance(context);
        init(builder);
}

    private void init(Builder builder) {
        mContext = builder.context;
        fileName = builder.fileName;
        artist = builder.art;
        saveDirPath = builder.saveDirPath;
        completedSize = builder.completedSize;
        downloadInfo = builder.downloadInfo;
        url = builder.url;
        totalSize = builder.totalSize;
        completedSize = builder.completedSize;
        id = builder.id;
        downloadStatus = builder.downloadStatus;
        UPDATE_SIZE = builder.UPDATE_SIZE;
        listeners = builder.listeners;

    }

    public static class Builder {

        private String url;
        private String fileName = url;    // File name when saving
        private String art;
        private String saveDirPath;
        private Context context;
        private DownLoadInfo downloadInfo = null;

        private String id;
        private long totalSize;
        private long completedSize;         //  Download section has been completed

        private int UPDATE_SIZE = 50 * 1024;    // The database is updated once every 50k

        private int downloadStatus = DownloadStatus.DOWNLOAD_STATUS_INIT;

        private List<DownloadTaskListener> listeners = new ArrayList<>();

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }


        public Builder(Context context, String url) {
            this.url = url;
            this.context = context.getApplicationContext();
        }

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setArtName(String art) {
            this.art = art;
            return this;
        }

        public Builder setSaveDirPath(String saveDirPath) {
            this.saveDirPath = saveDirPath;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setCache(int UPDATE_SIZE) {
            this.UPDATE_SIZE = UPDATE_SIZE;
            return this;
        }


        public Builder setCompletedSize(long completedSize) {
            this.completedSize = completedSize;
            return this;
        }

        public Builder setTotalSize(long totalSize) {
            this.totalSize = totalSize;
            return this;
        }

        public Builder setDownLoadInfo(DownLoadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
            downloadStatus = downloadInfo.getDownloadStatus();
            url = downloadInfo.getUrl();
            id = downloadInfo.getDownloadId();
            fileName = downloadInfo.getFileName();
            art = downloadInfo.getArtist();
            saveDirPath = downloadInfo.getSaveDirPath();
            completedSize = downloadInfo.getCompletedSize();
            totalSize = downloadInfo.getTotalSize();

            return this;
        }


        public Builder setListeners(List<DownloadTaskListener> listeners) {
            this.listeners = listeners;
            return this;
        }

        public Builder setDownloadStatus(int downloadStatus) {
            this.downloadStatus = downloadStatus;
            return this;
        }


        public DownloadTask build() {
            // id = (saveDirPath + fileName).hashCode() + "";

            return new DownloadTask(context, this);
        }

    }


    @Override
    public void run() {
        Log.e("start", completedSize + "");
        downloadStatus = DownloadStatus.DOWNLOAD_STATUS_PREPARE;
        //  id = (saveDirPath + fileName).hashCode() + "";

        onPrepare();

        InputStream inputStream = null;
        BufferedInputStream bis = null;
        try {
            downloadInfo = downLoadDB.getDownLoadInfo(id);
            file = new RandomAccessFile(saveDirPath + fileName, "rwd");
            if (downloadInfo != null) {
                completedSize = downloadInfo.getCompletedSize();
                totalSize = downloadInfo.getTotalSize();
            }
            if (file.length() < completedSize) {
                completedSize = file.length();
            }
            long fileLength = file.length();
            if (fileLength != 0 && totalSize == fileLength) {
                downloadStatus = DownloadStatus.DOWNLOAD_STATUS_COMPLETED;
                totalSize = completedSize = fileLength;
                downloadInfo = new DownLoadInfo(id, totalSize, completedSize, url, saveDirPath, fileName, artist, downloadStatus);
                downLoadDB.insert(downloadInfo);
                Log.e(TAG, "file is completed , file length = " + fileLength + "  file totalsize = " + totalSize);
                Toast.makeText(mContext, fileName + "已经下载完成", Toast.LENGTH_SHORT).show();
                onCompleted();
                return;
            } else if (fileLength > totalSize) {
                completedSize = 0;
                totalSize = 0;
            }
            downloadStatus = DownloadStatus.DOWNLOAD_STATUS_START;

            onStart();
            Request request = new Request.Builder()
                    .url(url)
                    .header("RANGE", "bytes=" + completedSize + "-")//  Http value set breakpoints RANGE
                    .addHeader("Referer", url)
                    .build();
            Log.e("comlesize", completedSize + "");
            file.seek(completedSize);
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                downloadStatus = DownloadStatus.DOWNLOAD_STATUS_DOWNLOADING;
                if (totalSize <= 0)
                    totalSize = responseBody.contentLength();

                inputStream = responseBody.byteStream();
                bis = new BufferedInputStream(inputStream);
                byte[] buffer = new byte[4 * 1024];
                int length = 0;
                int buffOffset = 0;
                if (downloadInfo == null) {
                    downloadInfo = new DownLoadInfo(id, totalSize, 0L, url, saveDirPath, fileName, artist, downloadStatus);
                    downLoadDB.insert(downloadInfo);
                }
                while ((length = bis.read(buffer)) > 0 && downloadStatus != DownloadStatus.DOWNLOAD_STATUS_CANCEL && downloadStatus != DownloadStatus.DOWNLOAD_STATUS_PAUSE) {
                    file.write(buffer, 0, length);
                    completedSize += length;
                    buffOffset += length;
                    if (buffOffset >= UPDATE_SIZE) {
                        // Update download information database
                        if (totalSize <= 0 || downloadInfo.getTotalSize() <= 0)
                            downloadInfo.setTotalSize(totalSize);
                        buffOffset = 0;
                        downloadInfo.setCompletedSize(completedSize);
                        downloadInfo.setDownloadStatus(downloadStatus);
                        downLoadDB.update(downloadInfo);
                        onDownloading();
                    }
                }
                //这两句根据需要自行选择是否注释，注释掉的话由于少了数据库的读取，速度会快一点，但同时如果在下载过程程序崩溃的话，程序不会保存最新的下载进度
                downloadInfo.setCompletedSize(completedSize);
                downloadInfo.setDownloadStatus(downloadStatus);
                downLoadDB.update(downloadInfo);

                onDownloading();
            }
        } catch (FileNotFoundException e) {
            downloadStatus = DownloadStatus.DOWNLOAD_STATUS_ERROR;
            onError(DownloadTaskListener.DOWNLOAD_ERROR_FILE_NOT_FOUND);
            return;
//            e.printStackTrace();
        } catch (IOException e) {
            downloadStatus = DownloadStatus.DOWNLOAD_STATUS_ERROR;
            onError(DownloadTaskListener.DOWNLOAD_ERROR_IO_ERROR);
            return;
        } finally {

            //String  nP = fileName.substring(0, path.length() - 5);
            downloadInfo.setCompletedSize(completedSize);
            downloadInfo.setFileName(fileName);
            downLoadDB.update(downloadInfo);
            if (bis != null) try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file != null) try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (totalSize == completedSize) {
            String path = saveDirPath + fileName;
            File file = new File(path);
            Log.e("rename", path.substring(0, path.length() - 5));
            boolean c = file.renameTo(new File(path + ".mp3"));
            Log.e("rename", c + "");

            downloadStatus = DownloadStatus.DOWNLOAD_STATUS_COMPLETED;
            downloadInfo.setDownloadStatus(downloadStatus);
            downLoadDB.update(downloadInfo);
            Uri contentUri = Uri.fromFile(new File(saveDirPath + fileName + ".mp3"));
            //发送广播，扫描音乐刷新音乐库
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
            mContext.sendBroadcast(mediaScanIntent);
        }


        switch (downloadStatus) {
            case DownloadStatus.DOWNLOAD_STATUS_COMPLETED:
                onCompleted();
                break;
            case DownloadStatus.DOWNLOAD_STATUS_PAUSE:
                onPause();
                break;
            case DownloadStatus.DOWNLOAD_STATUS_CANCEL:
                downLoadDB.deleteTask(downloadInfo.getDownloadId());
                File temp = new File(saveDirPath + fileName);
                if (temp.exists()) temp.delete();
                onCancel();
                break;
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public float getPercent() {
        if (totalSize == 0) {
            return 0;
        }
        return completedSize * 100 / totalSize;
    }

    public void setPreparingDown(boolean b) {
        isPreparingDown = b;
    }

    public boolean getPreparingDown() {
        return isPreparingDown;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }


    public long getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(long completedSize) {
        this.completedSize = completedSize;
    }

    public String getSaveDirPath() {
        return saveDirPath;
    }

    public void setSaveDirPath(String saveDirPath) {
        this.saveDirPath = saveDirPath;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public void setdownLoadDB(DownLoadDB downLoadDB) {
        this.downLoadDB = downLoadDB;
    }

    public void setDbEntity(DownLoadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    public DownLoadInfo getDbEntity() {
        return downloadInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHttpClient(OkHttpClient client) {
        this.client = client;
    }

    public String getFileName() {
        return fileName;
    }

    public String getArtistName() {
        return artist;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public void cancel() {
        setDownloadStatus(DownloadStatus.DOWNLOAD_STATUS_CANCEL);
        File temp = new File(saveDirPath + fileName);
        if (temp.exists()) temp.delete();
    }

    public void pause() {
        setDownloadStatus(DownloadStatus.DOWNLOAD_STATUS_PAUSE);
    }

    private void onPrepare() {
        if (listeners == null) {
            return;
        }
        for (DownloadTaskListener listener : listeners) {
            listener.onPrepare(this);
        }
    }

    private void onStart() {
        if (listeners == null) {
            return;
        }
        for (DownloadTaskListener listener : listeners) {
            listener.onStart(this);
        }
    }

    private void onDownloading() {
        if (listeners == null) {
            return;
        }
        for (DownloadTaskListener listener : listeners) {
            listener.onDownloading(this);
        }
    }

    private void onCompleted() {
        if (listeners == null) {
            return;
        }
        for (DownloadTaskListener listener : listeners) {
            listener.onCompleted(this);
        }
    }

    private void onPause() {
        if (listeners == null) {
            return;
        }
        for (DownloadTaskListener listener : listeners) {
            listener.onPause(this);
        }
    }

    private void onCancel() {
        if (listeners == null) {
            return;
        }
        for (DownloadTaskListener listener : listeners) {
            listener.onCancel(this);
        }
    }

    private void onError(int errorCode) {
        if (listeners == null) {
            return;
        }
        for (DownloadTaskListener listener : listeners) {
            listener.onError(this, errorCode);
        }
    }

    public void addDownloadListener(DownloadTaskListener listener) {
        Log.e("downtask", (listeners == null) + "");
        if (listener != null)
            listeners.add(listener);
    }

    /**
     * if listener is null,clear all listener
     *
     * @param listener
     */
    public void removeDownloadListener(DownloadTaskListener listener) {
        if (listener == null) {
            listeners.clear();
        } else {
            listeners.remove(listener);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DownloadTask)) {
            return false;
        }
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(saveDirPath)) {
            return false;
        }
        return url.equals(((DownloadTask) o).url) && saveDirPath.equals(((DownloadTask) o).saveDirPath);
    }

    public static DownloadTask parse(DownLoadInfo entity, Context context) {
        //  DownloadTask task = new DownloadTask(context);
        DownloadTask task = new Builder(context).setDownLoadInfo(entity).build();

//        task.setDownloadStatus(entity.getDownloadStatus());
//        task.setId(entity.getDownloadId());
//        task.setUrl(entity.getUrl());
//        task.setFileName(entity.getFileName());
//        task.setSaveDirPath(entity.getSaveDirPath());
//        task.setCompletedSize(entity.getCompletedSize());
//        task.setDbEntity(entity);
//        task.setTotalSize(entity.getTotalSize());
        return task;
    }
}
