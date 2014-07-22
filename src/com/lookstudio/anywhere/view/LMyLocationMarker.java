package com.lookstudio.anywhere.view;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.lookstudio.anywhere.model.LLocationManager;
import com.lookstudio.anywhere.model.LLocationManager.LLocationChangedListener;
import com.lookstudio.anywhere.model.LMapController;


public class LMyLocationMarker implements LocationSource, LLocationChangedListener{

	private int mResId;
	private LMapController mController;
	private OnLocationChangedListener mLisener;
	
	public LMyLocationMarker(int resId,LMapController controller)
	{
		mResId = resId;
		mController = controller;
	}
	
	public void setVisible() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(mResId));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		// myLocationStyle.radiusFillColor(color)//设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
		
		AMap aMap = mController.get();
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setMyLocationRotateAngle(180);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		
	}

	public void setLocation(AMapLocation location)
	{
		onLocationChanged(location);
	}
	
	public void requestUpdate()
	{
		//监听位置
		LLocationManager.getManager().setLocationListener(this);
		LLocationManager.getManager().requestLocationUpdates();
	}
	
	public void stopUpdate()
	{
		LLocationManager.getManager().setLocationListener(null);
	}
	
	@Override
	public void activate(
			OnLocationChangedListener paramOnLocationChangedListener) {
		mLisener = paramOnLocationChangedListener;
		
	}

	@Override
	public void deactivate() {
		mLisener = null;
		
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if((null != mLisener) && (null != location))
		{
			mLisener.onLocationChanged(location);// 显示系统小蓝点
			float bearing = mController.get().getCameraPosition().bearing;
			mController.get().setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
		}
	}
	
	
}
