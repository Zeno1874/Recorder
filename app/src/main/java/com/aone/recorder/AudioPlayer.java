package com.aone.recorder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


import com.aone.recorder.views.VisualizerView;
import com.aone.recorder.views.render.BarGraphRenderer;
import com.aone.recorder.views.render.LineRenderer;

import java.io.IOException;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder
 * @ClassName: AudioPlayer
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/14 6:11
 * @Desc:
 */
public class AudioPlayer extends MediaPlayer {
    private static final String TAG = AudioPlayer.class.getSimpleName();
    private MediaPlayer mMediaPlayer;

    private String FileName;
    private String FileFormat;
    private String FilePath;
    private String FileRecordLength;
    private String FileCreatedTime;
    private View mView;
    private VisualizerView mVisualizerView;
    private ImageButton ib;
    private boolean ib_state = true;
    public AudioPlayer(String FilePath, View view) {
//        FileName = recordFile.getFileName();
//        FileFormat = recordFile.getFileFormat();
//        FilePath = recordFile.getFilePath();
//        FileRecordLength = recordFile.getFileRecordLength();
//        FileCreatedTime = recordFile.getFileCreatedTime();
        this.FilePath = FilePath;
        this.mView = view;
    }

    public void start(){
        try {
            initPlay();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }

        mVisualizerView.clearRenderers();
        mVisualizerView.release();
    }

    private void initPlay() throws IOException {
        destoryMediaPlayer();

        mMediaPlayer = new MediaPlayer();
//        Uri uri = Uri.parse("file://" + FilePath);
//        mMediaPlayer.create(mContext, uri);
        mMediaPlayer.setDataSource(FilePath);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();

                mVisualizerView = mView.findViewById(R.id.visualizerView);
                mVisualizerView.link(mMediaPlayer);
                addBarGraphRenderers();

                ib = mView.findViewById(R.id.imgBtn_state);
                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ib_state) {
                            mMediaPlayer.pause();
                            ib.setImageResource(R.drawable.btn_play);
                        }
                        else {
                            mMediaPlayer.start();
                            ib.setImageResource(R.drawable.btn_pause);
                        }
                    }
                });
            }
        });
    }

    private void destoryMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnPreparedListener(null);
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    private void addBarGraphRenderers()
    {
        Paint paint = new Paint();
        paint.setStrokeWidth(10f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(200, 56, 138, 252));
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(4, paint, false);
        mVisualizerView.addRenderer(barGraphRendererBottom);

//        Paint paint2 = new Paint();
//        paint2.setStrokeWidth(12f);
//        paint2.setAntiAlias(true);
//        paint2.setColor(Color.argb(200, 181, 111, 233));
//        BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(4, paint2, true);
//        mVisualizerView.addRenderer(barGraphRendererTop);
    }

    private void addLineRenderer()
    {
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(1f);
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.argb(88, 0, 128, 255));

        Paint lineFlashPaint = new Paint();
        lineFlashPaint.setStrokeWidth(5f);
        lineFlashPaint.setAntiAlias(true);
        lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
        LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint, true);
        mVisualizerView.addRenderer(lineRenderer);
    }
}
