package com.martsforever.owa.timekeeper.dbbean;

import com.avos.avoscloud.AVObject;
import com.martsforever.owa.timekeeper.javabean.FriendShip;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

/**
 * Created by OWA on 2017/5/2.
 */
@Table(name = "friendship")
public class DBFriendShip {
    @Column(
            name = "ID",
            isId = true,
            autoGen = true
    )
    private int id;
    @Column(name = "selfId")
    private int selfId;
    @Column(name = "friendId")
    private int friendId;
    @Column(name = "scheduleAvailable")
    private boolean scheduleAvailable;
    @Column(name = "invitationAvailable")
    private boolean invitationAvailable;
    @Column(name = "objectId")
    private String objectId;

    DBUser self;
    DBUser friend;

    public DBUser getSelf() {
        try {
            return DBUtils.getDbManager().findById(DBUser.class, selfId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSelf(DBUser self) {
        this.self = self;
    }

    public DBUser getFriend() {
        try {
            return DBUtils.getDbManager().findById(DBUser.class, friendId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setFriend(DBUser friend) {
        this.friend = friend;
    }

    @Override
    public String toString() {
        return "DBFriendShip{" +
                "id=" + id +
                ", selfId=" + selfId +
                ", friendId=" + friendId +
                ", scheduleAvailable=" + scheduleAvailable +
                ", invitationAvailable=" + invitationAvailable +
                '}';
    }

    public DBFriendShip() {
    }

    public DBFriendShip(int selfId, int friendId, boolean scheduleAvailable, boolean invitationAvailable) {
        this.selfId = selfId;
        this.friendId = friendId;
        this.scheduleAvailable = scheduleAvailable;
        this.invitationAvailable = invitationAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSelfId() {
        return selfId;
    }

    public void setSelfId(int selfId) {
        this.selfId = selfId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public static int save(AVObject friendship) {
        DBFriendShip dbFriendShip = new DBFriendShip();
        dbFriendShip.setFriendId(DBUser.save(friendship.getAVUser(FriendShip.FRIEND)));
        dbFriendShip.setSelfId(DBUser.save(friendship.getAVUser(FriendShip.SELF)));
        dbFriendShip.setInvitationAvailable(friendship.getBoolean(FriendShip.INVITATION_AVAILABLE));
        dbFriendShip.setScheduleAvailable(friendship.getBoolean(FriendShip.SCHEDULE_AVAILABLE));
        dbFriendShip.setObjectId(friendship.getObjectId());
        try {
            if (DBUtils.getDbManager().saveBindingId(dbFriendShip)) {
                return dbFriendShip.getId();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static AVObject getFriendship(DBFriendShip dbFriendShip){
        AVObject friendship = new AVObject(FriendShip.TABLE_FRIENDSHIP);
        friendship.setObjectId(dbFriendShip.getObjectId());
        friendship.put(FriendShip.INVITATION_AVAILABLE,dbFriendShip.isInvitationAvailable());
        friendship.put(FriendShip.SCHEDULE_AVAILABLE,dbFriendShip.isScheduleAvailable());
        friendship.put(FriendShip.SELF,DBUser.getAVUser(dbFriendShip.getSelf()));
        friendship.put(FriendShip.FRIEND,DBUser.getAVUser(dbFriendShip.getFriend()));
        return friendship;
    }

    public static void delete(DBFriendShip dbFriendShip) {
        DBUser.delete(dbFriendShip.getSelf());
        DBUser.delete(dbFriendShip.getFriend());
        DBUtils.deleteById(DBFriendShip.class, dbFriendShip.getId());
    }

}
