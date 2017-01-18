package com.martsforever.owa.timekeeper.launch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by StormLion on 2016/5/6 16:57.
 */
public class IndicatorView extends View{

    private static final String LOG_TAG = IndicatorView.class.getName();
//    当前标签页
    private int currentIndex = 0;
//    标签页的个数，初始化为0，当LaunchActivity初始化的时候设置标签页的个数
    private int count = 0;
//    indicater之间的距离
    private int distance = 30; // 圆点之间的距离（圆心距）
//    以View左上角为原点，开始绘制indicater的横轴距离
    private int x;
//    以View左上角为原点，开始绘制indicater的纵轴距离
    private int y;
//    indicater被选中时的颜色
    private int selectedColor = 0xff58d2e8;
//    indicater未被选中时的颜色
    private int unselectedColor = 0xfff2b6b6;

    private Paint paint;

    public IndicatorView(Context context) {
        super(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs){
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        int drawWidth = (count + 1) * distance;
//        水平居中
        x = (viewWidth - drawWidth) / 2;
//        垂直居中
        y = viewHeight / 2;

        setMeasuredDimension(viewWidth, viewHeight) ;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float currentX = x;
        for(int i = 0; i < count; i++) {
            currentX += distance;
            if(i == currentIndex) {
                paint.setColor(selectedColor);
            } else {
                paint.setColor(unselectedColor);
            }
            canvas.drawCircle(currentX, y, 10, paint);
        }
    }

    public void setIndicatorCount(int count) {
        this.count = count;
    }

    public void setCurIndicatorIndex(int index) {
        currentIndex = index;
        invalidate();
    }
}
