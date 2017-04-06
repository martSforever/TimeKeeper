package com.martsforever.owa.timekeeper.leanCloud;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 17/03/07.
 */

public class TimeKeeperApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        /*初始化leancloud*/
        LeanCloudUtil.initializeLeancloud(this);
        /*初始化xutils*/
        x.Ext.init(this);
    }
}
