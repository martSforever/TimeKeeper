package com.martsforever.owa.timekeeper.main.todo.festival;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.DeleteCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.leanCloud.TimeKeeperApplication;
import com.martsforever.owa.timekeeper.main.MainActivity;
import com.martsforever.owa.timekeeper.main.todo.TodoAdapter;
import com.martsforever.owa.timekeeper.main.todo.TodoDetailActivity;
import com.martsforever.owa.timekeeper.util.DataUtils;
import com.martsforever.owa.timekeeper.util.NetWorkUtils;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_days_todo)
public class DaysTodoActivity extends AppCompatActivity implements SlideAndDragListView.OnListItemClickListener, SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {

    private int year;
    private int month;
    private int day;

    private Handler festivalHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FestivalOfDay festivalOfDay = (FestivalOfDay) msg.obj;
            dateText.setText(festivalOfDay.getLunarYear() + "（" + festivalOfDay.getAnimalsYear() + "年）" + festivalOfDay.getLunar());
            String weekString = festivalOfDay.getWeekday();
            if (festivalOfDay.getHoliday() != null)
                weekString += "（" + festivalOfDay.getHoliday() + "）";
            weekText.setText(weekString);
            suitText.setText("宜：" + festivalOfDay.getSuit());
            avoidText.setText("忌：" + festivalOfDay.getAvoid());
        }
    };

    public static final int TODO_CHANGE = 0x001;
    public static final String USER2TODO_CHANGE = "user2todo change";
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

    @ViewInject(R.id.days_todo_date_text)
    private TextView dateText;
    @ViewInject(R.id.days_todo_week_text)
    private TextView weekText;
    @ViewInject(R.id.days_todo_suit_text)
    private TextView suitText;
    @ViewInject(R.id.days_todo_avoid_text)
    private TextView avoidText;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            user2todos = (List<AVObject>) msg.obj;
            todoAdapter = new TodoAdapter(DaysTodoActivity.this, user2todos);
            initUiAndListener();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
        initFestival();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        day = bundle.getInt("day");
        titleText.setText(year + "-" + month + "-" + day);
        android.os.Message message = new android.os.Message();
        message.obj = ((TimeKeeperApplication) getApplicationContext()).getCategoryUser2todoList();
        handler.sendMessage(message);
    }

    public void initMenu() {
        listViewMenu = new Menu(false, false);
        listViewMenu.addItem(new MenuItem.Builder().setWidth(DataUtils.dp2px(90, DaysTodoActivity.this))
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

    public static void actionStart(Activity activity, List<AVObject> categoryUser2todo, int year, int month, int day) {
        ((TimeKeeperApplication) activity.getApplicationContext()).setCategoryUser2todoList(categoryUser2todo);
        Intent intent = new Intent();
        intent.setClass(activity, DaysTodoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        bundle.putInt("day", day);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, 0);
    }

    @Event(R.id.category_schedule_back_img)
    private void back(View view) {
        backToMainActivity();
    }

    @Override
    public void onBackPressed() {
        backToMainActivity();
    }

    private void backToMainActivity() {
        setResult(MainActivity.USER2TODO_CHANGE, getIntent());
        finish();
    }

    @Override
    public void onListItemClick(View v, int position) {
        TodoDetailActivity.actionStart(DaysTodoActivity.this, user2todos.get(position), position);
    }

    @Override
    public void onItemDelete(View view, int position) {
        user2todos.remove(position - todoListview.getHeaderViewsCount());
        todoAdapter.notifyDataSetChanged();
    }

    @Override
    public int onMenuItemClick(View v, final int itemPosition, int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        if (!NetWorkUtils.isNetworkAvailable(DaysTodoActivity.this)) {
                            NetWorkUtils.showNetworkNotAvailable(DaysTodoActivity.this);
                            return Menu.ITEM_NOTHING;
                        } else {
                            final AVObject user2todo = user2todos.get(itemPosition);
                            user2todo.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(AVException e) {
                                    Intent intent = new Intent(USER2TODO_CHANGE);
                                    intent.putExtra("objectId", user2todo.getObjectId());
                                    DaysTodoActivity.this.sendBroadcast(intent);
                                }
                            });
                            if (getIntent().getStringExtra(INTENT_PARAMETER_TITLE).equals("All"))
                                user2todos.add(itemPosition, user2todo);
                            return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                        }
                }
        }
        return Menu.ITEM_NOTHING;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case TODO_CHANGE:
                todoAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void initFestival() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FestivalOfDay festivalOfDay = FestivalUtils.getDayInfo(year, month, day);
                Message message = new Message();
                message.obj = festivalOfDay;
                festivalHandler.sendMessage(message);
            }
        }).start();
    }
}
