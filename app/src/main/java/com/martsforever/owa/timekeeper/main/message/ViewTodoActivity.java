package com.martsforever.owa.timekeeper.main.message;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.javabean.User2Todo;
import com.martsforever.owa.timekeeper.main.todo.CategoryTodoActivity;
import com.martsforever.owa.timekeeper.main.todo.TodoDetailActivity;
import com.martsforever.owa.timekeeper.util.DateUtil;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_view_todo)
public class ViewTodoActivity extends AppCompatActivity {

    /*data*/
    public static final String ACTION_START_PARAMETER_USER2TODO = "todo";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
        peopleInit();
        initView();
        setEditable(false);
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
        startTimeEdit.setText(DateUtil.date2String(todo.getDate(Todo.START_TIME), DateUtil.COMPLICATED_DATE));
        endTimeEdit.setText(DateUtil.date2String(todo.getDate(Todo.END_TIME), DateUtil.COMPLICATED_DATE));
        descriptionEdit.setText(todo.getString(Todo.DESCRIPTION));
        createdByEdit.setText(user2todo.getAVUser(User2Todo.USER).getString(Person.NICK_NAME));
    }

    private void setEditable(boolean enable) {
        titleEdit.setEnabled(enable);
        placeEdit.setEnabled(enable);
        descriptionEdit.setEnabled(enable);
    }

    public static void actionStart(Activity activity, AVObject user2todo, int position) {
        Intent intent = new Intent();
        intent.setClass(activity, ViewTodoActivity.class);
        intent.putExtra(CategoryTodoActivity.INTENT_PARAMETER_POSITION, position);
        intent.putExtra(TodoDetailActivity.ACTION_START_PARAMETER_USER2TODO, user2todo.toString());
        activity.startActivityForResult(intent, 0);
    }

    @Event(R.id.todo_detail_back_btn)
    private void back(View view) {
        finish();
    }
    private void peopleInit() {
        AVQuery<AVObject> query = new AVQuery<>(User2Todo.TABLE_USER_2_TODO);
        query.whereEqualTo(User2Todo.TODO, user2todo.getAVObject(User2Todo.TODO));
        query.include(User2Todo.USER+"."+ Person.NICK_NAME);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> user2todoList, AVException e) {
                if (e != null) {
                    ShowMessageUtil.tosatSlow(e.getMessage(), ViewTodoActivity.this);
                } else {
                    String people = "";
                    for (AVObject user2todo : user2todoList) {
                        people += user2todo.getAVUser(User2Todo.USER).getString(Person.NICK_NAME)+",";
                    }
                    peopleEdit.setText(people.substring(0,people.length()-1));
                }
            }
        });
    }
}
