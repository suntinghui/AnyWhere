package com.lookstudio.anywhere.model;



import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMyLocationChangeListener;
import com.amap.api.maps.MapView;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.LSetting;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class LMapManager {

	private MapView mMapView;
	private LMapController controller;
	
	public void create(Context context,ViewGroup container,MapView mapView,Bundle savedInstanceState)
	{
		mMapView = mapView;
		onCreate(savedInstanceState);
	}
	
	public void init()
	{
		mMapView.getMap().setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
			
			@Override
			public void onMyLocationChange(Location location) {
				LLog.info("onMyLocationChange location = " + location);
				
			}
		});
		
		mMapView.getMap().setTrafficEnabled(LSetting.instance().trafficEnabled());
		mMapView.getMap().setMapType(LSetting.instance().mapType(AMap.MAP_TYPE_NORMAL));
		mMapView.getMap().getUiSettings().setCompassEnabled(true);
		mMapView.getMap().getUiSettings().setScaleControlsEnabled(false);
		mMapView.getMap().getUiSettings().setZoomControlsEnabled(false);
		//getMapController().initZoom();
	}
	
	
	public MapView getMapView()
	{
		return mMapView;
	}
	
	public LMapController getMapController()
	{
		if(null == controller)
		{
			controller = new LMapController(mMapView.getMap());
		}
		return controller;
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		mMapView.onCreate(savedInstanceState);
	}
	
	public void onSaveInstanceState(Bundle outState)
	{
		mMapView.onSaveInstanceState(outState);
	}
	

	public void onResume() {
		mMapView.onResume();
	}

	
	public void onPause() {
		mMapView.onPause();
	}
	
	public void onDestroy()
	{
		mMapView.onDestroy();
		mMapView = null;
	}
	
}
