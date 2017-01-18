package com.martsforever.owa.timekeeper.main;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.martsforever.owa.timekeeper.R;

import java.util.List;

/**
 * Created by owa on 2017/1/18.
 */

public class MainInterfacePagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private List<View> views;
    private MainActivity mainActivity;
    private ImageView underlineImg;

    private int screenWidth;

    public MainInterfacePagerAdapter(List<View> views, MainActivity mainActivity) {
        this.views = views;
        this.mainActivity = mainActivity;

        Display display = mainActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        screenWidth = dm.widthPixels;
        underlineImg = (ImageView) mainActivity.findViewById(R.id.label_underline);
        ViewGroup.LayoutParams params = underlineImg.getLayoutParams();
        params.width = screenWidth / mainActivity.getPagerItems().size();
        underlineImg.setLayoutParams(params);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position), 0);
        return views.get(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

//            获取每一个标签页所占宽度
        int section = screenWidth / views.size();
//            currentIndex为上一次滑动所处标签页位置(0,1,2)
        Animation animation = new TranslateAnimation(
                section * mainActivity.getCurrentIndex(), section * position, 0, 0);
//            重新设置当前页
        mainActivity.setCurrentIndex(position);

        animation.setDuration(250);
//            动画结束后停留在当前所处位置
        animation.setFillAfter(true);
        underlineImg.startAnimation(animation);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
