package com.martsforever.owa.timekeeper.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

/**
 * Created by owa on 2017/1/14.
 */

public class RetrivePasswordByEmailDialog extends Dialog {

    private Activity activity;
    private AlertDialog dialog;

    private EditText emailEdit;
    private Button submitBtn;

    /*横向占屏比*/
    double widthProportion = 0.8;

    public RetrivePasswordByEmailDialog(Context context, final Activity activity) {
        super(context);
        this.activity = activity;

        dialog = new android.app.AlertDialog.Builder(context).create();
        dialog.show();

        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_retrive_password_by_email);

        /*就是这个属性导致不能获取焦点,默认的是FLAG_NOT_FOCUSABLE,故名思义不能获取输入焦点*/
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        /*设置对话框的宽高*/
        Window dialogWindow = dialog.getWindow();
        WindowManager manager = activity.getWindowManager();
        /*获取屏幕的宽高信息*/
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams parameter = dialogWindow.getAttributes();
        parameter.width = (int) (display.getWidth() * widthProportion);
        dialogWindow.setAttributes(parameter);

        emailEdit = (EditText) window.findViewById(R.id.retrive_email_email_edit);

        submitBtn = (Button) window.findViewById(R.id.retrive_email_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailEdit.getText().toString().trim();
                AVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            ShowMessageUtil.tosatSlow("reset password request is successful,please go to the " + email + " mailbox to reset password", activity);
                        } else {
                            ShowMessageUtil.tosatSlow("request failure!:" + e.getMessage(), activity);
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
