package com.martsforever.owa.timekeeper.main.alarm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.javabean.User2Todo;
import com.martsforever.owa.timekeeper.leanCloud.TimeKeeperApplication;
import com.martsforever.owa.timekeeper.main.todo.TodoDetailActivity;
import com.martsforever.owa.timekeeper.util.DateUtil;
import com.martsforever.owa.timekeeper.util.NetWorkUtils;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.sql.Time;
import java.util.List;

@ContentView(R.layout.activity_alarm_todo_detail)
public class AlarmTodoDetailActivity extends AppCompatActivity {

    @ViewInject(R.id.alarm_todo_title_text)
    TextView titleText;
    @ViewInject(R.id.alarm_todo_creator_text)
    TextView creatorText;
    @ViewInject(R.id.alarm_todo_level_text)
    TextView levelText;
    @ViewInject(R.id.alarm_todo_place_text)
    TextView placeText;
    @ViewInject(R.id.alarm_todo_people_text)
    TextView peopleText;
    @ViewInject(R.id.alarm_todo_start_time_text)
    TextView startTimeText;
    @ViewInject(R.id.alarm_todo_end_time_text)
    TextView endTimeText;
    @ViewInject(R.id.alarm_todo_description_text)
    TextView descriptionText;

    private AVObject user2todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        AlarmService.startMusic(this);
        closeClock();
    }

    public void initView() {
        int user2todoDBId = getIntent().getIntExtra(AlarmUtils.PARAMETER_USER2TODO_DB_ID, 0);
        user2todo = getUser2todoByDBId(user2todoDBId);

        AVObject todo = user2todo.getAVObject(User2Todo.TODO);
        titleText.setText(todo.getString(Todo.TITLE));

        switch (todo.getInt(Todo.LEVEL)) {
            case Todo.LEVEL_IMPORTANT_NONE:
                levelText.setText(Todo.LEVEL_NOT_IMPORTANT);
                break;
            case Todo.LEVEL_IMPORTANT_LOW:
                levelText.setText(Todo.LEVEL_NOT_VERY_IMPORTABT);
                break;
            case Todo.LEVEL_IMPORTANT_MIDDLE:
                levelText.setText(Todo.LEVEL_IMPORTANT);
                break;
            case Todo.LEVEL_IMPORTANT_HEIGHT:
                levelText.setText(Todo.LEVEL_VERY_IMPORTANT);
                break;
        }
        placeText.setText(todo.getString(Todo.PLACE));
        startTimeText.setText(DateUtil.date2String(todo.getDate(Todo.START_TIME), DateUtil.COMPLICATED_DATE));
        endTimeText.setText(DateUtil.date2String(todo.getDate(Todo.END_TIME), DateUtil.COMPLICATED_DATE));
        descriptionText.setText(todo.getString(Todo.DESCRIPTION));
        creatorText.setText(user2todo.getAVUser(User2Todo.USER).getString(Person.NICK_NAME));

        AVQuery<AVObject> query = new AVQuery<>(User2Todo.TABLE_USER_2_TODO);
        query.whereEqualTo(User2Todo.TODO, user2todo.getAVObject(User2Todo.TODO));
        query.include(User2Todo.USER + "." + Person.NICK_NAME);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> user2todoList, AVException e) {
                if (e != null) {
                    ShowMessageUtil.tosatSlow(e.getMessage(), AlarmTodoDetailActivity.this);
                } else {
                    String people = "";
                    for (AVObject user2todo : user2todoList) {
                        people += user2todo.getAVUser(User2Todo.USER).getString(Person.NICK_NAME) + ",";
                    }
                    peopleText.setText(people.substring(0, people.length() - 1));
                }
            }
        });

    }

    public static void actionStart(Context context, int user2todoDBId) {
        Intent intent = new Intent(context, AlarmTodoDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AlarmUtils.PARAMETER_USER2TODO_DB_ID, user2todoDBId);
        context.startActivity(intent);
    }

    public AVObject getUser2todoByDBId(int user2todoDBId) {
        List<AVObject> allUser2todoList = ((TimeKeeperApplication) getApplicationContext()).getAllUser2todoList();
        for (AVObject user2todo : allUser2todoList) {
            if (user2todo.getInt("id") == user2todoDBId) return user2todo;
        }
        List<AVObject> offlineUser2todoList = ((TimeKeeperApplication) getApplicationContext()).getOfflineUser2todoList();
        for (AVObject user2todo : offlineUser2todoList) {
            if (user2todo.getInt("id") == user2todoDBId) return user2todo;
        }
        return null;
    }

    @Event(R.id.alarm_todo_stop_music_btn)
    private void stopMusic(View view) {
        AlarmService.stopMusic(this);
    }

    @Event(R.id.alarm_todo_finish_btn)
    private void finish(View view) {
        AlarmService.stopMusic(this);
        if (!NetWorkUtils.isNetworkAvailable(AlarmTodoDetailActivity.this)) {
            NetWorkUtils.showNetworkNotAvailable(AlarmTodoDetailActivity.this);
            return;
        }
        AVObject todo = (AVObject) user2todo.get(User2Todo.TODO);
        todo.put(Todo.STATE, Todo.STATUS_COMPLETE);
        todo.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null)
                    finish();
                else ShowMessageUtil.tosatSlow(e.getMessage(), AlarmTodoDetailActivity.this);
            }
        });
    }
    private void closeClock(){
        user2todo.put(User2Todo.SWITCH,false);
    }

    @Event(R.id.alarm_todo_back_btn)
    private void back(View view) {
        AlarmService.stopMusic(this);
        finish();
    }

    @Override
    public void onBackPressed() {
        back(null);
    }
}
