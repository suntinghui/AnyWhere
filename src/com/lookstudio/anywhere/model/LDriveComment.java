package com.lookstudio.anywhere.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.text.TextUtils;

import com.lookstudio.anywhere.util.LLog;

public class LDriveComment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4118973755265260711L;
	
	private final double LAT_MAX_INTERVAL = 1.0E-4;
	private final double LON_MAX_INTERVAL = 1.0E-4;
	
	private List<String> texts = new ArrayList<String>();
	private String radioPath;
	private List<String> picPaths = new ArrayList<String>();
	private double latitude;
	private double longitude;


	public LDriveComment(){}
	public LDriveComment add(LDriveComment comment){
		if(comment.hasText())
		{
			this.texts.addAll(comment.getTexts());
		}
		
		if(comment.hasRadio())
		{
			this.radioPath = comment.radioPath;
		}
		
		if(comment.hasPhoto())
		{
			picPaths.addAll(comment.getPhotos());
		}
		
		return this;
	}
	
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	public double getLatitude()
	{
		return latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	public void addPhoto(String path)
	{
		if(picPaths.contains(path))
		{
			return;
		}
		picPaths.add(path);
	}
	
	public void removePhoto(String path)
	{
		picPaths.remove(path);
	}
	
	public List<String> getPhotos()
	{
		return picPaths;
	}
	
	public void setRadio(String path)
	{
		radioPath = path;
	}
	
	public void clearRadio()
	{
		radioPath = null;
	}
	
	public String getRadio()
	{
		return radioPath;
	}

	public void setText(String text)
	{
		texts.add(text);
	}
	
	public List<String> getTexts()
	{
		return texts;
	}
	
	public boolean hasText()
	{
		return false == texts.isEmpty();
	}
	
	public boolean hasPhoto()
	{
		return (false == getPhotos().isEmpty());
	}
	
	public boolean hasRadio()
	{
		return (null != getRadio()) && (false == "".equals(getRadio()));
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("LatLng(" + latitude + "," + longitude);
		builder.append("\n");
		for(String text:texts)
		{
			builder.append("text= " + text);
			builder.append("\n");
		}
		builder.append("radio=" + radioPath);
		builder.append("\n");
		builder.append("photos");
		builder.append("\n");
		for(String path : picPaths)
		{
			builder.append("path = " + path);
			builder.append("\n");
		}
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj instanceof LDriveComment)
		{
			LDriveComment comment = (LDriveComment)obj;
			
			double latInterval = Math.abs(latitude - comment.latitude);
			double lonInterval = Math.abs(longitude - comment.longitude);
			
			LLog.info("interval lat = " + latInterval + "  lon = " + lonInterval);
			
			return (latInterval <= LAT_MAX_INTERVAL) && (lonInterval <= LON_MAX_INTERVAL);
		}
		return false;
	}
}
