package com.lookstudio.anywhere.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;

import com.lookstudio.anywhere.app.LApplication;



public class LFileLogger {

	public static final int VERBOSE = 2;

	public static final int DEBUG = 3;

	public static final int INFO = 4;

	public static final int WARN = 5;

	public static final int ERROR = 6;

	public static final int ASSERT = 7;
	private String mPath;
	private Writer mWriter;

	private static final SimpleDateFormat TIMESTAMP_FMT = new SimpleDateFormat(
			"[yyyy-MM-dd HH:mm:ss] ");
	private String basePath = "";
	private static String LOG_DIR = "log";
	public static  File logDir;

	static
	{
		logDir = new File(LCommonUtil.getStorageDirectory(),LOG_DIR);
	}
	
	public LFileLogger()
	{

	}

	public void open()
	{
		
		if(false == logDir.exists())
		{
			logDir.mkdirs();
		}
		try
		{
			File file = createNewFile();
			
			mWriter = new BufferedWriter(new FileWriter(file), 2048);
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private static Set<File> mCreatedFiles = new HashSet<File>(); 
	private File createNewFile()
	{
		File file = new File(logDir.getAbsolutePath() + "/" + getCurrentTimeString() + ".log");
		if(false == file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		mCreatedFiles.add(file);
		return file;
	}
	
	
	private String getCurrentTimeString()
	{
		Date now = new Date();
		/*SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");*/
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		return simpleDateFormat.format(now);
	}

	public String getPath()
	{
		return mPath;
	}


	public void d(String tag, String message)
	{
		// TODO Auto-generated method stub
		println(DEBUG, tag, message);
	}


	public void e(String tag, String message)
	{
		println(ERROR, tag, message);
	}

	
	public void e(String tag, String message,Throwable th) {
		println(ERROR,tag,message + "exception:" + exceptionMessage(th));
		
	}

	public void i(String tag, String message)
	{
		println(INFO, tag, message);
	}


	public void v(String tag, String message)
	{
		println(VERBOSE, tag, message);
	}


	public void w(String tag, String message)
	{
		println(WARN, tag, message);
	}

	public void println(int priority, String tag, String message)
	{
		Context context = LApplication.appContext;
		String printMessage = "";
		switch (priority)
		{
		case VERBOSE:
			printMessage = "[V]|"
					+ tag
					+ "|"
					+ context.getPackageName() + "|" + message;
			break;
		case DEBUG:
			printMessage = "[D]|"
					+ tag
					+ "|"
					+ context.getPackageName() + "|" + message;
			break;
		case INFO:
			printMessage = "[I]|"
					+ tag
					+ "|"
					+ context.getPackageName() + "|" + message;
			break;
		case WARN:
			printMessage = "[W]|"
					+ tag
					+ "|"
					+ context.getPackageName() + "|" + message;
			break;
		case ERROR:
			printMessage = "[E]|"
					+ tag
					+ "|"
					+ context.getPackageName() + "|" + message;
			break;
		default:

			break;
		}
		println(printMessage);

	}

	public void println(String message)
	{
		checkToCreateNewFile();
		try
		{
			mWriter.write(TIMESTAMP_FMT.format(new Date()));
			mWriter.write(message);
			mWriter.write('\n');
			mWriter.flush();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void close()
	{
		try
		{
			mWriter.close();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String exceptionMessage(Throwable th)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("\n" + "-------||:-)--------EXCEPTION----||:o|-------" + "\n");
		builder.append(th + "\n");
		StackTraceElement[] elements = th.getStackTrace();
		for(StackTraceElement element : elements)
		{
			builder.append(element.toString() + " \n");
		}
		
		return builder.toString();
	}
	
	private static int HOUR = -1;
	private void checkToCreateNewFile()
	{
		if(HOUR == -1)
		{
			open();
		}
		
		int cur_hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(HOUR != cur_hour)
		{
			HOUR = cur_hour;
			close();
			open();
		}
	}


	
}
