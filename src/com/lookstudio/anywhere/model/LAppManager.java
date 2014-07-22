package com.lookstudio.anywhere.model;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.lookstudio.anywhere.model.TaskHandler.Task;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LSetting;

import android.content.Context;

public class LAppManager {

	
	public static void appStart(final Context context)
	{
		LSetting.init(context);
		LLocationManager.getManager().setContext(context);
		LLocationManager.getManager().requestLocationUpdates();
	}
	
	public static void onLogin(final Context context)
	{
		
	}
	public static void appExit()
	{
		
	}
}
