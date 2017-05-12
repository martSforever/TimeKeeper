package com.martsforever.owa.timekeeper.main.todo.festival;

/**
 * Created by owa on 2017/1/9.
 */

public class CalendarItem {
    private int dayOfdate;
    private String desc;
    private Boolean isCueerntMonth;
    private String key;
    private int badgeNumber;

    public CalendarItem() {
    }

    public int getDayOfdate() {
        return dayOfdate;
    }

    public CalendarItem(int dayOfdate, String desc, Boolean isCueerntMonth, String key) {
        this.dayOfdate = dayOfdate;
        this.desc = desc;
        this.isCueerntMonth = isCueerntMonth;
        this.key = key;
        badgeNumber = 0;
    }

    public void setDayOfdate(int dayOfdate) {
        this.dayOfdate = dayOfdate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getCueerntMonth() {
        return isCueerntMonth;
    }

    public void setCueerntMonth(Boolean cueerntMonth) {
        isCueerntMonth = cueerntMonth;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }
}
