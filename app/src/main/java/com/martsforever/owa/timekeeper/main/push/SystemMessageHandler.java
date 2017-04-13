package com.martsforever.owa.timekeeper.main.push;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.martsforever.owa.timekeeper.main.message.SystemMessageDetailActivity;

/**
 * Created by OWA on 2017/4/12.
 */

public class SystemMessageHandler implements MessageHandler {
    @Override
    public JSONObject getNotificationMessage(JSONObject jsonObject) {
        JSONObject result = new JSONObject();
        result.put(MessageReceiver.NOTIFICATION_TITLE, "System Message!");
        result.put(MessageReceiver.NOTIFICATION_MESSAGE, "Veritify message:" + jsonObject.get(MESSAGE_SENDER_MESSAGE));
        result.put(MessageReceiver.NOTIFICATION_TICKER, "You have new Message!");
        return result;
    }

    @Override
    public void onMessageClick(Activity activity, String messageId, int messagePosition) {
        SystemMessageDetailActivity.actionStart(activity, messageId);
    }
}
