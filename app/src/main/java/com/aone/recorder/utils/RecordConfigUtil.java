package com.aone.recorder.utils;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaRecorder;

import com.aone.recorder.DAO.ConfigDAO;
import com.aone.recorder.model.RecordConfig;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.utils
 * @ClassName: RecordConfigUtil
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/13 5:00
 * @Desc:
 */
public class RecordConfigUtil {
    public static RecordConfig getRecordConfig(Context context){
        // 初始化数据库
        ConfigDAO configDAO = new ConfigDAO();
        // 获取配置数据
        Cursor ConfigCursor = configDAO.queryConfig(context);
        return new RecordConfig(ConfigCursor.getInt(ConfigCursor.getColumnIndex("AudioSource")),
                                ConfigCursor.getInt(ConfigCursor.getColumnIndex("AudioSamplingRate")),
                                ConfigCursor.getInt(ConfigCursor.getColumnIndex("OutputFormat")),
                                ConfigCursor.getInt(ConfigCursor.getColumnIndex("AudioChannels")),
                                ConfigCursor.getInt(ConfigCursor.getColumnIndex("AudioEncoder")),
                                ConfigCursor.getInt(ConfigCursor.getColumnIndex("AudioEncodingBitRate")),
                                ConfigCursor.getString(ConfigCursor.getColumnIndex("DefaultFilePath")),
                                ConfigCursor.getString(ConfigCursor.getColumnIndex("OutputFileFormat")));
    }

    public static String decodeAudioSource(int audioSourceCode){
        switch (audioSourceCode){
            case MediaRecorder.AudioSource.MIC:
                return "MIC";
            case MediaRecorder.AudioSource.VOICE_UPLINK:
                return "VOICE UPLINK";
            case MediaRecorder.AudioSource.VOICE_DOWNLINK:
                return"VOICE DOWNLINK";
            case MediaRecorder.AudioSource.VOICE_CALL:
                return "VOICE CALL";
            case MediaRecorder.AudioSource.VOICE_RECOGNITION:
                return "VOICE RECOGNITION";
            case MediaRecorder.AudioSource.VOICE_COMMUNICATION:
                return "VOICE COMMUNICATION";
            default:
                return "Default";
        }
    }

    public static int encodeAudioSource(String audioSource){
        switch (audioSource){
            case "MIC":
                return MediaRecorder.AudioSource.MIC;
            case "VOICE UPLINK":
                return MediaRecorder.AudioSource.VOICE_UPLINK;
            case "VOICE DOWNLINK":
                return MediaRecorder.AudioSource.VOICE_DOWNLINK;
            case "VOICE CALL":
                return MediaRecorder.AudioSource.VOICE_CALL;
            case "VOICE RECOGNITION":
                return MediaRecorder.AudioSource.VOICE_RECOGNITION;
            case "VOICE COMMUNICATION":
                return MediaRecorder.AudioSource.VOICE_COMMUNICATION;
            default:
                return 0;
        }
    }

    public static String decodeOutputFileFormat(String outputFileFormat){
        switch (outputFileFormat){
            case "MP3":
                return "MP3";
            case "WAV":
                return "WAVE";
            case "AAC":
                return "AAC";
            case "AMR":
                return "AMR";
            default:
                return "Default";
        }
    }

    public static String encodeOutputFileFormat(String outputFileFormat){
        switch (outputFileFormat){
            case "MP3":
                return "MP3";
            case "WAVE":
                return "WAV";
            case "AAC":
                return "AAC";
            case "AMR":
                return "AMR";
            default:
                return "Default";
        }
    }

    public static String decodeAudioChannels(int audioChannelsCode){
        switch (audioChannelsCode){
            case 1:
                return "Single";
            case 2:
                return "Double";
            default:
                return "Default";
        }
    }

    public static int encodeAudioChannels(String audioChannels){
        switch (audioChannels){
            case "Single":
                return 1;
            case "Double":
                return 2;
            default:
                return 0;
        }
    }

    public static String decodeAudioSamplingRate(int audioSamplingRateCode){
        return audioSamplingRateCode + "Hz";
    }

    public static String encodeAudioSamplingRate(String audioSamplingRate){
        return audioSamplingRate.split("Hz")[0];
    }
}
