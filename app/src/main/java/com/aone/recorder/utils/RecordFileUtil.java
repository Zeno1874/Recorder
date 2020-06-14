package com.aone.recorder.utils;

import android.content.Context;
import android.database.Cursor;

import com.aone.recorder.DAO.RecordFileDAO;
import com.aone.recorder.model.RecordFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.utils
 * @ClassName: RecordFileUtil
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/14 1:23
 * @Desc:
 */
public class RecordFileUtil {
    public static List<RecordFile> getListData(Context context){
        RecordFileDAO mRecordFileDAO = new RecordFileDAO(context);
        Cursor cursor = mRecordFileDAO.queryFiles();
        List<RecordFile> mRecordFiles = new ArrayList<>();
        RecordFile mRecordFile;
        while (cursor.moveToNext()){
            mRecordFile = new RecordFile();
            mRecordFile.setFileName(cursor.getString(cursor.getColumnIndex("FileName")));
            mRecordFile.setFileFormat(cursor.getString(cursor.getColumnIndex("FileFormat")));
            mRecordFile.setFilePath(cursor.getString(cursor.getColumnIndex("FilePath")));
            mRecordFile.setFileRecordLength(cursor.getString(cursor.getColumnIndex("FileRecordLength")));
            mRecordFile.setFileCreatedTime(cursor.getString(cursor.getColumnIndex("FileCreatedTime")));

            mRecordFiles.add(mRecordFile);
        }
        return mRecordFiles;
    }

    public void decodeMP3(){

    }
}
