package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.github.zagum.switchicon.SwitchIconView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.dbbean.DBUser2Todo;
import com.martsforever.owa.timekeeper.dbbean.DBUtils;
import com.martsforever.owa.timekeeper.javabean.FriendShip;
import com.martsforever.owa.timekeeper.javabean.Message;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.javabean.User2Todo;
import com.martsforever.owa.timekeeper.leanCloud.LeanCloudUtil;
import com.martsforever.owa.timekeeper.leanCloud.TimeKeeperApplication;
import com.martsforever.owa.timekeeper.main.MainActivity;
import com.martsforever.owa.timekeeper.main.push.FriendsInvitationMessageHandler;
import com.martsforever.owa.timekeeper.main.push.MessageHandler;
import com.martsforever.owa.timekeeper.main.push.TodosInvitationMessageHandler;
import com.martsforever.owa.timekeeper.util.DateUtil;
import com.martsforever.owa.timekeeper.util.InformDialog;
import com.martsforever.owa.timekeeper.util.NetWorkUtils;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tencent.qc.stat.common.User;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ContentView(R.layout.activity_todo_detail)
public class TodoDetailActivity extends AppCompatActivity {
    /*data*/
    public static final String ACTION_START_PARAMETER_USER2TODO = "todo";
    private List<AVObject> friendships;
    private AVObject user2todo;
    /*view*/
    @ViewInject(R.id.todo_detail_title_edit)
    MaterialEditText titleEdit;
    @ViewInject(R.id.todo_detail_level_edit)
    MaterialEditText levelEdit;
    @ViewInject(R.id.todo_detail_place_edit)
    MaterialEditText placeEdit;
    @ViewInject(R.id.todo_detail_start_time_edit)
    MaterialEditText startTimeEdit;
    @ViewInject(R.id.todo_detail_end_time_edit)
    MaterialEditText endTimeEdit;
    @ViewInject(R.id.todo_detail_description_edit)
    EditText descriptionEdit;
    @ViewInject(R.id.todo_detail_people_edit)
    MaterialEditText peopleEdit;
    @ViewInject(R.id.todo_detail_createdby_edit)
    MaterialEditText createdByEdit;
    @ViewInject(R.id.todo_detail_state_edit)
    MaterialEditText stateEdit;
    @ViewInject(R.id.todo_detail_level_select_btn)
    ImageView pickLevelImg;
    @ViewInject(R.id.todo_detail_people_pick_btn)
    ImageView pickPeopleImg;
    @ViewInject(R.id.todo_detail_select_start_time_btn)
    ImageView pickStartTimeImg;
    @ViewInject(R.id.todo_detail_select_end_time_btn)
    ImageView pickEndTimeImg;
    @ViewInject(R.id.todo_detail_save_btn)
    ImageView editImg;
    @ViewInject(R.id.todo_detail_switch_btn)
    SwitchIconView switchIconView;
    @ViewInject(R.id.todo_detail_finish_btn)
    Button finishedBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
        peopleInit();
        initView();
        initFriendshipsData();
        setEditable(false);
    }

    private void initData() {
        Intent intent = getIntent();
        user2todo = ((TimeKeeperApplication) getApplicationContext()).getCategoryUser2todoList().get(intent.getIntExtra(CategoryTodoActivity.INTENT_PARAMETER_POSITION, 0));
    }

    private void initView() {
        AVObject todo = (AVObject) user2todo.get(User2Todo.TODO);
        titleEdit.setText(todo.getString(Todo.TITLE));
        switch (todo.getInt(Todo.LEVEL)) {
            case Todo.LEVEL_IMPORTANT_NONE:
                levelEdit.setText(Todo.LEVEL_NOT_IMPORTANT);
                break;
            case Todo.LEVEL_IMPORTANT_LOW:
                levelEdit.setText(Todo.LEVEL_NOT_VERY_IMPORTABT);
                break;
            case Todo.LEVEL_IMPORTANT_MIDDLE:
                levelEdit.setText(Todo.LEVEL_IMPORTANT);
                break;
            case Todo.LEVEL_IMPORTANT_HEIGHT:
                levelEdit.setText(Todo.LEVEL_VERY_IMPORTANT);
                break;
        }
        placeEdit.setText(todo.getString(Todo.PLACE));
        startTimeEdit.setText(DateUtil.date2String(todo.getDate(Todo.START_TIME), DateUtil.COMPLICATED_DATE));
        endTimeEdit.setText(DateUtil.date2String(todo.getDate(Todo.END_TIME), DateUtil.COMPLICATED_DATE));
        descriptionEdit.setText(todo.getString(Todo.DESCRIPTION));
        createdByEdit.setText(user2todo.getAVUser(User2Todo.USER).getString(Person.NICK_NAME));
        stateEdit.setText(Todo.getStateString(todo.getInt(Todo.STATE)));
        switchIconView.setIconEnabled(user2todo.getBoolean(User2Todo.SWITCH));
        levelEdit.setTag(Todo.LEVEL_IMPORTANT_NONE);
        if (todo.getInt(Todo.STATE) == Todo.STATUS_COMPLETE) {
            finishedBtn.setEnabled(false);
            finishedBtn.setBackgroundResource(R.drawable.bg_todo_detail_finish_btn_disable);
        }
    }

    private void initFriendshipsData() {
        AVQuery<AVObject> query = new AVQuery<>(FriendShip.TABLE_FRIENDSHIP);
        query.include(FriendShip.FRIEND + "." + Person.NICK_NAME);
        query.whereEqualTo(FriendShip.SELF, AVUser.getCurrentUser());
        query.include(FriendShip.FRIEND);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null)
                    friendships = list;
                else {
                    ShowMessageUtil.tosatSlow(e.getMessage(), TodoDetailActivity.this);
                }
            }
        });
    }

    public static void actionStart(Activity activity, AVObject user2todo, int position) {
        Intent intent = new Intent();
        intent.setClass(activity, TodoDetailActivity.class);
        intent.putExtra(CategoryTodoActivity.INTENT_PARAMETER_POSITION, position);
        intent.putExtra(TodoDetailActivity.ACTION_START_PARAMETER_USER2TODO, user2todo.toString());
        activity.startActivityForResult(intent, 0);
    }

    @Event(R.id.todo_detail_people_pick_btn)
    private void multiPickPeople(View view) {
        if (friendships != null) {
            List<String> names = new ArrayList<>();
            for (AVObject avObject : friendships)
                names.add(avObject.getAVUser(FriendShip.FRIEND).getString(Person.NICK_NAME));
            MultiPickDialog dialog = new MultiPickDialog(this, names);
            dialog.setTitle("Pick Friends");
            dialog.setOnItemOkListener(new MultiPickDialog.OnItemOkListener() {
                @Override
                public void OnOk(List<Boolean> isCheckedList) {
                    String peopleNames = "";
                    for (int position = 0; position < isCheckedList.size(); position++)
                        if (isCheckedList.get(position)) {
                            peopleNames += friendships.get(position).getAVUser(FriendShip.FRIEND).getString(Person.NICK_NAME);
                            peopleNames += ",";
                        }
                    for (int position = 0; position < isCheckedList.size(); position++)
                        if (isCheckedList.get(position)) {
                            pushTodosInviteMessage(friendships.get(position).getAVUser(FriendShip.FRIEND), peopleNames.substring(0, peopleNames.length() - 1), user2todo);
                        }
                    InformDialog.inform(TodoDetailActivity.this, null, "System Message", "Send message to " + peopleNames.substring(0, peopleNames.length() - 1) + " successful!");
                }
            });
        } else {
            ShowMessageUtil.tosatSlow("System error!", TodoDetailActivity.this);
        }
    }

    @Event(R.id.todo_detail_select_start_time_btn)
    private void selectStartTime(View view) {
        selectTime(startTimeEdit);
    }

    @Event(R.id.todo_detail_select_end_time_btn)
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

    @Event(R.id.todo_detail_level_select_btn)
    private void selectLevel(View view) {
        PickDialog pickDialog = new PickDialog(TodoDetailActivity.this, Todo.getLevelSelectData());
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

    @Event(R.id.todo_detail_save_btn)
    private void edit(View view) {
        if (!NetWorkUtils.isNetworkAvailable(TodoDetailActivity.this)) {
            NetWorkUtils.showNetworkNotAvailable(TodoDetailActivity.this);
            return;
        }
        modifiedTodoOnline(titleEdit.isEnabled());
        setEditable(!titleEdit.isEnabled());
    }

    private void setEditable(boolean enable) {
        titleEdit.setEnabled(enable);
        pickLevelImg.setEnabled(enable);
        placeEdit.setEnabled(enable);
        pickPeopleImg.setEnabled(enable);
        pickStartTimeImg.setEnabled(enable);
        pickEndTimeImg.setEnabled(enable);
        descriptionEdit.setEnabled(enable);
        switchIconView.setEnabled(enable);
        if (enable) {
            editImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_save));
            pickPeopleImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_add_people));
            pickLevelImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down));
            pickStartTimeImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_select));
            pickEndTimeImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_select));
        } else {
            editImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_edit));
            pickPeopleImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_add_people_disable));
            pickLevelImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_arrow_down_disable));
            pickStartTimeImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_select_disable));
            pickEndTimeImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_select_disable));
        }
    }

    @Event(R.id.todo_detail_back_btn)
    private void back(View view) {
        backToAllTodoActivity();
    }

    @Override
    public void onBackPressed() {
        backToAllTodoActivity();
    }

    @Event(R.id.todo_detail_switch_btn)
    private void switchTodoAvailable(View view) {
        switchIconView.switchState();
        user2todo.put(User2Todo.SWITCH, switchIconView.isIconEnabled());
        user2todo.saveInBackground();
    }

    private void backToAllTodoActivity() {
        Intent intent = getIntent();
        intent.putExtra(CategoryTodoActivity.INTENT_PARAMETER_USER2TODO, user2todo.toString());
        setResult(CategoryTodoActivity.TODO_CHANGE, intent);
        finish();
    }

    @Event(R.id.todo_detail_finish_btn)
    private void finishedTodo(View view) {
        if (!NetWorkUtils.isNetworkAvailable(TodoDetailActivity.this)) {
            NetWorkUtils.showNetworkNotAvailable(TodoDetailActivity.this);
            return;
        }
        AVObject todo = (AVObject) user2todo.get(User2Todo.TODO);
        todo.put(Todo.STATE, Todo.STATUS_COMPLETE);
        todo.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null)
                    backToAllTodoActivity();
                else ShowMessageUtil.tosatSlow(e.getMessage(), TodoDetailActivity.this);
            }
        });
    }

    private void pushTodosInviteMessage(AVUser friend, String friendsString, AVObject user2todo) {
        String installationId = friend.get(Person.INSTALLATION_ID).toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MessageHandler.MESSAGE_SENDER_MESSAGE, AVUser.getCurrentUser().getString(Person.NICK_NAME) + " invited " + friendsString + " to join his todo.");
        jsonObject.put(MessageHandler.MESSAGE_SENDER_NAME, AVUser.getCurrentUser().get(Person.NICK_NAME).toString());
        jsonObject.put(MessageHandler.MESSAGE_HANDLE_CLASS, TodosInvitationMessageHandler.class.getName());
        LeanCloudUtil.pushMessage(installationId, jsonObject, this);

        AVObject message = new AVObject(com.martsforever.owa.timekeeper.javabean.Message.TABLE_MESSAGE);
        message.put(Message.MESSAGE_TYPE, Message.MESSAGE_TYPE_TODOS_INVITATION);
        message.put(Message.SENDER, AVUser.getCurrentUser());
        message.put(Message.RECEIVER, friend);
        message.put(Message.IS_READ, Message.UNREAD);
        message.put(Message.TIME, new Date());
        message.put(Message.HANDLE_CLASS_NAME, TodosInvitationMessageHandler.class.getName());
        message.put(Message.VERIFY_MESSAGE, AVUser.getCurrentUser().getString(Person.NICK_NAME) + " invited " + friendsString + " to join his todo.");
        message.put(Message.USER2TODO, user2todo);
        message.saveInBackground();
    }

    private void peopleInit() {
        AVQuery<AVObject> query = new AVQuery<>(User2Todo.TABLE_USER_2_TODO);
        query.whereEqualTo(User2Todo.TODO, user2todo.getAVObject(User2Todo.TODO));
        query.include(User2Todo.USER + "." + Person.NICK_NAME);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> user2todoList, AVException e) {
                if (e != null) {
                    ShowMessageUtil.tosatSlow(e.getMessage(), TodoDetailActivity.this);
                } else {
                    String people = "";
                    for (AVObject user2todo : user2todoList) {
                        people += user2todo.getAVUser(User2Todo.USER).getString(Person.NICK_NAME) + ",";
                    }
                    peopleEdit.setText(people.substring(0, people.length() - 1));
                }
            }
        });
    }

    private void modifiedTodoOnline(boolean isEnable) {
        if (isEnable) {
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
            final AVObject todo = user2todo.getAVObject(User2Todo.TODO);
            todo.put(Todo.TITLE, title);
            todo.put(Todo.DESCRIPTION, description);
            todo.put(Todo.PLACE, place);
            todo.put(Todo.START_TIME, startTime);
            todo.put(Todo.END_TIME, endTime);
            todo.put(Todo.LEVEL, level);
            todo.put(Todo.STATE, state);
            user2todo.put(User2Todo.SWITCH, switchIconView.isIconEnabled());
            user2todo.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    try {
                        DBUser2Todo dbUser2Todo = DBUtils.getDbManager().findById(DBUser2Todo.class,user2todo.getInt("id"));
                        DBUser2Todo.delete(dbUser2Todo);
                        user2todo.put("id",DBUser2Todo.save(user2todo));
                    } catch (DbException e1) {
                        e1.printStackTrace();
                    }
                    InformDialog.inform(TodoDetailActivity.this, null, "System Message", "modified successful!");
                }
            });
        }
    }

}
