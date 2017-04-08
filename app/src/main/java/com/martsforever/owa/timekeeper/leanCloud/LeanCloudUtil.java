package com.martsforever.owa.timekeeper.leanCloud;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.martsforever.owa.timekeeper.main.MainActivity;
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
}
