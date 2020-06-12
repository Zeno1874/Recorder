package com.aone.recorder.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aone.recorder.R;

import java.util.List;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.adpater
 * @ClassName: SettingListViewAdapter
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/13 4:30
 * @Desc:
 */
public class SettingListViewAdapter extends BaseAdapter {

    private List<String> data;
    private LayoutInflater inflater;

    public SettingListViewAdapter(Context context, List<String> data){
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_setting_listview, parent,false);
            viewHolder.tv = convertView.findViewById(R.id.tv_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv.setText(data.get(position));
        return convertView;
    }

    private class ViewHolder{
        private TextView tv;
    }
}
