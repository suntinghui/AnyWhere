package com.lookstudio.anywhere.model;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.app.LApplication;
import com.lookstudio.anywhere.chweather.LWeatherUtils;
import com.lookstudio.anywhere.model.LLocationManager.LLocationChangedListener;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.LScreenshot;
import com.lookstudio.anywhere.util.ToastUtil;
import com.lookstudio.anywhere.view.LMarkerManager;
import com.lookstudio.anywhere.view.LMarkerManager.MarkerInfo;

public class LTravelProxy {

	private Timer   mTimer;
	
	private TravelListener mTravelListener;
	private LDriveRecord record = null;
	
	private boolean isStarted = false;
	private Handler mMainHandler;
	private final static int MSG_TYPE_TIME = 1;
	private final static int MSG_TYPE_DISTANCE = 2;
	private final float  SCAPE_TO_IGNORE    = 10;
	private LMapController controller;
	private LMarkerManager mMarkerManager;
	private OnMarkerClickListener onMarkerClickListener;
	private OnTravelStateChangedListener onTravelStateChangedListener;
	
	public interface TravelListener
	{
		public void onStart();
		public void onDistanceChanged(String distance);
		
		public void onTimeChanged(String count);
		public void onStop();
	}

	public interface OnTravelStateChangedListener
	{
		public void onStateChanged(boolean isStarted);
	}
	
	public LTravelProxy(LMapController controller,LMarkerManager markerManager)
	{
		this.controller = controller;
		this.mMarkerManager = markerManager;
		mMainHandler = new Handler(Looper.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch(msg.what)
				{
					case MSG_TYPE_TIME:
						if(null != mTravelListener)
						{
							MessageBody body = (MessageBody)msg.obj;
							mTravelListener.onTimeChanged(body.arg);
						}
						break;
					case MSG_TYPE_DISTANCE:
						if(null != mTravelListener)
						{
							MessageBody body = (MessageBody)msg.obj;
							mTravelListener.onDistanceChanged(body.arg);
						}
						break;
					default:
						break;
				}
				
				
			}
		};
	}
	
	public void setOnMarkerClickListener(OnMarkerClickListener listener)
	{
		onMarkerClickListener = listener;
	}
	
	public void setOnTravelStateChangedListener(OnTravelStateChangedListener listener)
	{
		onTravelStateChangedListener = listener;
	}
	
	public void setTravelListener(TravelListener travelListener)
	{
		mTravelListener = travelListener;
	}
	
	public boolean isStarted()
	{
		return isStarted;
	}
	
	public void start()
	{	
		setStartFlag(true);
		
		reset();
		startTimer();
		startRecordTranvelLocations();
		if(null != mTravelListener)
		{
			mTravelListener.onStart();
		}
	}
	
	public void stop()
	{
		setStartFlag(false);
		
		if(null != mTravelListener)
		{
			mTravelListener.onStop();
		}
		
		stopTimer();
		LLocationManager.getManager().removeLocationListener(locationChangedListener);
		ToastUtil.show(LApplication.appContext, "已停止驾驶");
		
		LDriveRecordGenerator gen = new LDriveRecordGenerator(record,controller);
		if(gen.enabled())
		{
			gen.generate();
		}
		
	}
	
	public LDriveRecord getRecord()
	{
		return record;
	}
	
	public void comment(LDriveComment comment)
	{
		LDriveComment oldComment = record.findCommentByLatLng(comment);
		if(null != oldComment)
		{
			comment = comment.add(oldComment);
			record.deleteComment(oldComment);
		}
		record.date = LWeatherUtils.currentDate();
		mMarkerManager.showMarker(comment, onMarkerClickListener);
		record.addComment(comment);
	}
	
	
	private void reset()
	{
		record = null;
		record = new LDriveRecord();
		record.timeInSeconds = 1;
		record.distanceInMeter = 0.0;
	}
	
	private void startTimer()
	{
		if(null == mTimer)
		{
			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
				

					MessageBody body = new MessageBody(MSG_TYPE_TIME, LCommonUtil.formatTime(record.timeInSeconds));
					mMainHandler.sendMessage(mMainHandler.obtainMessage(MSG_TYPE_TIME,body));
					record.timeInSeconds ++;
				}
			}, 0, 1000);
		}
		
	}
	
	private void stopTimer()
	{
		mTimer.cancel();
		mTimer = null;
	}
	
	private void tranvelChanged(AMapLocation oldLocation,AMapLocation newLocation)
	{
		float distance = oldLocation.distanceTo(newLocation);
		LLog.info("travelChanged,distance:" + distance + "\n old:" + oldLocation + "\n new:" + newLocation);
		if( distance >= SCAPE_TO_IGNORE)
		{
			record.distanceInMeter += oldLocation.distanceTo(newLocation);
		}
		
		//LLog.info("Travel proxy,tranvelChanged distance = " + getStrDistance(record.distance));
		MessageBody body = new MessageBody(MSG_TYPE_DISTANCE,LCommonUtil.fromMtoKM(record.distanceInMeter) + "km");
		mMainHandler.sendMessage(mMainHandler.obtainMessage(MSG_TYPE_DISTANCE,body));
	}
	
	private void startRecordTranvelLocations()
	{
		
		MessageBody body = new MessageBody(MSG_TYPE_DISTANCE,0.00 + " km");
		mMainHandler.sendMessage(mMainHandler.obtainMessage(MSG_TYPE_DISTANCE,body));
		LLocationManager.getManager().setLocationListener(locationChangedListener);
	}
	
	
	
	class MessageBody
	{
		int what;
		String arg;
		
		public MessageBody(int what,String arg)
		{
			this.what = what;
			this.arg  = arg;
		}
	}
	
	private LLocationChangedListener locationChangedListener = new LLocationChangedListener() {
		
		private AMapLocation lastLocation;
		@Override
		public void onLocationChanged(AMapLocation location) {
			if((null != location) && (!isLocationEquals(lastLocation,location)))
			{
				if(null != lastLocation)
				{
					tranvelChanged(lastLocation,location);	
				}
				
				record.addTranvelLocation(location);
				controller.drawTravel(record.locations);
				lastLocation = location;
			}
			
		}
	};
	
	private boolean isLocationEquals(AMapLocation oldLocation,AMapLocation newLocation)
	{
		if(null == oldLocation)
		{
			return false;
		}
		
		if(null == newLocation)
		{
			return false;
		}
		return (oldLocation.getLatitude() == newLocation.getLatitude()) 
				&& (oldLocation.getLongitude() == newLocation.getLongitude());
	}
	
	private void setStartFlag(boolean isStarted)
	{
		this.isStarted = isStarted;
		if(null != onTravelStateChangedListener)
		{
			onTravelStateChangedListener.onStateChanged(isStarted);
		}
	}
}
