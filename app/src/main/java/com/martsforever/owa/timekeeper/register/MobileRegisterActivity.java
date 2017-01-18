package com.martsforever.owa.timekeeper.register;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.bmob.BmobUtil;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.main.MainActivity;
import com.martsforever.owa.timekeeper.util.ActivityManager;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MobileRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText repeatpasswordEdit;
    private EditText mobilePhoneEdit;
    private EditText vertificationCodeEdit;
    private Button getVertificationCodeBtn;
    private Button submitBtn;

    private final int waitTime = 10;

    private Handler waitHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int leftTime = msg.what;
            if (leftTime > 0) {
                getVertificationCodeBtn.setText(leftTime + " second left");
                getVertificationCodeBtn.setClickable(false);
            } else {
                getVertificationCodeBtn.setText("get vertification code");
                getVertificationCodeBtn.setClickable(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile);
        initView();
        initData();

    }

    private void initView() {
        usernameEdit = (EditText) findViewById(R.id.register_username_edit);
        passwordEdit = (EditText) findViewById(R.id.register_password_edit);
        repeatpasswordEdit = (EditText) findViewById(R.id.register_repeat_password_edit);
        mobilePhoneEdit = (EditText) findViewById(R.id.register_mobile_phone_edit);
        vertificationCodeEdit = (EditText) findViewById(R.id.register_vertification_code_edit);
        getVertificationCodeBtn = (Button) findViewById(R.id.register_get_vertification_code_btn);
        submitBtn = (Button) findViewById(R.id.register_submit_btn);
        getVertificationCodeBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    private void initData() {
        BmobUtil.bmobInitialize(this);
        BmobUtil.bmobSmsInitialize(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_get_vertification_code_btn:
                String mobileNumber = mobilePhoneEdit.getText().toString().trim();
                if (mobileNumber.equals("")) {
                    ShowMessageUtil.tosatFast("Phone number can not be empty!", this);
                    break;
                }
                sendVertificationCode(mobileNumber, this);
                waitForMinute();
                break;
            case R.id.register_submit_btn:
                if (!checkWetherTheInputIsCorrent(this)) break;
                register(this);

        }
    }

    /*set button 60 seconds not available*/
    private void waitForMinute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int time = waitTime;
                    while (time > 0) {
                        Message message = new Message();
                        message.what = --time;
                        waitHandler.sendMessage(message);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*send vertification code*/
    private void sendVertificationCode(String mobile, final Context context) {
        BmobSMS.requestSMSCode(this, mobile, "默认模板", new RequestSMSCodeListener() {

            @Override
            public void done(Integer integer, cn.bmob.sms.exception.BmobException e) {
                if (e == null) {
                    ShowMessageUtil.tosatFast("SMS vertification code sent successfully!", context);
                } else {
                    ShowMessageUtil.tosatSlow("SMS failure!" + e.getMessage(), context);
                }
            }
        });
    }

    /*check wether the input is corrent*/
    private boolean checkWetherTheInputIsCorrent(Context context) {
        String username = usernameEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String repeatPassword = repeatpasswordEdit.getText().toString().trim();
        String mobile = mobilePhoneEdit.getText().toString().trim();
        String code = vertificationCodeEdit.getText().toString().trim();
        if (username.equals("") || password.equals("") || repeatPassword.equals("") || mobile.equals("") || code.equals("")) {
            ShowMessageUtil.tosatSlow("every item can not be empty!", context);
            return false;
        }
        if (!password.equals(repeatPassword)) {
            ShowMessageUtil.tosatSlow("Enter passwords differ!", context);
        }
        return true;
    }

    /*register and login by mobile*/
    private void register(final Context context) {
        String mobile = mobilePhoneEdit.getText().toString().trim();
        String code = vertificationCodeEdit.getText().toString().trim();

        Person person = new Person();
        person.setUsername(usernameEdit.getText().toString().trim());
        person.setPassword(passwordEdit.getText().toString().trim());
        person.setMobilePhoneNumber(mobile);

        person.signOrLogin(code, new SaveListener<Person>() {
            @Override
            public void done(Person person, BmobException e) {
                if (e == null) {
                    ShowMessageUtil.tosatFast("register successful!", context);
                    ActivityManager.entryMainActivity(MobileRegisterActivity.this, MainActivity.class);
                } else {
                    ShowMessageUtil.tosatFast("register failure! " + e.getMessage(), context);
                }
            }
        });
    }
}
