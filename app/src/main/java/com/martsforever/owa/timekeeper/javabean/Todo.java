package com.martsforever.owa.timekeeper.javabean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by owa on 2017/1/11.
 */

public class Todo extends BmobObject {

    private BmobDate startTime;
    private BmobDate endTime;
    private String title;
    private Integer state;
    private String description;
    private Integer level;
    private String place;
    private String peoples;

}
