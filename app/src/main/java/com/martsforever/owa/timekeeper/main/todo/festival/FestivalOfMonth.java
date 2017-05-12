package com.martsforever.owa.timekeeper.main.todo.festival;

/**
 * Created by OWA on 2017/5/11.
 */

public class FestivalOfMonth {
    private String desc;
    private String festival;
    private String name;

    public FestivalOfMonth(String desc, String festival, String name) {
        this.desc = desc;
        this.festival = festival;
        this.name = name;
    }

    public FestivalOfMonth() {
    }

    @Override
    public String toString() {
        return "FestivalOfMonth{" +
                "desc='" + desc + '\'' +
                ", festival='" + festival + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
