package com.lookstudio.anywhere.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.geocoder.RegeocodeAddress;

public class LLocation implements Serializable{

	private static final long serialVersionUID = 5017628152415593944L;
	
	public double latitude;
	public double longitude;
	public String shortAddress;
	public String longAddress;
	
	public LLocation()
	{
		
	}
	
	public LLocation(RegeocodeAddress addr,double latitude,double longitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		
		List<String> list = new ArrayList<String>();
		
		if(false == TextUtils.isEmpty(addr.getProvince()))
		{
			list.add(addr.getProvince());
		}
		
		if(false == TextUtils.isEmpty(addr.getCity()))
		{
			list.add(addr.getCity());
		}
		 
		if(false == TextUtils.isEmpty(addr.getDistrict()))
		{
			list.add(addr.getDistrict());
		}
		
		if((null != addr.getRoads()) && (false == addr.getRoads().isEmpty()))
		{
			if(false == TextUtils.isEmpty(addr.getRoads().get(0).getName()))
			{
				list.add(addr.getRoads().get(0).getName());
			}
		}
		if(null != addr.getStreetNumber())
		{
			if(false == TextUtils.isEmpty(addr.getStreetNumber().getStreet()))
			{
				list.add(addr.getStreetNumber().getStreet());
			}
		}
		
		if(false == list.isEmpty())
		{
			this.shortAddress = list.get(list.size() - 1);
		}
		
		StringBuilder builder = new StringBuilder();
		for(String strAddr: list)
		{
			builder.append(strAddr);
		}
		
		this.longAddress  = builder.toString();
	}
	
	public LLocation(double latitude,double longitude,String shortAddress,String longAddress)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.shortAddress = shortAddress;
		this.longitude = longitude;
	}
	
	public LLocation(AMapLocation location)
	{
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
		
	}
	
	public boolean isValid()
	{
		return (latitude != 0) && (longitude != 0);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj instanceof LLocation)
		{
			LLocation loc = (LLocation)obj;
			return (latitude == loc.latitude) && (longitude == loc.longitude);
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		builder.append("latitude="+latitude);
		builder.append(",");
		builder.append("longitude="+longitude);
		builder.append(",");
		builder.append("shortAddress="+shortAddress);
		builder.append(",");
		builder.append("longAddress="+longAddress);

		return builder.toString();
	}
}
