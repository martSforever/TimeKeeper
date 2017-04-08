package com.martsforever.owa.timekeeper.javabean;

import com.avos.avoscloud.AVUser;

/**
 * Created by OWA on 2017/4/6.
 */

public class Message {

    public static String MESSAGE_TYPE_FRIENDS_INVITATION = "friends invitation";
    public static String MESSAGE_TYPE_TODOS_INVITATION = "todosinvitation";

    /*table name*/
    public static final String TABLE_MESSAGE = "MESSAGE";

    /*sender*/
    private AVUser sender;
    /*receiver*/
    private AVUser receiver;
    /*message type*/
    private String MESSAGE_TYPE = "message type";
    /*verify message*/
    private String VERIFY_MESSAGE = "verify message";
    /*handle class name*/
    private String HANDLE_CLASS_NAME = "handle class name";

}
