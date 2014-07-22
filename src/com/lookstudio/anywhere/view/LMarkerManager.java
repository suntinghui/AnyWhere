package com.lookstudio.anywhere.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.lookstudio.anywhere.R;


import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.lookstudio.anywhere.util.AMapUtil;
import com.lookstudio.anywhere.util.ToastUtil;
import com.lookstudio.anywhere.app.LApplication;
import com.lookstudio.anywhere.model.LDriveComment;
import com.lookstudio.anywhere.model.LMapController;

public class LMarkerManager implements OnMarkerClickListener,OnInfoWindowClickListener {

	private List<Marker> mMarkers = new LinkedList<Marker>();
	private LMapController mController;
	
	public LMarkerManager(LMapController controller)
	{
		mController = controller;
		registerListeners();
	}
	
	public void showMarker(LDriveComment comment,OnMarkerClickListener onMarkerClickListener)
	{
		MarkerInfo info = new MarkerInfo();
		info.comment = comment;
		info.latLng = new LatLng(comment.getLatitude(),comment.getLongitude());
		info.resId  = getResourceId(comment);
		info.onMarkerClickListener = onMarkerClickListener;
		showMarkerAt(info);
	}
	
	public void showMarkers(List<LDriveComment> comments,OnMarkerClickListener onMarkerClickListener)
	{
		if(null != comments)
		{
			for(LDriveComment comment : comments)
			{
				showMarker(comment,onMarkerClickListener);
			}
		}
	}
	
	public void showMarkerAt(MarkerInfo info)
	{
		Marker found = find(info.comment);
		if(null == found)
		{
			MarkerOptions markerOption = new MarkerOptions();
			markerOption.position(info.latLng);
			if(info.title != null)
			{
				markerOption.title(info.title);
			}
			
			if(info.snippet != null)
			{
				markerOption.snippet(info.snippet);
			}
			
			markerOption.perspective(true);
			markerOption.draggable(false);
			markerOption.icon(BitmapDescriptorFactory
					.fromResource(getIcon(info)));
			found = mController.get().addMarker(markerOption);
			mMarkers.add(found);
		}
		

		found.setIcon(BitmapDescriptorFactory
					.fromResource(getIcon(info)));
		found.setObject(info);
	}
	
	private int getIcon(MarkerInfo info)
	{
		int resId = info.resId;
		if(resId == 0)
		{
			resId = MarkerInfo.DEFAULT_MARKER;
		}
		
		return resId;
	}
	
	private Marker find(LDriveComment comment)
	{
		Marker destMarker = null;
		
		for(Marker marker : mMarkers)
		{
			MarkerInfo markerInfo = (MarkerInfo)marker.getObject();
			if(comment.equals(markerInfo.comment))
			{
				destMarker = marker;
				break;
			}
		}
		return destMarker;
	}
	/*public Marker retrieveMarker(int id)
	{
		return mMarkers.get(new Integer(id));
	}*/
	
	private void registerListeners()
	{
		mController.get().setOnMarkerClickListener(this);
		mController.get().setOnInfoWindowClickListener(this);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		
		MarkerInfo markerInfo = (MarkerInfo)marker.getObject();
		if(null != markerInfo.onMarkerClickListener)
		{
			markerInfo.onMarkerClickListener.onMarkerClick(marker);
		}
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		
		MarkerInfo markerInfo = (MarkerInfo)marker.getObject();
		if(null != markerInfo.onInfoWindowClickListener)
		{
			 markerInfo.onInfoWindowClickListener.onInfoWindowClick(null);
		}
	}
	
	/**
	 * ��յ�ͼ�������Ѿ���ע��marker
	 */
	public void clearAllMarkers()
	{
		mMarkers.clear();
		mController.get().clear();
	}
	
	private int getResourceId(LDriveComment comment)
	{
		
		int resId = 0;
		if(comment.hasText() && !comment.hasPhoto() && !comment.hasRadio())
		{
			//resId = R.drawable.selector_text_marker;
			resId = R.drawable.img_text_marker_normal;
		}
		else if(!comment.hasText() && comment.hasPhoto() && !comment.hasRadio())
		{
			//resId = R.drawable.selector_photo_marker;
			resId = R.drawable.img_photo_marker_normal;
		}
		else if(!comment.hasText() && !comment.hasPhoto() && comment.hasRadio())
		{
			//resId = R.drawable.selector_radio_marker;
			resId = R.drawable.img_radio_marker_normal;
		}
		else
		{
			//resId = R.drawable.selector_mix_marker;
			resId = R.drawable.img_mix_marker_normal;
		}
		
		return resId;
	}
	
	public static class MarkerInfo 
	{
		public static final int DEFAULT_MARKER = R.drawable.img_mix_marker_normal;
		public LatLng latLng;
		public String title;
		public String snippet;
		public int resId;
		public OnMarkerClickListener onMarkerClickListener;
		public OnInfoWindowClickListener onInfoWindowClickListener;
		public LDriveComment           comment;
		public Object extra;
		
		@Override
		public boolean equals(Object obj)
		{
			if(this == obj)
			{
				return true;
			}
			
			if(obj instanceof MarkerInfo)
			{
				MarkerInfo info = (MarkerInfo)obj;
				return (info.latLng.latitude == latLng.latitude)
						&& (info.latLng.longitude == latLng.longitude);
			}
			return false;
		}
	}
}
