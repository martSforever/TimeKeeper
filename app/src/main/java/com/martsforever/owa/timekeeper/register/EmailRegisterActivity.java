package com.martsforever.owa.timekeeper.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

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
//                check if it has been registered
                BmobQuery<Person> query = new BmobQuery<Person>();
                query.addWhereEqualTo("email", editEmail.getText().toString().trim());
                query.findObjects(new FindListener<Person>() {
                    @Override
                    public void done(List<Person> list, BmobException e) {
                        if (e == null) {
//                            if the email is not registered
                            if (list.size() == 0) {
                                register();
                            } else if (!list.get(0).getEmailVerified()) {
                                openValidateDialog();
                            }else {
                                ShowMessageUtil.tosatFast("the email has been registered and verified!", EmailRegisterActivity.this);
                            }
                        } else {
                            ShowMessageUtil.tosatFast("fail to access data!"+e.getMessage(), EmailRegisterActivity.this);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    public void register() {
        String username = editUsername.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        registerByEmail(username, email, password);
    }

    private void registerByEmail(String username, String email, String password) {
        Person person = new Person();
        person.setUsername(username);
        person.setEmail(email);
        person.setPassword(password);
        person.signUp(new SaveListener<Person>() {
            @Override
            public void done(Person person, BmobException e) {
                if (e == null) {
                    ShowMessageUtil.tosatFast("register successful!", EmailRegisterActivity.this);
                    openValidateDialog();
                } else {
                    ShowMessageUtil.tosatSlow("fail to register:" + e.getMessage(), EmailRegisterActivity.this);
                }
            }
        });
    }

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
        return true;
    }

    private void openValidateDialog(){
        final ValidateEmailDialog dialog = new ValidateEmailDialog(EmailRegisterActivity.this, EmailRegisterActivity.this,editEmail.getText().toString().trim());
    }
}
