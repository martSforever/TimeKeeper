package com.martsforever.owa.timekeeper.main.self;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.util.ActivityManager;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import java.util.regex.Pattern;

/**
 * Created by OWA on 2017/4/19.
 */

public class BindMobileDialog extends Dialog implements View.OnClickListener {

    private Activity activity;
    private AlertDialog dialog;
    /*横向占屏比*/
    private double widthProportion = 0.8;

    private EditText bindMobileEdit;
    private EditText bindVerifyCodeEdit;
    private Button getVerifyCodeBtn;
    private Button submitBtn;

    public BindMobileDialog(@NonNull Context context, Activity activity) {
        super(context);
        this.activity = activity;
        initView(initDialog());
    }

    private Window initDialog() {
        dialog = new android.app.AlertDialog.Builder(activity).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_bind_mobile);
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
        return window;
    }

    private void initView(Window window) {
        bindMobileEdit = (EditText) window.findViewById(R.id.bind_mobile_edit);
        bindVerifyCodeEdit = (EditText) window.findViewById(R.id.bind_mobile_verify_code_edit);
        getVerifyCodeBtn = (Button) window.findViewById(R.id.bind_mobile_verify_code_get_btn);
        submitBtn = (Button) window.findViewById(R.id.bind_mobile_submit_btn);
        getVerifyCodeBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_mobile_verify_code_get_btn:
                String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
                final String mobile = bindMobileEdit.getText().toString().trim();
                if (Pattern.matches(regex, mobile)) {
                    final AVUser user = AVUser.getCurrentUser();
                    user.setMobilePhoneNumber(mobile);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                ShowMessageUtil.tosatSlow("Send verify code successful, Please enter verify code!", activity);
                            } else {
                                ShowMessageUtil.tosatSlow(e.getMessage(), activity);
                            }
                        }
                    });
                } else {
                    ShowMessageUtil.tosatSlow("Incorrect mobile phone number!", activity);
                }
                break;
            case R.id.bind_mobile_submit_btn:
                AVUser.verifyMobilePhoneInBackground(bindVerifyCodeEdit.getText().toString().trim(), new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e ==null){
                            ShowMessageUtil.tosatSlow("Verify code correct, you have to signin again!", activity);
                            ActivityManager.destroyAllActivity();
                            activity.finish();
                        }else {
                            ShowMessageUtil.tosatSlow(e.getMessage(),activity);
                        }
                    }
                });
                break;
        }
    }
}
