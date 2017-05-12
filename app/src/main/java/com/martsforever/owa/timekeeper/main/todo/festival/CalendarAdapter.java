package com.martsforever.owa.timekeeper.main.todo.festival;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.javabean.User2Todo;
import com.martsforever.owa.timekeeper.leanCloud.TimeKeeperApplication;
import com.martsforever.owa.timekeeper.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by owa on 2017/1/9.
 */

public class CalendarAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<CalendarItem> currentCalendarItemList = new ArrayList<>();
    private List<CalendarItem> allCalendarItemList = new ArrayList<>();
    private Map<String, CalendarItem> calendarItemMap = new HashMap<>();
    private OnItemClickListener onItemClickListener;
    private List<QBadgeView> qBadgeViewList = new ArrayList<>();
    private int year;
    private int month;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CalendarAdapter.this.notifyDataSetChanged();
        }
    };

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CalendarAdapter(Context context, int year, int month) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.year = year;
        this.month = month;
        initCurrentCalendarItemList();
        initBadgeView();
    }

    @Override
    public int getCount() {
        return allCalendarItemList == null ? 0 : allCalendarItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.calendar_item, null);
            viewHolder = new ViewHolder();
            viewHolder.dayOfDate = (TextView) convertView.findViewById(R.id.textDayOfDate);
            viewHolder.dayOfDate.setTextSize(20);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.desc);
            viewHolder.desc.setTextSize(10);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        final CalendarItem item = allCalendarItemList.get(position);

        viewHolder.dayOfDate.setText(item.getDayOfdate() + "");
        if (item.getDesc() != null) {
            viewHolder.desc.setVisibility(View.VISIBLE);
            viewHolder.desc.setText(item.getDesc());
        } else {
            viewHolder.desc.setVisibility(View.INVISIBLE);
        }

        if (item.getCueerntMonth() == null) {
            viewHolder.dayOfDate.setTextColor(Color.parseColor("#000000"));
            viewHolder.desc.setTextColor(Color.parseColor("#000000"));
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        } else if (!item.getCueerntMonth()) {
            viewHolder.dayOfDate.setTextColor(Color.GRAY);
            viewHolder.desc.setTextColor(Color.GRAY);
            convertView.setBackgroundColor(Color.parseColor("#59b4ca"));
        } else {
            viewHolder.dayOfDate.setTextColor(Color.WHITE);
            viewHolder.desc.setTextColor(Color.WHITE);
            convertView.setBackgroundColor(Color.parseColor("#59b4ca"));
        }
        if (onItemClickListener != null) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(year, month, item.getDayOfdate());
                }
            });
        }
        QBadgeView qBadgeView = qBadgeViewList.get(position);
        qBadgeView.setBadgeNumber(item.getBadgeNumber());
        if (item.getBadgeNumber() == 0) qBadgeView.hide(true);
        else {
            qBadgeView.hide(false);
            qBadgeView.setBadgeNumber(item.getBadgeNumber());
            qBadgeView.bindTarget(viewHolder.dayOfDate);
        }
        return convertView;
    }

    class ViewHolder {
        public TextView dayOfDate;
        public TextView desc;
    }

    private void initCurrentCalendarItemList() {
        allCalendarItemList.clear();
        currentCalendarItemList.clear();
        CalendarUtil.getCurrentCalendarItems(year, month, currentCalendarItemList);
        for (CalendarItem calendarItem : currentCalendarItemList)
            allCalendarItemList.add(calendarItem);
        CalendarUtil.addPrevoousCalendarItems(year, month, allCalendarItemList);
        CalendarUtil.addNextCalendarItems(year, month, allCalendarItemList);
        calendarItemMap.clear();
        for (CalendarItem calendarItem : allCalendarItemList) {
            calendarItemMap.put(year + "-" + calendarItem.getKey(), calendarItem);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FestivalOfMonth> festivalOfMonthList = FestivalUtils.getMonthInfo(year, month);
                for (FestivalOfMonth festivalOfMonth : festivalOfMonthList) {
                    if (calendarItemMap.get(festivalOfMonth.getFestival()) != null) {
                        calendarItemMap.get(festivalOfMonth.getFestival()).setDesc(festivalOfMonth.getName());
                        calendarItemMap.get(festivalOfMonth.getFestival()).setCueerntMonth(null);
                    }
                }
                initBadgeNumber();
                Message message = new Message();
                handler.sendMessage(message);
            }
        }).start();
    }

    private void initBadgeNumber() {
        TimeKeeperApplication application = (TimeKeeperApplication) context.getApplicationContext();
        List<AVObject> user2TodoList = application.getAllUser2todoList();
        for (AVObject user2Todo : user2TodoList) {
            Date time = user2Todo.getAVObject(User2Todo.TODO).getDate(Todo.START_TIME);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            String timeString = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DATE);
            CalendarItem calendarItem = calendarItemMap.get(timeString);
            if (calendarItem != null) {
                calendarItem.setBadgeNumber(calendarItem.getBadgeNumber() + 1);
            }
        }
    }

    private void initBadgeView() {
        for (int i = 0; i < 42; i++) {
            QBadgeView qBadgeView = new QBadgeView(context);
            qBadgeView.setBadgeTextSize(10, true);
            qBadgeView.setGravityOffset(0, 0, true);
            qBadgeView.setBadgeNumber(0);
            qBadgeView.setBadgeBackgroundColor(Color.parseColor("#346E7B"));
            qBadgeViewList.add(qBadgeView);
        }
    }

    public void previous() {
        month--;
        if (month < 1) {
            month = 12;
            year--;
        }
        initCurrentCalendarItemList();
        this.notifyDataSetChanged();
    }

    public void next() {
        month++;
        if (month > 12) {
            month = 1;
            year++;
        }
        initCurrentCalendarItemList();
        this.notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        public void onClick(int year, int month, int day);
    }

}
