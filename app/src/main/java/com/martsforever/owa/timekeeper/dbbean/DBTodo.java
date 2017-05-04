package com.martsforever.owa.timekeeper.dbbean;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.martsforever.owa.timekeeper.javabean.Todo;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.util.Date;

/**
 * Created by OWA on 2017/5/2.
 */
@Table(name = "todo")
public class DBTodo {
    @Column(
            name = "ID",
            isId = true,
            autoGen = true
    )
    private int id;
    @Column(name = "createdUserId")
    private int createdUserId;
    @Column(name = "createdUserNickname")
    private String createdUserNickname;
    @Column(name = "startTime")
    private Date startTime;
    @Column(name = "endTime")
    private Date endTime;
    @Column(name = "title")
    private String title;
    @Column(name = "state")
    private int state;
    @Column(name = "description")
    private String description;
    @Column(name = "level")
    private int level;
    @Column(name = "place")
    private String place;
    @Column(name = "objectId")
    private String objectId;

    DBUser createdUser;

    public DBUser getCreatedUser() {
        try {
            return DBUtils.getDbManager().findById(DBUser.class, createdUserId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setCreatedUser(DBUser createdUser) {
        this.createdUser = createdUser;
    }

    @Override
    public String toString() {
        return "DBTodo{" +
                "id=" + id +
                ", createdUserId=" + createdUserId +
                ", createdUserNickname='" + createdUserNickname + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", title='" + title + '\'' +
                ", state=" + state +
                ", description='" + description + '\'' +
                ", level=" + level +
                ", place='" + place + '\'' +
                '}';
    }

    public DBTodo() {
    }

    public DBTodo(int createdUserId, String createdUserNickname, Date startTime, Date endTime, String title, int state, String description, int level, String place) {
        this.createdUserId = createdUserId;
        this.createdUserNickname = createdUserNickname;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.state = state;
        this.description = description;
        this.level = level;
        this.place = place;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(int createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getCreatedUserNickname() {
        return createdUserNickname;
    }

    public void setCreatedUserNickname(String createdUserNickname) {
        this.createdUserNickname = createdUserNickname;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public static int save(AVObject todo) {
        DBTodo dbTodo = new DBTodo();
        dbTodo.setCreatedUserNickname(todo.getString(Todo.CREATED_BY_NICKNAME));
        dbTodo.setDescription(todo.getString(Todo.DESCRIPTION));
        dbTodo.setEndTime(todo.getDate(Todo.END_TIME));
        dbTodo.setLevel(todo.getInt(Todo.LEVEL));
        dbTodo.setPlace(todo.getString(Todo.PLACE));
        dbTodo.setStartTime(todo.getDate(Todo.START_TIME));
        dbTodo.setState(todo.getInt(Todo.STATE));
        dbTodo.setTitle(todo.getString(Todo.TITLE));
        dbTodo.setObjectId(todo.getObjectId());

        AVUser createdUser = todo.getAVUser(Todo.CREATED_BY);
        dbTodo.setCreatedUserId(DBUser.save(createdUser));
        try {
            if (DBUtils.getDbManager().saveBindingId(dbTodo)) return dbTodo.getId();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static AVObject getTodo(DBTodo dbTodo) {
        AVObject todo = new AVObject(Todo.TABLE_TODO);
        todo.setObjectId(dbTodo.getObjectId());
        todo.put(Todo.CREATED_BY, DBUser.getAVUser(dbTodo.getCreatedUser()));
        todo.put(Todo.CREATED_BY_NICKNAME, dbTodo.getCreatedUserNickname());
        todo.put(Todo.DESCRIPTION, dbTodo.getDescription());
        todo.put(Todo.LEVEL, dbTodo.getLevel());
        todo.put(Todo.PLACE, dbTodo.getPlace());
        todo.put(Todo.START_TIME, dbTodo.getStartTime());
        todo.put(Todo.END_TIME, dbTodo.getEndTime());
        todo.put(Todo.TITLE, dbTodo.getTitle());
        todo.put(Todo.STATE, dbTodo.getState());
        return todo;
    }

    public static void delete(DBTodo dbTodo) {
        DBUser.delete(dbTodo.getCreatedUser());
        DBUtils.deleteById(DBTodo.class, dbTodo.getId());
    }
}
