package com.aone.recorder.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aone.recorder.DAO.RecordFileDAO;
import com.aone.recorder.R;
import com.aone.recorder.model.RecordFile;
import com.aone.recorder.utils.FileUtil;
import com.aone.recorder.utils.RecordFileUtil;
import com.aone.recorder.views.StaticWaveView;
import com.aone.recorder.views.VisualizerView;

import java.util.List;

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
    private LayoutInflater inflater;

    private String mFileName,
                   mFilePath;
    public ListListViewAdapter(Context context, List<RecordFile> data){
        mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_list_listview, parent,false);
            viewHolder.tv_file_name = convertView.findViewById(R.id.tv_file_name);
            viewHolder.tv_file_record_length = convertView.findViewById(R.id.tv_file_record_length);
            viewHolder.tv_file_created_time = convertView.findViewById(R.id.tv_file_created_time);

            viewHolder.ll_SimpleDetail = convertView.findViewById(R.id.ll_SimpleDetail);
            viewHolder.imgBtn_state = convertView.findViewById(R.id.imgBtn_state);
            viewHolder.tv_delete = convertView.findViewById(R.id.tv_delete);
            viewHolder.tv_rename = convertView.findViewById(R.id.tv_rename);
            viewHolder.ll_more = convertView.findViewById(R.id.more);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.visualizerView = convertView.findViewById(R.id.visualizerView);



        viewHolder.tv_file_name.setText(data.get(position).getFileName());
        viewHolder.tv_file_record_length.setText(data.get(position).getFileRecordLength());
        viewHolder.tv_file_created_time.setText(data.get(position).getFileCreatedTime());

        viewHolder.tv_delete.setOnClickListener(this);
        viewHolder.tv_delete.setTag(position);

        viewHolder.tv_rename.setOnClickListener(this);
        viewHolder.ll_more.setOnClickListener(this);

//        viewHolder.staticWaveView = convertView.findViewById(R.id.staticWaveView);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        RecordFileDAO mRecordFileDAO = new RecordFileDAO(mContext);

        switch (v.getId()) {
            case R.id.tv_delete:
                int chosenPosition = (Integer) v.getTag();
                RecordFile recordFile = (RecordFile)getItem(chosenPosition);

                mFileName = recordFile.getFileName();
                mFilePath = recordFile.getFilePath();

                // 删除记录及文件
                mRecordFileDAO.deleteFile(mFileName);
                FileUtil.deleteFile(mFilePath);
                // 更新数据
                data = RecordFileUtil.getListData(mContext);
                this.notifyDataSetChanged();

                break;
            case R.id.tv_rename:

            case R.id.more:
                break;
        }
    }

    private static class ViewHolder{
        private TextView tv_file_name;
        private TextView tv_file_created_time;
        private TextView tv_file_record_length;

        private LinearLayout ll_SimpleDetail;
        private ImageButton imgBtn_state;
        private TextView tv_delete;
        private TextView tv_rename;
        private LinearLayout ll_more;
        private VisualizerView visualizerView;
    }


}
