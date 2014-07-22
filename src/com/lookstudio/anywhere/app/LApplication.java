package com.lookstudio.anywhere.app;

import com.lookstudio.anywhere.bitmap.caches.StrongBitmapCache;
import com.lookstudio.anywhere.model.LAppManager;
import com.lookstudio.anywhere.model.LLocationManager;
import com.lookstudio.anywhere.util.LSetting;

import android.app.Application;
import android.content.Context;

public class LApplication extends Application {

	public static Context appContext = null;
	private StrongBitmapCache mBitmapCache;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		appContext = getApplicationContext();
		LAppManager.appStart(getApplicationContext());
		mBitmapCache = StrongBitmapCache.build(this);  
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
    }
	
	public StrongBitmapCache getBitmapCache(){ 
        return mBitmapCache; 
    }
}
