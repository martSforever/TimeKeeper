package com.martsforever.owa.timekeeper.leanCloud;

import android.app.Application;

/**
 * Created by Administrator on 17/03/07.
 */

public class TimeKeeperApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        LeanCloudUtil.initializeLeancloud(this);
    }
}
