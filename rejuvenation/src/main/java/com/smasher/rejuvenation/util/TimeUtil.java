package com.smasher.rejuvenation.util;

import android.annotation.SuppressLint;

import com.smasher.core.other.ApplicationContext;
import com.smasher.rejuvenation.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具
 * Created by lijunnan on 2017/10/30.
 */
public class TimeUtil {
    // 一秒钟（毫秒）
    public static final long MILLISECOND_ONE_SECOND = 1000L;
    // 一分钟（毫秒）
    public static final long MILLISECOND_ONE_MINUTE = 60 * MILLISECOND_ONE_SECOND;
    // 一小时（毫秒）
    public static final long MILLISECOND_ONE_HOUR = 60 * MILLISECOND_ONE_MINUTE;
    // 一天（毫秒）
    public static final long MILLISECOND_ONE_DAY = 24 * MILLISECOND_ONE_HOUR;
    // 一个月（毫秒，30天）
    public static final long MILLISECOND_ONE_MONTH = 30 * MILLISECOND_ONE_DAY;
    // 一年（毫秒，365天）
    public static final long MILLISECOND_ONE_YEAR = 365 * MILLISECOND_ONE_DAY;

    private static String getString(int sId) {
        return ApplicationContext.getInstance().getString(sId);
    }

    /**
     * 返回“x月x日”
     **/
    @SuppressLint("DefaultLocale")
    public static String format2(long second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(second);
        return String.format("%1$d%2$s%3$d%4$s", calendar.get(Calendar.MONTH) + 1, getString(R.string.date_month), calendar.get(Calendar.DAY_OF_MONTH), getString(R.string.date_day));
    }

    /**
     * 返回“xxxx年xx月xx日”
     **/
    @SuppressLint("DefaultLocale")
    public static String format3(long second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(second);
        return String.format("%1$d%2$s%3$d%4$s%5$d%6$s",
                calendar.get(Calendar.YEAR), getString(R.string.date_year),
                calendar.get(Calendar.MONTH) + 1, getString(R.string.date_month),
                calendar.get(Calendar.DAY_OF_MONTH), getString(R.string.date_day));
    }

    /**
     * 改版V700加入的时间规范 1（距现在）
     * 仅需要感知发生时间距离现在多久
     * - 刚刚（1分钟内）
     * - xx分钟前/后（1分钟~1小时内）
     * - xx小时前/后（1小时~1天内）
     * - xx天前/后（超过1天，1个月内）
     * - xx个月前/后（超过1个月，1年内）
     * - xx年前/后
     *
     * @param millisecond 毫秒
     */
    @SuppressLint("DefaultLocale")
    public static String format7001(long millisecond) {
        // 时间间隔
        long interval = System.currentTimeMillis() - millisecond;
        // 是否在当前时间前
        boolean beforeCurrent = interval >= 0;
        interval = Math.abs(interval);
        if (interval < MILLISECOND_ONE_MINUTE) {
            // 1分钟内
            return getString(R.string.time_a_moment_ago);
        } else if (interval < MILLISECOND_ONE_HOUR) {
            // 1小时内
            long min = interval / MILLISECOND_ONE_MINUTE;
            return String.format("%1$d%2$s", min, getString(beforeCurrent ? R.string.few_minute_ago : R.string.few_minute_later));
        } else if (interval < MILLISECOND_ONE_DAY) {
            // 1天内
            long hour = interval / MILLISECOND_ONE_HOUR;
            return String.format("%1$d%2$s", hour, getString(beforeCurrent ? R.string.few_hours_ago : R.string.few_hours_later));
        } else if (interval < MILLISECOND_ONE_MONTH) {
            // 1个月内
            long day = interval / MILLISECOND_ONE_DAY;
            return String.format("%1$d%2$s", day, getString(beforeCurrent ? R.string.few_days_ago : R.string.few_days_later));
        } else if (interval < MILLISECOND_ONE_YEAR) {
            // 1年内
            long month = interval / MILLISECOND_ONE_MONTH;
            return String.format("%1$d%2$s", month, getString(beforeCurrent ? R.string.few_month_ago : R.string.few_month_later));
        } else {
            long year = interval / MILLISECOND_ONE_YEAR;
            return String.format("%1$d%2$s", year, getString(beforeCurrent ? R.string.few_year_ago : R.string.few_year_later));
        }
    }

