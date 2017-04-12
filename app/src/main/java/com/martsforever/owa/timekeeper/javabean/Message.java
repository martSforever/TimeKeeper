package com.martsforever.owa.timekeeper.javabean;

import com.avos.avoscloud.AVUser;

/**
 * Created by OWA on 2017/4/6.
 */

public class Message {

    /*message type, used to be message title*/
    public static final String MESSAGE_TYPE_FRIENDS_INVITATION = "Friends invitation";
    public static final String MESSAGE_TYPE_TODOS_INVITATION = "Todos invitation";
    public static final String MESSAGE_TYPE_SYATEM = "System Message";
    /*read state*/
    public static final int READ = 1;
    public static final int UNREAD = 2;
    public static final int REJECT = 3;
    public static final int ACCEPT = 4;

    /*table name*/
    public static final String TABLE_MESSAGE = "MESSAGE";

    /*sender*/
    public static final String SENDER = "sender";
    /*receiver*/
    public static final String RECEIVER = "receiver";
    /*message type*/
    public static final String MESSAGE_TYPE = "messageType";
    /*verify message*/
    public static final String VERIFY_MESSAGE = "verifyMessage";
    /*is readed?*/
    public static final String IS_READ = "isRead";
    /*time*/
    public static final String TIME = "time";
    /*handle class name*/
    public static final String HANDLE_CLASS_NAME = "handleClassName";

}
