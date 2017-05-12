package com.martsforever.owa.timekeeper.main.todo.festival;

import java.util.Calendar;
import java.util.List;

/**
 * Created by owa on 2017/1/9.
 */

public class CalendarUtil {
    public static List<CalendarItem> getCurrentCalendarItems(int year, int month, List<CalendarItem> calendarItems) {

        /*添加当前月*/
        for (int i = 0; i < getDaysByYearMonth(year, month); i++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR,year);
            cal.set(Calendar.MONTH,month-1);
            cal.set(Calendar.DATE,i+1);
            Lunar lunar = new Lunar(cal);
            CalendarItem item = new CalendarItem(i + 1, lunar.getLunarMonthAndDay(), true,month+"-"+(i+1));
            calendarItems.add(item);
        }
        return calendarItems;
    }

    public static List<CalendarItem> addPrevoousCalendarItems(int year, int month, List<CalendarItem> calendarItems) {
         /*添加上一个月的结尾*/
        int week = getWeekByYearMonthDay(year, month, 1);
        int daysOfLastMonth = getDaysByYearMonth(year, month - 1);
        for (int i = week; i > 1; i--) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR,year);
            cal.set(Calendar.MONTH,month - 1-1);
            cal.set(Calendar.DATE,daysOfLastMonth);
            Lunar lunar = new Lunar(cal);
            calendarItems.add(0, new CalendarItem(daysOfLastMonth--,lunar.getLunarMonthAndDay(), false,(month-1)+"-"+(daysOfLastMonth+1)));
        }
        return calendarItems;
    }

    public static List<CalendarItem> addNextCalendarItems(int year, int month, List<CalendarItem> calendarItems) {
          /*添加下一个月的开头*/
        int week = getWeekByYearMonthDay(year, month, getDaysByYearMonth(year, month));
        for (int i = 1; i <= 14 - week; i++) {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR,year);
            cal.set(Calendar.MONTH,month);
            cal.set(Calendar.DATE,i);
            Lunar lunar = new Lunar(cal);
            calendarItems.add(new CalendarItem(i, lunar.getLunarMonthAndDay(), false,(month+1)+"-"+i));
            if (calendarItems.size() >= 42) break;
        }
        return calendarItems;
    }


    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


    /**
     * 根据年月日获取星期数,7为星期天
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int getWeekByYearMonthDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, day);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) week = 7;
        return week;
    }

}
