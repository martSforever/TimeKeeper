package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.FriendShip;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.javabean.User2Todo;
import com.martsforever.owa.timekeeper.util.DateUtil;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
        initView();
        initFriendshipsData();
    }

    private void initData() {
        Intent intent = getIntent();
        try {
            user2todo = AVObject.parseAVObject(intent.getStringExtra(TodoDetailActivity.ACTION_START_PARAMETER_USER2TODO));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        startTimeEdit.setText(DateUtil.date2String(todo.getDate(Todo.START_TIME),DateUtil.COMPLICATED_DATE));
        endTimeEdit.setText(DateUtil.date2String(todo.getDate(Todo.END_TIME),DateUtil.COMPLICATED_DATE));
        descriptionEdit.setText(todo.getString(Todo.DESCRIPTION));
    }

    private void initFriendshipsData() {
        AVQuery<AVObject> query = new AVQuery<>(FriendShip.TABLE_FRIENDSHIP);
        query.whereEqualTo(FriendShip.SELF, AVUser.getCurrentUser());
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

    public static void actionStart(Activity activity, AVObject user2todo) {
        Intent intent = new Intent();
        intent.setClass(activity, TodoDetailActivity.class);
        intent.putExtra(TodoDetailActivity.ACTION_START_PARAMETER_USER2TODO, user2todo.toString());
        activity.startActivity(intent);
    }

    @Event(R.id.todo_add_people_pick_btn)
    private void multiPickPeople(View view) {
        if (friendships != null) {
            List<String> names = new ArrayList<>();
            for (AVObject avObject : friendships)
                names.add(avObject.getString(FriendShip.FRIEND_NAME));
            MultiPickDialog dialog = new MultiPickDialog(this, names);
            dialog.setTitle("Pick Friends");
            dialog.setOnItemOkListener(new MultiPickDialog.OnItemOkListener() {
                @Override
                public void OnOk(List<Boolean> isCheckedList) {
                    String peopleNames = "";
                    for (int position = 0; position < isCheckedList.size(); position++)
                        if (isCheckedList.get(position)) {
                            peopleNames += friendships.get(position).getString(FriendShip.FRIEND_NAME);
                            peopleNames += ",";
                        }
                    peopleEdit.setText(peopleNames.substring(0, peopleNames.length() - 1));
                }
            });
        } else {
            ShowMessageUtil.tosatSlow("System error!", TodoDetailActivity.this);
        }
    }
}
