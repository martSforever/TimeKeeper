package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.martsforever.owa.timekeeper.R;

public class FestivalTodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_todo);
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, FestivalTodoActivity.class);
        activity.startActivity(intent);
    }
}
