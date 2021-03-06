package com.hcc.overscrollerstudy;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.BounceInterpolator;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class JellyTextView extends AppCompatTextView {
    private OverScroller mScroller;
    private float lastX;
    private float lastY;
    private float startX;
    private float startY;

    public JellyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(context, new BounceInterpolator());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float disX = event.getRawX() - lastX;
                float disY = event.getRawY() - lastY;
                offsetLeftAndRight((int) disX);
                offsetTopAndBottom((int) disY);

                lastX = event.getRawX();
                lastY = event.getRawY();

                break;
            case MotionEvent.ACTION_UP:
                    mScroller.startScroll((int)getX(),(int)getY(),-(int)(getX()-startX),-(int)(getY()-startY));
                    invalidate();
                break;
        }


        return super.onTouchEvent(event);
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            setX(mScroller.getCurrX());
            setY(mScroller.getCurrY());
            invalidate();
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getX();
        startY = getY();
    }


    public void springBack(){
        if (mScroller.springBack((int)getX(),(int)getY(),0,(int)getX(),0,(int)(getY()-100))){
            Log.d("TAG", "getX()=" + getX() + "__getY()=" + getY());
            invalidate();
        }
    }
}
