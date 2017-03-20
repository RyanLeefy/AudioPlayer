package com.example.administrator.audioplayer.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.DownActivity;
import com.example.administrator.audioplayer.bean.DownLoadInfo;
import com.example.administrator.audioplayer.db.DownLoadDB;
import com.example.administrator.audioplayer.http.HttpUtils;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.utils.PrintLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by on 2017/3/1 0001.
 */

public class DownloadService extends Service {
    public static final String PACKAGE = "com.example.administrator.audioplayer";

    public static final String ADD_DOWNTASK = "com.example.administrator.audioplayer.downtaskadd";
    public static final String ADD_MULTI_DOWNTASK = "com.example.administrator.audioplayer.multidowntaskadd";
    public static final String CANCLE_DOWNTASK = "com.example.administrator.audioplayer.cacletask";
    public static final String CANCLE_ALL_DOWNTASK = "com.example.administrator.audioplayer.caclealltask";
    public static final String START_ALL_DOWNTASK = "com.example.administrator.audioplayer.startalltask";
    public static final String RESUME_START_DOWNTASK = "com.example.administrator.audioplayer.resumestarttask";
    public static final String PAUSE_TASK = "com.example.administrator.audioplayer.pausetask";
    public static final String PAUSE_ALLTASK = "com.example.administrator.audioplayer.pausealltask";

    public static final String UPDATE_DOWNSTAUS = "com.example.administrator.audioplayer.updatedown";
    public static final String TASK_STARTDOWN = "com.example.administrator.audioplayer.taskstart";
    public static final String TASKS_CHANGED = "com.example.administrator.audioplayer.taskchanges";

    private boolean d = true;
    private static final String TAG = "DownService";
    private static DownLoadDB downloadDb;
    private ExecutorService executorService;
    private static ArrayList<String> prepareTaskList = new ArrayList<>();
    private int downTaskCount = 0;
    private int downTaskDownloaded = -1;
    private DownloadTask currentTask;
    private NotificationManager mNotificationManager;
    private Context mContext;
    private RemoteViews remoteViews;
    private int notificationid = 10;
    private boolean isForeground;
    private DownloadTaskListener listener = new DownloadTaskListener() {
        @Override
        public void onPrepare(DownloadTask info) {
            PrintLog.d(TAG, TAG + " task onPrepare");
        }

        @Override
        public void onStart(DownloadTask info) {
            PrintLog.d(TAG, TAG + " task onStart");
            Intent intent = new Intent(TASK_STARTDOWN);
            intent.putExtra("completesize", info.getCompletedSize());
            intent.putExtra("totalsize", info.getTotalSize());
            intent.setPackage(PACKAGE);
            sendBroadcast(intent);
        }

        @Override
        public void onDownloading(DownloadTask info) {
            // L.D(d,TAG, TAG + " task onDownloading");
            Intent intent = new Intent(UPDATE_DOWNSTAUS);
            intent.putExtra("completesize", info.getCompletedSize());
            intent.putExtra("totalsize", info.getTotalSize());
            intent.setPackage(PACKAGE);
            sendBroadcast(intent);
        }

        @Override
        public void onPause(DownloadTask info) {
            PrintLog.d(TAG, TAG + " task onPause");
            sendIntent(TASKS_CHANGED);
            if (prepareTaskList.size() > 0) {
                if(currentTask != null)
                    prepareTaskList.remove(currentTask.getId());
            }
            currentTask = null;
            upDateNotification();
            startTask();
        }

        @Override
        public void onCancel(DownloadTask info) {
            PrintLog.d(TAG, TAG + " task onCancel");
            sendIntent(TASKS_CHANGED);
            if (prepareTaskList.size() > 0) {
                if(currentTask != null)
                    prepareTaskList.remove(currentTask.getId());
            }
            currentTask = null;
            upDateNotification();
            startTask();
        }

        @Override
        public void onCompleted(DownloadTask info) {
            sendIntent(TASKS_CHANGED);
            PrintLog.d(TAG, TAG + " task Completed");
            if (prepareTaskList.size() > 0) {
                if(currentTask != null)
                    prepareTaskList.remove(currentTask.getId());
            }
            currentTask = null;
            downTaskDownloaded++;
            PrintLog.d(TAG, "complete task and start");
            startTask();

        }

        @Override
        public void onError(DownloadTask info, int errorCode) {
            PrintLog.d(TAG, TAG + " task onError");
            startTask();
        }
    };

