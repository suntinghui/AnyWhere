package com.lookstudio.anywhere.model;

import java.util.Date;

import android.content.Intent;
import android.os.Looper;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.lookstudio.anywhere.HomeActivity;
import com.lookstudio.anywhere.app.LApplication;
import com.lookstudio.anywhere.chweather.LWeatherUtils;
import com.lookstudio.anywhere.http.LHttpCommunication;
import com.lookstudio.anywhere.http.LResponse;
import com.lookstudio.anywhere.model.TaskHandler.Task;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.LScreenshot;
import com.lookstudio.anywhere.util.LScreenshot.OnScreenshotFinishListener;
import com.lookstudio.anywhere.util.ToastUtil;

public class LDriveRecordGenerator implements Runnable{

	private LDriveRecord record;
	private LMapController controller;
	
	public LDriveRecordGenerator(LDriveRecord record,LMapController controller)
	{
		this.record = record;
		this.controller = controller;
	}

	public boolean enabled()
	{
		if(null == record)
		{
			return false;
		}
		
		if(null == controller)
		{
			return false;
		}
		
		return true;
	}
	public void generate()
	{
	    Thread thread = new Thread(this,"thread_drive_record_generator");
	    thread.start();
	}
	
	@Override
	public void run() {
		
		Looper.prepare();
		LLocation startLocation = null;
		LLocation endLocation   = null;
		
		if(record.locations.size() >= 1)
		{
			GeocodeSearch search = new GeocodeSearch(LApplication.appContext);
			
			LLocation startLoc = record.locations.get(0);
			LatLonPoint startPoint = new LatLonPoint(startLoc.latitude,startLoc.longitude);
			
			RegeocodeQuery startQ = new RegeocodeQuery(startPoint,200,GeocodeSearch.AMAP);
			try {
				startLocation = new LLocation(search.getFromLocation(startQ),startLoc.latitude,startLoc.longitude);
			} catch (AMapException e) {
				LLog.error("error when regeocoding start point,location = " + startLoc, e);
			}
			
			if(record.locations.size() >= 2)
			{
				LLocation endLoc = record.locations.get(record.locations.size() - 1);
				LatLonPoint endPoint  = new LatLonPoint(endLoc.latitude,endLoc.longitude);
				RegeocodeQuery endQ = new RegeocodeQuery(endPoint,200,GeocodeSearch.AMAP);
				
				try {
					endLocation = new LLocation(search.getFromLocation(endQ),endLoc.latitude,endLoc.longitude);
				} catch (AMapException e) {
					LLog.error("error when regeocoding end point,location = " + endLoc, e);
				}
			}
			
		}
		
		if(null != startLocation)
		{
			record.start = startLocation.shortAddress;
		}
		
		if(null != endLocation)
		{
			record.end = endLocation.shortAddress;
		}
		
		record.date = LWeatherUtils.currentDate();
		record.createTime = new Date().getTime();
		LScreenshot s = new LScreenshot();
		s.screenshort(controller);
		s.setOnScreenshotFinishListener(new OnScreenshotFinishListener()
		{

			@Override
			public void onScreenshotFinish(String path) {
				if("".equals(path))
				{
					ToastUtil.show(LApplication.appContext, "截屏失败");
				}
				
				record.screenshotPath = path;
				
				final LSaver saver = new LSaver(LApplication.appContext);
				saver.saveRecord(record);
				LApplication.appContext.sendBroadcast(new Intent(HomeActivity.ACTION_NEW_RECORD_COME));
				
			/*	TaskHandler.getHandler().exeTask(new Task()
				{

					@Override
					public boolean onRun() {
						
						if(!LCommonUtil.isNetworkActivate(LApplication.appContext))
						{
							return false;
						}
						
						LUploadNewRecordRequest req = new LUploadNewRecordRequest(record);
						LResponse<LUploadNewRecordResp> response = req.parse(LHttpCommunication.post(req));
						if((null != response.getBean()) && (true == response.getBean().isSuccessful()))
						{
							record.id = response.getBean().getId(); 
							return true;
						}
						
						return false;
						
					}

					@Override
					public int delayInMillis() {
						
						return 5*Task.MINUTE;
					}
					
				});*/
				
			}
			
		});
		
		Looper.myLooper().quit();
	}
}