    /**
     * 改版V700加入的时间规范 2
     * 多用于社交类型，高更新频率，即时性
     * - 刚刚（1分钟内）
     * - xx分钟前（1分钟~1小时内）
     * - 今天 xx:xx（1小时~1天内）
     * - 昨天 xx:xx
     * - 前天 xx:xx
     * - xx月xx日 xx:xx （大前天开始，1年内）
     * - xxxx年xx月xx日 （1年前）
     *
     * @param millisecond 毫秒
     */
    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    public static String format7002(long millisecond) {
        // 时间间隔
        long interval = System.currentTimeMillis() - millisecond;
        if (interval < MILLISECOND_ONE_MINUTE) {
            // 1分钟内
            return getString(R.string.time_a_moment_ago);
        } else if (interval < MILLISECOND_ONE_HOUR) {
            // 1小时内
            long min = interval / MILLISECOND_ONE_MINUTE;
            return String.format("%1$d%2$s", min, getString(R.string.few_minute_ago));
        }

        SimpleDateFormat simpleDateFormat;
        Date time = new Date(millisecond);
        // 今天
        long today = System.currentTimeMillis();
        // 昨天
        long yesterday = today - MILLISECOND_ONE_DAY;
        // 前天
        long beforeYesterday = yesterday - MILLISECOND_ONE_DAY;
        if (DateUtil.isToday(millisecond, today)) {
            // 今天 xx:xx
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            return String.format(getString(R.string.time_today), simpleDateFormat.format(time));
        } else if (DateUtil.isToday(millisecond, yesterday)) {
            // 昨天 xx:xx
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            return String.format(getString(R.string.time_yesterday), simpleDateFormat.format(time));
        } else if (DateUtil.isToday(millisecond, beforeYesterday)) {
            // 前天 xx:xx
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            return String.format(getString(R.string.time_the_day_before_yesterday), simpleDateFormat.format(time));
        } else if (DateUtil.isThisYear(millisecond, System.currentTimeMillis())) {
            // 1年内，返回xx月xx日 xx:xx
            return format7003(millisecond);
        } else { // 返回xx年xx月xx日
            return format3(millisecond);
        }
    }

    /**
     * 改版V700加入的时间规范 3
     * 精准时间表达可供追溯和查找
     * - xx月xx日 xx:xx （同年）
     * - xxxx年xx月xx日 xx:xx （1年前）
     *
     * @param millisecond 毫秒
     */
    @SuppressLint("SimpleDateFormat")
    public static String format7003(long millisecond) {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date time = new Date(millisecond);
        if (DateUtil.isThisYear(millisecond, System.currentTimeMillis())) {
            // 1年内
            return String.format("%1$s %2$s", format2(millisecond), simpleDateFormat.format(time));
        } else {
            return String.format("%1$s %2$s", format3(millisecond), simpleDateFormat.format(time));
        }
    }

    /**
     * 改版V700加入的时间规范 4
     * 栏目标题或时间段的表示（时间段）
     * - xx月xx日 (同年）
     * - xxxx年xx月xx日  （1年前）
     *
     * @param millisecond 毫秒
     */
    public static String format7004(long millisecond) {
        if (DateUtil.isThisYear(millisecond, System.currentTimeMillis())) {
            return format2(millisecond);
        } else {
            return format3(millisecond);
        }
    }

    /**
     * 改版V700加入的时间规范 5
     * 栏目标题或时间段的表示（倒计时）
     * - xx天xx时xx分 （大于1天）
     * - xx时xx分xx秒 （小于1天）
     *
     * @param millisecond 毫秒
     */
    @SuppressLint("DefaultLocale")
    public static String format7005Str(long millisecond) {
        long day = millisecond / MILLISECOND_ONE_DAY;
        long hour = (millisecond % MILLISECOND_ONE_DAY) / MILLISECOND_ONE_HOUR;
        long minute = (millisecond % MILLISECOND_ONE_HOUR) / MILLISECOND_ONE_MINUTE;
        long second = (millisecond % MILLISECOND_ONE_MINUTE) / MILLISECOND_ONE_SECOND;

        if (day > 0) {
            return String.format("%1$d%2$s%3$d%4$s%5$d%6$s", day, getString(R.string.date_day_chinese), hour, getString(R.string.time_hour), minute, getString(R.string.time_minute));
        } else {
            return String.format("%1$d%2$s%3$d%4$s%5$d%6$s", hour, getString(R.string.time_hour), minute, getString(R.string.time_minute), second, getString(R.string.time_second));
        }
    }

    /**
     * 改版V700加入的时间规范 5
     * 栏目标题或时间段的表示（倒计时）
     * xx:xx:xx（天:时:分）
     *
     * @param millisecond 毫秒
     */
    public static String format7005Digital(long millisecond) {
        long[] result = new long[3];
        if (millisecond > 0) {
            result[0] = (millisecond % (MILLISECOND_ONE_DAY)) / (MILLISECOND_ONE_HOUR);
            result[1] = (millisecond % (MILLISECOND_ONE_HOUR)) / (MILLISECOND_ONE_MINUTE);
            result[2] = (millisecond % (MILLISECOND_ONE_MINUTE)) / 1000;
        } else {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            long temp = result[i];
            if (temp > -1 && temp < 10) {
                // 一位数，补0
                stringBuilder.append("0").append(temp);
            } else {
                stringBuilder.append(temp);
            }
            if (i != result.length - 1) {
                stringBuilder.append(":");
            }
        }

        return stringBuilder.toString();
    }


    //==========================================================

}