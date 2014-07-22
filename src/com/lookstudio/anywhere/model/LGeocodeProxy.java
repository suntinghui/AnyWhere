package com.lookstudio.anywhere.model;

import android.content.Context;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.lookstudio.anywhere.util.LLog;

public class LGeocodeProxy {

	public interface OnGetAddressListener
	{
		public void onGetAddress(LLocation location);
	}
	
	private GeocodeSearch search;
	private OnGetAddressListener listener;
	
	public LGeocodeProxy(){}
	public LGeocodeProxy(Context context){
		search = new GeocodeSearch(context);
	}
	
	public void setOnGetAddressListener(OnGetAddressListener listener)
	{
		this.listener = listener;
	}
	
	public void startQuery(final double latitude,final double longitude)
	{
		Thread thread = new Thread("")
		{
			@Override
			public void run()
			{
				query(latitude,longitude);
			}
		};
		thread.start();
	}
	
	public void query(double latitude,double longitude)
	{
		LatLonPoint point = new LatLonPoint(latitude,longitude);
		
		RegeocodeQuery query = new RegeocodeQuery(point,200,GeocodeSearch.AMAP);
		
		try {
			RegeocodeAddress addr = search.getFromLocation(query);
			
			if(null != listener)
			{
				listener.onGetAddress(new LLocation(addr,latitude,longitude));
			}
		} catch (AMapException e) {
			LLog.error("Ã»ÓÐÄæµØÖ·±àÂëÊ§°Ü",e);
		}
	}

}
