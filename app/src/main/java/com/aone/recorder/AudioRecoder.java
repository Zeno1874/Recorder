package com.aone.recorder;

import android.media.MediaRecorder;

import com.aone.recorder.model.RecordConfig;
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

    public AudioRecoder(RecordConfig recordConfig){
        mAudioSource = recordConfig.getAudioSource();
        mAudioSamplingRate = recordConfig.getAudioSamplingRate();
        mAudioChannels = recordConfig.getAudioChannels();
        mAudioEncoder = recordConfig.getAudioEncoder();
        mAudioEncodingBitRate = recordConfig.getAudioEncodingBitRate();
        mOutputFormat = recordConfig.getOutputFormat();

        mOutputFileFormat = recordConfig.getOutputFileFormat();
        mOutputFile = recordConfig.getOutputFilePath();

        DateUtil dateUtil = new DateUtil();
        mOutputFile += dateUtil.getDateTime();
        mOutputFile += "." + mOutputFileFormat;

        mOutputFileName = dateUtil.getDateTime() + "." + mOutputFileFormat;
    }

    public void start(){
        try {
            initRecord();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        if (mOnRecordFinishListener != null) {
            mOnRecordFinishListener.onRecordFinish();
        }
        cancelRecord();
    }

    private void initRecord() {
        mMediaRecord = new MediaRecorder();
        mMediaRecord.setOnErrorListener(this);
        mMediaRecord.setOnInfoListener(this);

        mMediaRecord.setAudioSource(mAudioSource);
        mMediaRecord.setAudioSamplingRate(mAudioSamplingRate);
        mMediaRecord.setOutputFormat(mOutputFormat);
        mMediaRecord.setAudioEncoder(mAudioEncoder);
        mMediaRecord.setAudioChannels(mAudioChannels);
        mMediaRecord.setAudioEncodingBitRate(mAudioEncodingBitRate);

        mMediaRecord.setOutputFile(mOutputFile);

        try {
            mMediaRecord.prepare();
            mMediaRecord.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelRecord() {
        if (mMediaRecord != null) {
            mMediaRecord.setOnErrorListener(null);
            mMediaRecord.setOnInfoListener(null);
            try {
                mMediaRecord.stop();
            }catch (Exception e) {
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

    public void setOnRecordFinishListener(OnRecordFinishListener listener){
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

    public int updateMicStatus(){
        double ratio = (double) 100 * mMediaRecord.getMaxAmplitude() / 32767;
        double db = 0;// 分贝
        if (ratio > 1) db = 300 * Math.log10(ratio);
        else db = 0;
        return (int) db;
    }

    public String getOutputFileName(){
        return mOutputFileName;
    }

    public String getOutputFileFormat(){
        return mOutputFileFormat;
    }
}
