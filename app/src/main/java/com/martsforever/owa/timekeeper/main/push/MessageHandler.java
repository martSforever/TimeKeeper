package com.martsforever.owa.timekeeper.main.push;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by OWA on 2017/4/8.
 */

public interface MessageHandler {

    public static String MESSAGE_SENDER_NAME = "message sender's name";
    public static String MESSAGE_SENDER_MESSAGE = "message sender's message";
    public static String MESSAGE_HANDLE_CLASS = "message handle class";

    public JSONObject getNotificationMessage(JSONObject jsonObject);
}
