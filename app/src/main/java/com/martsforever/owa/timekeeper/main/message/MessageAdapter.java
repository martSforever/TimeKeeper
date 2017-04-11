package com.martsforever.owa.timekeeper.main.message;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Message;
import com.martsforever.owa.timekeeper.main.common.BaseSwipListAdapter;
import com.martsforever.owa.timekeeper.main.push.MessageHandler;
import com.martsforever.owa.timekeeper.util.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by OWA on 2017/4/10.
 */

public class MessageAdapter extends BaseSwipListAdapter {

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
        String isRead = message.get(Message.IS_READ).toString();
        viewHolder.isReadText.setText(isRead);
        if (isRead.equals(Message.READ)) {
            viewHolder.isReadText.setTextColor(0x6600ff00);
        } else {
            viewHolder.isReadText.setTextColor(0x88ff0000);
        }
        return convertView;
    }

    class ViewHolder {
        TextView titleText;
        TextView isReadText;
        TextView verifyMessageText;
        TextView timeText;
    }
}
