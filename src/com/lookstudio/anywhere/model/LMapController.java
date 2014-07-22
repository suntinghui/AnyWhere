package com.lookstudio.anywhere.model;

import java.util.List;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.lookstudio.anywhere.util.LLog;


public class LMapController {

	private AMap mMapController;
	private final int ZOOM_LEVEL = 16;
	public LMapController(AMap mapController)
	{
		mMapController = mapController;
	}
	
	public AMap get()
	{
		return mMapController;
	}
	
	public void animateTo(LatLng latlng)
	{
		
		CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
				latlng, ZOOM_LEVEL, 0, 30));
		mMapController.animateCamera(update, 1000, null);
	}
	
	public void moveTo(LatLng latlng)
	{
		CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
				latlng, ZOOM_LEVEL, 0, 30));
		mMapController.moveCamera(update);
	}
	
	public void zoomIn(boolean animate)
	{
		if(animate)
		{
			mMapController.animateCamera(CameraUpdateFactory.zoomIn());
		}
		else
		{
			mMapController.moveCamera(CameraUpdateFactory.zoomIn());
		}
		
	}
	
	public void zoomOut(boolean animate)
	{
		if(animate)
		{
			mMapController.animateCamera(CameraUpdateFactory.zoomOut());
		}
		else
		{
			mMapController.moveCamera(CameraUpdateFactory.zoomOut());
		}
	}
	
	public void zoomTo(float value)
	{
		mMapController.moveCamera(CameraUpdateFactory.zoomTo(value));
	}
	
	public void initZoom()
	{
		float value = mMapController.getMaxZoomLevel() - 5;
		mMapController.moveCamera(CameraUpdateFactory.zoomTo(value));
	}
	/**
	 * ��ͼ�ϻ���·��
	 * @param locations
	 */
	public void drawTravel(List<LLocation> locations)
	{
		if((null != locations)&&(1 < locations.size()))
		{
			PolylineOptions options = new PolylineOptions();
			for(LLocation loc : locations)
			{
				if(null == loc)
				{
					LLog.warn("��յ�����");
					continue;
				}
				
				if(false == loc.isValid())
				{
					LLog.warn("��Ƿ�������");
					continue;
				}
				options.add(new LatLng(loc.latitude,loc.longitude));
			}
			options.geodesic(true).color(Color.BLUE);
			get().addPolyline(options);

		}
	}
}
