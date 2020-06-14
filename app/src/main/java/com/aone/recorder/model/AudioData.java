package com.aone.recorder.model;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.views.render
 * @ClassName: AudioData
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/14 11:44
 * @Desc:
 */
public class AudioData {
    public AudioData(byte[] bytes)
    {
        this.bytes = bytes;
    }

    public byte[] bytes;
}
