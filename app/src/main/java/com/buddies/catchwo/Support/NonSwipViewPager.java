package com.buddies.catchwo.Support;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.buddies.catchwo.Adapter.ProfAdapter;

import java.lang.reflect.Field;

public class NonSwipViewPager extends ViewPager {
    public NonSwipViewPager(@NonNull Context context) {
        super(context);
        setMyScroller();
    }

    public NonSwipViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();

    }

    private void setMyScroller() {
        try{
            Class<?> viewpage = ViewPager.class;
            Field scroller = viewpage.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this,new MyScroller(getContext()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    private class MyScroller extends Scroller {

        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy,int duration) {
            super.startScroll(startX, startY, dx, dy,350);
        }
    }
}
