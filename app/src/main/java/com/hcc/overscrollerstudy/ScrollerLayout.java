package com.hcc.overscrollerstudy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;
import android.widget.Scroller;

public class ScrollerLayout extends ViewGroup {
    private OverScroller mScroller;//滚动计算辅助类
    private int mTouchSlop;//拖动的最小滑动像素
    private int leftBoard;//左边界
    private int rightBoard;//右边界
    private float mXDown;//按下的坐标
    private float mLastMove;//上次触发move的坐标
    private float mXMove;//手指现在所在的屏幕坐标


    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //创建Scroller实例
        mScroller = new OverScroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量子view
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //为ViewGroup中的每个子view测量大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                //为每个view 在水平方向上进行布局
                childView.layout(i * childView.getMeasuredWidth(), 0, (i + 1) * childView.getMeasuredWidth(), childView.getMeasuredHeight());
            }
            //初始化左右边界
            leftBoard = getChildAt(0).getLeft();
            rightBoard = getChildAt(getChildCount() - 1).getRight();
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                mLastMove = mXDown;
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mLastMove = mXMove;
                //拖动的手指距离大于mTouchSlop，拦截子View的事件
                if (diff > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
                int scrolledX = (int) (mLastMove -mXMove);
                if (getScrollX()+scrolledX<leftBoard){//超过
                     scrollTo(leftBoard,0);
                     return true;
                }else if (getScrollX()+scrolledX+getWidth()>rightBoard){
                    scrollTo(rightBoard-getWidth(),0);
                    return true;
                }
                scrollBy(scrolledX,0);
                mLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起  根据滚动值判断应该滚动到哪个子View的界面
                int targetIndex = (getScrollX()+getWidth()/2)/getWidth();
                int dx = targetIndex*getWidth() - getScrollX();
                mScroller.startScroll(getScrollX(),0,dx,0);
                invalidate();
                break;
        }


        return super.onTouchEvent(event);

    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
    }
}
