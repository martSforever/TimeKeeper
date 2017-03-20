package com.martsforever.owa.timekeeper.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 17/03/07.
 */

public class DateUtil {

    public static final int COMPLICATED_DATE = 0x01;
    public static final int SIMPLE_DATE = 0x02;
    public static final int COMPLICATE_DATE_TOW_LINE = 0x03;

    private static SimpleDateFormat complicatedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat complicatedDateTwoLine = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss");
    private static SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * converts the date time
     *
     * @param date
     * @param DATE_MODE
     * @return
     */
    public static String dateToString(Date date, int DATE_MODE) {
        switch (DATE_MODE) {
            case COMPLICATED_DATE:
                return complicatedDate.format(date);
            case SIMPLE_DATE:
                return simpleDate.format(date);
            case COMPLICATE_DATE_TOW_LINE:
                return complicatedDateTwoLine.format(date);
            default:
                System.out.println("date mode is wrong!");
                return "date mode is wrong";
        }
    }

    public static Date getRandomDate() {
        Calendar calendar = Calendar.getInstance();
        //注意月份要减去1
        calendar.set(1990, 11, 31);
        calendar.getTime().getTime();
        //根据需求，这里要将时分秒设置为0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long min = calendar.getTime().getTime();
        calendar.set(2013, 11, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.getTime().getTime();
        long max = calendar.getTime().getTime();
        //得到大于等于min小于max的double值
        double randomDate = Math.random() * (max - min) + min;
        //将double值舍入为整数，转化成long类型
        calendar.setTimeInMillis(Math.round(randomDate));
        return calendar.getTime();
    }

}
