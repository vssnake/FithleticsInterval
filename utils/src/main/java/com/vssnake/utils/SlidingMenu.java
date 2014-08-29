package com.vssnake.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by unai on 27/08/2014.
 */
public class SlidingMenu {

    FrameLayout mSlidingFrame;
    RelativeLayout.LayoutParams mLayoutParams;
    MaxSliding mMaxSliding;
    int mFractionSliding;
    int mDefaultMargin;
    Context mContext;

    public interface SlidingEvents{
       void onMovementFinished(boolean slideOpen);
    }

    SlidingEvents mSlidingEventListener;

    public void setEvents(SlidingEvents eventsListener){
        this.mSlidingEventListener = eventsListener;
    }
    public static final String TAG ="SlidingMenu";

    public enum MaxSliding{
        ALL_FRAME,
        FRACTION
    }

    private SlidingMenu(Context context,FrameLayout frame,MaxSliding maxSliding,int fractionSliding,
                        int defaultMargin){
        mContext = context;
        mSlidingFrame = frame;
        mMaxSliding = maxSliding;
        mFractionSliding = fractionSliding;
        mDefaultMargin = defaultMargin;

        setInitialMargin();

        mSlidingFrame.setOnTouchListener(onTouchFrame);
    }


    public static SlidingMenu newInstance(Context context,FrameLayout frame,MaxSliding maxSliding,
                                         int fractionSliding,
                            int defaultMargin){
        return new SlidingMenu(context,frame,maxSliding,fractionSliding,defaultMargin);
    }

     public static SlidingMenu newInstance(Context context,FrameLayout frame){
        return new SlidingMenu(context,frame,MaxSliding.ALL_FRAME,0,0);
    }

    private void setInitialMargin(){
        if (mLayoutParams== null){
            mLayoutParams = (RelativeLayout.LayoutParams)mSlidingFrame.getLayoutParams();
        }
        mLayoutParams.topMargin = -mSlidingFrame.getHeight() + mDefaultMargin;
        mLayoutParams.height = mSlidingFrame.getHeight();
        mSlidingFrame.setLayoutParams(mLayoutParams);
        mSlidingFrame.invalidate();

    }

    int mXDelta;
    int mYDelta;
    int mLastY;
    boolean mMenuDown;

    private View.OnTouchListener onTouchFrame = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            RelativeLayout.LayoutParams layoutParams;
            switch (event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:


                    mXDelta = X - mLayoutParams.leftMargin;
                    mYDelta = Y;//mLayoutParams.topMargin +mLayoutParams.height + Y;
                    mLastY = Y;
                    Log.v(TAG, "onTouchFrame | Action Down" + mYDelta);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mMenuDown = (mLastY < Y) ? true:false;
                    moveInfoIntervalFragment(X - mXDelta, Y- mLastY);
                    mLastY = Y;
                    break;
                case MotionEvent.ACTION_UP:
                    endMovement();
                    break;
                default:

            }
            return true;
        }

    };


    public boolean moveInfoIntervalFragment(float x, float y) {

        Log.d(TAG,"on moveInfoIntervalFragment |" +
                " y->" + ((int)y- mLastY) +
                " topMargin->" + mLayoutParams.topMargin);

        if (y+ mLayoutParams.topMargin < -mLayoutParams.height + mDefaultMargin){
            setInitialMargin();
        }else if(mLayoutParams.topMargin + y>= 0){
            mLayoutParams.topMargin = 0;
        }else{
            mLayoutParams.topMargin += (int) y;
        }
        mSlidingFrame.setLayoutParams(mLayoutParams);
        mSlidingFrame.invalidate();

        return true;

    }

    public void openCloseMenu(final boolean open){
        final Scroller scroller = new Scroller(mContext,null);
        final Handler handler = new Handler();
        final int lastTop = mLayoutParams.topMargin;

        if (open){
            scroller.startScroll(0,0,0,mLayoutParams.topMargin,450);
            Log.d(TAG,"on endMovement | set Scroller->" + (mLayoutParams.topMargin));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(scroller.computeScrollOffset()) {
                        handler.postDelayed(this,10);
                    }else{
                        if (mSlidingEventListener!=null){
                            mSlidingEventListener.onMovementFinished(open);
                        }
                    }
                    Log.d(TAG,"on endMovement | Scroller new position->" + (scroller
                            .getCurrY()));
                    mLayoutParams.topMargin =lastTop - scroller.getCurrY();
                    mSlidingFrame.setLayoutParams(mLayoutParams);
                    mSlidingFrame.invalidate();
                }
            },10);
        }else{
            scroller.startScroll(0,0,0,mLayoutParams.height - mDefaultMargin - Math.abs(mLayoutParams.topMargin),450);
            Log.d(TAG,"on endMovement | Scroller new position->" + (scroller.getCurrY()));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(scroller.computeScrollOffset()) {
                        handler.postDelayed(this,10);
                    }else{
                        if (mSlidingEventListener!=null){
                            mSlidingEventListener.onMovementFinished(open);
                        }

                    }
                    Log.d(TAG,"on endMovement | Scroller new position->" + (scroller.getCurrY())
                            + " " + scroller.getStartY());
                    mLayoutParams.topMargin =-scroller.getCurrY() + lastTop;

                    mSlidingFrame.setLayoutParams(mLayoutParams);
                    mSlidingFrame.invalidate();

                }
            },10);
        }
    }

    void endMovement(){

        if (mMenuDown){
            openCloseMenu(true);
        }else{
            openCloseMenu(false);
        }
    }
}
