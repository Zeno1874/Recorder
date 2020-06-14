package com.aone.recorder.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
    @SuppressLint("SimpleDateFormat")
    public String getDateTime(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        return dateFormat.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public String strFormatTrans(String time) throws ParseException {
        DateFormat oldFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        Date date = oldFormat.parse(time);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd");
        return newFormat.format(date);
    }

    /**
     * 格式化String类型时间
     * @param context
     * @param dateTime 输入格式："yyyy/MM/dd"
     * @return 输出格式：2020年6月14日 上午8:00
     * @throws ParseException
     */
    public String dateFormat(Context context, String dateTime) throws ParseException {

        DateFormat iso8601Format = new SimpleDateFormat("yyyy/MM/dd");

        Date date = iso8601Format.parse(dateTime);

        long when = date.getTime();
        int flags = 0;
        flags |= DateUtils.FORMAT_SHOW_TIME;
        flags |= DateUtils.FORMAT_SHOW_DATE;
        flags |= DateUtils.FORMAT_ABBREV_MONTH;
        flags |= DateUtils.FORMAT_SHOW_YEAR;

        String finalDateTime = android.text.format.DateUtils.formatDateTime(context,
                when + TimeZone.getDefault().getOffset(when), flags);

        return finalDateTime;
    }
}
