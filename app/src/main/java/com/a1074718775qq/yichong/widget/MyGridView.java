package com.a1074718775qq.yichong.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * create by 刘晓童
 * on 2018/1/31 0031
 */
public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

    @Override
    public int getNumColumns() {//可以不复写，但必须在xml中声明android:numColumns="3"
        return 3;
    }
}
