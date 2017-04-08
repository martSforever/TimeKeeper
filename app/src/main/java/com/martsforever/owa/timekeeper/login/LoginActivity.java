package com.martsforever.owa.timekeeper.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.leanCloud.LeanCloudUtil;
import com.martsforever.owa.timekeeper.main.MainActivity;
import com.martsforever.owa.timekeeper.register.RetrivePasswordActivity;
import com.martsforever.owa.timekeeper.register.SelectRegisterActivity;
import com.martsforever.owa.timekeeper.util.ActivityManager;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

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
     * login by username or mobile
     */
    private void login() {

        String account = emailOrMobileEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        System.out.println("account=" + account + ", password=" + password);


        /*if the character length of the account is greater than 10, this is mobile login process, otherwise this is the username login process*/
        if (account.length() > 10) loginByMobile(account, password);
        else loginByUsername(account, password);
    }


    /**
     * login by username and password
     *
     * @param username
     * @param password
     */
    private void loginByUsername(String username, String password) {
        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                loginProcessing(avUser, e);
            }
        });
    }

    /**
     * login by mobile and password
     *
     * @param mobile
     * @param password
     */
    private void loginByMobile(String mobile, String password) {
        AVUser.loginByMobilePhoneNumberInBackground(mobile, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                loginProcessing(avUser, e);
            }
        });
    }

    /**
     * handle login result
     *
     * @param avUser
     * @param e
     */
    private void loginProcessing(AVUser avUser, AVException e) {
        if (e == null) {
            ShowMessageUtil.tosatFast(avUser.getUsername() + " login success!", LoginActivity.this);
            ActivityManager.entryMainActivity(LoginActivity.this, MainActivity.class);
            /*sign in successful, bind the installation id of the device */
        } else {
            ShowMessageUtil.tosatSlow("login failure! " + e.getMessage(), LoginActivity.this);
        }
    }

}
