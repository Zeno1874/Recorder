package com.aone.recorder;

import android.media.MediaRecorder;

import com.aone.recorder.model.RecordConfig;
import com.aone.recorder.model.RecordFile;
import com.aone.recorder.utils.DateUtil;


/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder
 * @ClassName: AudioRecoder
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/13 5:26
 * @Desc:
 */
public class AudioRecoder implements MediaRecorder.OnErrorListener, MediaRecorder.OnInfoListener {
    private static final String TAG = AudioRecoder.class.getSimpleName();

    private MediaRecorder mMediaRecord;

    private int mAudioSource;
    private int mAudioSamplingRate;
    private int mAudioEncoder;
    private int mAudioChannels;
    private int mAudioEncodingBitRate;
    private int mOutputFormat;
    private String mOutputFile;
    private String mOutputFileName;
    private String mOutputFileFormat;

    private String FileCreateTime;

    public AudioRecoder(RecordConfig recordConfig) {
        mAudioSource = recordConfig.getAudioSource();
        mAudioSamplingRate = recordConfig.getAudioSamplingRate();
        mAudioChannels = recordConfig.getAudioChannels();
        mAudioEncoder = recordConfig.getAudioEncoder();
        mAudioEncodingBitRate = recordConfig.getAudioEncodingBitRate();
        mOutputFormat = recordConfig.getOutputFormat();

        DateUtil dateUtil = new DateUtil();
        String time = dateUtil.getDateTime();

        mOutputFileFormat = recordConfig.getOutputFileFormat();
        mOutputFileName = time + "." + mOutputFileFormat;


        mOutputFile = recordConfig.getOutputFilePath();
        mOutputFile += mOutputFileName;

        FileCreateTime = dateUtil.strFormatTrans(time);
    }

    public void start() {
        try {
            initRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mOnRecordFinishListener != null) {
            mOnRecordFinishListener.onRecordFinish();
        }
        cancelRecord();
    }

    private void initRecord() {
        mMediaRecord = new MediaRecorder();
        mMediaRecord.setOnErrorListener(this);
        mMediaRecord.setOnInfoListener(this);
        // 设置录音源
        mMediaRecord.setAudioSource(mAudioSource);
        // 设置媒体输出格式
        mMediaRecord.setOutputFormat(mOutputFormat);
        // 设置媒体音频编码器
        mMediaRecord.setAudioEncoder(mAudioEncoder);
        // 设置媒体音频采样率
        mMediaRecord.setAudioSamplingRate(mAudioSamplingRate);
        // 设置媒体音频声道数
        mMediaRecord.setAudioChannels(mAudioChannels);
        // 设置音频每秒录制的字节数
        mMediaRecord.setAudioEncodingBitRate(mAudioEncodingBitRate);
        // 设置文件保存路径
        mMediaRecord.setOutputFile(mOutputFile);

        try {
            // 媒体录制准备就绪
            mMediaRecord.prepare();
            // 媒体录制器开始录制
            mMediaRecord.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelRecord() {
        if (mMediaRecord != null) {
            mMediaRecord.setOnErrorListener(null);
            mMediaRecord.setOnInfoListener(null);
            try {
                mMediaRecord.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaRecord.release();
            mMediaRecord = null;
        }
    }


    private OnRecordFinishListener mOnRecordFinishListener;

    private interface OnRecordFinishListener {
        void onRecordFinish();
    }

    public void setOnRecordFinishListener(OnRecordFinishListener listener) {
        mOnRecordFinishListener = listener;
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        if (mr != null) {
            mr.release();
        }
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {

    }

    public int updateMicStatus() {
        double ratio = (double) 100 * mMediaRecord.getMaxAmplitude() / 32767;
        double db;// 分贝
        if (ratio > 1) db = 300 * Math.log10(ratio);
        else return 0;
        return (int) db;
    }

    public String getFileName() {
        return mOutputFileName;
    }

    public RecordFile getRecordFile() {
        RecordFile recordFile = new RecordFile();
        recordFile.setFileName(mOutputFileName);
        recordFile.setFilePath(mOutputFile);
        recordFile.setFileFormat(mOutputFileFormat);
        recordFile.setFileCreatedTime(FileCreateTime);
        return recordFile;
    }
}
