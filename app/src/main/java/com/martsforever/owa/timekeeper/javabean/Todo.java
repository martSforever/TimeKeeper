package com.martsforever.owa.timekeeper.javabean;


import com.martsforever.owa.timekeeper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by owa on 2017/1/11.
 */

public class Todo {

    /*表名*/
    public static final String TABLE_TODO = "TODO";
    /*创建人*/
    public static final String CREATED_BY = "createdBy";
    /*创建人昵称*/
    public static final String CREATED_BY_NICKNAME = "createdByNickname";
    /*开始时间*/
    public static final String START_TIME = "startTime";
    /*结束时间*/
    public static final String END_TIME = "endTime";
    /*标题*/
    public static final String TITLE = "title";
    /*完成状态*/
    public static final String STATE = "state";
    /*描述*/
    public static final String DESCRIPTION = "description";
    /*重要等级*/
    public static final String LEVEL = "level";
    /*执行地点*/
    public static final String PLACE = "place";
    /*相关人员*/
    public static final String PEOPLES = "peoples";

    /*level*/
    public static final int LEVEL_IMPORTANT_NONE = 0X001;
    public static final int LEVEL_IMPORTANT_LOW = 0X002;
    public static final int LEVEL_IMPORTANT_MIDDLE = 0X003;
    public static final int LEVEL_IMPORTANT_HEIGHT = 0X004;

    public static final String LEVEL_NOT_IMPORTANT= "Not Important";
    public static final String LEVEL_NOT_VERY_IMPORTABT = "Not Very Important";
    public static final String LEVEL_IMPORTANT = "Important";
    public static final String LEVEL_VERY_IMPORTANT = "Very Important";

    /*status*/
    public static final int STATUS_NOTSTART = 0X001;
    public static final int STATUS_DOING = 0X002;
    public static final int STATUS_COMPLETE = 0X003;
    public static final int STATUS_NOTCOMPLETE = 0X004;


    /**
     * get level image according to the level
     *
     * @param level
     * @return
     */
    public static int getLevelImage(int level) {
//        System.out.println("level:" + level);
        switch (level) {
            case LEVEL_IMPORTANT_NONE:
                return R.drawable.important_none;
            case LEVEL_IMPORTANT_LOW:
                return R.drawable.important_low;
            case LEVEL_IMPORTANT_MIDDLE:
                return R.drawable.important_middle;
            case LEVEL_IMPORTANT_HEIGHT:
                return R.drawable.important_height;
            default:
                return R.drawable.important_none;
        }
    }

    /**
     * get status image according to the todo's status
     *
     * @param status
     * @return
     */
    public static int getStatusImage(int status) {
//        System.out.println("status:" + status);
        switch (status) {
            case STATUS_NOTSTART:
                return R.drawable.status_notstart;
            case STATUS_DOING:
                return R.drawable.status_doing;
            case STATUS_COMPLETE:
                return R.drawable.status_complete;
            case STATUS_NOTCOMPLETE:
                return R.drawable.status_notcomplete;
            default:
                return R.drawable.status_notstart;
        }
    }

    public static List<Object> getLevelSelectData() {
        List<Object> data = new ArrayList<>();
        data.add("Not important");
        data.add("Not very important");
        data.add("important");
        data.add("Very important");
        return data;
    }

}
