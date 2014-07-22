package com.lookstudio.anywhere.model;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.util.ToastUtil;
import com.lookstudio.anywhere.view.LMyLocationMarker;

public class LocateProxy {

	private LMapController controller;
	private Context context;
	private LMyLocationMarker mMyLocaitonMark;
	
	
	public LocateProxy(LMapController controller,Context context)
	{
		this.controller = controller;
		this.context = context;
	}
	
	
	public void locate()
	{
		AMapLocation loc = LLocationManager.getManager().getLastKnownLocation();
		if(null != loc)
		{
			LatLng latlng = new LatLng(loc.getLatitude(),loc.getLongitude());
			
			if(null == mMyLocaitonMark)
			{
				mMyLocaitonMark = new LMyLocationMarker(R.drawable.location_marker,controller);
				mMyLocaitonMark.requestUpdate();
			}
			
	    	mMyLocaitonMark.setVisible();
	    	mMyLocaitonMark.setLocation(loc);
	    	controller.animateTo(latlng);
		}
		else
		{
			ToastUtil.show(context, "定位失败，请稍后再试");
		}
		
	}
	
	 
}
