package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.util.DataUtils;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_category_todo)
public class OfflineTodoActivity extends AppCompatActivity implements SlideAndDragListView.OnListItemClickListener, SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {


    public static final int TODO_CHANGE = 0x001;
    public static final String INTENT_PARAMETER_USER2TODO = "user2todo";
    public static final String INTENT_PARAMETER_POSITION = "position";
    public static final String INTENT_PARAMETER_CATEGORY_USER2TODO = "category user2todo";
    public static final String INTENT_PARAMETER_TITLE = "title";

    List<AVObject> user2todos;
    Menu listViewMenu;
    TodoAdapter todoAdapter;
    @ViewInject(R.id.todo_all_listview)
    SlideAndDragListView<AVObject> todoListview;
    @ViewInject(R.id.category_schedule_title_text)
    TextView titleText;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            user2todos = (List<AVObject>) msg.obj;
            todoAdapter = new TodoAdapter(OfflineTodoActivity.this, user2todos);
            initUiAndListener();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        titleText.setText(bundle.getCharSequence(INTENT_PARAMETER_TITLE));
        ArrayList<CharSequence> charSequences = bundle.getCharSequenceArrayList(INTENT_PARAMETER_CATEGORY_USER2TODO);
        List<AVObject> allUser2todo = new ArrayList<>();
        for (CharSequence charSequence : charSequences) {
            try {
                allUser2todo.add(AVObject.parseAVObject(charSequence.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        android.os.Message message = new android.os.Message();
        message.obj = allUser2todo;
        handler.sendMessage(message);
    }

    public void initMenu() {
        listViewMenu = new Menu(false, false);
        listViewMenu.addItem(new MenuItem.Builder().setWidth(DataUtils.dp2px(90, OfflineTodoActivity.this))
                .setBackground((new ColorDrawable(Color.argb(0xdd, 222, 140, 104))))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setIcon(getResources().getDrawable(R.drawable.icon_delete))
                .build());
    }

    public void initUiAndListener() {
        initMenu();
        todoListview.setMenu(listViewMenu);
        todoListview.setAdapter(todoAdapter);
        todoListview.setOnListItemClickListener(this);
        todoListview.setOnMenuItemClickListener(this);
        todoListview.setOnItemDeleteListener(this);
    }

    public static void actionStart(Activity activity, List<AVObject> categoryUser2todo, String title) {
        Intent intent = new Intent();
        intent.setClass(activity, OfflineTodoActivity.class);
        Bundle bundle = new Bundle();
        ArrayList<CharSequence> charSequences = new ArrayList<>();
        for (AVObject user2todo : categoryUser2todo)
            charSequences.add(user2todo.toString());
        bundle.putCharSequenceArrayList(INTENT_PARAMETER_CATEGORY_USER2TODO, charSequences);
        bundle.putCharSequence(INTENT_PARAMETER_TITLE, title);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, 0);
    }

    @Event(R.id.category_schedule_back_img)
    private void back(View view) {
        finish();
    }

    @Override
    public void onListItemClick(View v, int position) {
        OfflineTodoDetailActivity.actionStart(OfflineTodoActivity.this, user2todos.get(position), position);
    }

    @Override
    public void onItemDelete(View view, int position) {
        user2todos.remove(position - todoListview.getHeaderViewsCount());
        todoAdapter.notifyDataSetChanged();
    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        ShowMessageUtil.tosatFast("delete", this);
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                }
        }
        return Menu.ITEM_NOTHING;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case TODO_CHANGE:
                String user2todoString = data.getStringExtra(INTENT_PARAMETER_USER2TODO);
                try {
                    AVObject user2todo = AVObject.parseAVObject(user2todoString);
                    int position = data.getIntExtra(INTENT_PARAMETER_POSITION, 0);
                    user2todos.remove(position);
                    user2todos.add(position, user2todo);
                    todoAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
