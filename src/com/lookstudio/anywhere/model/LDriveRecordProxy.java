package com.lookstudio.anywhere.model;

import java.util.List;

public class LDriveRecordProxy {

	public LDriveRecordProxy(){}
	/*private static LDriveRecordProxy instance;
	
	public static LDriveRecordProxy get()
	{
		if(null == instance)
		{
			instance = new LDriveRecordProxy();
		}
		return instance;
	}*/
	
	public List<LDriveRecord> allRecords()
	{
		return null;
	}
	
	public LDriveOverviewInfo driveOverviewInfo(List<LDriveRecord> records)
	{
		LDriveOverviewInfo info = new LDriveOverviewInfo();
		if((null != records) && (false == records.isEmpty()))
		{
			for(LDriveRecord record : records)
			{
				info.totalDistance += record.distanceInMeter;
				info.totalTime +=  record.timeInSeconds;
			}
			
			
		}
		
		return info;
	}
	
	
}
