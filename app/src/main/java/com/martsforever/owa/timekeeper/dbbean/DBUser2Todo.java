package com.martsforever.owa.timekeeper.dbbean;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.martsforever.owa.timekeeper.javabean.User2Todo;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

/**
 * Created by OWA on 2017/5/2.
 */

@Table(name = "user2todo")
public class DBUser2Todo {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "objectId")
    private String objectId;
    @Column(name = "userId")
    private int userId;
    @Column(name = "todoId")
    private int todoId;
    @Column(name = "switch")
    private boolean swt;

    DBUser user;
    DBTodo todo;

    public DBUser2Todo() {
    }

    public DBUser2Todo(String objectId, int userId, int todoId, boolean swt) {
        this.objectId = objectId;
        this.userId = userId;
        this.todoId = todoId;
        this.swt = swt;
    }

    @Override
    public String toString() {
        return "DBUser2Todo{" +
                "id=" + id +
                ", objectId='" + objectId + '\'' +
                ", userId=" + userId +
                ", todoId=" + todoId +
                ", swt=" + swt +
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    public boolean isSwt() {
        return swt;
    }

    public void setSwt(boolean swt) {
        this.swt = swt;
    }

    public DBUser getUser() {
        try {
            return DBUtils.getDbManager().findById(DBUser.class, userId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setUser(DBUser user) {
        this.user = user;
    }

    public DBTodo getTodo() {
        try {
            return DBUtils.getDbManager().findById(DBTodo.class, todoId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setTodo(DBTodo todo) {
        this.todo = todo;
    }

    public static int save(AVObject user2todo) {
        DBUser2Todo dbUser2Todo = new DBUser2Todo();
        dbUser2Todo.setObjectId(user2todo.getObjectId());
        dbUser2Todo.setSwt(user2todo.getBoolean(User2Todo.SWITCH));
        AVUser user = user2todo.getAVUser(User2Todo.USER);
        AVObject todo = user2todo.getAVObject(User2Todo.TODO);
        dbUser2Todo.setUserId(DBUser.save(user));
        dbUser2Todo.setTodoId(DBTodo.save(todo));
        try {
            if (DBUtils.getDbManager().saveBindingId(dbUser2Todo)) {
                return dbUser2Todo.getId();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static AVObject getUser2todo(DBUser2Todo dbUser2Todo) {
        AVObject user2todo = new AVObject(User2Todo.TABLE_USER_2_TODO);
        user2todo.setObjectId(dbUser2Todo.getObjectId());
        user2todo.put(User2Todo.SWITCH, dbUser2Todo.isSwt());
        user2todo.put(User2Todo.USER, DBUser.getAVUser(dbUser2Todo.getUser()));
        user2todo.put(User2Todo.TODO, DBTodo.getTodo(dbUser2Todo.getTodo()));
        return user2todo;
    }

    public static void delete(DBUser2Todo dbUser2Todo) {
        DBUser.delete(dbUser2Todo.getUser());
        DBTodo.delete(dbUser2Todo.getTodo());
        DBUtils.deleteById(DBUser2Todo.class, dbUser2Todo.getId());
    }

}
