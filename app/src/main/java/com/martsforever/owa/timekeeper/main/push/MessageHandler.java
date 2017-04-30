package com.martsforever.owa.timekeeper.main.push;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVObject;
import com.martsforever.owa.timekeeper.javabean.Message;

/**
 * Created by OWA on 2017/4/8.
 */

public interface MessageHandler {

    public static final String MESSAGE_SENDER_NAME = "message sender's name";
    public static final String MESSAGE_SENDER_MESSAGE = "message sender's message";
    public static final String MESSAGE_HANDLE_CLASS = "message handle class";
    public static final String MESSAGE_ADD_NEW_FRIEND = "should add new friend";
    public static final String MESSAGE_FRIENDSHIP_ID = "friendship's id";
    public static final String MESSAGE_ADD_NEW_TODO = "should add new todo";
    public static final String MESSAGE_USER2TODO_ID = "user2todo's id";

    public JSONObject getNotificationMessage(JSONObject jsonObject);

    public void onMessageClick(Activity activity, String messageId, int messagePosition);
}
