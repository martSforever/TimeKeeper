package com.martsforever.owa.timekeeper.main.message;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Message;

public class MessageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
    }

    public static void actionStart(Context context, String messageId){
        Intent intent = new Intent();
        intent.setClass(context, MessageDetailActivity.class);
        intent.putExtra("messageId",messageId);
        context.startActivity(intent);
    }
}
