package com.lookstudio.anywhere.util;

import android.util.Log;



public class LLog {

	private static String LOG_TAG = "anywhere";
	public static boolean DEBUG = true;
	private static LFileLogger sFileLogger = new LFileLogger();
	
	public static void info(String msg)
	{
		if (DEBUG) {
			Log.i(LOG_TAG, msg);
			sFileLogger.i(LOG_TAG, msg);
		}
	}
	
	
	public static void warn(String msg)
	{
		if (DEBUG) {
			Log.w(LOG_TAG, msg);
			sFileLogger.w(LOG_TAG, msg);
		}
	}
	
	public static void error(String msg)
	{
		if (DEBUG) {
			Log.e(LOG_TAG, msg);
			sFileLogger.e(LOG_TAG,msg);
		}
	}
	
	public static void error(String msg,Throwable th)
	{
		if (DEBUG) {
			Log.e(LOG_TAG, msg, th);
			sFileLogger.e(LOG_TAG,msg,th);
		}
	}
	
}
