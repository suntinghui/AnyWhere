package com.lookstudio.anywhere.util;


import java.io.File;
import java.io.IOException;
import java.util.List;

import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.model.LDriveOverviewInfo;
import com.lookstudio.anywhere.model.LDriveRecord;
import com.lookstudio.anywhere.model.LSaver;
import com.lookstudio.anywhere.model.TimeHandler;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class LCommonUtil {

	public static String formatTime(int timeInSeconds)
	{
		TimeHandler timeHandler = new TimeHandler(timeInSeconds*1000);
		return timeHandler.toString();
	}
	
	public static String formatTimeNormal(int timeInSeconds)
	{
		TimeHandler timeHandler = new TimeHandler(timeInSeconds*1000);
		return timeHandler.getHourWithPre0() + " : " + timeHandler.getMinuteWithPre0() + " : " + timeHandler.getSecondWithPre0();
	}
	
	public static String getElapseTime(int timeInMillis)
	{
		TimeHandler timeHandler = new TimeHandler(timeInMillis);
		String result = "";
		
		if(0 != timeHandler.getHour())
		{
			result += timeHandler.getHour() + "小时";
		}
		
		if(0 != timeHandler.getMinute())
		{
			result += timeHandler.getMinute() + "分钟";
		}
		
		return result;
	}
	
	public static Bitmap loadBitmap(String filePath)
	{
		if((null == filePath) || "".equals(filePath))
		{
			return null;
		}

	    Bitmap bm =  BitmapFactory.decodeFile(filePath); 
	    return bm;
	}
	
	public static Bitmap loadBitmap(String filePath,int width,int height)
	{
		if((null == filePath) || "".equals(filePath))
		{
			return null;
		}
		 // First decode with inJustDecodeBounds=true to check dimensions  
	    final BitmapFactory.Options options = new BitmapFactory.Options();  
	    options.inJustDecodeBounds = true;  
	    BitmapFactory.decodeFile(filePath, options);  
	  
	    // Calculate inSampleSize  
	    options.inSampleSize = calculateInSampleSize(options, width, height);  
	  
	    // Decode bitmap with inSampleSize set  
	    options.inJustDecodeBounds = false;  
	    Bitmap bm =  BitmapFactory.decodeFile(filePath, options); 
	    Bitmap scaleBm = scaleBitmap(bm,width,height);
	    bm.recycle();
	    return scaleBm;
	}
	
	public static File getStorageDirectory()
	{
		String appName = "anywhere"; 
		File dir = new File(Environment.getExternalStorageDirectory(),appName);
		if(false == dir.exists())
		{
			if(false == dir.mkdirs())
			{
				LLog.error("fail to create directory:" + dir.getAbsolutePath());
				throw new RuntimeException("fail to create directory:" + dir.getAbsolutePath());
			}
		}
		
		return dir;
	}
	
	
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {  
	    // Raw height and width of image  
	    final int height = options.outHeight;  
	    final int width = options.outWidth;  
	    int inSampleSize = 1;  
	  
	    if (height > reqHeight || width > reqWidth) {  
	        if (width > height) {  
	            inSampleSize = Math.round((float)height / (float)reqHeight);  
	        } else {  
	            inSampleSize = Math.round((float)width / (float)reqWidth);  
	        }  
	    }  
	    return inSampleSize;  
	}  
	
	public static Bitmap scaleBitmap(Bitmap src,int newWidth,int newHeight)
	{
		int width = src.getWidth();
		int height = src.getHeight();
		LLog.info("before scaleBitmap,dimension(" + ",");
	 	float scaleWidth = ((float) newWidth) / width;  
        float scaleHeight = ((float) newHeight) / height;  
  
        Matrix matrix = new Matrix();  
        matrix.postScale(scaleWidth, scaleHeight);  
  
        // create the new Bitmap object  
        Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, width,  
                height, matrix, true);  
        
        return resizedBitmap;
	}
	
	public static void play(String filePath,OnCompletionListener listener)
	{
		MediaPlayer player = new MediaPlayer();
		if (player.isPlaying())
		{
			player.stop();
		}
		player.setOnCompletionListener(listener);
		try {
			player.reset();
			player.setDataSource(filePath);
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			LLog.error("error" ,e);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			LLog.error("error" ,e);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			LLog.error("error" ,e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LLog.error("error" ,e);
		}
	}
	
	

	public static boolean isNetworkActivate(Context context) {

		boolean isConnected = false;
		ConnectivityManager cgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cgr == null) {
			return false;
		}
		NetworkInfo info = cgr.getActiveNetworkInfo();
		if ((info != null)  && info.isConnected() ) {
			isConnected = true;
		}
		return isConnected;
	}


	private static final double W_DEVIDE_H = 0.562f;
	public static int getWidthInConstraint(int height)
	{
		return (int)(height*W_DEVIDE_H);
	}
	

	public static LDriveOverviewInfo driveOverviewInfo(List<LDriveRecord> records)
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
	
	public static LDriveOverviewInfo driveOverviewInfo(Context context)
	{
		LSaver saver = new LSaver(context);
		return driveOverviewInfo(saver.readRecords());
	}
	
	public static void  sendMailByIntent(Context context) {  
        String[] reciver = new String[] { context.getString(R.string.advice_email_name) };  
        String[] mySbuject = new String[] { context.getString(R.string.str_mail_subjuect) };  
/*        String myCc = "";  
        String mybody = "测试Email Intent"; */ 
        Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);  
        myIntent.setType("plain/text");  
        myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);  
        //myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);  
        myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySbuject);  
        //myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody); 
        
        Intent intent = Intent.createChooser(myIntent, context.getString(R.string.str_title_choose_mail));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);  
  
    } 
	
	public static double fromMtoKM(double disInMeter)
	{
		return ((int)(disInMeter/1000 * 100)) / 100;
	} 
	
	public static void wipeDirectory(File dir)
	{
		LLog.info("wipeDirectory " + dir);
		File[] files = dir.listFiles();
		for(File file : files)
		{
			if(file.isDirectory())
			{
				wipeDirectory(file);
			}
			else
			{
				file.delete();
			}
			
		}
	}
}
