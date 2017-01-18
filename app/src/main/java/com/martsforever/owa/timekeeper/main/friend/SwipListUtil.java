package com.martsforever.owa.timekeeper.main.friend;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.martsforever.owa.timekeeper.R;

/**
 * Created by owa on 2017/1/18.
 */

public class SwipListUtil {


    /** create the style of swip menu
     * @param activity
     * @return
     */
    public static SwipeMenuCreator getFriendsMenuCreater(final Activity activity){
        return new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                /*add delete swip menu item*/
                SwipeMenuItem deleteItem = new SwipeMenuItem(activity.getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90,activity));
                deleteItem.setIcon(R.drawable.icon_delete);
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
