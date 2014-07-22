package com.lookstudio.anywhere.view;


import com.lookstudio.anywhere.util.LLog;

import android.content.Context;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 为支持点击隐藏划出菜单{@link FMViewSlidingMenu},在onTouch前截获touch事件的Layout
 * @author: zzf
 */
public class LInterceptTouchEventLayout extends RelativeLayout {

	private LViewSlidingMenu mViewSlidingMenu;
	private GestureDetector  detector;
	private float              MIN_XSPAN = 80.0f;
	public LInterceptTouchEventLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LInterceptTouchEventLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		detector = new GestureDetector(context,new GestureDetector.OnGestureListener()
		{

			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				
				LLog.info("(e2X-e1X)=" + (e2.getX()-e1.getX()) + " vX = " + velocityX + " vY = " + velocityY + " sliding menu isShow= " + mViewSlidingMenu.isShow());
				float xSpan = e2.getX()-e1.getX();
				if((xSpan >= MIN_XSPAN) && (!mViewSlidingMenu.isShow()))
				{
					mViewSlidingMenu.show();	
				}
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				if(mViewSlidingMenu.isShow())
				{
					mViewSlidingMenu.hide();
				}
				return false;
			}
			
		});
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		return detector.onTouchEvent(ev);
		/*//show or hide
		if(mFmViewSlidingMenu.isShow())
		{
			mFmViewSlidingMenu.hide();
		}
		return false;*/
	}


	public void setSlidingMenu(LViewSlidingMenu slidingMenu) {
		mViewSlidingMenu = slidingMenu;
	}
}