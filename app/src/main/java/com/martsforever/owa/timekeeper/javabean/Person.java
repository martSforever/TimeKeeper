package com.martsforever.owa.timekeeper.javabean;

/**
 * Created by owa on 2017/1/11.
 */

public class Person {

    /*表名*/
    public static final String TABLE_PERSON = "_User";

    /*以下是AVUser默认自带的变量属性*/
    public static final String OBJECT_ID = "objectId";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String USER_NAME = "username";
    public static final String CREATE_AT = "createAt";
    public static final String MOBILE_PHONE_NUMBER = "mobilePhoneNumber";

    /*以下是AvUser非自带属性，但是在控制台自带的属性，有可能用得到*/
    public static final String EMAIL_VERIFIED = "emailVerified";
    public static final String MOBILE_PHONE_VERIFIED = "mobilePhoneVerified";
    public static final String UPDATE_AT = "updateAt";
    public static final String NICK_NAME="nickname";/*昵称*/
    public static final String INSTALLATION_ID = "installationId";/*关联设备的id*/
    public static final String SCHEDULE_AVAILABLE = "scheduleAvailable";/*是否拒绝所有人查看用户的日程信息*/
    public static final String RECEIVE_INVITATION = "receiveInvitation";/*是否拒绝所有人的日程邀请*/

    /*自定义属性*/
    public static final String FRIEND = "friend";


}
