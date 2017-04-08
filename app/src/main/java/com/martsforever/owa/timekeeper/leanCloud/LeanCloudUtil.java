package com.martsforever.owa.timekeeper.leanCloud;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.martsforever.owa.timekeeper.main.MainActivity;
import com.martsforever.owa.timekeeper.main.friend.FriendDetailActivity;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

/**
 * Created by owa on 2017/2/8.
 */

public class LeanCloudUtil {

    public static String APP_ID = "IkX4Q555cIUq2pVBipdnkRog-gzGzoHsz";
    public static String APP_KEY = "N1CzOnYOl7hUzAqH4BSbr2lx";

    public static void initializeLeancloud(Context context) {
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(context, APP_ID, APP_KEY);

        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可,在应用发布之前，请关闭调试日志。
        AVOSCloud.setDebugLogEnabled(true);

        /*默认启用中国节点*/
        AVOSCloud.useAVCloudCN();

    }

    /**
     * if you want to use the leancloud service, you have to bind a installation id to your device
     */

    public static void registerPushService(final Context context){
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    System.out.println("save installation successful!");
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    System.out.println(installationId);
                    // 设置默认打开的 Activity,一定要有这句代码，不然会导致无法收到推送消息
                    PushService.setDefaultPushCallback(context, MainActivity.class);
                } else {
                    ShowMessageUtil.tosatFast(e.getMessage(), context);
                }
            }
        });
    }

    public static void pushMessage(String installationId, JSONObject jsonObject, final Context context){
         /*push to specific android device|*/
        AVQuery pushQuery = AVInstallation.getQuery();
        // 假设 THE_INSTALLATION_ID 是保存在用户表里的 installationId，
        // 可以在应用启动的时候获取并保存到用户表
        AVPush push = new AVPush();
        String action = "com.avos.UPDATE_STATUS";
        jsonObject.put("action", action);

        push.setData(jsonObject);
        push.setCloudQuery("select * from _Installation where installationId ='" + installationId + "'");
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // push successfully.
                    ShowMessageUtil.tosatFast("push successful", context);
                } else {
                    // something wrong.
                    ShowMessageUtil.tosatFast(e.getMessage(), context);
                }
            }
        });
    }
}
