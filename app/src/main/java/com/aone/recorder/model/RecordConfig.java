package com.aone.recorder.model;

import android.content.Context;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.model
 * @ClassName: RecordConfig
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/13 3:54
 * @Desc:
 */
public class RecordConfig {
    private int AudioSource;
    private int AudioSamplingRate;
    private int OutputFormat;
    private int AudioChannels;
    private int AudioEncoder;
    private int AudioEncodingBitRate;
    private String OutputFilePath;
    private String OutputFileFormat;

    public RecordConfig(){};
    public RecordConfig(Context context, int AudioSource, int AudioSamplingRate, int OutputFormat, int AudioChannels, int AudioEncoder, int AudioEncodingBitRate, String OutputFileFormat){
        this.AudioSource = AudioSource;
        this.AudioSamplingRate = AudioSamplingRate;
        this.OutputFormat = OutputFormat;
        this.AudioChannels = AudioChannels;
        this.AudioEncoder = AudioEncoder;
        this.AudioEncodingBitRate = AudioEncodingBitRate;
        this.OutputFilePath = context.getExternalCacheDir().getAbsolutePath() + "/";
        this.OutputFileFormat = OutputFileFormat;
    }


    public int getAudioSource() {
        return AudioSource;
    }

    public void setAudioSource(int audioSource) {
        AudioSource = audioSource;
    }

    public int getAudioSamplingRate() {
        return AudioSamplingRate;
    }

    public void setAudioSamplingRate(int audioSamplingRate) {
        AudioSamplingRate = audioSamplingRate;
    }

    public int getOutputFormat() {
        return OutputFormat;
    }

    public void setOutputFormat(int outputFormat) {
        OutputFormat = outputFormat;
    }

    public int getAudioChannels() {
        return AudioChannels;
    }

    public void setAudioChannels(int audioChannels) {
        AudioChannels = audioChannels;
    }

    public int getAudioEncoder() {
        return AudioEncoder;
    }

    public void setAudioEncoder(int audioEncoder) {
        AudioEncoder = audioEncoder;
    }

    public int getAudioEncodingBitRate() {
        return AudioEncodingBitRate;
    }

    public void setAudioEncodingBitRate(int audioEncodingBitRate) {
        AudioEncodingBitRate = audioEncodingBitRate;
    }

    public String getOutputFilePath() {
        return OutputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        OutputFilePath = outputFilePath;
    }

    public String getOutputFileFormat() {
        return OutputFileFormat;
    }

    public void setOutputFileFormat(String outputFileFormat) {
        OutputFileFormat = outputFileFormat;
    }
}
