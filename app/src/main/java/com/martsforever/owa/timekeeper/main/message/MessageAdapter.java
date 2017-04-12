package com.martsforever.owa.timekeeper.main.message;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Message;
import com.martsforever.owa.timekeeper.util.DateUtil;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by OWA on 2017/4/10.
 */

public class MessageAdapter extends BaseAdapter {

    List<AVObject> messages;
    LayoutInflater layoutInflater;
    Context context;

    public MessageAdapter(List<AVObject> messages, Context context) {
        this.messages = messages;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_message_swip_list, null);
            viewHolder = new ViewHolder();
            viewHolder.titleText = (TextView) convertView.findViewById(R.id.item_message_title_txt);
            viewHolder.isReadText = (TextView) convertView.findViewById(R.id.item_message_isread_txt);
            viewHolder.verifyMessageText = (TextView) convertView.findViewById(R.id.item_message_verify_message_txt);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.item_message_time_txt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AVObject message = messages.get(position);
        viewHolder.titleText.setText(message.get(Message.MESSAGE_TYPE).toString());
        viewHolder.verifyMessageText.setText("Verify message: " + message.get(Message.VERIFY_MESSAGE).toString());
        viewHolder.timeText.setText(DateUtil.dateToString((Date) (message.get(Message.TIME)), DateUtil.COMPLICATED_DATE));
        int isRead = message.getInt(Message.IS_READ);
        switch (isRead){
            case Message.READ:
                viewHolder.isReadText.setTextColor(0xff376956);
                viewHolder.isReadText.setBackgroundColor(Color.argb(0x00, 255, 255, 255));
                viewHolder.isReadText.setText("READ");
                viewHolder.isReadText.setOnClickListener(null);
                break;
            case Message.UNREAD:
                viewHolder.isReadText.setTextColor(0xaaffffff);
                viewHolder.isReadText.setBackgroundColor(Color.argb(0xff, 89, 180, 202));
                viewHolder.isReadText.setText("UNREAD");
                viewHolder.isReadText.setOnClickListener(mOnClickListener);
                break;
            case Message.REJECT:
                viewHolder.isReadText.setTextColor(Color.argb(0xff, 179, 24, 0));
                viewHolder.isReadText.setBackgroundColor(Color.argb(0x00, 255, 255, 255));
                viewHolder.isReadText.setOnClickListener(null);
                viewHolder.isReadText.setText("REJECT");
                break;
            case Message.ACCEPT:
                viewHolder.isReadText.setTextColor(Color.argb(0xff, 73, 90, 128));
                viewHolder.isReadText.setBackgroundColor(Color.argb(0x00, 255, 255, 255));
                viewHolder.isReadText.setOnClickListener(null);
                viewHolder.isReadText.setText("ACCEPT");
                break;
        }

        viewHolder.isReadText.setTag(position);
        return convertView;
    }

    class ViewHolder {
        TextView titleText;
        TextView isReadText;
        TextView verifyMessageText;
        TextView timeText;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final Object o = v.getTag();
            if (o instanceof Integer) {
                AVObject message = messages.get((Integer)o);
                message.put(Message.IS_READ,Message.READ);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null){
                            v.setBackgroundColor(Color.argb(0x00, 255, 255, 255));
                            ((TextView)v).setTextColor(0xff376956);
                            ((TextView)v).setText("READ");
                        }else {
                            ShowMessageUtil.tosatFast(e.getMessage(),context);
                        }
                    }
                });
            }
        }
    };
}
