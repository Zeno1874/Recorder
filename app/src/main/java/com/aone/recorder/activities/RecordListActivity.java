package com.aone.recorder.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aone.recorder.DAO.RecordFileDAO;
import com.aone.recorder.MainActivity;
import com.aone.recorder.R;
import com.aone.recorder.adapter.ListListViewAdapter;
import com.aone.recorder.model.RecordFile;

import java.util.ArrayList;
import java.util.List;

public class RecordListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = RecordSettingActivity.class.getSimpleName();

    // 录音配置
    private RecordFileDAO mRecordFileDAO;
    private RecordFile mRecordFile;
    private List<RecordFile> mRecordFiles;

    // 控件
    private ImageButton imgBtn_list2record,
            imgBtn_menu;
    private ListView lv_record_list;
    private TextView tv_notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        initView();
        setEvent();

        mRecordFileDAO = new RecordFileDAO(this);
        int FileCount = mRecordFileDAO.getFileCount();


        if (FileCount != 0) {
            initData();
            lv_record_list.setAdapter(new ListListViewAdapter(this, mRecordFiles));
            lv_record_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                    LinearLayout ll_SimpleDetail = view.findViewById(R.id.ll_SimpleDetail);
                    switch (ll_SimpleDetail.getVisibility()) {
                        case View.GONE:
                            ll_SimpleDetail.setVisibility(View.VISIBLE);
                            break;
                        case View.VISIBLE:
                            ll_SimpleDetail.setVisibility(View.GONE);
                            break;
                        case View.INVISIBLE:
                            break;
                    }
                }
            });
        }

        lv_record_list.setEmptyView(tv_notice);

    }

    private void setEvent() {
        imgBtn_list2record.setOnClickListener(this);
        imgBtn_menu.setOnClickListener(this);
    }

    private void initView() {
        imgBtn_list2record = findViewById(R.id.imgBtn_list2record);
        imgBtn_menu = findViewById(R.id.imgBtn_menu);
        lv_record_list = findViewById(R.id.lv_record_list);
        tv_notice = findViewById(R.id.tv_notice);

    }

    public void initData() {
        Cursor cursor = mRecordFileDAO.queryFiles();
        mRecordFiles = new ArrayList<>();
        while (cursor.moveToNext()) {
            mRecordFile = new RecordFile();
            mRecordFile.setFileName(cursor.getString(cursor.getColumnIndex("FileName")));
            mRecordFile.setFileFormat(cursor.getString(cursor.getColumnIndex("FileFormat")));
            mRecordFile.setFilePath(cursor.getString(cursor.getColumnIndex("FilePath")));
            mRecordFile.setFileRecordLength(cursor.getString(cursor.getColumnIndex("FileRecordLength")));
            mRecordFile.setFileCreatedTime(cursor.getString(cursor.getColumnIndex("FileCreatedTime")));
            mRecordFile.setFileDBs(cursor.getString(cursor.getColumnIndex("FileDBs")));
            mRecordFiles.add(mRecordFile);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_list2record:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.imgBtn_menu:
                openOptionsMenu();
                Log.e(TAG,"Menu Button onClick");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.list_menu,menu);
        Log.e(TAG,"Menu Display");
        return true;
    }
}

