package com.martsforever.owa.timekeeper.main.self;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.martsforever.owa.timekeeper.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

@ContentView(R.layout.activity_security)
public class SecurityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    @Event(R.id.security_back_btn)
    private void back(View view) {
        this.finish();
    }

    @Event(R.id.security_reset_password_text)
    private void resetPassword(View view) {
        System.out.println("reset password");
    }

    @Event(R.id.security_change_email_text)
    private void changeEmail(View view) {
        System.out.println("change email");
    }

    @Event(R.id.security_change_mobile_text)
    private void changeMobile(View view) {
        System.out.println("change mobile");
    }


    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SecurityActivity.class);
        context.startActivity(intent);
    }

}
