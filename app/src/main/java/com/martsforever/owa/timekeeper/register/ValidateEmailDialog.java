package com.martsforever.owa.timekeeper.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.main.MainActivity;
import com.martsforever.owa.timekeeper.util.ActivityManager;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by owa on 2017/1/13.
 */

public class ValidateEmailDialog extends Dialog {

    /*这是个验证邮箱的dialog，所以在初始化的时候需要传入邮箱地址*/
    String email;

    Activity activity;
    Context context;
    AlertDialog dialog;
    TextView msg;
    Button positiveButton;
    Button negativeBUtton;

    /*信息显示大小*/
    int textSize = 20;
    /*横向占屏比*/
    double widthProportion = 0.8;
    /*纵向占屏比*/
    double heightProportion = 0.6;

    public ValidateEmailDialog(Context context, final Activity activity, final String email) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.email = email;
        dialog = new android.app.AlertDialog.Builder(context).create();
        dialog.show();

        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_register_validate_email);
        msg = (TextView) window.findViewById(R.id.validate_dialog_email_text);
        msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        msg.setText("We have sent the validation information to your  registered email,please check.the verify email sent to you will become invalidate within a week");

        /*设置对话框的宽高*/
        Window dialogWindow = dialog.getWindow();
        WindowManager manager = activity.getWindowManager();
        /*获取屏幕的宽高信息*/
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams parameter = dialogWindow.getAttributes();
        parameter.height = (int) (display.getHeight() * heightProportion);
        parameter.width = (int) (display.getWidth() * widthProportion);
        dialogWindow.setAttributes(parameter);

        positiveButton = (Button) window.findViewById(R.id.validate_dialog_email_positive_btn);
        positiveButton.setText("I have checked the email");
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<Person> query = new BmobQuery<Person>();
                query.addWhereEqualTo("email", email);
                query.findObjects(new FindListener<Person>() {
                    @Override
                    public void done(List<Person> list, BmobException e) {
                        if (e == null) {
//                            if the email is not registered
                            if (!list.get(0).getEmailVerified()) {
                                msg.setText("Are you sure you have opened the verification link in your email?We haven't received any messages yet");
                            } else {
                                ShowMessageUtil.tosatFast("verify pass!", activity);
                                ActivityManager.entryMainActivity(activity,MainActivity.class);
                            }
                        } else {
                            ShowMessageUtil.tosatFast("fail to access data!" + e.getMessage(), activity);
                        }
                    }
                });

            }
        });

        negativeBUtton = (Button) window.findViewById(R.id.validate_dialog_email_negative_btn);
        negativeBUtton.setText("I don't receive the velidate email");
        negativeBUtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person.requestEmailVerify(email, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            msg.setText("request email validation success!please go to " + email + " mailbox to active!");
                        } else {
                            ShowMessageUtil.tosatFast("fail to access data!" + e.getMessage(), activity);
                        }
                    }
                });
            }
        });


    }

    public void dismiss() {
        dialog.dismiss();
    }
}
