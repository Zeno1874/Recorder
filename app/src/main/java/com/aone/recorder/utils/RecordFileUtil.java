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
    public static List<RecordFile> getListData(Context context) {
        RecordFileDAO mRecordFileDAO = new RecordFileDAO(context);
        Cursor cursor = mRecordFileDAO.queryFiles();
        List<RecordFile> mRecordFiles = new ArrayList<>();
        RecordFile mRecordFile;
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
        return mRecordFiles;
    }

    public static void addFileRecord(Context context, RecordFile recordFile) {
        RecordFileDAO mRecordFileDAO = new RecordFileDAO(context);
        mRecordFileDAO.insertFile(recordFile);
        mRecordFileDAO.close();
    }

    public static List<Double> getDB(String strRecordDBs) {

        List<Double> RecordDBs = new ArrayList<>();
        List<Double> temp = new ArrayList<>();
        List<Double> finalDBs = new ArrayList<>();
        if (null == strRecordDBs) {
            finalDBs.add(0.0);
            return finalDBs;
        }
        String[] strs = strRecordDBs.split(" ");
        int max = 0;
        for (String str : strs) {
            if (max <= Integer.parseInt(str))
                max = Integer.parseInt(str);
            RecordDBs.add(Double.parseDouble(str));
        }

        if (max == 0) {
            return RecordDBs;
        }

        if (RecordDBs.size() < 100) {
            for (int i = 0; i < RecordDBs.size(); i++) {
                double item = RecordDBs.get(i) / max;
                finalDBs.add(item);
            }
            return finalDBs;
        }
        int DBsCount = RecordDBs.size();
        double finalCount = 100.0;
        int each = (int) Math.floor(DBsCount / finalCount);
        for (int i = 0; i < RecordDBs.size(); i++) {
            temp.add(RecordDBs.get(i) * 1.0 / max);
            if (i % each == 0) {
                double sum = 0.0;
                for (double item : temp)
                    sum += item;
                double avg = sum / temp.size();
                finalDBs.add(avg);
                temp.clear();
            }

            if (i == RecordDBs.size() - 1) {
                double sum = 0.0;
                for (double item : temp)
                    sum += item;
                double avg = sum / temp.size();
                finalDBs.add(avg);
                temp.clear();
            }
        }
        return finalDBs;
    }
}
