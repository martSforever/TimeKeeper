package com.martsforever.owa.timekeeper.register;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.main.MainActivity;
import com.martsforever.owa.timekeeper.util.ActivityManager;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_get_vertification_code_btn:
                if (!checkRegisterInfoIsCorrect(this)) break;
                else register(this);
                break;
            case R.id.register_submit_btn:
                if (!checkMobileAndCodeIsCorrect(this)) break;
                else verifiedCode(this);

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

    /**
     * verify vertification code
     *
     * @param context
     */
    private void verifiedCode(final Context context) {
        String code = vertificationCodeEdit.getText().toString().trim();

        AVUser.verifyMobilePhoneInBackground(code, new AVMobilePhoneVerifyCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ShowMessageUtil.tosatFast("verify vertification code successfully!", context);
                    ActivityManager.entryMainActivity(MobileRegisterActivity.this, MainActivity.class);
                } else {
                    ShowMessageUtil.tosatSlow("verify vertification code failure!" + e.getMessage(), context);
                }
            }
        });
    }

    /**
     * check wether the input is corrent
     *
     * @param context
     * @return
     */
    private boolean checkMobileAndCodeIsCorrect(Context context) {
        String mobile = mobilePhoneEdit.getText().toString().trim();
        String code = vertificationCodeEdit.getText().toString().trim();
        if (mobile.length() != 11 || code.equals("")) {
            ShowMessageUtil.tosatSlow("mobile phone or code is wrong!", context);
            return false;
        }
        return true;
    }

    /**
     * check wether the register info is correct
     *
     * @param context
     * @return
     */
    private boolean checkRegisterInfoIsCorrect(Context context) {
        String username = usernameEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String repeatPassword = repeatpasswordEdit.getText().toString().trim();
        String mobile = mobilePhoneEdit.getText().toString().trim();
        if (username.equals("") || password.equals("") || repeatPassword.equals("") || mobile.equals("")) {
            ShowMessageUtil.tosatSlow("every item can not be empty!", context);
            return false;
        }
        if (!password.equals(repeatPassword)) {
            ShowMessageUtil.tosatSlow("Enter passwords differ!", context);
            return false;
        }
        if (username.length() > 10) {
            ShowMessageUtil.tosatSlow("the character length of the username can't be over than 10", context);
            return false;
        }
        return true;
    }

    /**
     * register by mobile
     *
     * @param context
     */
    private void register(final Context context) {
        String username = usernameEdit.getText().toString().trim();
        final String mobile = mobilePhoneEdit.getText().toString().trim();
        AVQuery<AVUser> query = new AVQuery<>(Person.TABLE_PERSON);
        query.whereEqualTo(Person.USER_NAME, username);
        query.whereEqualTo(Person.MOBILE_PHONE_NUMBER, mobile);
        query.getFirstInBackground(new GetCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    if (avUser == null) {
                        final AVUser user = new AVUser();
                        user.setUsername(usernameEdit.getText().toString().trim());
                        user.setPassword(passwordEdit.getText().toString().trim());
                        user.setMobilePhoneNumber(mobilePhoneEdit.getText().toString().trim());
                        user.put(Person.NICK_NAME,user.getUsername());
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    ShowMessageUtil.tosatFast(user.getUsername() + " register successful! id:" + user.getObjectId() + " request sms code successful, please check your mobile message.", context);
                                } else {
                                    ShowMessageUtil.tosatFast("register failure! " + e.getMessage(), context);
                                }
                            }
                        });
                    } else {
                        AVUser.requestMobilePhoneVerifyInBackground(mobile, new RequestMobileCodeCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    ShowMessageUtil.tosatFast("request mobile verify code successful!", context);
                                } else {
                                    ShowMessageUtil.tosatFast("request mobile verify code failure!"+e.getMessage(), context);
                                }
                            }
                        });
                    }
                } else {
                    ShowMessageUtil.tosatSlow("fail to access data! " + e.getMessage(), context);
                }
            }
        });
    }
}
