package com.lookstudio.anywhere.chweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.util.EncodingUtils;

import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.util.LLog;

import android.content.Context;



public class LWeatherUtils {

	public static String loadAssetTxt(Context context){
		String res = "";
		InputStream in = null;
		try {
			in = context.getResources().getAssets().open("citycode.txt");
			int length = in.available(); 
			byte[] buffer = new byte[length]; 
			in.read(buffer); 
			res = EncodingUtils.getString(buffer, "UTF-8");
			LLog.info(res);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static String findCityCodeByName(String cityname,Context context){
		if(null==cityname||"".equals(cityname)) 
			return null;
	      try { 
              InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open("citycode.txt") ); 
              BufferedReader bufReader = new BufferedReader(inputReader);
              String line="";
              String[] str = new String[2];
              while((line = bufReader.readLine()) != null){
              	  str = line.split("=");
              	  if(str.length==2&&null!=str[1]&&!"".equals(str[1])&&cityname.equals(str[1])){
              		  
              		  return str[0];
              	  }
              }
          } catch (Exception e) { 
              e.printStackTrace(); 
              return null;
          }
          return null;
	}
	
	public static String getWeekday(Context context)
	{
		int weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		String []array = context.getResources().getStringArray(R.array.str_array_weekday);
		return array[weekday];
	}
	
	public static String currentDate()
	{
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
		return DATE_FORMAT.format(new Date());
	}
	
	public static String getDirection(int dirInDegree)
	{
		String dir = "";
		String[] dirs = {"北","东北","东","东南","南","西南","西","西北"};
		float unit = 45/2;
		int areaNum = (int)(dirInDegree/unit);
		
		if((0 == areaNum) || (15 <= areaNum))
		{
			dir = dirs[0];
		}
		else
		{
			int index = (int)(areaNum/2);
			dir = dirs[index];
		}
		
		LLog.info("getDirection dirInDegree = " + dirInDegree + " dir = " + dir);
		return dir;
	}
}
