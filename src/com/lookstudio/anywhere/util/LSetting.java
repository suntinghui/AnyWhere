package com.lookstudio.anywhere.util;

import com.amap.api.location.AMapLocation;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LSetting {

	public static final String KEY_MAP_TYPE = "map_type";
	public static final String KEY_TRAFFIC_ENABLE = "traffic_enable";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_USER_ICON_WIDTH = "user_icon_width";
	public static final String KEY_USER_ICON_HEIGHT = "user_icon_height";
	private final String name = "setting";
	private SharedPreferences preferences;
	private static LSetting instance;
	
	private LSetting(Context context)
	{
		preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	
	public static void init(Context context)
	{
		if(null == instance)
		{
			instance = new LSetting(context);
		}
	}
	
	public static LSetting instance()
	{
		
		
		return instance;
	}
	
	public int mapType(int defaultType)
	{
		return preferences.getInt(KEY_MAP_TYPE, defaultType);
	}
	
	public void setMapType(int mapType)
	{
		Editor editor = preferences.edit();
		editor.putInt(KEY_MAP_TYPE, mapType);
		editor.commit();
	}
	
	public boolean trafficEnabled()
	{
		return preferences.getBoolean(KEY_TRAFFIC_ENABLE, false);
	}
	
	public void setTrafficEnabled(boolean enabled)
	{
		Editor editor = preferences.edit();
		editor.putBoolean(KEY_TRAFFIC_ENABLE, enabled);
		editor.commit();
	}
	
	public void setLocation(AMapLocation location)
	{
		Editor editor = preferences.edit();
		editor.putString(KEY_LATITUDE, String.valueOf(location.getLatitude()));
		editor.putString(KEY_LONGITUDE,String.valueOf(location.getLongitude()));
		editor.commit();
	}
	
	public double getLatitude()
	{
		return Double.valueOf(preferences.getString(KEY_LATITUDE, "0"));
	}
	
	public double getLongitude()
	{
		return Double.valueOf(preferences.getString(KEY_LONGITUDE, "0"));
	}
	
	public void setUserIconDimension(int width,int height)
	{
		Editor editor = preferences.edit();
		editor.putInt(KEY_USER_ICON_WIDTH,width);
		editor.putInt(KEY_USER_ICON_HEIGHT,height);
		editor.commit();
	}
	
	public int getUserIconWidth(int defValue)
	{
		return preferences.getInt(KEY_USER_ICON_WIDTH, defValue);
	}
	
	public int getUserIconHeight(int defValue)
	{
		return preferences.getInt(KEY_USER_ICON_HEIGHT,defValue);
	}
}
