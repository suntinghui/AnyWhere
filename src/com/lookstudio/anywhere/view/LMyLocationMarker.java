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
		// �Զ���ϵͳ��λС����
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(mResId));// ����С�����ͼ��
		myLocationStyle.strokeColor(Color.BLACK);// ����Բ�εı߿���ɫ
		// myLocationStyle.radiusFillColor(color)//����Բ�ε������ɫ
		// myLocationStyle.anchor(int,int)//����С�����ê��
		myLocationStyle.strokeWidth(0.1f);// ����Բ�εı߿��ϸ
		
		AMap aMap = mController.get();
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setMyLocationRotateAngle(180);
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		
	}

	public void setLocation(AMapLocation location)
	{
		onLocationChanged(location);
	}
	
	public void requestUpdate()
	{
		//����λ��
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
			mLisener.onLocationChanged(location);// ��ʾϵͳС����
			float bearing = mController.get().getCameraPosition().bearing;
			mController.get().setMyLocationRotateAngle(bearing);// ����С������ת�Ƕ�
		}
	}
	
	
}
