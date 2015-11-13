package com.justdoit.pics.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * frame layout类型的layout里面包含多个可滚动view
 * Created by mengwen on 2015/11/13.
 */
public class MultiSwipeRefreshLayout extends SwipeRefreshLayout {

    private View[] childViews = null; // frame layout类型的layout里面的views

    private boolean isSwipeUp = true; // 是否顶部下拉

    public MultiSwipeRefreshLayout(Context context) {
        super(context);
    }

    public MultiSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化child views
     * @param ids view id
     */
    public void setChildViews(final int... ids) {
        assert ids != null;

        if (ids.length > 0) {
            childViews = new View[ids.length];
        }

        for (int i = 0; i < ids.length; i++) {
            childViews[i] = findViewById(ids[i]);
        }
    }

    public boolean isSwipeUp() {
        return isSwipeUp;
    }

    public void setIsSwipeUp(boolean isSwipeUp) {
        this.isSwipeUp = isSwipeUp;
    }

    @Override
    public boolean canChildScrollUp() {
        int direction; // 默认顶部下拉

        if (isSwipeUp) {
            direction = -1; // 顶部下拉
        } else {
            direction = 1; // 底部上拉
        }

        for (View v : childViews) {
            return ViewCompat.canScrollVertically(v, direction);
        }

        return super.canChildScrollUp();
    }
}
