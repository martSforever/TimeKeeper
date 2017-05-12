package com.martsforever.owa.timekeeper.main.todo.festival;

/**
 * Created by OWA on 2017/5/11.
 */

public class FestivalOfDay {
    private String holiday;
    private String avoid;
    private String animalsYear;
    private String weekday;
    private String suit;
    private String lunarYear;
    private String lunar;
    private String date;

    @Override
    public String toString() {
        return "FestivalOfDay{" +
                "holiday='" + holiday + '\'' +
                ", avoid='" + avoid + '\'' +
                ", animalsYear='" + animalsYear + '\'' +
                ", weekday='" + weekday + '\'' +
                ", suit='" + suit + '\'' +
                ", lunarYear='" + lunarYear + '\'' +
                ", lunar='" + lunar + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getAvoid() {
        return avoid;
    }

    public void setAvoid(String avoid) {
        this.avoid = avoid;
    }

    public String getAnimalsYear() {
        return animalsYear;
    }

    public void setAnimalsYear(String animalsYear) {
        this.animalsYear = animalsYear;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getLunarYear() {
        return lunarYear;
    }

    public void setLunarYear(String lunarYear) {
        this.lunarYear = lunarYear;
    }

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FestivalOfDay(String holiday, String avoid, String animalsYear, String weekday, String suit, String lunarYear, String lunar, String date) {
        this.holiday = holiday;
        this.avoid = avoid;
        this.animalsYear = animalsYear;
        this.weekday = weekday;
        this.suit = suit;
        this.lunarYear = lunarYear;
        this.lunar = lunar;
        this.date = date;
    }
}
