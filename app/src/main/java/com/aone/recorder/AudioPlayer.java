package com.aone.recorder;

import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;


import com.aone.recorder.views.VisualizerView;
import com.aone.recorder.views.render.BarGraphRenderer;

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

    private String FilePath;

    private View mView;
    private VisualizerView mVisualizerView;
    private ImageButton imgBtn;
    private boolean imgBtn_state = true;

    public AudioPlayer(String FilePath, View view) {
        this.FilePath = FilePath;
        this.mView = view;
    }

    public void start() {
        try {
            initPlay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }

        mVisualizerView.clearRenderers();
        mVisualizerView.release();
    }

    private void initPlay() throws IOException {
        destroyMediaPlayer();

        mMediaPlayer = new MediaPlayer();
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

                imgBtn = mView.findViewById(R.id.imgBtn_state);
                imgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (imgBtn_state) {
                            mMediaPlayer.pause();
                            imgBtn.setImageResource(R.drawable.btn_play);
                        } else {
                            mMediaPlayer.start();
                            imgBtn.setImageResource(R.drawable.btn_pause);
                        }
                    }
                });
            }
        });
    }

    private void destroyMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnPreparedListener(null);
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    private void addBarGraphRenderers() {
        Paint paint = new Paint();
        paint.setStrokeWidth(10f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(200, 56, 138, 252));
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(4, paint, false);
        mVisualizerView.addRenderer(barGraphRendererBottom);
    }

}
