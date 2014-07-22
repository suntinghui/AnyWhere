package com.lookstudio.anywhere.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LScreenUtil {

	
	public static void forward(Context context,Class<?> dest)
	{
		forward(context,dest,null);
	}
	
	public static void forward(Context context,Class<?> dest,Bundle extras)
	{
		Intent intent = new Intent(context,dest);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(null != extras)
		{
			intent.putExtras(extras);
		}
		context.startActivity(intent);
	}
	
	public static void backward(Activity activity)
	{
		activity.finish();
	}
}
