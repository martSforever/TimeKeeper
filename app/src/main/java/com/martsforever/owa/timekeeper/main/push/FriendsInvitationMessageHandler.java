package com.martsforever.owa.timekeeper.main.push;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.martsforever.owa.timekeeper.main.message.FriendsInviteMessageDetailActivity;

/**
 * Created by OWA on 2017/4/8.
 */

public class FriendsInvitationMessageHandler implements MessageHandler {
    @Override
    public JSONObject getNotificationMessage(JSONObject jsonObject) {
        JSONObject result = new JSONObject();
        result.put(MessageReceiver.NOTIFICATION_TITLE, jsonObject.get(MESSAGE_SENDER_NAME) + " apply to add you as a friend.");
        result.put(MessageReceiver.NOTIFICATION_MESSAGE, "Veritify message:" + jsonObject.get(MESSAGE_SENDER_MESSAGE));
        result.put(MessageReceiver.NOTIFICATION_TICKER, "You have new Message!");
        return result;
    }

    @Override
    public void onMessageClick(Context context, String messageId) {
        FriendsInviteMessageDetailActivity.actionStart(context, messageId);
    }

}
