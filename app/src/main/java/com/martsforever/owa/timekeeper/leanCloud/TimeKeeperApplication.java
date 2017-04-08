package com.martsforever.owa.timekeeper.leanCloud;

import android.app.Application;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.martsforever.owa.timekeeper.main.MainActivity;
import com.martsforever.owa.timekeeper.main.friend.FriendDetailActivity;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import org.xutils.x;

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
}