    public static ArrayList<String> getPrepareTasks() {
        return prepareTaskList;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        PrintLog.d(TAG, TAG + " oncreate");
        mContext = this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        executorService = Executors.newSingleThreadExecutor();
        downloadDb = DownLoadDB.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PrintLog.d(TAG, TAG + " onstartcommand");
        if(intent == null){
            mNotificationManager.cancel(notificationid);
        }
        String action = null;
        try {
            action = intent.getAction();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (action == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        switch (action) {
            case ADD_DOWNTASK:
                String name = intent.getStringExtra("name");
                String artist = intent.getStringExtra("artist");
                String url = intent.getStringExtra("url");
                addDownloadTask(name, artist, url);
                break;
            case ADD_MULTI_DOWNTASK:
                String[] names = intent.getStringArrayExtra("names");
                String[] artists = intent.getStringArrayExtra("artists");
                ArrayList<String> urls = intent.getStringArrayListExtra("urls");
                addDownloadTask(names, artists, urls);
                break;
            case RESUME_START_DOWNTASK:
                String taskid = intent.getStringExtra("downloadid");
                PrintLog.d(TAG, "resume task = " + taskid);
                resume(taskid);
                break;
            case PAUSE_TASK:
                String taskid1 = intent.getStringExtra("downloadid");
                PrintLog.d(TAG, "pause task = " + taskid1);
                pause(taskid1);
                break;
            case CANCLE_DOWNTASK:
                String taskid3 = intent.getStringExtra("downloadid");
                PrintLog.d(TAG, "cancle task = " + taskid3);
                cancel(taskid3);
                break;
            case CANCLE_ALL_DOWNTASK:
                if (prepareTaskList.size() > 1) {
                    prepareTaskList.clear();
                    if(currentTask != null)
                        prepareTaskList.add(currentTask.getId());
                }
                if(currentTask != null)
                    cancel(currentTask.getId());
                downloadDb.deleteAllDowningTasks();
                sendIntent(TASKS_CHANGED);
                break;
            case START_ALL_DOWNTASK:
                String[] ids = downloadDb.getDownLoadInfoListAllDowningIds();
                for (int i = 0; i < ids.length; i++) {
                    if (!prepareTaskList.contains(ids[i])) {
                        prepareTaskList.add(ids[i]);
                    }
                }
                startTask();

                break;
            case PAUSE_ALLTASK:

                if (prepareTaskList.size() > 1) {
                    prepareTaskList.clear();
                    if(currentTask != null)
                        prepareTaskList.add(currentTask.getId());
                }
                if(currentTask != null)
                    pause(currentTask.getId());
                break;
        }


        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotificationManager.cancel(notificationid);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private String getDownSave() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/audioplayer/");
            if (!file.exists()) {
                boolean r = file.mkdirs();
                if (!r) {
                    Toast.makeText(mContext, "储存卡无法创建文件", Toast.LENGTH_SHORT).show();
                    return null;
                }
                return file.getAbsolutePath() + "/";
            }
            return file.getAbsolutePath() + "/";
        } else {
            Toast.makeText(mContext, "没有储存卡", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void addDownloadTask(String[] names, String[] artists, ArrayList<String> urls) {

        PrintLog.d(TAG, "add task name = " + names + "  taskid = " + (urls).hashCode() + "  task artsit = " + artists);
        int len = urls.size();

        for (int i = 0; i < len; i++) {
            //先从数据库中查询有没有
            DownLoadInfo info = downloadDb.getDownLoadInfo((urls.get(i)).hashCode() + "");
            //没有的话才添加的数据库，添加到任务队列中
            if(info == null) {
                DownLoadInfo downloadinfo = new DownLoadInfo((urls.get(i)).hashCode() + "", 0l,
                        0l, urls.get(i), getDownSave(), names[i], artists[i], DownloadStatus.DOWNLOAD_STATUS_INIT);
                downloadDb.insert(downloadinfo);
                prepareTaskList.add(downloadinfo.getDownloadId());
                downTaskCount++;
            }
        }
        Toast.makeText(mContext,"已加入到下载队列", Toast.LENGTH_SHORT).show();
        upDateNotification();
        if (currentTask != null) {
            PrintLog.d(TAG, "add task wrong, current task is not null");
            return;
        }

        startTask();

    }

    private void addDownloadTask(String name, String artist, String url) {


        PrintLog.d(TAG, "add task name = " + name + "  taskid = " + (url).hashCode() + "  task artsit = " + artist);
        //先从数据库中查询有没有
        DownLoadInfo info = downloadDb.getDownLoadInfo(url.hashCode() + "");
        if(info == null) {
            //没有的话才添加的数据库，添加到任务队列中
            DownLoadInfo downloadinfo = new DownLoadInfo((url).hashCode() + "", 0l,
                    0l, url, getDownSave(), name, artist, DownloadStatus.DOWNLOAD_STATUS_INIT);
            downloadDb.insert(downloadinfo);
            prepareTaskList.add(downloadinfo.getDownloadId());
            downTaskCount++;
            upDateNotification();
            Toast.makeText(mContext, "已加入到下载", Toast.LENGTH_SHORT).show();
            if (currentTask != null) {
                PrintLog.d(TAG, "add task wrong, current task is not null");
                return;
            }

            startTask();
        } else {
            Toast.makeText(mContext, "歌曲下载过了或已经在下载列表当中", Toast.LENGTH_SHORT).show();
        }

    }

    private void upDateNotification() {
        if (currentTask == null) {
            return;
        }
        if (!isForeground) {
            startForeground(notificationid, getNotification(false));
            isForeground = true;
        } else {
            mNotificationManager.notify(notificationid, getNotification(false));
        }
    }

    private void cancleNotification() {
        PrintLog.d(TAG, " canclenotification");
        stopForeground(true);
        isForeground = false;
        mNotificationManager.notify(notificationid, getNotification(true));
        downTaskCount = 0;
        downTaskDownloaded = -1;

    }


    public void startTask() {
        PrintLog.d(TAG, TAG + " start task task size = " + prepareTaskList.size());
        if (currentTask != null) {
            PrintLog.d(TAG, "start task wrong, current task is running");
            return;
        }
        if (prepareTaskList.size() > 0) {
            DownloadTask task = null;
            PrintLog.d(TAG, prepareTaskList.get(0));
            DownLoadInfo downLoadInfo = downloadDb.getDownLoadInfo(prepareTaskList.get(0));

            if (downLoadInfo != null) {
                PrintLog.d(TAG, "entity id = " + downLoadInfo.getDownloadId());
                task = DownloadTask.parse(downLoadInfo, mContext);
            }
            if (task == null) {
                PrintLog.d(TAG, "can't create downloadtask");
                return;
            }
            PrintLog.d(TAG, "start task ,task name = " + task.getFileName() + "  taskid = " + task.getId());
            if (task.getDownloadStatus() != DownloadStatus.DOWNLOAD_STATUS_COMPLETED) {
                task.setDownloadStatus(DownloadStatus.DOWNLOAD_STATUS_PREPARE);
                task.setdownLoadDB(downloadDb);
                task.setHttpClient(HttpUtils.builder.build());
                task.addDownloadListener(listener);
                executorService.submit(task);
                currentTask = task;
                upDateNotification();
                sendIntent(TASKS_CHANGED);
            }
        } else {
            PrintLog.d(TAG, " no task");
            cancleNotification();
        }
    }

    /**
     * if return null,the task does not exist
     *
     * @param taskId
     * @return
     */
    public void resume(String taskId) {

        downTaskCount++;
        prepareTaskList.add(taskId);
        upDateNotification();
        sendIntent(TASKS_CHANGED);
        if (currentTask == null) {
            startTask();
        }

        PrintLog.d(TAG, "resume task = " + taskId);
    }


    public void cancel(String taskId) {
        if (currentTask != null) {
            if (taskId.equals(currentTask.getId())) {
                currentTask.cancel();
                currentTask.setDownloadStatus(DownloadStatus.DOWNLOAD_STATUS_CANCEL);
            }
        }

        if (prepareTaskList.contains(taskId)) {
            downTaskCount--;
            prepareTaskList.remove(taskId);
        }

        if (prepareTaskList.size() == 0) {
            currentTask = null;
        }
        downloadDb.deleteTask(taskId);
        upDateNotification();
        sendIntent(TASKS_CHANGED);
        PrintLog.d(TAG, "cancle task = " + taskId);
    }

    public void pause(String taskid) {
        downTaskCount--;

        if (currentTask != null && taskid.equals(currentTask.getId())) {
            currentTask.pause();
        }
        prepareTaskList.remove(taskid);
        if (prepareTaskList.size() == 0) {
            currentTask = null;
        }
        upDateNotification();
        sendIntent(TASKS_CHANGED);
    }


    private Notification getNotification(boolean complete) {

        if (downTaskCount == 0) {
            downTaskCount = prepareTaskList.size();
        }
        PrintLog.d(TAG, "notification downtaskcount = " + downTaskCount);
        if (downTaskDownloaded == -1) {
            downTaskDownloaded = 0;
        }
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.down_notification);

        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0,
                new Intent(this.getApplicationContext(), DownActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        final Intent nowPlayingIntent = new Intent();
        nowPlayingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        nowPlayingIntent.setComponent(new ComponentName("com.example.administrator.audioplayer", "com.example.administrator.audioplayer.activity.DownActivity"));
        PendingIntent clickIntent = PendingIntent.getActivity(this,0,nowPlayingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setImageViewResource(R.id.image, R.drawable.placeholder_disk);
        if(complete){
            remoteViews.setTextViewText(R.id.title, "audioplayer" );
            remoteViews.setTextViewText(R.id.text, "下载完成，点击查看" );
            remoteViews.setTextViewText(R.id.time, showDate());
        }else {
            remoteViews.setTextViewText(R.id.title, "下载进度：" + downTaskDownloaded + "/" + downTaskCount);
            remoteViews.setTextViewText(R.id.text, "正在下载：" + currentTask.getFileName());
            remoteViews.setTextViewText(R.id.time, showDate());
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setContent(remoteViews)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(clickIntent);

        if (CommonUtils.isJellyBeanMR1()) {
            builder.setShowWhen(false);
        }
        return builder.build();
    }



    public static String showDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("a hh:mm");
        String date = sDateFormat.format(new Date());
        return date;
    }

    private void sendIntent(String action){
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setPackage(PACKAGE);
        sendBroadcast(intent);
    }

}
