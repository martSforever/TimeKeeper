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

import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by owa on 2017/1/14.
 */

public class RetrivePasswordByMobileDialog extends Dialog implements View.OnClickListener{

    private Activity activity;
    AlertDialog dialog;

    EditText passwordEdit;
    EditText repeatPasswordEdit;
    EditText mobileEdit;
    EditText vertificationCodeEdit;
    Button codeBtn;
    Button submitBtn;

    /*横向占屏比*/
    double widthProportion = 0.8;

    public RetrivePasswordByMobileDialog(Context context, Activity activity) {
        super(context);
        this.activity = activity;

        dialog = new android.app.AlertDialog.Builder(context).create();
        dialog.show();

        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_retrive_password_by_mobile);

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

        passwordEdit = (EditText) window.findViewById(R.id.retrive_mobile_password_edit);
        repeatPasswordEdit = (EditText) window.findViewById(R.id.retrive_mobile_repeat_password_edit);
        mobileEdit = (EditText) window.findViewById(R.id.retrive_mobile_number_edit);
        vertificationCodeEdit = (EditText) dialogWindow.findViewById(R.id.retrive_mobile_code_edit);
        codeBtn = (Button) window.findViewById(R.id.retrive_mobile_code_btn);
        submitBtn = (Button) window.findViewById(R.id.retrive_mobile_submit_btn);
        codeBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.retrive_mobile_code_btn:sendVertificationCode();break;
            case R.id.retrive_mobile_submit_btn:resetPassword();break;
        }
    }

    private void sendVertificationCode(){
        String mobile = mobileEdit.getText().toString().trim();
        BmobSMS.requestSMSCode(activity,mobile,"默认模板", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e==null){
                    ShowMessageUtil.tosatFast("SMS vertification code sent successfully!", activity);
                }
                else {
                    ShowMessageUtil.tosatSlow("SMS failure!" + e.getMessage(), activity);
                }
            }
        });
    }

    private void resetPassword(){
        if (checkWetherInputIsCorrect()){
            String password = passwordEdit.getText().toString().trim();
            String phoneNumber = mobileEdit.getText().toString().trim();
            String code = vertificationCodeEdit.getText().toString().trim();
            Person.resetPasswordBySMSCode(code, password, new UpdateListener() {
                @Override
                public void done(cn.bmob.v3.exception.BmobException e) {
                    if (e==null){
                        ShowMessageUtil.tosatFast("reset password successfully!", activity);
                    }else {
                        ShowMessageUtil.tosatFast("reset password failure!", activity);
                    }
                }
            });
        }
    }

    private boolean checkWetherInputIsCorrect(){
        String password = passwordEdit.getText().toString().trim();
        String repeatPassword = repeatPasswordEdit.getText().toString().trim();
        String phoneNumber = mobileEdit.getText().toString().trim();
        String code = vertificationCodeEdit.getText().toString().trim();
        if (password.equals("")||repeatPassword.equals("")||phoneNumber.equals("")||code.equals("")) return false;
        return true;
    }
}
