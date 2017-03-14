package com.martsforever.owa.timekeeper.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 17/03/07.
 */

public class DateUtil {

    public static final int COMPLICATED_DATE = 0x01;
    public static final int SIMPLE_DATE = 0x02;

    private static SimpleDateFormat complicatedDate = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private static SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");

    public static String dateToString(Date date, int DATE_MODE) {
        switch (DATE_MODE) {
            case COMPLICATED_DATE:
                return complicatedDate.format(date);
            case SIMPLE_DATE:
                return simpleDate.format(date);
            default:
                System.out.println("date mode is wrong!");
                return "date mode is wrong";
        }
    }
}
