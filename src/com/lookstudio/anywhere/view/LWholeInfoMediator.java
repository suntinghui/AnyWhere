package com.lookstudio.anywhere.view;

import java.util.Timer;
import java.util.TimerTask;

import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.interfaces.LMediator;
import com.lookstudio.anywhere.model.LTravelProxy.TravelListener;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LWholeInfoMediator implements TravelListener {

	private TextView mDistanceView;
	private TextView mTimeView;
	private View container;
	private Timer timer;
	private Handler uiHandler;
	private int mCount = 0;
	private TextView countView;
	
	public LWholeInfoMediator(ViewGroup container)
	{
		this.container = container.findViewById(R.id.view_driving_record);
		mDistanceView = (TextView)container.findViewById(R.id.view_drive_distance);
		mTimeView     = (TextView)container.findViewById(R.id.view_drive_time);
		countView     = (TextView)container.findViewById(R.id.view_drive_time_count);
		
		uiHandler = new Handler();
	}
	
	public void update(String distance,String time)
	{
		mDistanceView.setText(distance);
		mTimeView.setText(time);
	}

	@Override
	public void onDistanceChanged(String distance) {
		mDistanceView.setText(distance);
		show();
	}

	@Override
	public void onTimeChanged(String count) {
		mTimeView.setText(count);
		show();
		restartTimer();
	}
	
	@Override
	public void onStart() {
		//Nothing
		
	}

	@Override
	public void onStop() {
		stopTimer();
		
	};
	
	public void show()
	{
		container.setVisibility(View.VISIBLE);
	}
	
	public void hide()
	{
		container.setVisibility(View.GONE);
	}
	
	private synchronized int increaseAndGetCount()
	{
		
		mCount ++;
		if(mCount >= 10)
		{
			mCount = 1;
		}
		return mCount;
	}
	
	private synchronized void resetCount()
	{
		mCount = 0;
	}
	
	public void stopTimer()
	{
		if(null != timer)
		{
			timer.cancel();
			timer = null;
		}
	}
	
	private void restartTimer()
	{
		resetCount();
		if(null == timer)
		{
			timer  = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					countUpdater.setCount(increaseAndGetCount());
					uiHandler.post(countUpdater);
				}
				
			}, 0, 150);
		}
	}
	
	private CountUpdater countUpdater = new CountUpdater();
	private class CountUpdater implements Runnable
	{
		private final String DIVIDER = " . ";
		private int count;
		
		
		public void setCount(int count)
		{
			this.count = count;
		}
		
		@Override
		public void run() {
			countView.setText(DIVIDER + count);
			
		}
		
	}

}
