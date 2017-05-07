package com.martsforever.owa.timekeeper.leanCloud;

import android.app.Application;

import com.avos.avoscloud.AVObject;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 17/03/07.
 */

public class TimeKeeperApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        /*initialize leancloud store*/
        LeanCloudUtil.initializeLeancloud(this);
        /*initialize leancloud push service*/
        LeanCloudUtil.registerPushService(this);
        /*初始化xutils*/
        x.Ext.init(this);
    }

    private List<AVObject> allUser2todoList;
    private List<AVObject> categoryUser2todoList;

    public List<AVObject> getAllUser2todoList() {
        return allUser2todoList;
    }

    public void setAllUser2todoList(List<AVObject> allUser2todoList) {
        this.allUser2todoList = allUser2todoList;
    }

    public List<AVObject> getCategoryUser2todoList() {
        return categoryUser2todoList;
    }

    public void setCategoryUser2todoList(List<AVObject> categoryUser2todoList) {
        this.categoryUser2todoList = categoryUser2todoList;
    }
}
