package com.aone.recorder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aone.recorder.AudioPlayer;
import com.aone.recorder.DAO.RecordFileDAO;
import com.aone.recorder.MainActivity;
import com.aone.recorder.R;
import com.aone.recorder.model.RecordFile;
import com.aone.recorder.utils.FileUtil;
import com.aone.recorder.utils.RecordFileUtil;
import com.aone.recorder.views.StaticWaveView;
import com.aone.recorder.views.VisualizerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.adpater
 * @ClassName: ListListAdapter
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/13 22:03
 * @Desc:
 */
public class ListListViewAdapter extends BaseAdapter implements View.OnClickListener {
    private static final String TAG = ListListViewAdapter.class.getSimpleName();
    private Context mContext;
    private List<RecordFile> data;
    private List<TextView> TimerTextViews;
    private List<ImageButton> PlayImageButtons;
    private List<ImageButton> PauseImageButtons;
    private List<View> Views;
    private LayoutInflater inflater;

    private AudioPlayer mAudioPlayer;
    private int play_id;
    public static String timerStr;
    public ListListViewAdapter(Context context, List<RecordFile> data) {
        mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.PlayImageButtons = new ArrayList<>();
        this.PauseImageButtons = new ArrayList<>();
        this.Views = new ArrayList<>();
this.TimerTextViews = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RecordFile getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_listview, parent, false);

            viewHolder.ll_simple_detail = convertView.findViewById(R.id.ll_SimpleDetail);

            viewHolder.imgBtn_play_state = convertView.findViewById(R.id.imgBtn_play_state);
            viewHolder.imgBtn_pause_state = convertView.findViewById(R.id.imgBtn_pause_state);

            viewHolder.staticWaveView = convertView.findViewById(R.id.staticWaveView);
            viewHolder.visualizerView = convertView.findViewById(R.id.visualizerView);

            viewHolder.tv_file_name = convertView.findViewById(R.id.tv_file_name);
            viewHolder.tv_file_record_length = convertView.findViewById(R.id.tv_file_record_length);
            viewHolder.tv_file_created_time = convertView.findViewById(R.id.tv_file_created_time);
            viewHolder.tv_file_record_length2 = convertView.findViewById(R.id.tv_file_record_length_timer);
            viewHolder.tv_delete = convertView.findViewById(R.id.tv_delete);
            viewHolder.tv_rename = convertView.findViewById(R.id.tv_rename);
            viewHolder.ll_more = convertView.findViewById(R.id.more);

            TimerTextViews.add(viewHolder.tv_file_record_length);
            PlayImageButtons.add(viewHolder.imgBtn_play_state);
            PauseImageButtons.add(viewHolder.imgBtn_pause_state);
            convertView.setTag(viewHolder);
            Views.add(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Item info
        viewHolder.tv_file_name.setText(data.get(position).getFileName());
        viewHolder.tv_file_record_length.setText(data.get(position).getFileRecordLength());
        viewHolder.tv_file_created_time.setText(data.get(position).getFileCreatedTime());
        viewHolder.tv_file_record_length2.setText(data.get(position).getFileRecordLength());

        List<Double> dbs = RecordFileUtil.getDB(data.get(position).getFileDBs());
        viewHolder.staticWaveView.setData(dbs);

        // play & pause
        viewHolder.imgBtn_play_state.setOnClickListener(this);
        viewHolder.imgBtn_pause_state.setOnClickListener(this);

        viewHolder.imgBtn_play_state.setTag(position);
        viewHolder.imgBtn_pause_state.setTag(position);
        // VisualizerView
        viewHolder.visualizerView.setTag(position);
        // Item Controller
        viewHolder.tv_delete.setOnClickListener(this);
        viewHolder.tv_rename.setOnClickListener(this);
        viewHolder.ll_more.setOnClickListener(this);

        viewHolder.tv_delete.setTag(position);
        viewHolder.tv_rename.setTag(position);
        viewHolder.ll_more.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        RecordFileDAO mRecordFileDAO = new RecordFileDAO(mContext);
        final int position;
        switch (v.getId()) {
            case R.id.imgBtn_play_state:
                position = (Integer) v.getTag();
                String filePath = getItem(position).getFilePath();
                if (mAudioPlayer != null) {
                    mAudioPlayer.stop();
                    mAudioPlayer.release();
                    mAudioPlayer = null;
                    showPlayImageButton(play_id);
                }
                timerStr = data.get(position).getFileRecordLength();
                mAudioPlayer = new AudioPlayer(filePath, timerStr, Views.get(position));
                mAudioPlayer.start();

                showPauseImageButton(position);
                play_id = position;
                break;
            case R.id.imgBtn_pause_state:
                position = (Integer) v.getTag();
//                if (null != mAudioPlayer && mAudioPlayer.isPlaying()){
//                    mAudioPlayer.stop();
//                }
                mAudioPlayer.stop();
                mAudioPlayer.release();
                mAudioPlayer = null;
                TimerTextViews.get(position).setText(data.get(position).getFileRecordLength());
                showPlayImageButton(position);

                break;
            case R.id.tv_delete:
                position = (Integer) v.getTag();
                RecordFile recordFile = (RecordFile) getItem(position);
                // 删除记录及文件
                mRecordFileDAO.deleteFile(recordFile.getFileName());
                FileUtil.deleteFile(recordFile.getFilePath());
                // 更新数据
                data = RecordFileUtil.getListData(mContext);
                this.notifyDataSetChanged();
                for (View view : Views)
                    view.findViewById(R.id.ll_SimpleDetail).setVisibility(View.GONE);
                break;
            case R.id.tv_rename:
            case R.id.more:
                break;
        }
    }

    private static class ViewHolder {
        private LinearLayout ll_simple_detail;

        private ImageButton imgBtn_play_state,
                imgBtn_pause_state;

        private TextView tv_file_name,
                tv_file_created_time,
                tv_file_record_length,
                tv_file_record_length2;

        private TextView tv_delete;
        private TextView tv_rename;
        private LinearLayout ll_more;

        private VisualizerView visualizerView;
        private StaticWaveView staticWaveView;
    }

    private void showPlayImageButton(int position) {
        PlayImageButtons.get(position).setVisibility(View.VISIBLE);
        PauseImageButtons.get(position).setVisibility(View.GONE);
    }

    private void showPauseImageButton(int position) {
        PlayImageButtons.get(position).setVisibility(View.GONE);
        PauseImageButtons.get(position).setVisibility(View.VISIBLE);
    }
}
