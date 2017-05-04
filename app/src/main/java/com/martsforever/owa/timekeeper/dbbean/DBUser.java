package com.martsforever.owa.timekeeper.dbbean;

import com.avos.avoscloud.AVUser;
import com.martsforever.owa.timekeeper.javabean.Person;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

/**
 * Created by OWA on 2017/5/2.
 */
@Table(name = "user")
public class DBUser {
    @Column(
            name = "ID",
            isId = true,
            autoGen = true
    )
    private int id;
    @Column(name = "objectId")
    private String objectId;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "username")
    private String username;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "isEmailVertified")
    private boolean isEmailVertified;
    @Column(name = "isMobileVerified")
    private boolean isMobileVerified;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "installationId")
    private String installationId;
    @Column(name = "scheduleAvailable")
    private boolean scheduleAvailable;
    @Column(name = "invitationAvailable")
    private boolean invitationAvailable;

    public DBUser() {
    }

    public DBUser(String objectId, String email, String password, String username, String mobile, boolean isEmailVertified, boolean isMobileVerified, String nickname, String installationId, boolean scheduleAvailable, boolean invitationAvailable) {
        this.objectId = objectId;
        this.email = email;
        this.password = password;
        this.username = username;
        this.mobile = mobile;
        this.isEmailVertified = isEmailVertified;
        this.isMobileVerified = isMobileVerified;
        this.nickname = nickname;
        this.installationId = installationId;
        this.scheduleAvailable = scheduleAvailable;
        this.invitationAvailable = invitationAvailable;
    }

    @Override
    public String toString() {
        return "DBUser{" +
                "id=" + id +
                ", objectId='" + objectId + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", isEmailVertified=" + isEmailVertified +
                ", isMobileVerified=" + isMobileVerified +
                ", nickname='" + nickname + '\'' +
                ", installationId='" + installationId + '\'' +
                ", scheduleAvailable=" + scheduleAvailable +
                ", invitationAvailable=" + invitationAvailable +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isEmailVertified() {
        return isEmailVertified;
    }

    public void setEmailVertified(boolean emailVertified) {
        isEmailVertified = emailVertified;
    }

    public boolean isMobileVerified() {
        return isMobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        isMobileVerified = mobileVerified;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public boolean isScheduleAvailable() {
        return scheduleAvailable;
    }

    public void setScheduleAvailable(boolean scheduleAvailable) {
        this.scheduleAvailable = scheduleAvailable;
    }

    public boolean isInvitationAvailable() {
        return invitationAvailable;
    }

    public void setInvitationAvailable(boolean invitationAvailable) {
        this.invitationAvailable = invitationAvailable;
    }

    public static int save(AVUser user) {
        DBUser dbUser = new DBUser();
        dbUser.setEmail(user.getString(Person.EMAIL));
        dbUser.setEmailVertified(user.getBoolean(Person.EMAIL_VERIFIED));
        dbUser.setInstallationId(user.getString(Person.INSTALLATION_ID));
        dbUser.setInvitationAvailable(user.getBoolean(Person.RECEIVE_INVITATION));
        dbUser.setMobile(user.getString(Person.MOBILE_PHONE_NUMBER));
        dbUser.setMobileVerified(user.getBoolean(Person.MOBILE_PHONE_VERIFIED));
        dbUser.setNickname(user.getString(Person.NICK_NAME));
        dbUser.setObjectId(user.getObjectId());
        dbUser.setPassword(user.getString(Person.PASSWORD));
        dbUser.setScheduleAvailable(user.getBoolean(Person.SCHEDULE_AVAILABLE));
        dbUser.setUsername(user.getString(Person.USER_NAME));
        try {
            if (DBUtils.getDbManager().saveBindingId(dbUser)) return dbUser.getId();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static AVUser getAVUser(DBUser dbUser) {
        AVUser user = new AVUser();
        user.setObjectId(dbUser.getObjectId());
        user.setMobilePhoneNumber(dbUser.getMobile());
        user.setEmail(dbUser.getEmail());
        user.setPassword(dbUser.getPassword());
        user.setUsername(dbUser.getUsername());
        user.put(Person.INSTALLATION_ID, dbUser.getInstallationId());
        user.put(Person.NICK_NAME, dbUser.getNickname());
        user.put(Person.EMAIL_VERIFIED, dbUser.isEmailVertified());
        user.put(Person.MOBILE_PHONE_VERIFIED, dbUser.isMobileVerified());
        user.put(Person.RECEIVE_INVITATION, dbUser.isInvitationAvailable());
        user.put(Person.SCHEDULE_AVAILABLE, dbUser.isScheduleAvailable());
        return user;
    }


    public static void delete(DBUser dbUser) {
        DBUtils.deleteById(DBUser.class, dbUser.getId());
    }
}
