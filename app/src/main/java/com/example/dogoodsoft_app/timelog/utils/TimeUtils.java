package com.example.dogoodsoft_app.timelog.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dogoodsoft-app on 2017/11/17.
 */

public class TimeUtils {

    /**
     * 毫秒时间
     * Long类型时间转换成时长
     */
    public static String format(Long time) {
        if (time == null) {
            return null;
        } else {
            Date date = new Date(time);
            long hour = time / (60 * 60 * 1000);
            long minute = (time - hour * 60 * 60 * 1000) / (60 * 1000);
            long second = (time - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
            return (hour == 0 ? "00" : (hour > 10 ? hour : ("0" + hour))) + ":" + (minute == 0 ? "00" : (minute > 10 ? minute : ("0" + minute))) + ":" + (second == 0 ? "00" : (second > 10 ? second : ("0" + second)));
        }
    }

    /**
     * 时间为秒
     * Long类型时间转换成时长
     */
    public static String formatTime(Long time) {
        if (time == null) {
            return null;
        } else {
            Date date = new Date(time);
            long hour = time / (60 * 60);
            long minute = (time - hour * 60 * 60) / 60;
            long second = time - hour * 60 * 60 - minute * 60;
            return (hour == 0 ? "00" : (hour > 10 ? hour : ("0" + hour))) + ":" + (minute == 0 ? "00" : (minute > 10 ? minute : ("0" + minute))) + ":" + (second == 0 ? "00" : (second > 10 ? second : ("0" + second)));

        }

    }

    public static String parseTime(long  l){

        //将Long类型转化为Date
        Date date = new Date(l);

        //将Date类型格式化
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH");
        String dateString = sdf.format(date);
        return dateString;

    }

}
