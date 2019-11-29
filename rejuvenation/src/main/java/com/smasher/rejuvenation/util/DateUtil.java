package com.smasher.rejuvenation.util;

import android.text.format.DateFormat;
import android.text.format.Time;

import androidx.annotation.StringDef;

import com.smasher.core.other.ApplicationContext;
import com.smasher.rejuvenation.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author moyu
 * @date 2017/3/17
 */
public class DateUtil {
    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    public static final String FORMAT_1 = "yyyy-MM-dd";
    public static final String FORMAT_2 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_3 = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String FORMAT_4 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_5 = "MM.dd";

    @StringDef({FORMAT_1, FORMAT_2, FORMAT_3, FORMAT_4, FORMAT_5})
    @Retention(RetentionPolicy.SOURCE)
    @interface FormatString {
    }


    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY && interval > -1L * MILLIS_IN_DAY && toDay(ms1) == toDay(ms2);
    }


    public static boolean isToday(long when, long today) {
        Time time = new Time();
        time.set(when);
        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(today);
        return (thenYear == time.year) && (thenMonth == time.month) && (thenMonthDay == time.monthDay);
    }

    public static boolean isThisYear(long when, long today) {
        Time time = new Time();
        time.set(when);

        int thenYear = time.year;

        time.set(today);
        return thenYear == time.year;
    }


    /**
     * 格式化时间
     *
     * @param time    time
     * @param pattern pattern
     * @return String
     */
    public static String getTime(long time, @FormatString String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        dateFormat.applyPattern(pattern);
        Date date = new Date(time);
        return dateFormat.format(date);
    }


    /**
     * 当前时间之前
     */
    public static String formatTimeAgo(long time) {
        long nowTime = System.currentTimeMillis();
        int temp = (int) (nowTime - time) / 1000;
        if (temp < 60 && temp >= 0) {
            return ApplicationContext.getInstance().getString(R.string.time_in_minute);
        } else if (temp < 60 * 60 && temp >= 0) {
            //小时内
            int min = temp / 60;
            return min + ApplicationContext.getInstance().getString(R.string.few_minute_ago);
        } else if (temp < 60 * 60 * 24 && temp >= 0) {
            //天内
            int hour = (temp / (60 * 60));
            return hour + ApplicationContext.getInstance().getString(R.string.few_hours_ago);
        } else if (temp < 60 * 60 * 24 * 30 && temp >= 0) {
            //月内
            int day = (temp / (60 * 60 * 24));
            return day + ApplicationContext.getInstance().getString(R.string.few_days_ago);
        } else {
            return DateFormat.format(ApplicationContext.getInstance().getString(R.string.date_format), time).toString();
        }
    }


    /**
     * 当前时间之后
     */
    public static String formatTimeAfter(long time) {
        long temp = time - System.currentTimeMillis();
        int n = (int) (temp / 1000L);
        if (n > (60 * 60 * 24)) {
            long day = n / (60 * 60 * 24) + 1;
            return day + ApplicationContext.getInstance().getString(R.string.few_days_later);
        } else if (n > (60 * 60)) {
            long hour = n / (60 * 60);
            return hour + ApplicationContext.getInstance().getString(R.string.few_hours_later);
        } else if (n > 60) {
            long min = n / 60;
            return min + ApplicationContext.getInstance().getString(R.string.few_minute_later);
        }
        return ApplicationContext.getInstance().getString(R.string.date_deprecated);
    }


    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }


    public static Date parseDate(String time, String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            return dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
