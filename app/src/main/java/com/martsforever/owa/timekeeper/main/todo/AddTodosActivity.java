package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.dbbean.DBOfflineUser2Todo;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.javabean.User2Todo;
import com.martsforever.owa.timekeeper.util.DateUtil;
import com.martsforever.owa.timekeeper.util.NetWorkUtils;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ContentView(R.layout.activity_add_todos)
public class AddTodosActivity extends AppCompatActivity {

    public static final String ADD_NEW_ONLINE_TODO = "com.martsforever.owa.ADD_NEW_ONLINE_TODO";
    public static final String ADD_NEW_OFFLINE_TODO = "com.martsforever.owa.ADD_NEW_OFFLINE_TODO";

    @ViewInject(R.id.todo_start_start_time_edit)
    MaterialEditText startTimeEdit;
    @ViewInject(R.id.todo_detail_end_time_edit)
    MaterialEditText endTimeEdit;
    @ViewInject(R.id.todo_add_level_edit)
    MaterialEditText levelEdit;
    @ViewInject(R.id.todo_add_description_edit)
    EditText descriptionEdit;
    @ViewInject(R.id.todo_add_title_edit)
    MaterialEditText titleEdit;
    @ViewInject(R.id.todo_add_place_edit)
    MaterialEditText placeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        levelEdit.setTag(Todo.LEVEL_IMPORTANT_NONE);
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, AddTodosActivity.class);
        activity.startActivityForResult(intent, 0);
    }

    @Event(R.id.todo_add_back_btn)
    private void back(View view) {
        finish();
    }

    @Event(R.id.todo_add_submit_btn)
    private void submit(View view) {
        String description = descriptionEdit.getText().toString().trim();
        String title = titleEdit.getText().toString().trim();
        String place = placeEdit.getText().toString().trim();
        Date startTime = DateUtil.string2Date(startTimeEdit.getText().toString().trim(), DateUtil.COMPLICATED_DATE);
        Date endTime = DateUtil.string2Date(endTimeEdit.getText().toString().trim(), DateUtil.COMPLICATED_DATE);
        int level = Integer.parseInt(levelEdit.getTag().toString());
        int state;
        Date now = new Date();
        if (now.getTime() < startTime.getTime()) state = Todo.STATUS_NOTSTART;
        else if (now.getTime() > endTime.getTime()) state = Todo.STATUS_NOTCOMPLETE;
        else state = Todo.STATUS_DOING;
        final AVUser user = AVUser.getCurrentUser();
        final AVObject todo = new AVObject(Todo.TABLE_TODO);
        todo.put(Todo.TITLE, title);
        todo.put(Todo.DESCRIPTION, description);
        todo.put(Todo.PLACE, place);
        todo.put(Todo.START_TIME, startTime);
        todo.put(Todo.END_TIME, endTime);
        todo.put(Todo.LEVEL, level);
        todo.put(Todo.STATE, state);
        todo.put(Todo.CREATED_BY, user);
        todo.put(Todo.CREATED_BY_NICKNAME, user.getString(Person.NICK_NAME));
        if (NetWorkUtils.isNetworkAvailable(this)) saveUser2todoOnline(todo);
        else
            saveUser2todoOffline(todo);
    }

    private void saveUser2todoOnline(final AVObject todo) {
        System.out.println("saveUser2todoOnline");
        todo.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    final AVObject user2todo = new AVObject(User2Todo.TABLE_USER_2_TODO);
                    user2todo.put(User2Todo.USER, AVUser.getCurrentUser());
                    user2todo.put(User2Todo.TODO, todo);
                    user2todo.put(User2Todo.SWITCH, false);
                    user2todo.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            AddTodosActivity.this.finish();
                            Intent intent = new Intent(ADD_NEW_ONLINE_TODO);
                            intent.putExtra(ADD_NEW_ONLINE_TODO, user2todo.toString());
                            sendBroadcast(intent);
                            TodoDetailActivity.actionStart(AddTodosActivity.this, user2todo, 0);
                        }
                    });
                } else {
                    ShowMessageUtil.tosatSlow(e.getMessage(), AddTodosActivity.this);
                }
            }
        });
    }

    private void saveUser2todoOffline(AVObject todo) {
        System.out.println("saveUser2todoOffline");
        AVObject user2todo = new AVObject(User2Todo.TABLE_USER_2_TODO);
        user2todo.put(User2Todo.USER, AVUser.getCurrentUser());
        user2todo.put(User2Todo.TODO, todo);
        user2todo.put(User2Todo.SWITCH, false);
        DBOfflineUser2Todo.save(user2todo);

        Intent intent = new Intent(ADD_NEW_OFFLINE_TODO);
        intent.putExtra(ADD_NEW_OFFLINE_TODO, user2todo.toString());
        sendBroadcast(intent);
    }

    @Event(R.id.todo_add_select_start_time_btn)
    private void selectStartTime(View view) {
        selectTime(startTimeEdit);
    }

    @Event(R.id.todo_add_select_end_time_btn)
    private void selectEndTime(View view) {
        selectTime(endTimeEdit);
    }

    private void selectTime(final MaterialEditText edit) {
        final Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        edit.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                edit.setText(edit.getText().toString() + " " + hourOfDay + ":" + minute + ":" + second);
                            }
                        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
                        tpd.show(getFragmentManager(), "TimePickerDialog");
                        tpd.setAccentColor(Color.parseColor("#41899A"));
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setAccentColor(Color.parseColor("#41899A"));
    }

    @Event(R.id.todo_add_level_select_btn)
    private void selectLevel(View view) {
        PickDialog pickDialog = new PickDialog(AddTodosActivity.this, Todo.getLevelSelectData());
        pickDialog.setTitle("PICK LEVEL");
        pickDialog.setOnItemOkListener(new PickDialog.OnItemOkListener() {
            @Override
            public void OnOk(Object object, int position) {
                levelEdit.setText(object.toString());
                switch (position) {
                    case 0:
                        levelEdit.setTag(Todo.LEVEL_IMPORTANT_NONE);
                        break;
                    case 1:
                        levelEdit.setTag(Todo.LEVEL_IMPORTANT_LOW);
                        break;
                    case 2:
                        levelEdit.setTag(Todo.LEVEL_IMPORTANT_MIDDLE);
                        break;
                    case 3:
                        levelEdit.setTag(Todo.LEVEL_IMPORTANT_HEIGHT);
                        break;
                }
            }
        });
    }


}
