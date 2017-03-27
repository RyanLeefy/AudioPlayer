package com.example.administrator.audioplayer.recognition;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.activity.BaseActivity;
import com.example.administrator.audioplayer.http.HttpUtils;
import com.example.administrator.audioplayer.utils.ActivityManager;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
                            uploadAndRecognize(new File(filepath));
                        }


                        break;
                }
                return false;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }


    /**
     * 开始录音
     */
    public void startRecord() {
        mediaRecorder = new MediaRecorder();

        // 设置音频来源(一般为麦克风)
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置音频输出格式（默认的输出格式）
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        // 设置音频编码方式（默认的编码方式）
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //采样率
        mediaRecorder.setAudioSamplingRate(44100);
        //双声道
        //mediaRecorder.setAudioChannels(2);

        //文件地址
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audioplayer/recoginze/";

        filepath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/audioplayer/recoginze/" + String.valueOf(System.currentTimeMillis() + ".aac");

        File filedir = new File(path);
        if (!filedir.exists()) {
            try {
                filedir.mkdir();
            } catch (Exception e) {
            }
        }
        File file = new File(filepath);
        if (!file.exists()) {
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

    public void testServer() {
        String httpAction = "http://10.42.0.1:5000/index";
        HttpUtils.testServer(httpAction, new Callback() {
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


    /**
     * 构造http头，用post上传文件
     * @param file
     */
    private void uploadAndRecognize(final File file) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String httpAction = "http://10.42.0.1:5000/recognize";
                    String end = "\r\n";
                    String hyphens = "--";
                    //随机的
                    String boundary = "FlPm4LpSXsE";
                    URL url = new URL(httpAction);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    /* 允许使用输入流，输出流，不允许使用缓存*/
                    conn.setReadTimeout(20000);
                    conn.setConnectTimeout(20000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
			        /* 请求方式*/
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Charset", "utf-8");
                    conn.setRequestProperty("Connection", "keep-alive");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			        /* 当文件不为空，把文件包装并且上传*/
                    Log.e(TAG, file.toString());
                    if (file != null) {
                        DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
				    /* name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
				     * filename是文件的名字，包含后缀名的   比如:abc.png*/

                        //file内容
                        StringBuffer sb = new StringBuffer();
                        sb.append(hyphens);
                        sb.append(boundary);
                        sb.append(end);

                        sb.append("Content-Disposition: form-data; name=\"data\";filename=" + "\"" + file.getName() + "\"" + end);
                        sb.append("Content-Type: application/octet-stream" + end);
                        sb.append(end);
                        ds.write(sb.toString().getBytes());

                        InputStream input = new FileInputStream(file);
                        int size = 1024;
                        byte[] buffer = new byte[size];
                        int length = 0;
				        /* 从文件读取数据至缓冲区*/
                        while ((length = input.read(buffer)) != -1) {
                            ds.write(buffer, 0, length);
                        }
                        input.close();
                        //写入文件二进制内容
                        ds.write(end.getBytes());
                        //写入end data
                        byte[] end_data = (hyphens + boundary + hyphens + end).getBytes();
                        ds.write(end_data);
                        ds.flush();

			        	/* 获取响应码*/
                        Log.e(TAG, conn.getResponseCode() + "=======");
                        if (conn.getResponseCode() == 200) {
                            String json = getResponseBody(conn.getInputStream());
                            JSONObject jsonObject = new JSONObject(json);
                            JSONObject object = jsonObject.getJSONObject("result");
                            final String songName = object.getString("song_name");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MyApplication.getContext(), "识别到的歌曲为:" + songName, Toast.LENGTH_LONG);
                                    tv_state.setText(songName);
                                }
                            });
                            Logger.e("上传成功!!" + 200);
                        } else {
                            Logger.e("上传失败!!" + conn.getResponseCode());
                        }
                    }

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Logger.e("上传失败!!");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Logger.e("上传失败!!");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public String getResponseBody(InputStream stream) {
        StringBuffer sb = new StringBuffer();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while((len = stream.read(buffer)) != -1) {
                sb.append(buffer);
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();

    }


}
