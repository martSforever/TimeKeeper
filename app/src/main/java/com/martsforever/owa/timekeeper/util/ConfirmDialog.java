package com.martsforever.owa.timekeeper.util;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.martsforever.owa.timekeeper.R;

/**
 * Created by OWA on 2017/4/30.
 */

public class ConfirmDialog {
    private Activity activity;
    private TextView titleText;
    private TextView messageText;
    private Button okBtn;
    private Button cancelBtn;

    private AlertDialog dialog;
    View.OnClickListener onOkBtnClickListene;
    View.OnClickListener onCancelClickListener;
    private String title;
    private String message;

    private static ConfirmDialog informDialog;

    public ConfirmDialog(Activity activity, View.OnClickListener onOkBtnClickListene, View.OnClickListener onCancelClickListener, String title, String message) {
        this.activity = activity;
        this.onOkBtnClickListene = onOkBtnClickListene;
        this.onCancelClickListener = onCancelClickListener;
        this.title = title;
        this.message = message;
        initView(initDialog());
    }

    private View initDialog() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.dialog_confirm, null);
        dialog = new AlertDialog.Builder(activity).setView(convertView).show();
        return convertView;
    }

    private void initView(View view) {
        titleText = (TextView) view.findViewById(R.id.dialog_confirm_title_text);
        messageText = (TextView) view.findViewById(R.id.dialog_confirm_message_text);
        okBtn = (Button) view.findViewById(R.id.dialog_confirm_ok_btn);
        cancelBtn = (Button) view.findViewById(R.id.dialog_confirm_cancel_btn);
        if (onOkBtnClickListene == null)
            onOkBtnClickListene = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dissmiss();
                }
            };
        okBtn.setOnClickListener(onOkBtnClickListene);
        if (onCancelClickListener == null)
            onCancelClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dissmiss();
                }
            };
        cancelBtn.setOnClickListener(onCancelClickListener);
        titleText.setText(title);
        messageText.setText(message);
    }

    public static void inform(Activity activity, View.OnClickListener onOkBtnClickListene, View.OnClickListener onCancelClickListener, String title, String message) {
        informDialog = new ConfirmDialog(activity, onOkBtnClickListene, onCancelClickListener, title, message);
    }

    public static void dissmiss() {
        if (informDialog != null) {
            informDialog.dialog.dismiss();
        }
    }
}
