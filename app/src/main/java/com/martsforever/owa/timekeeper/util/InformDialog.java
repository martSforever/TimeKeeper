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

public class InformDialog {
    private Activity activity;
    private TextView titleText;
    private TextView messageText;
    private Button okBtn;
    private AlertDialog dialog;
    View.OnClickListener onOkBtnClickListene;
    private String title;
    private String message;

    private static InformDialog informDialog;

    public InformDialog(Activity activity, View.OnClickListener onOkBtnClickListene, String title, String message) {
        this.activity = activity;
        this.onOkBtnClickListene = onOkBtnClickListene;
        this.title = title;
        this.message = message;
        initView(initDialog());
    }

    private View initDialog() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.dialog_inform, null);
        dialog = new AlertDialog.Builder(activity).setView(convertView).show();
        return convertView;
    }

    private void initView(View view) {
        titleText = (TextView) view.findViewById(R.id.dialog_inform_title_text);
        messageText = (TextView) view.findViewById(R.id.dialog_inform_message_text);
        okBtn = (Button) view.findViewById(R.id.dialog_inform_ok_btn);
        if (onOkBtnClickListene == null)
            onOkBtnClickListene = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dissmiss();
                }
            };
        okBtn.setOnClickListener(onOkBtnClickListene);
        titleText.setText(title);
        messageText.setText(message);
    }

    public static void inform(Activity activity, View.OnClickListener onOkBtnClickListene, String title, String message) {
        informDialog = new InformDialog(activity, onOkBtnClickListene, title, message);
    }

    public static void dissmiss() {
        if (informDialog != null) {
            informDialog.dialog.dismiss();
        }
    }
}
