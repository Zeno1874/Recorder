package com.aone.recorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aone.recorder.DAO.ConfigDAO;
import com.aone.recorder.activities.RecordListActivity;
import com.aone.recorder.activities.RecordSettingActivity;
import com.aone.recorder.model.RecordConfig;
import com.aone.recorder.utils.RecordConfigUtil;
import com.aone.recorder.views.WaveView;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    // 权限
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    // 录音相关
    private AudioRecoder mAudioRecorder;
    private ConfigDAO configDAO;
    private RecordConfig mRecordConfig;

    private boolean RecordState = true;

    private String dp_audioSource,
                   dp_outputFileFormat,
                   dp_audioChannels,
                   dp_audioSamplingRate;
    // 计时器
    private Timer mTimer;
    private long baseTimer;
    // 控件变量
    private ImageButton imgBtn_record2list,
            imgBtn_record2setting,
            imgBtn_recorder;
    private TextView tv_timer,
            tv_audioSamplingRate,
            tv_audioChannels;
    private WaveView mWaveView;
    private ImageView iv_outputFileFormat;
    // 其他
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        // 初始化数据库
        configDAO = new ConfigDAO();
        configDAO.initConfig(this);

        mRecordConfig = RecordConfigUtil.getRecordConfig(this);

        mHandler = new Handler();
        // 音频可视化
        mWaveView = findViewById(R.id.waveView);
        // 计时器
        tv_timer = findViewById(R.id.tv_timer);
        // 首页录音配置显示
        tv_audioSamplingRate = findViewById(R.id.tv_audioSamplingRate);
        tv_audioChannels = findViewById(R.id.tv_audioChannels);
        iv_outputFileFormat = findViewById(R.id.iv_outputFileFormat);

        dp_outputFileFormat = RecordConfigUtil.decodeOutputFileFormat(mRecordConfig.getOutputFileFormat());
        dp_audioChannels = RecordConfigUtil.decodeAudioChannels(mRecordConfig.getAudioChannels());
        dp_audioSamplingRate = RecordConfigUtil.decodeAudioSamplingRate(mRecordConfig.getAudioSamplingRate());

        tv_audioSamplingRate.setText(dp_audioSamplingRate);
        tv_audioChannels.setText(dp_audioChannels);

        switch (dp_outputFileFormat){
            case "MP3":
                iv_outputFileFormat.setImageResource(R.drawable.ic_mp3);
                break;
            case "WAVE":
                iv_outputFileFormat.setImageResource(R.drawable.ic_wave);
                break;
            case "AAC":
                iv_outputFileFormat.setImageResource(R.drawable.ic_aac);
                break;
            case "AMR":
                iv_outputFileFormat.setImageResource(R.drawable.ic_amr);
                break;
        }

        // 按钮
        imgBtn_recorder = findViewById(R.id.imgBtn_record);
        imgBtn_record2list = findViewById(R.id.imgBtn_record2list);
        imgBtn_record2setting = findViewById(R.id.imgBtn_record2setting);

        imgBtn_recorder.setOnClickListener(this);
        imgBtn_record2list.setOnClickListener(this);
        imgBtn_record2setting.setOnClickListener(this);
    }

    /**
     * 请求权限
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imgBtn_record:
                if (RecordState){
                    // 开始录音
                    StartRecord();
                    mHandler.post(runnable);
                } else {
                    // 停止录音
                    StopRecord();

                }
                RecordState = !RecordState;
                break;
            case R.id.imgBtn_record2list:
                intent = new Intent(this, RecordListActivity.class);
                startActivity(intent);
                break;
            case R.id.imgBtn_record2setting:
                intent = new Intent(this, RecordSettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mWaveView.addSpectrum(mAudioRecorder.updateMicStatus());
            mHandler.postDelayed(this, 100);
        }
    };

    private void StartRecord() {
        // 变更按钮图案
        imgBtn_recorder.setImageResource(R.drawable.btn_confirm);

        mAudioRecorder = new AudioRecoder(mRecordConfig);
        mAudioRecorder.start();
        Log.e(TAG,"Record Start!");

        mTimer = new Timer();
        MainActivity.this.baseTimer = SystemClock.elapsedRealtime();
        @SuppressLint("HandlerLeak")
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tv_timer.setText((String) msg.obj);
            }
        };
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int time = (int)((SystemClock.elapsedRealtime() - MainActivity.this.baseTimer) / 1000);
                String hh = new DecimalFormat("00").format(time / 3600);
                String mm = new DecimalFormat("00").format(time % 3600 / 60);
                String ss = new DecimalFormat("00").format(time % 60);
                String timeFormat = new String(hh + ":" + mm + ":" + ss);
                Message msg = new Message();
                msg.obj = timeFormat;
                handler.sendMessage(msg);
            }
        };
        mTimer.schedule(task,0,1000L);
    }

    private void StopRecord() {
        mAudioRecorder.stop();
        mTimer.cancel();
        Log.e(TAG,"Record Stop!");
        // 清空计时器
        tv_timer.setText("00:00:00");
        // 变更录音按钮
        imgBtn_recorder.setImageResource(R.drawable.btn_controller);
        // 音频可视化清空
        mHandler.removeCallbacks(runnable);
        mWaveView.cleanCanvas();
        // 弹窗
        Toast toast=Toast.makeText(MainActivity.this,"Record Finish.\n"+mAudioRecorder.getOutputFileName(), Toast.LENGTH_SHORT);
        toast.show();
    }
}