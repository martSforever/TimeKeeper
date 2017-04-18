package com.martsforever.owa.timekeeper.main.self;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVUser;
import com.martsforever.owa.timekeeper.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_person_info)
public class PersonInfoActivity extends AppCompatActivity {

    @ViewInject(R.id.self_username_edit)
    private EditText useranmeEdit;

    AVUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    @Event(R.id.self_username_edit_btn)
    private void editUsername(View view) {
        useranmeEdit.setEnabled(true);
        useranmeEdit.setFocusable(true);
    }

    private void initView() {
        user = AVUser.getCurrentUser();
        useranmeEdit.setText(user.getUsername());
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PersonInfoActivity.class);
        context.startActivity(intent);
    }
}
