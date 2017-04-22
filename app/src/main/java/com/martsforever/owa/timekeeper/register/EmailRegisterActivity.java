package com.martsforever.owa.timekeeper.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SignUpCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import java.util.List;

public class EmailRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editUsername;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editRepeatPassword;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        initView();
    }

    private void initView() {
        editUsername = (EditText) findViewById(R.id.register_username_edit);
        editEmail = (EditText) findViewById(R.id.register_email_edit);
        editPassword = (EditText) findViewById(R.id.register_password_edit);
        editRepeatPassword = (EditText) findViewById(R.id.register_repeat_password_edit);
        btnSubmit = (Button) findViewById(R.id.register_submit_btn);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_submit_btn:

                if (!validateInput()) break;
                AVQuery<AVUser> query = new AVQuery<>(Person.TABLE_PERSON);
                query.whereEqualTo(Person.EMAIL, editEmail.getText().toString().trim());
                query.findInBackground(new FindCallback<AVUser>() {
                    @Override
                    public void done(List<AVUser> list, AVException e) {
                        if (e == null) {
//                            if the email is not registered
                            if (list.size() == 0) {
                                registerByEmail();
                            } else if (!list.get(0).getBoolean(Person.EMAIL_VERIFIED)) {
                                openValidateDialog();
                            } else {
                                ShowMessageUtil.tosatFast("the email has been registered and verified!", EmailRegisterActivity.this);
                            }
                        } else {
                            ShowMessageUtil.tosatFast("fail to access data!" + e.getMessage(), EmailRegisterActivity.this);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * register by email process
     */
    private void registerByEmail() {

        String username = editUsername.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        AVUser person = new AVUser();
        person.setUsername(username);
        person.setEmail(email);
        person.setPassword(password);
        person.put(Person.NICK_NAME,username);

        person.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ShowMessageUtil.tosatFast("register successful!", EmailRegisterActivity.this);
                    openValidateDialog();
                } else {
                    ShowMessageUtil.tosatSlow("fail to register:" + e.getMessage(), EmailRegisterActivity.this);
                }
            }
        });
    }

    /** verify user input data
     * @return
     */
    private boolean validateInput() {
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String repeatPassword = editRepeatPassword.getText().toString().trim();
        if (!password.equals(repeatPassword)) {
            ShowMessageUtil.tosatSlow("Enter passwords differ", EmailRegisterActivity.this);
            return false;
        } else if (username.equals("") || email.equals("") || password.equals("")) {
            ShowMessageUtil.tosatSlow("every item can not be empty!", EmailRegisterActivity.this);
            return false;
        }
        if (username.length()>10){
            ShowMessageUtil.tosatSlow("the character length of the username can't be over than 10", EmailRegisterActivity.this);
            return false;
        }
        return true;
    }

    /**
     * open verify email dialog, send verify email
     */
    private void openValidateDialog() {
        final ValidateEmailDialog dialog = new ValidateEmailDialog(EmailRegisterActivity.this, EmailRegisterActivity.this, editEmail.getText().toString().trim());
    }
}
