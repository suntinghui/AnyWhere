package com.lookstudio.anywhere.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.lookstudio.anywhere.bitmap.BitmapLoader;
import com.lookstudio.anywhere.interfaces.LMediator;
import com.lookstudio.anywhere.interfaces.LMediatorable;
import com.lookstudio.anywhere.view.LDriveRecordMediator;

public class LDriveRecord implements Serializable,LMediatorable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3999351379663969500L;
	public String date = "";
	public String start = "";
	public String end= "";
	public double distanceInMeter;
	public int    timeInSeconds;
	public String screenshotPath= "";
	public LinkedList<LLocation> locations = new LinkedList<LLocation>();
	public LinkedList<LDriveComment> comments = new LinkedList<LDriveComment>();

	public long createTime;
	public String  id;
	private transient LDriveRecordMediator mediator;
	public LDriveRecord()
	{
		
	}
	
	public LDriveRecord(String date,String start,String end,double distance,int timeInSeconds,String path)
	{
		this.date = date;
		this.start = start;
		this.end   = end;
		this.distanceInMeter = distance;
		this.timeInSeconds = timeInSeconds;
		this.screenshotPath = path;
	}

	@Override
	public LMediator createMediator() {
		if(null == mediator)
		{
			mediator = new LDriveRecordMediator();
		}
		return mediator;
	}
	
	public void addTranvelLocation(AMapLocation location)
	{
		LLocation loc = new LLocation(location);
		if(false == locations.contains(loc))
		{
			locations.add(loc);
		}
		
	}

	public  List<LLocation>  travelLocations()
	{
		return locations;
	}
	
	public void addComment(LDriveComment comment)
	{
		comments.add(comment);
	}
	
	/*public LDriveComment findCommentByDistance(LDriveComment comment)
	{
		//��λm
		final double DISTANCE = 5.0f;
		LDriveComment result = null;
		for(LDriveComment com : comments)
		{
			if(distanceBlow(comment,com,DISTANCE))
			{
				result = com;
				break;
			}
		}
		 
		 return result;
	}
	
	private boolean distanceBlow(LDriveComment commentA,LDriveComment commentB,double distance)
	{
		
		if((null == commentA) || (null == commentB))
		{
			return false;
		}
		
		if((null == commentA.getLocation()) || (null == commentB.getLocation()))
		{
			return false;
		}
		
		
		return commentA.getLocation().distanceTo(commentB.getLocation()) < distance;
	}*/
	
	public LDriveComment findCommentByLatLng(LDriveComment comment)
	{
		 LDriveComment result = null;
		 int index = comments.indexOf(comment);
		 if(-1 != index)
		 {
			 result = comments.get(index);
		 }
		 
		 return result;
	}
	
	public void deleteComment(LDriveComment comment)
	{
		comments.remove(comment);
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		long temp;
		temp = Double.doubleToLongBits(distanceInMeter);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result
				+ ((locations == null) ? 0 : locations.hashCode());
		result = prime * result
				+ ((screenshotPath == null) ? 0 : screenshotPath.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + timeInSeconds;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LDriveRecord other = (LDriveRecord) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (Double.doubleToLongBits(distanceInMeter) != Double
				.doubleToLongBits(other.distanceInMeter))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (locations == null) {
			if (other.locations != null)
				return false;
		} else if (!locations.equals(other.locations))
			return false;
		if (screenshotPath == null) {
			if (other.screenshotPath != null)
				return false;
		} else if (!screenshotPath.equals(other.screenshotPath))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (timeInSeconds != other.timeInSeconds)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("start = " + start);
		builder.append(",");
		builder.append("end = " + end);
		builder.append(",");
		builder.append("distance = " + distanceInMeter);
		builder.append(",");
		builder.append("timeInSeconds =" + timeInSeconds);
		builder.append(",");
		builder.append("date= " +date);
		builder.append(",");
		builder.append("screenshotPath = " + screenshotPath);

		return builder.toString();
	}
	
}
