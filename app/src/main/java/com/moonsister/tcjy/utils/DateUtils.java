//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.moonsister.tcjy.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final long INTERVAL_IN_MILLISECONDS = 30000L;

    public DateUtils() {
    }

    public static String getTimestampString(Date var0) {
        String var1 = null;
        String var2 = Locale.getDefault().getLanguage();
        boolean var3 = var2.startsWith("zh");
        long var4 = var0.getTime();
        if (isSameDay(var4)) {
            var1 = "HH:mm";
        } else if (isYesterday(var4)) {
            if (!var3) {
                return "Yesterday " + (new SimpleDateFormat("HH:mm", Locale.ENGLISH)).format(var0);
            }

            var1 = "昨天 HH:mm";
        } else if (var3) {
            var1 = "M月d日 HH:mm";
        } else {
            var1 = "MMM dd HH:mm";
        }

        return var3 ? (new SimpleDateFormat(var1, Locale.CHINESE)).format(var0) : (new SimpleDateFormat(var1, Locale.ENGLISH)).format(var0);
    }

    public static boolean isCloseEnough(long var0, long var2) {
        long var4 = var0 - var2;
        if (var4 < 0L) {
            var4 = -var4;
        }

        return var4 < 30000L;
    }

    private static boolean isSameDay(long var0) {
        TimeInfo var2 = getTodayStartAndEndTime();
        return var0 > var2.getStartTime() && var0 < var2.getEndTime();
    }

    private static boolean isYesterday(long var0) {
        TimeInfo var2 = getYesterdayStartAndEndTime();
        return var0 > var2.getStartTime() && var0 < var2.getEndTime();
    }

    public static Date StringToDate(String var0, String var1) {
        SimpleDateFormat var2 = new SimpleDateFormat(var1);
        Date var3 = null;

        try {
            var3 = var2.parse(var0);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return var3;
    }

    public static String toTime(long var0) {
        var0 /= 1000;
        long var1 = var0 / 60;
        boolean var2 = false;
        if (var1 >= 60) {
            long var4 = var1 / 60;
            var1 %= 60;
        }

        long var3 = var0 % 60;
        return String.format("%02d:%02d", new Object[]{Long.valueOf(var1), Long.valueOf(var3)});
    }

    public static String toTimeBySecond(int var0) {
        int var1 = var0 / 60;
        boolean var2 = false;
        if (var1 >= 60) {
            int var4 = var1 / 60;
            var1 %= 60;
        }

        int var3 = var0 % 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(var1), Integer.valueOf(var3)});
    }

    //
    public static TimeInfo getYesterdayStartAndEndTime() {
        Calendar var0 = Calendar.getInstance();
        var0.add(Calendar.DAY_OF_MONTH, -1);
        var0.set(Calendar.HOUR_OF_DAY, 0);
        var0.set(Calendar.MINUTE, 0);
        var0.set(Calendar.SECOND, 0);
        var0.set(Calendar.MILLISECOND, 0);
        Date var1 = var0.getTime();
        long var2 = var1.getTime();
        Calendar var4 = Calendar.getInstance();
        var4.add(Calendar.DAY_OF_MONTH, -1);
        var4.set(Calendar.HOUR_OF_DAY, 23);
        var4.set(Calendar.MINUTE, 59);
        var4.set(Calendar.SECOND, 59);
        var4.set(Calendar.MILLISECOND, 999);
        Date var5 = var4.getTime();
        long var6 = var5.getTime();
        TimeInfo var8 = new TimeInfo();
        var8.setStartTime(var2);
        var8.setEndTime(var6);
        return var8;
    }

    public static TimeInfo getTodayStartAndEndTime() {
        Calendar var0 = Calendar.getInstance();
        var0.set(Calendar.HOUR_OF_DAY, 0);
        var0.set(Calendar.MINUTE, 0);
        var0.set(Calendar.SECOND, 0);
        var0.set(Calendar.MILLISECOND, 0);
        Date var1 = var0.getTime();
        long var2 = var1.getTime();
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
        Calendar var5 = Calendar.getInstance();
        var5.set(Calendar.HOUR_OF_DAY, 23);
        var5.set(Calendar.MINUTE, 59);
        var5.set(Calendar.SECOND, 59);
        var5.set(Calendar.MILLISECOND, 999);
        Date var6 = var5.getTime();
        long var7 = var6.getTime();
        TimeInfo var9 = new TimeInfo();
        var9.setStartTime(var2);
        var9.setEndTime(var7);
        return var9;
    }
//
//    public static TimeInfo getBeforeYesterdayStartAndEndTime() {
//        Calendar var0 = Calendar.getInstance();
//        var0.add(5, -2);
//        var0.set(11, 0);
//        var0.set(12, 0);
//        var0.set(13, 0);
//        var0.set(14, 0);
//        Date var1 = var0.getTime();
//        long var2 = var1.getTime();
//        Calendar var4 = Calendar.getInstance();
//        var4.add(5, -2);
//        var4.set(11, 23);
//        var4.set(12, 59);
//        var4.set(13, 59);
//        var4.set(14, 999);
//        Date var5 = var4.getTime();
//        long var6 = var5.getTime();
//        TimeInfo var8 = new TimeInfo();
//        var8.setStartTime(var2);
//        var8.setEndTime(var6);
//        return var8;
//    }
//
//    public static TimeInfo getCurrentMonthStartAndEndTime() {
//        Calendar var0 = Calendar.getInstance();
//        var0.set(5, 1);
//        var0.set(11, 0);
//        var0.set(12, 0);
//        var0.set(13, 0);
//        var0.set(14, 0);
//        Date var1 = var0.getTime();
//        long var2 = var1.getTime();
//        Calendar var4 = Calendar.getInstance();
//        Date var5 = var4.getTime();
//        long var6 = var5.getTime();
//        TimeInfo var8 = new TimeInfo();
//        var8.setStartTime(var2);
//        var8.setEndTime(var6);
//        return var8;
//    }
//
//    public static TimeInfo getLastMonthStartAndEndTime() {
//        Calendar var0 = Calendar.getInstance();
//        var0.add(2, -1);
//        var0.set(5, 1);
//        var0.set(11, 0);
//        var0.set(12, 0);
//        var0.set(13, 0);
//        var0.set(14, 0);
//        Date var1 = var0.getTime();
//        long var2 = var1.getTime();
//        Calendar var4 = Calendar.getInstance();
//        var4.add(2, -1);
//        var4.set(5, 1);
//        var4.set(11, 23);
//        var4.set(12, 59);
//        var4.set(13, 59);
//        var4.set(14, 999);
//        var4.roll(5, -1);
//        Date var5 = var4.getTime();
//        long var6 = var5.getTime();
//        TimeInfo var8 = new TimeInfo();
//        var8.setStartTime(var2);
//        var8.setEndTime(var6);
//        return var8;
//    }

    public static String getTimestampStr() {
        return Long.toString(System.currentTimeMillis());
    }
}
