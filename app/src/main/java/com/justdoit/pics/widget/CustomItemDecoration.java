package com.justdoit.pics.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * recycler view 的分隔线
 * Created by mengwen on 2015/11/11.
 */
public class CustomItemDecoration extends RecyclerView.ItemDecoration {

    private final String TAG = "CustomItemDecoration";

    private int mSize = 1; // 分隔线粗细

    private Paint mPaint;

    /**
     * 创建paint，然后设置颜色
     */
    public CustomItemDecoration() {
        mPaint = new Paint();

        mPaint.setColor(Color.GRAY);
    }

    public CustomItemDecoration(int mSize, Paint mPaint) {
        this.mSize = mSize;
        this.mPaint = mPaint;
    }

    public int getmSize() {
        return mSize;
    }

    public void setmSize(int mSize) {
        this.mSize = mSize;
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        // top:设置为item的底部，加上layout margin
        // bottom:设置为top + 分隔线粗细
        // left:child view的left，加上padding left
        // right:left 加上child view的width和减去padding right
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            float left = child.getLeft() + child.getPaddingLeft();
            float top = child.getBottom() + layoutParams.bottomMargin;
            float right = left + child.getWidth() - child.getPaddingRight();
            float bottom = top + mSize;

            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}
