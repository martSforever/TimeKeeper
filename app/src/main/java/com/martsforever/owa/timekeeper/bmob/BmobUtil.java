package com.martsforever.owa.timekeeper.bmob;

import android.app.Activity;
import android.content.Context;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;

/**
 * Created by owa on 2017/1/14.
 */

public class BmobUtil {

    private static String applicationId = "3ab6713eb42069c56cb0f9c4869f5bfc";

    public static void bmobInitialize(Context context) {
        Bmob.initialize(context, "3ab6713eb42069c56cb0f9c4869f5bfc");
    }

    public static void bmobSmsInitialize(Context context) {
        BmobSMS.initialize(context,applicationId);
    }

}
