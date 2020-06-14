package com.aone.recorder.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.DAO
 * @ClassName: DBHelper
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/13 3:36
 * @Desc:
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String CONFIG_TABLE_NAME = "RecordConfig";
    public static final String LIST_TABLE_NAME = "RecordList";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_config_db_sql = "create table if not exists " + CONFIG_TABLE_NAME + " (id integer primary key, AudioSource integer, AudioSamplingRate integer, OutputFormat integer, AudioChannels integer, AudioEncoder integer, AudioEncodingBitRate integer, DefaultFilePath text, OutputFileFormat text)";
        String create_list_db_sql = "create table if not exists " + LIST_TABLE_NAME + " (id integer primary key, FileName text, FileFormat text, FilePath text, FileRecordLength text, FileCreatedTime text, FileDBs text)";
        db.execSQL(create_config_db_sql);
        db.execSQL(create_list_db_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
