package com.aone.recorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.aone.recorder.DAO.RecordConfigDAO;
import com.aone.recorder.activities.RecordListActivity;
import com.aone.recorder.activities.RecordSettingActivity;
import com.aone.recorder.model.RecordConfig;
import com.aone.recorder.model.RecordFile;
import com.aone.recorder.utils.RecordConfigUtil;
import com.aone.recorder.utils.RecordFileUtil;
import com.aone.recorder.views.DynamicWaveView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    // 权限
    private static final int REQUEST_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK};

    // 录音相关
    private AudioRecoder mAudioRecorder;
    private RecordConfigDAO mRecordConfigDAO;
    private RecordConfig mRecordConfig;
    private boolean RecordState = true;
    private List<Integer> RecordDBs;
    // 计时器
    private Timer mTimer;
    private long baseTimer;
    private String TIMER_RECORD;
    // 控件变量
    private ImageButton imgBtn_record2list,
            imgBtn_record2setting,
            imgBtn_recorder;
    private TextView tv_timer,
            tv_audioSamplingRate,
            tv_audioChannels;
    private DynamicWaveView mDynamicWaveView;
    private ImageView iv_outputFileFormat;
    // 其他
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 请求权限
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);


        // 初始化数据库
        mRecordConfigDAO = new RecordConfigDAO(this);
        mRecordConfigDAO.initConfig(this);
        // 获取配置数据
        mRecordConfig = RecordConfigUtil.getRecordConfig(this);

        mHandler = new Handler();
        RecordDBs = new ArrayList<>();

        initView();
        setEvent();

    }

    /**
     * 设置事件
     */
    private void setEvent() {
        imgBtn_recorder.setOnClickListener(this);
        imgBtn_record2list.setOnClickListener(this);
        imgBtn_record2setting.setOnClickListener(this);
    }

    /**
     * 初始化View
     */
    private void initView() {
        // 音频可视化
        mDynamicWaveView = findViewById(R.id.waveView);
        // 计时器
        tv_timer = findViewById(R.id.tv_timer);
        // 按钮
        imgBtn_recorder = findViewById(R.id.imgBtn_record);
        imgBtn_record2list = findViewById(R.id.imgBtn_record2list);
        imgBtn_record2setting = findViewById(R.id.imgBtn_record2setting);
        // 首页录音配置显示
        tv_audioSamplingRate = findViewById(R.id.tv_audioSamplingRate);
        tv_audioChannels = findViewById(R.id.tv_audioChannels);
        iv_outputFileFormat = findViewById(R.id.iv_outputFileFormat);

        tv_audioSamplingRate.setText(RecordConfigUtil.decodeAudioSamplingRate(mRecordConfig.getAudioSamplingRate()));
        tv_audioChannels.setText(RecordConfigUtil.decodeAudioChannels(mRecordConfig.getAudioChannels()));

        switch (RecordConfigUtil.decodeOutputFileFormat(mRecordConfig.getOutputFileFormat())) {
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
    }

    /**
     * 请求权限
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToRecordAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                permissionToRecordAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                permissionToRecordAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                break;

        }
        if (!permissionToRecordAccepted) finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imgBtn_record:
                if (RecordState) {
                    // 开始录音
                    StartRecord();

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

    private void StartRecord() {
        // 变更按钮图案
        imgBtn_recorder.setImageResource(R.drawable.ic_confirm);

        mAudioRecorder = new AudioRecoder(mRecordConfig);
        mAudioRecorder.start();
        Log.e(TAG, "Record Start!");

        mTimer = new Timer();
        baseTimer = SystemClock.elapsedRealtime();
        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tv_timer.setText((String) msg.obj);
                TIMER_RECORD = (String) msg.obj;
            }
        };
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int time = (int) ((SystemClock.elapsedRealtime() - MainActivity.this.baseTimer) / 1000);
                String hh = new DecimalFormat("00").format(time / 3600);
                String mm = new DecimalFormat("00").format(time % 3600 / 60);
                String ss = new DecimalFormat("00").format(time % 60);
                String timeFormat = hh + ":" + mm + ":" + ss;
                Message msg = new Message();
                msg.obj = timeFormat;
                handler.sendMessage(msg);
            }
        };
        mTimer.schedule(task, 0, 1000L);
        mHandler.post(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int db = mAudioRecorder.updateMicStatus();
            RecordDBs.add(db);
            mDynamicWaveView.addSpectrum(db);
            mHandler.postDelayed(this, 100);
        }
    };

    private void StopRecord() {
        // 将文件记录写入SQLite
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < RecordDBs.size(); i++)
            str.append(RecordDBs.get(i)).append(" ");
        RecordDBs.clear();
        Log.e(TAG, str.toString());
        RecordFile recordFile = mAudioRecorder.getRecordFile();
        recordFile.setFileRecordLength(TIMER_RECORD);
        recordFile.setFileSpectrum(str.toString());
        RecordFileUtil.addRecordFile(this, recordFile);
        // 停止AudioRecorder
        mAudioRecorder.stop();
        // 清空计时器
        TIMER_RECORD = null;
        if (mTimer != null)
            mTimer.cancel();
        tv_timer.setText("00:00:00");
        // 变更录音按钮
        imgBtn_recorder.setImageResource(R.drawable.ic_controller);
        // 音频可视化清空
        mHandler.removeCallbacks(runnable);
        mDynamicWaveView.cleanCanvas();
        // 弹窗
        Toast toast = Toast.makeText(MainActivity.this, "Record Finish.Named: \n" + mAudioRecorder.getFileName(), Toast.LENGTH_SHORT);
        toast.show();
        Log.e(TAG, "Record Stop!");
    }
}