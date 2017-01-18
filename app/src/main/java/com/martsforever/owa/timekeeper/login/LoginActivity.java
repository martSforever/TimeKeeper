package com.martsforever.owa.timekeeper.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.register.RetrivePasswordActivity;
import com.martsforever.owa.timekeeper.register.SelectRegisterActivity;
import com.martsforever.owa.timekeeper.util.ActivityManager;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView registerText;
    TextView forgetPasswordText;
    EditText emailOrMobileEdit;
    EditText passwordEdit;
    Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        registerText = (TextView) findViewById(R.id.login_register_text);
        forgetPasswordText = (TextView) findViewById(R.id.login_forget_password_text);
        emailOrMobileEdit = (EditText) findViewById(R.id.login_mobile_or_email_edit);
        passwordEdit = (EditText) findViewById(R.id.login_password_edit);
        loginBtn = (Button) findViewById(R.id.login_submit_btn);
        registerText.setOnClickListener(this);
        forgetPasswordText.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register_text:
                entryRegisterActivity();
                break;
            case R.id.login_forget_password_text:
                ActivityManager.entryRetrivePasswordActivity(this, RetrivePasswordActivity.class);
                break;
            case R.id.login_submit_btn:
                login();
                break;
        }
    }

    /**
     * entry register activity
     */
    private void entryRegisterActivity() {
        Intent intent = new Intent();
        intent.setClass(this, SelectRegisterActivity.class);
        startActivity(intent);
        ActivityManager.addDestoryActivity(this, LoginActivity.class.getName());
    }

    /**
     * login by email
     */
    private void login() {

        Person.loginByAccount(emailOrMobileEdit.getText().toString().trim(), passwordEdit.getText().toString().trim(), new LogInListener<Person>() {
            @Override
            public void done(Person person, BmobException e) {
                if (person != null) {
                    ShowMessageUtil.tosatFast("login success!", LoginActivity.this);
                } else {
                    ShowMessageUtil.tosatSlow("login failure! " + e.getMessage(), LoginActivity.this);
                }
            }
        });
    }
}
