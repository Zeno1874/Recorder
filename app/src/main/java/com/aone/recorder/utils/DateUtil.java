package com.aone.recorder.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.utils
 * @ClassName: DateUtil
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/13 18:07
 * @Desc:
 */
public class DateUtil {
    private static final String DATE_FORMAT_FOR_FILE_NAME = "yyyy年MM月dd日HH时mm分ss秒";
    private static final String DATE_FORMAT_FOR_FILE_CREATED_TIME = "yyyy/MM/dd";

    @SuppressLint("SimpleDateFormat")
    public String getDateTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_FOR_FILE_NAME);
        return dateFormat.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public String strFormatTrans(String time) {
        DateFormat oldFormat = new SimpleDateFormat(DATE_FORMAT_FOR_FILE_NAME);
        Date date = null;
        try {
            date = oldFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat(DATE_FORMAT_FOR_FILE_CREATED_TIME);
        assert date != null;
        return newFormat.format(date);
    }
}
