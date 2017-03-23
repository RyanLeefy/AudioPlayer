package com.example.administrator.audioplayer.recognition;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.BaseActivity;
import com.example.administrator.audioplayer.activity.RecentActivity;
import com.example.administrator.audioplayer.http.HttpUtils;
import com.example.administrator.audioplayer.utils.ActivityManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by on 2017/3/22 0022.
 */

public class RecognizeActivity extends BaseActivity {

    private ActionBar ab;
    private Toolbar toolbar;
    private ImageView img_recognize;
    private TextView tv_state;

    private MediaRecorder mediaRecorder;

    private String filepath;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RecognizeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //把Activity放入ActivityManager中进行管理，方便一键退出所有
        ActivityManager.getInstance().pushOneActivity(this);
        setContentView(R.layout.activity_recognize);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar();

        tv_state = (TextView) findViewById(R.id.tv_recognize_state);

        img_recognize = (ImageView) findViewById(R.id.img_recognize);
        img_recognize.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //开始录音
                    case MotionEvent.ACTION_DOWN:

                        if (ActivityCompat.checkSelfPermission(RecognizeActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(RecognizeActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);
                        } else {
                            startRecord();
                            tv_state.setText("录音中..");
                        }
                        break;

                    //开始上传文件并识别
                    case MotionEvent.ACTION_UP:
                        if (ActivityCompat.checkSelfPermission(RecognizeActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                        } else {
                            stopRecord();
                            tv_state.setText("识别中..");
                        }


                        break;
                }
                return false;
            }
        });


        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(RecognizeActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(RecognizeActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);
                } else {
                    startRecord();
                }
            }
        });

        Button stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord();
            }
        });

    }


    /**
     * 初始化toolbar
     */
    private void setToolbar() {
        toolbar.setTitle("听歌识曲");
        setSupportActionBar(toolbar);

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }


    /**
     * 开始录音
     */
    public void startRecord()  {
        mediaRecorder = new MediaRecorder();

        // 设置音频来源(一般为麦克风)
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置音频输出格式（默认的输出格式）
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        // 设置音频编码方式（默认的编码方式）
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        //采样率
        mediaRecorder.setAudioSamplingRate(44010);
        //双声道
        mediaRecorder.setAudioChannels(2);

        //文件地址
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audioplayer/recoginze/";

        filepath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/audioplayer/recoginze/" + String.valueOf(System.currentTimeMillis() + ".aac");

        File filedir = new File(path);
        if(!filedir.exists()) {
            try {
            filedir.mkdir();
            } catch (Exception e) {
            }
        }
        File file = new File(filepath);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
            }
        }

            //输出文件
        mediaRecorder.setOutputFile(filepath);

        try {
            mediaRecorder.prepare();// 准备录制

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaRecorder.start();// 开始录制
        } catch (Exception e) {

        }
    }

    /**
     * 结束录音
     */
    public void stopRecord() {
        mediaRecorder.stop();// 停止刻录
        mediaRecorder.release(); // 刻录完成一定要释放资源
    }



/*
        @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                         @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == 10) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startRecord();
                }else{
                    //User denied Permission.
                }
            }
        }
*/



    public void uploadFileAndRecognize(String path) {
        String httpAction = "10./";
        HttpUtils.postFile(httpAction, path, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e("上传失败!!" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Logger.e("上传成功!!" + json);
                } else {
                    Logger.e("上传失败!!" + response.code());
                }
            }
        });
    }



}
