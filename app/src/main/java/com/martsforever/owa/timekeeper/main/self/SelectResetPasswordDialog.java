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

import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.register.RetrivePasswordByEmailDialog;
import com.martsforever.owa.timekeeper.register.RetrivePasswordByMobileDialog;

/**
 * Created by OWA on 2017/4/19.
 */

public class SelectResetPasswordDialog extends Dialog implements View.OnClickListener {

    private Activity activity;
    private AlertDialog dialog;
    /*横向占屏比*/
    private double widthProportion = 0.8;

    private Button resetByEmailBtn;
    private Button resetByMobileBtn;

    public SelectResetPasswordDialog(@NonNull Context context, Activity activity) {
        super(context);
        this.activity = activity;
        Window window = initDialog();
        initView(window);
    }

    private Window initDialog() {
        dialog = new android.app.AlertDialog.Builder(activity).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_reset_password_select);
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
        resetByEmailBtn = (Button) window.findViewById(R.id.reset_password_by_email);
        resetByMobileBtn = (Button) window.findViewById(R.id.reset_password_by_mobile);
        resetByEmailBtn.setOnClickListener(this);
        resetByMobileBtn.setOnClickListener(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_password_by_email:
                dismiss();
                new RetrivePasswordByEmailDialog(activity,activity);
                break;
            case R.id.reset_password_by_mobile:
                dismiss();
                new RetrivePasswordByMobileDialog(activity,activity);
                break;
        }
    }
}
