package com.martsforever.owa.timekeeper.main.self;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

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
        new SelectResetPasswordDialog(this, this);
    }

    @Event(R.id.security_bind_email_text)
    private void bindEmail(View view) {
        AVUser cuerrentUser = AVUser.getCurrentUser();
        if (cuerrentUser.getBoolean(Person.EMAIL_VERIFIED)) {
            ShowMessageUtil.tosatSlow("You have already binded and veritified the email: " + cuerrentUser.getEmail(), SecurityActivity.this);
        } else {
            new BindEmailDialog(this, this);
        }
    }

    @Event(R.id.security_bind_mobile_text)
    private void bindMobile(View view) {
        AVUser cuerrentUser = AVUser.getCurrentUser();
        if (cuerrentUser.getBoolean(Person.MOBILE_PHONE_VERIFIED)) {
            ShowMessageUtil.tosatSlow("You have already binded and veritified the mobile: " + cuerrentUser.getMobilePhoneNumber(), SecurityActivity.this);
        } else {
            new BindMobileDialog(this, this);
        }
    }


    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SecurityActivity.class);
        context.startActivity(intent);
    }

}
