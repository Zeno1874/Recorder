package com.aone.recorder.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aone.recorder.model.RecordFile;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.DAO
 * @ClassName: FileDAO
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/13 22:09
 * @Desc:
 */
public class RecordFileDAO {
    public static final String LIST_TABLE_NAME = "RecordList";

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;

    public RecordFileDAO(Context context) {
        dbHelper = new DBHelper(context, "recorder", null, 1);
        db = dbHelper.getWritableDatabase();
    }

    public void insertFile(RecordFile recordFile) {
        ContentValues values = new ContentValues();
        values.put("FileName", recordFile.getFileName());
        values.put("FileFormat", recordFile.getFileFormat());
        values.put("FilePath", recordFile.getFilePath());
        values.put("FileRecordLength", recordFile.getFileRecordLength());
        values.put("FileCreatedTime", recordFile.getFileCreatedTime());
        values.put("FileDBs", recordFile.getFileDBs());
        db.insert(LIST_TABLE_NAME, null, values);
        close();
    }

    /**
     * 获取录音文件数据集
     *
     * @return Cursor数据集
     */
    public Cursor queryFiles() {
        cursor = db.query(LIST_TABLE_NAME, new String[]{"FileName", "FileFormat", "FilePath", "FileRecordLength", "FileCreatedTime", "FileDBs"}, null, null, null, null, null);
        return cursor;
    }

    /**
     * 删除文件
     *
     * @param FileName String 文件名称
     */
    public void deleteFile(String FileName) {
        String sql = "delete from " + LIST_TABLE_NAME + " where FileName = '" + FileName + "'";
//        db.delete(LIST_TABLE_NAME,"FileName", new String[]{FileName});
        db.execSQL(sql);
        close();
    }

    /**
     * 获取文件总数
     *
     * @return 数据表中的录音记录数量
     */
    public int getFileCount() {
        String sql = "select count(*) from " + LIST_TABLE_NAME;
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        return (int) cursor.getLong(0);
    }

    public void close() {
        if (cursor != null)
            cursor.close();
        if (db != null)
            db.close();
        if (dbHelper != null)
            dbHelper.close();
    }
}
