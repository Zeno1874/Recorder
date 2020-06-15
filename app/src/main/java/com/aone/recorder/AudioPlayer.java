package com.aone.recorder;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aone.recorder.adapter.ListListViewAdapter;
import com.aone.recorder.views.VisualizerView;
import com.aone.recorder.views.render.BarGraphRenderer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

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
public class AudioPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private static final String TAG = AudioPlayer.class.getSimpleName();
    private MediaPlayer mMediaPlayer;

    private Timer timer;
    private long baseTimer;
    private String FilePath;
    private String timerStr;
    private View mView;

    private VisualizerView mVisualizerView;
    private Visualizer mVisualizer;
    private ImageButton mImageButtonPlay,
            mImageButtonPause;
    private TextView mTimerTextView;
    public AudioPlayer(String FilePath, String timerStr, View view) {
        this.FilePath = FilePath;
        this.timerStr = timerStr;
        this.mView = view;
        mVisualizerView = mView.findViewById(R.id.visualizerView);
        mImageButtonPlay = mView.findViewById(R.id.imgBtn_play_state);
        mImageButtonPause = mView.findViewById(R.id.imgBtn_pause_state);
        mTimerTextView = mView.findViewById(R.id.tv_file_record_length);

    }

    public void start() {
        try {
            initPlay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
        if (timer != null)
            timer.cancel();
        mTimerTextView.setText(timerStr);
        showPlayImageButton();
    }

    private void initPlay() throws IOException {
//        destroyMediaPlayer();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(FilePath);
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.prepare();
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();

                timer = new Timer();
                baseTimer = SystemClock.elapsedRealtime();
                @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mTimerTextView.setText((String) msg.obj);
                    }
                };
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        int time = (int) ((SystemClock.elapsedRealtime() - baseTimer) / 1000);
                        String hh = new DecimalFormat("00").format(time / 3600);
                        String mm = new DecimalFormat("00").format(time % 3600 / 60);
                        String ss = new DecimalFormat("00").format(time % 60);
                        String timeFormat = hh + ":" + mm + ":" + ss;
                        Message msg = new Message();
                        msg.obj = timeFormat;
                        handler.sendMessage(msg);
                    }
                };
                timer.schedule(task, 0, 1000L);

                mVisualizerView.link(mVisualizer);
                addBarGraphRenderers();
            }
        });

        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
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

    private void showPlayImageButton(){
        mImageButtonPlay.setVisibility(View.VISIBLE);
        mImageButtonPause.setVisibility(View.GONE);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stop();
//        mVisualizerView.flash();
        mVisualizerView.release();
        Log.e(TAG,"Media Play Completed");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG,"Media Play Error Code: " + what);
        return false;
    }
}
