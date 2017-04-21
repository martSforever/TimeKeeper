package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.martsforever.owa.timekeeper.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

@ContentView(R.layout.activity_add_todos)
public class AddTodosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }


    public static void actionStart(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, AddTodosActivity.class);
        activity.startActivityForResult(intent, 0);
    }

    @Event(R.id.todo_add_back_btn)
    private void back(View view){finish();}
    @Event(R.id.todo_add_cancel_btn)
    private void cancel(View view){finish();}



}
