package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.martsforever.owa.timekeeper.R;

import java.util.List;

/**
 * Created by OWA on 2017/4/19.
 */

public class PickDialog implements View.OnClickListener, WheelPicker.OnItemSelectedListener {

    private List<Object> data;
    private Activity activity;

    private TextView titleText;
    private TextView valueText;
    private WheelPicker wheelPicker;
    private Button cancelBtn;
    private Button okBtn;
    private OnItemOkListener onItemOkListener;
    private OnItemCancelListener onItemCancelListener;
    private AlertDialog dialog;

    public void setOnItemOkListener(OnItemOkListener onItemOkListener) {
        this.onItemOkListener = onItemOkListener;
    }

    public void setOnItemCancelListener(OnItemCancelListener onItemCancelListener) {
        this.onItemCancelListener = onItemCancelListener;
    }

    public PickDialog(Activity activity, List<Object> data) {
        this.activity = activity;
        this.data = data;
        initView(initDialog());
    }

    private View initDialog() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.dialog_pick, null);
        dialog = new AlertDialog.Builder(activity).setView(convertView).show();
        return convertView;
    }

    private void initView(View view) {
        wheelPicker = (WheelPicker) view.findViewById(R.id.dialog_pick_select_list);
        wheelPicker.setData(data);
        if (wheelPicker.getVisibleItemCount() > 7)
            wheelPicker.setSelectedItemPosition(wheelPicker.getVisibleItemCount() / 2);
        wheelPicker.setOnItemSelectedListener(this);

        titleText = (TextView) view.findViewById(R.id.dialog_pick_title_text);
        valueText = (TextView) view.findViewById(R.id.dialog_pick_value_text);
        cancelBtn = (Button) view.findViewById(R.id.dialog_pick_cancel_btn);
        okBtn = (Button) view.findViewById(R.id.dialog_pick_ok_btn);
        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        valueText.setText(data.get(wheelPicker.getCurrentItemPosition()).toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_pick_ok_btn:
                if (onItemOkListener != null)
                    onItemOkListener.OnOk(data.get(wheelPicker.getCurrentItemPosition()), wheelPicker.getCurrentItemPosition());
                dialog.dismiss();
                break;
            case R.id.dialog_pick_cancel_btn:
                if (onItemCancelListener != null)
                    onItemCancelListener.onCancel();
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        valueText.setText(data.toString());
    }

    interface OnItemOkListener {
        public void OnOk(Object object, int position);
    }

    interface OnItemCancelListener {
        public void onCancel();
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }

}
