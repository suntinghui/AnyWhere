package com.lookstudio.anywhere.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.LObjectRW;
import com.lookstudio.anywhere.whether.LWeatherInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class LSaver {

	private static final String FILE_LOGIN    = "login.dat";
	private static final String FILE_REGISTER = "register.dat";
	private static final String FILE_WEATHER = "weather.dat";
	private static final String FILE_DRIVE_RECORDS = "driverecords.dat";
	private static final String FILE_USER_ICON = "usericon.dat";
	private static final String DIR_NAME           = "anywhere";
	private File dir;
	private final boolean DELETE_RECORDS = false;
	public LSaver(Context context)
	{
		dir = context.getDir(DIR_NAME, Context.MODE_PRIVATE);
	}
	
	public void writeWeatherInfo(LWeatherInfo info)
	{
		
		LObjectRW w = new LObjectRW(dir,FILE_WEATHER);
		w.write(info);
		
	}
	
	public LWeatherInfo readWeatherInfo(LWeatherInfo def)
	{
		
		LObjectRW r = new LObjectRW(dir,FILE_WEATHER);
		Serializable s = r.read();
		if(null == s)
		{
			return def;
		}
		return (LWeatherInfo)s;
	}
	
	public void  saveRecord(LDriveRecord record)
	{
		ArrayList<LDriveRecord> records = readRecords();
		if(null == records)
		{
			records = new ArrayList<LDriveRecord>();
		}
		records.add(record);
		
		writeRecords(records);
	}
	
	public void deleteAllRecord()
	{
		ArrayList<LDriveRecord> records = readRecords();
		if(null == records)
		{
			return;
		}
		records.clear();
		
		writeRecords(records);
	}
	
	public void deleteRecord(LDriveRecord record)
	{
		ArrayList<LDriveRecord> records = readRecords();
		if(null == records)
		{
			return;
		}
		records.remove(record);
		
		writeRecords(records);
	}
	
	public void  writeRecords(ArrayList<LDriveRecord> records)
	{
		
		LObjectRW w = new LObjectRW(dir,FILE_DRIVE_RECORDS);
		w.write(records);
		
	}
	

	
	public ArrayList<LDriveRecord> readRecords()
	{
	
		LObjectRW r = new LObjectRW(dir,FILE_DRIVE_RECORDS);
		if(DELETE_RECORDS)
		{
			r.delete();
			return null;
		}
		Serializable s = r.read();
		return (ArrayList<LDriveRecord>)s;
		
	}
	
	public void saveLoginInfo(LLoginInfo info)
	{
		LObjectRW w = new LObjectRW(dir,FILE_LOGIN);
		w.write(info);
	}
	
	public LLoginInfo readLoginInfo()
	{
		LObjectRW r = new LObjectRW(dir,FILE_LOGIN);
		return (LLoginInfo)r.read();
	}
	
	public void deleteLoginInfo()
	{
		File file = new File(dir,FILE_LOGIN);
		file.delete();
	}
	
	public void saveRegisterInfo(LRegisterInfo info)
	{
		LObjectRW w = new LObjectRW(dir,FILE_REGISTER);
		w.write(info);
	}
	
	private final static String FILE_NAME_USER_ICON = "usericon.png";
	public void saveUserIcon(Bitmap bm)
	{

		try {
			File dir = LCommonUtil.getStorageDirectory();
			File file = new File(dir,FILE_NAME_USER_ICON);
			FileOutputStream fos = new FileOutputStream(file);
			boolean b = bm.compress(CompressFormat.PNG, 100, fos);
			try {
				fos.flush();
			} catch (IOException e) {
				LLog.error("saveUserIcon error",e);
			}
			try {
				fos.close();
			} catch (IOException e) {
				LLog.error("saveUserIcon error",e);
			}
			if (!b)
			{
				LLog.error("saveUserIcon error");
			}
			else
			{
				//success
			}
		
		} catch (FileNotFoundException e) {
			LLog.error("save user icon error",e);
			
		}
	
	
	}
	
	public Bitmap getUserIcon()
	{
		File dir = LCommonUtil.getStorageDirectory();
		File file = new File(dir,FILE_NAME_USER_ICON);
		if(!file.exists())
		{
			return null;
		}
		
		Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
		return bm;
	}
}
