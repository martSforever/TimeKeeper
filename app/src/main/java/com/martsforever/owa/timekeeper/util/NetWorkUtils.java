package com.martsforever.owa.timekeeper.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

/**
 * Created by OWA on 2017/5/3.
 */

public class NetWorkUtils {
    public static boolean isNetworkAvailable(Context context){
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static void showNetworkNotAvailable(Activity activity){
        InformDialog.inform(activity,null,"Message","Network is not abailable, please check your network status!");
    }
}
