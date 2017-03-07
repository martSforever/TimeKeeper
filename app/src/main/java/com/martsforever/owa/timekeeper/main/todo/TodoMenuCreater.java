package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.martsforever.owa.timekeeper.R;

/**
 * Created by owa on 2017/1/24.
 */

public class TodoMenuCreater {
    /** create the style of swip menu
     * @param activity
     * @return
     */
    public static SwipeMenuCreator getFriendsMenuCreater(final Activity activity){
        return new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                /*add operation swip menu item*/
                SwipeMenuItem operationItem = new SwipeMenuItem(activity.getApplicationContext());
                operationItem.setBackground(new ColorDrawable(Color.argb(0xdd,129, 194, 214)));
                operationItem.setWidth(dp2px(90,activity));
                operationItem.setIcon(R.drawable.icon_operation);
                menu.addMenuItem(operationItem);
                /*add delete swip menu item*/
                SwipeMenuItem deleteItem = new SwipeMenuItem(activity.getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.argb(0xdd,222, 140, 104)));
                deleteItem.setWidth(dp2px(90,activity));
                deleteItem.setIcon(R.drawable.icon_trash);
                /*deleteItem.setTitle("delete");*/
                menu.addMenuItem(deleteItem);
            }
        };
    }

    private static int dp2px(int dp,Activity activity) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                activity.getResources().getDisplayMetrics());
    }
}
