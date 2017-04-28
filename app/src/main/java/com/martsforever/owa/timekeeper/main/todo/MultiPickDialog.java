package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.martsforever.owa.timekeeper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OWA on 2017/4/19.
 */

public class MultiPickDialog implements View.OnClickListener {


    private List<String> strings;
    private Activity activity;
    private List<Boolean> isCheckedList;

    private TextView titleText;
    private ListView multiPickListView;
    private Button okBtn;
    private Button cancelBtn;
    private OnItemOkListener onItemOkListener;
    private OnItemCancelListener onItemCancelListener;
    private AlertDialog dialog;

    public void setOnItemOkListener(OnItemOkListener onItemOkListener) {
        this.onItemOkListener = onItemOkListener;
    }

    public void setOnItemCancelListener(OnItemCancelListener onItemCancelListener) {
        this.onItemCancelListener = onItemCancelListener;
    }

    public MultiPickDialog(Activity activity, List<String> strings) {
        this.activity = activity;
        this.strings = strings;
        initView(initDialog());
    }

    private View initDialog() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.dialog_multi_pick, null);
        dialog = new AlertDialog.Builder(activity).setView(convertView).show();
        return convertView;
    }

    private void initView(View view) {
        isCheckedList = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) isCheckedList.add(false);

        titleText = (TextView) view.findViewById(R.id.dialog_multi_pick_title_text);
        multiPickListView = (ListView) view.findViewById(R.id.dialog_multi_pick_list_view);
        cancelBtn = (Button) view.findViewById(R.id.dialog_multi_pick_cancel_btn);
        okBtn = (Button) view.findViewById(R.id.dialog_multi_pick_ok_btn);
        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        multiPickListView.setAdapter(new MultiPickListAdapter(activity, strings, isCheckedList));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_multi_pick_ok_btn:
                if (onItemOkListener != null)
                    onItemOkListener.OnOk(isCheckedList);
                dialog.dismiss();
                break;
            case R.id.dialog_multi_pick_cancel_btn:
                if (onItemCancelListener != null)
                    onItemCancelListener.onCancel();
                dialog.dismiss();
                break;
        }
    }


    interface OnItemOkListener {
        public void OnOk(List<Boolean> isCheckedList);
    }

    interface OnItemCancelListener {
        public void onCancel();
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }

    class MultiPickListAdapter extends BaseAdapter {

        private Context context;
        private List<String> strings;
        private LayoutInflater layoutInflater;
        private List<Boolean> isCheckedList;

        public MultiPickListAdapter(Context context, List<String> strings, List<Boolean> isCheckedList) {
            this.strings = strings;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
            this.isCheckedList = isCheckedList;
        }

        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public Object getItem(int position) {
            return strings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_multi_pick_list, null);
                viewHolder = new ViewHolder();
                viewHolder.stringText = (TextView) convertView.findViewById(R.id.item_multi_pick_list_string_text);
                viewHolder.chedkedImg = (ImageView) convertView.findViewById(R.id.item_multi_pick_list_checked_img);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.stringText.setText(strings.get(position));
            setChecked(convertView, viewHolder, isCheckedList.get(position));
            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isCheckedList.set(position, !isCheckedList.get(position));
                    setChecked(finalConvertView, viewHolder, isCheckedList.get(position));
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView stringText;
            ImageView chedkedImg;
        }

        private void setChecked(View convertView, ViewHolder viewHolder, boolean isChecked) {
            if (isChecked) {
                viewHolder.stringText.setTextColor(Color.WHITE);
                convertView.setBackgroundColor(0xff41899a);
                viewHolder.chedkedImg.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_selected));
            } else {
                viewHolder.stringText.setTextColor(Color.BLACK);
                convertView.setBackgroundColor(Color.WHITE);
                viewHolder.chedkedImg.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_unselected));
            }
        }
    }

}
