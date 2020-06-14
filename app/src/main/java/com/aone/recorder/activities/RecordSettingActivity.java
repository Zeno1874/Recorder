package com.aone.recorder.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aone.recorder.DAO.RecordConfigDAO;
import com.aone.recorder.MainActivity;
import com.aone.recorder.R;
import com.aone.recorder.adpater.SettingListViewAdapter;
import com.aone.recorder.model.RecordConfig;
import com.aone.recorder.utils.RecordConfigUtil;

import java.util.Arrays;
import java.util.List;

public class RecordSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = RecordSettingActivity.class.getSimpleName();

    private List<String> OutputFileFormatList = Arrays.asList("MP3", "WAVE", "AAC", "AMR");
    private List<String> AudioChannelsList = Arrays.asList("Single", "Double");
    private List<String> AudioSamplingRateList = Arrays.asList("11025Hz", "22050Hz", "24000Hz", "44100Hz", "48000Hz");
    // 录音配置
    private RecordConfigDAO mRecordConfigDAO;
    private RecordConfig mRecordConfig;

    private String dp_outputFileFormat,
                   dp_audioChannels,
                   dp_audioSamplingRate;
    // 控件变量
    private ImageButton imgBtn_setting2record;
    private RelativeLayout rl_setting_outputFileFormat_select,
                           rl_setting_audioChannels_select,
                           rl_setting_audioSamplingRate_select;
    private TextView tv_setting_outputFileFormat,
                     tv_setting_audioChannels,
                     tv_setting_audioSamplingRate;
    private ImageView imgBtn_setting_outputFileFormat,
                      imgBtn_setting_audioChannels,
                      imgBtn_setting_audioSamplingRate;
    private ListView lv_setting_outputFileFormat,
                     lv_setting_audioChannels,
                     lv_setting_audioSamplingRate;
    private boolean lv_setting_outputFileFormat_state = true;
    private boolean lv_setting_audioChannels_state = true;
    private boolean lv_setting_audioSamplingRate_state = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_setting);
        // 初始化数据
        mRecordConfigDAO = new RecordConfigDAO(this);
        mRecordConfig = RecordConfigUtil.getRecordConfig(this);
        initConfig();

        initView();
        setEvent();
    }

    public void setEvent(){
        rl_setting_outputFileFormat_select.setOnClickListener(this);
        rl_setting_audioChannels_select.setOnClickListener(this);
        rl_setting_audioSamplingRate_select.setOnClickListener(this);

        imgBtn_setting2record.setOnClickListener(this);
    }
    private void initView(){
        // 选择框
        rl_setting_outputFileFormat_select = findViewById(R.id.rl_setting_outputFileFormat_select);
        rl_setting_audioChannels_select = findViewById(R.id.rl_setting_audioChannels_select);
        rl_setting_audioSamplingRate_select = findViewById(R.id.rl_setting_audioSamplingRate_select);
        // ListView
        lv_setting_outputFileFormat = findViewById(R.id.lv_setting_outputFileFormat);
        lv_setting_audioChannels = findViewById(R.id.lv_setting_audioChannels);
        lv_setting_audioSamplingRate = findViewById(R.id.lv_setting_audioSamplingRate);
        // ImageButton
        imgBtn_setting_outputFileFormat = findViewById(R.id.imgBtn_setting_outputFileFormat);
        imgBtn_setting_audioChannels = findViewById(R.id.imgBtn_setting_audioChannels);
        imgBtn_setting_audioSamplingRate = findViewById(R.id.imgBtn_setting_audioSamplingRate);
        // 返回主界面
        imgBtn_setting2record = findViewById(R.id.imgBtn_setting2record);
        // TextView
        tv_setting_outputFileFormat = findViewById(R.id.tv_setting_outputFileFormat);
        tv_setting_audioChannels = findViewById(R.id.tv_setting_audioChannels);
        tv_setting_audioSamplingRate = findViewById(R.id.tv_setting_audioSamplingRate);

        tv_setting_outputFileFormat.setText(dp_outputFileFormat);
        tv_setting_audioChannels.setText(dp_audioChannels);
        tv_setting_audioSamplingRate.setText(dp_audioSamplingRate);
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imgBtn_setting2record:
                mRecordConfigDAO.close();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting_outputFileFormat_select:
                if (lv_setting_outputFileFormat_state){
                    imgBtn_setting_outputFileFormat.setImageResource(R.drawable.btn_arrow_down);
                    lv_setting_outputFileFormat.setVisibility(View.VISIBLE);
                    // 设置适配器
                    lv_setting_outputFileFormat.setAdapter(new SettingListViewAdapter(this, OutputFileFormatList));

                    lv_setting_outputFileFormat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_setting_outputFileFormat.setText(OutputFileFormatList.get(position));
                            String value = RecordConfigUtil.encodeOutputFileFormat(OutputFileFormatList.get(position));
                            mRecordConfigDAO.updateConfig("OutputFileFormat", value);
                            imgBtn_setting_outputFileFormat.setImageResource(R.drawable.btn_arrow_right);
                            lv_setting_outputFileFormat.setVisibility(View.GONE);
                        }
                    });
                }else {
                    imgBtn_setting_outputFileFormat.setImageResource(R.drawable.btn_arrow_right);
                    lv_setting_outputFileFormat.setVisibility(View.GONE);
                }
                lv_setting_outputFileFormat_state = !lv_setting_outputFileFormat_state;
                break;
            case R.id.rl_setting_audioChannels_select:
                if (lv_setting_audioChannels_state){
                    imgBtn_setting_audioChannels.setImageResource(R.drawable.btn_arrow_down);
                    lv_setting_audioChannels.setVisibility(View.VISIBLE);
                    // 设置适配器
                    lv_setting_audioChannels.setAdapter(new SettingListViewAdapter(this, AudioChannelsList));

                    lv_setting_audioChannels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_setting_audioChannels.setText(AudioChannelsList.get(position));
                            String value = String.valueOf(RecordConfigUtil.encodeAudioChannels(AudioChannelsList.get(position)));
                            mRecordConfigDAO.updateConfig("AudioChannels", value);
                            imgBtn_setting_audioChannels.setImageResource(R.drawable.btn_arrow_right);
                            lv_setting_audioChannels.setVisibility(View.GONE);
                        }
                    });
                }else {
                    imgBtn_setting_audioChannels.setImageResource(R.drawable.btn_arrow_right);
                    lv_setting_audioChannels.setVisibility(View.GONE);
                }
                lv_setting_audioChannels_state = !lv_setting_audioChannels_state;
                break;
            case R.id.rl_setting_audioSamplingRate_select:
                if (lv_setting_audioSamplingRate_state){
                    imgBtn_setting_audioSamplingRate.setImageResource(R.drawable.btn_arrow_down);
                    lv_setting_audioSamplingRate.setVisibility(View.VISIBLE);
                    // 设置适配器
                    lv_setting_audioSamplingRate.setAdapter(new SettingListViewAdapter(this, AudioSamplingRateList));

                    lv_setting_audioSamplingRate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_setting_audioSamplingRate.setText(AudioSamplingRateList.get(position));
                            String value = String.valueOf(RecordConfigUtil.encodeAudioSamplingRate(AudioSamplingRateList.get(position)));
                            mRecordConfigDAO.updateConfig("AudioSamplingRate", value);
                            imgBtn_setting_audioSamplingRate.setImageResource(R.drawable.btn_arrow_right);
                            lv_setting_audioSamplingRate.setVisibility(View.GONE);
                        }
                    });
                }else {
                    imgBtn_setting_audioSamplingRate.setImageResource(R.drawable.btn_arrow_right);
                    lv_setting_audioSamplingRate.setVisibility(View.GONE);
                }
                lv_setting_audioSamplingRate_state = !lv_setting_audioSamplingRate_state;
                break;
        }
    }

    public void initConfig(){
        dp_outputFileFormat = RecordConfigUtil.decodeOutputFileFormat(mRecordConfig.getOutputFileFormat());
        dp_audioChannels = RecordConfigUtil.decodeAudioChannels(mRecordConfig.getAudioChannels());
        dp_audioSamplingRate = RecordConfigUtil.decodeAudioSamplingRate(mRecordConfig.getAudioSamplingRate());
    }

}