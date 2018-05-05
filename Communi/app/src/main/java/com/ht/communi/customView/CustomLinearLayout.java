package com.ht.communi.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/5/3.
 */

public class CustomLinearLayout extends LinearLayout {
    private int mLastX;
    private int mLastY;

    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 分别记录上次滑动的坐标
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //这里将返回值设为false是为了让子View可以接受到事件
                //如果为true则父容器将处理整个事件，子View将接受不到事件
                intercepted = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //根据x轴，y轴方向增量值判断(可以根据不同的规则进行修改)
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                intercepted = false;
                break;
            }
            default:
                break;
        }

        mLastXIntercept = x;
        mLastYIntercept = y;

        return intercepted;
    }



    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        int x = (int) ev.getX();
//        int y = (int) ev.getY();
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                /**
//                 * 子View的所有父ViewGroup都会跳过onInterceptTouchEvent的回调
//                 * 相当于ViewGroup的流程是dispatchTouchEvent() --> onTouchEvent()
//                 */
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int deltaX = x - mLastX;
//                int deltaY = y - mLastY;
//                if (Math.abs(deltaX) > Math.abs(deltaY) + 5) {//水平滑动，使得父类可以执行onInterceptTouchEvent
//                    /**
//                     * 不让所有的父容器跳过onInterceptTouchEvent()
//                     * 相当于ViewGroup的流程是:dispatchTouchEvent() --> onInterceptTouchEvent() --> onTouchEvent()
//                     * 也就是ViewGroup拦截以后交给自己的onTouchEvent()来处理
//                     */
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                break;
//        }
//        mLastX = x;
//        mLastY = y;
//        return super.dispatchTouchEvent(ev);
//    }

}
