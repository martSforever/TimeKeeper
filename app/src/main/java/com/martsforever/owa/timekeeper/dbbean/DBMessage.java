package com.martsforever.owa.timekeeper.dbbean;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.martsforever.owa.timekeeper.javabean.Message;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.util.Date;

/**
 * Created by OWA on 2017/5/2.
 */
@Table(name = "message")
public class DBMessage {
    @Column(
            name = "ID",
            isId = true,
            autoGen = true
    )
    private int id;
    @Column(name = "senderId")
    private int senderId;
    @Column(name = "receiverId")
    private int receiverId;
    @Column(name = "messageType")
    private String messageType;
    @Column(name = "verifiedMessage")
    private String verifiedMessage;
    @Column(name = "isRead")
    private int isRead;
    @Column(name = "time")
    private Date time;
    @Column(name = "handleClassName")
    private String handleClassName;
    @Column(name = "user2todoId")
    private int user2todoId;
    @Column(name = "objectId")
    private String objectId;

    DBUser sender;
    DBUser receiver;
    DBUser2Todo user2todo;

    public DBMessage() {
    }

    public DBMessage(int senderId, int receiverId, String messageType, String verifiedMessage, int isRead, Date time, String handleClassName, int user2todoId, DBUser sender, DBUser receiver, DBUser2Todo user2todo) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageType = messageType;
        this.verifiedMessage = verifiedMessage;
        this.isRead = isRead;
        this.time = time;
        this.handleClassName = handleClassName;
        this.user2todoId = user2todoId;
        this.sender = sender;
        this.receiver = receiver;
        this.user2todo = user2todo;
    }

    @Override
    public String toString() {
        return "DBMessage{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", messageType='" + messageType + '\'' +
                ", verifiedMessage='" + verifiedMessage + '\'' +
                ", isRead=" + isRead +
                ", time=" + time +
                ", handleClassName='" + handleClassName + '\'' +
                ", user2todoId=" + user2todoId +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", user2todo=" + user2todo +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getVerifiedMessage() {
        return verifiedMessage;
    }

    public void setVerifiedMessage(String verifiedMessage) {
        this.verifiedMessage = verifiedMessage;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getHandleClassName() {
        return handleClassName;
    }

    public void setHandleClassName(String handleClassName) {
        this.handleClassName = handleClassName;
    }

    public int getUser2todoId() {
        return user2todoId;
    }

    public void setUser2todoId(int user2todoId) {
        this.user2todoId = user2todoId;
    }

    public DBUser getSender() {
        try {
            return DBUtils.getDbManager().findById(DBUser.class, senderId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSender(DBUser sender) {
        this.sender = sender;
    }

    public DBUser getReceiver() {
        try {
            return DBUtils.getDbManager().findById(DBUser.class, receiverId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setReceiver(DBUser receiver) {
        this.receiver = receiver;
    }

    public DBUser2Todo getUser2todo() {
        try {
            return DBUtils.getDbManager().findById(DBUser2Todo.class, user2todoId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setUser2todo(DBUser2Todo user2todo) {
        this.user2todo = user2todo;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public static int save(AVObject message) {

        DBMessage dbMessage = new DBMessage();
        dbMessage.setHandleClassName(message.getString(Message.HANDLE_CLASS_NAME));
        dbMessage.setIsRead(message.getInt(Message.IS_READ));
        dbMessage.setMessageType(message.getString(Message.MESSAGE_TYPE));
        dbMessage.setTime(message.getDate(Message.TIME));
        dbMessage.setVerifiedMessage(message.getString(Message.VERIFY_MESSAGE));
        dbMessage.setObjectId(message.getObjectId());

        AVObject user2todo = message.getAVObject(Message.USER2TODO);
        AVUser sender = message.getAVUser(Message.SENDER);
        AVUser receiver = message.getAVUser(Message.RECEIVER);

        dbMessage.setSenderId(DBUser.save(sender));
        dbMessage.setReceiverId(DBUser.save(receiver));
        if (user2todo != null)
            dbMessage.setUser2todoId(DBUser2Todo.save(user2todo));
        try {
            DBUtils.getDbManager().saveBindingId(dbMessage);
            return dbMessage.getId();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static AVObject getMessage(DBMessage dbMessage) {
        AVObject message = new AVObject(Message.TABLE_MESSAGE);
        message.setObjectId(dbMessage.getObjectId());
        message.put(Message.IS_READ, dbMessage.getIsRead());
        message.put(Message.HANDLE_CLASS_NAME, dbMessage.getHandleClassName());
        message.put(Message.MESSAGE_TYPE, dbMessage.getMessageType());
        message.put(Message.RECEIVER, DBUser.getAVUser(dbMessage.getReceiver()));
        message.put(Message.SENDER, DBUser.getAVUser(dbMessage.getSender()));
        message.put(Message.TIME, dbMessage.getTime());
        message.put(Message.VERIFY_MESSAGE, dbMessage.getVerifiedMessage());
        if (dbMessage.getUser2todo() != null)
            message.put(Message.USER2TODO, DBUser2Todo.getUser2todo(dbMessage.getUser2todo()));
        return message;
    }

    public static void delete(DBMessage dbMessage) {
        DBUser.delete(dbMessage.getSender());
        DBUser.delete(dbMessage.getReceiver());
        DBUser2Todo user2Todo = dbMessage.getUser2todo();
        if (user2Todo != null) DBUser2Todo.delete(user2Todo);
        DBUtils.deleteById(DBMessage.class, dbMessage.getId());
    }
}
