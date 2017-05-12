package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVObject;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.javabean.User2Todo;
import com.martsforever.owa.timekeeper.leanCloud.TimeKeeperApplication;
import com.martsforever.owa.timekeeper.main.todo.festival.CalendarAdapter;
import com.martsforever.owa.timekeeper.main.todo.festival.DaysTodoActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@ContentView(R.layout.activity_festival_todo)
public class FestivalTodoActivity extends AppCompatActivity {

    @ViewInject(R.id.festival_gridview)
    private GridView gridView;
    @ViewInject(R.id.festival_date_text)
    private TextView dateText;

    int year = Calendar.getInstance().get(Calendar.YEAR);
    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    CalendarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        adapter = new CalendarAdapter(this, year, month);
        adapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onClick(int year, int month, int day) {
                System.out.println(year + "-" + month + "-" + day);
                List<AVObject> daysUser2todoList = new ArrayList<AVObject>();
                List<AVObject> allUser2todoList = ((TimeKeeperApplication) getApplicationContext()).getAllUser2todoList();
                for (AVObject user2todo : allUser2todoList) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(user2todo.getAVObject(User2Todo.TODO).getDate(Todo.START_TIME));
                    if (calendar.get(Calendar.YEAR) == year && (calendar.get(Calendar.MONTH)+1) == month && calendar.get(Calendar.DATE) == day) {
                        daysUser2todoList.add(user2todo);
                    }
                }
                DaysTodoActivity.actionStart(FestivalTodoActivity.this, daysUser2todoList, year, month, day);
            }
        });
        gridView.setAdapter(adapter);
        dateText.setText(year + "-" + month);
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, FestivalTodoActivity.class);
        activity.startActivity(intent);
    }

    @Event(R.id.festival_previous_btn)
    private void previousMonth(View view) {
        adapter.previous();
        dateText.setText(adapter.getYear() + "-" + adapter.getMonth());
    }

    @Event(R.id.festival_next_btn)
    private void nextMonth(View view) {
        adapter.next();
        dateText.setText(adapter.getYear() + "-" + adapter.getMonth());
    }

    @Event(R.id.festival_back_btn)
    private void back(View view) {
        finish();
    }

}
