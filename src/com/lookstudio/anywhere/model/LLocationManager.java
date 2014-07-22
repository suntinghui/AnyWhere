package com.lookstudio.anywhere.model;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.LSetting;

public class LLocationManager implements AMapLocationListener{

	private List<LLocationChangedListener> mListeners = new LinkedList<LLocationChangedListener>();
	private Context mContext;
	private static LLocationManager sInstance = null;
	private AMapLocation mLastKnownLocation;
	
	public interface LLocationChangedListener
	{
		public void onLocationChanged(AMapLocation location);
	}
	
	private LLocationManager()
	{
		
		sInstance = this;
	}
	
	public static LLocationManager getManager()
	{
		
		if(null == sInstance)
		{
			sInstance = new LLocationManager();
		}
		
		return sInstance;
	}
	
	public void setContext(Context context)
	{
		mContext = context;
	}
	
	public void setLocationListener(LLocationChangedListener listener)
	{
		mListeners.add(listener);
	}
	
	public void removeLocationListener(LLocationChangedListener listener)
	{
		mListeners.remove(listener);
	}
	
	private void notifyListeners(AMapLocation loc)
	{
		for(LLocationChangedListener listener : mListeners)
		{
			if(null != listener)
			{
				listener.onLocationChanged(loc);
			}
			
		}
	}
	
	public void requestLocationUpdates()
	{
		LLog.info("requestLocationUpdates");
		LocationManagerProxy mapLocationManager = LocationManagerProxy.getInstance(mContext);
		mapLocationManager.setGpsEnable(true);
		
		mapLocationManager.getLastKnownLocation(LocationProviderProxy.AMapNetwork);
		mapLocationManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);
	}

	public void destroy()
	{
		mListeners.clear();
		mListeners = null;
		LocationManagerProxy.getInstance(mContext).removeUpdates(this);
		LocationManagerProxy.getInstance(mContext).destory();
	}
	
	public AMapLocation getLastKnownLocation()
	{
		LocationManagerProxy mapLocationManager = LocationManagerProxy.getInstance(mContext);
		AMapLocation loc =  mapLocationManager.getLastKnownLocation(LocationProviderProxy.AMapNetwork);
		if(null != loc)
		{
			mLastKnownLocation = loc;
		}
		return mLastKnownLocation;
	}
	
	@Override
	public void onLocationChanged(AMapLocation location) {
		
		//LLog.info("onLocationChanged location = " + location);
		if(null != location)
		{
			mLastKnownLocation = location;
			LSetting.instance().setLocation(location);
			notifyListeners(location);
		}
	}
	

	@Override
	public void onLocationChanged(Location location) {
		/*if(location instanceof AMapLocation)
		{
			onLocationChanged((AMapLocation)location);
		}*/
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	
}
