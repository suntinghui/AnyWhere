package com.lookstudio.anywhere.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;

public class LFlippingView extends FrameLayout {




	private int scn_index = 0;
	private GestureDetector gestureDetector;
	private  ViewFlipper         viewFlipper;
	private int   childViewCount;
	
	public LFlippingView(Context context)
	{
		this(context,null);
	}
	
	public LFlippingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		gestureDetector = new GestureDetector(getContext(),flingHandler);
		initViews();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{	
		return gestureDetector.onTouchEvent(event);
	}
	
	private void initViews()
	{
		viewFlipper = new FlightViewFlipper(getContext());
		addView(viewFlipper);
	}
	
	public void addView(View view)
	{
		viewFlipper.addView(view);
		childViewCount++;
	}
	
	private AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
	private SimpleOnGestureListener flingHandler= new SimpleOnGestureListener()
	{
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			//left
			if(e1.getX() - e2.getX() < 0)
			{
				if(scn_index > 0)
				{
					viewFlipper.setInAnimation(getLeftInAnimation(accelerateInterpolator));
					viewFlipper.setOutAnimation(getRightOutAnimation(accelerateInterpolator));
					scn_index --;
					
					viewFlipper.showPrevious();
				}
			}
			else
			{
				if(scn_index == childViewCount - 1)
				{
					//startMainScreen();
				}
				else
				{
					viewFlipper.setInAnimation(getRightInAnimation(accelerateInterpolator) );
					viewFlipper.setOutAnimation(getLeftOutAnimation(accelerateInterpolator) );
					scn_index ++;
					
					viewFlipper.showNext();
				}
			}
			

			return true;
		}
	};
	
	private final int ANIM_DURATION = 350;
	
	/**
     * 定义从右侧进入的动画效果
     * @return
     */
    private Animation getRightInAnimation(AccelerateInterpolator accInter) {
            Animation inFromRight = new TranslateAnimation(
                            Animation.RELATIVE_TO_PARENT, +1.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f);
            inFromRight.setDuration(ANIM_DURATION);
            inFromRight.setInterpolator(accInter);
            return inFromRight;
    }

    /**
     * 定义从左侧退出的动画效果
     * @return
     */
    private Animation getLeftOutAnimation(AccelerateInterpolator accInter) {
            Animation outtoLeft = new TranslateAnimation(
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, -1.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f);
            outtoLeft.setDuration(ANIM_DURATION);
            outtoLeft.setInterpolator(accInter);
            return outtoLeft;
    }

    /**
     * 定义从左侧进入的动画效果
     * @return
     */
    private Animation getLeftInAnimation(AccelerateInterpolator accInter) {
            Animation inFromLeft = new TranslateAnimation(
                            Animation.RELATIVE_TO_PARENT, -1.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f);
            inFromLeft.setDuration(ANIM_DURATION);
            inFromLeft.setInterpolator(accInter);
            return inFromLeft;
    }


    /**
     * 定义从右侧退出时的动画效果
     * @return
     */
    private Animation getRightOutAnimation(AccelerateInterpolator accInter) {
            Animation outtoRight = new TranslateAnimation(
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, +1.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f);
            outtoRight.setDuration(ANIM_DURATION);
            outtoRight.setInterpolator(accInter);
            return outtoRight;
    }
	

}
