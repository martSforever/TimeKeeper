package com.martsforever.owa.timekeeper.main.push;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.martsforever.owa.timekeeper.main.message.FriendsInviteMessageDetailActivity;
import com.martsforever.owa.timekeeper.util.NotificationUtils;

/**
 * Created by OWA on 2017/4/8.
 */

public class FriendsInvitationMessageHandler implements MessageHandler {
    @Override
    public JSONObject getNotificationMessage(JSONObject jsonObject) {
        JSONObject result = new JSONObject();
        result.put(NotificationUtils.NOTIFICATION_TITLE, jsonObject.get(MESSAGE_SENDER_NAME) + " apply to add you as a friend.");
        result.put(NotificationUtils.NOTIFICATION_MESSAGE, "Veritify message:" + jsonObject.get(MESSAGE_SENDER_MESSAGE));
        result.put(NotificationUtils.NOTIFICATION_TICKER, "You have new Message!");
        return result;
    }

    @Override
    public void onMessageClick(Activity activity, String messageId, int messagePosition) {
        FriendsInviteMessageDetailActivity.actionStart(activity, messageId,messagePosition);
    }

}
