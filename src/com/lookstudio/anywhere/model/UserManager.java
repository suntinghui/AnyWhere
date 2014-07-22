package com.lookstudio.anywhere.model;

import com.lookstudio.anywhere.util.LCommonUtil;

import android.content.Context;

public class UserManager {
 
	private Context context;
	
	public UserManager(Context context)
	{
		this.context = context;
	}
	
	public void onNewUserRegister()
	{
		LCommonUtil.wipeDirectory(LCommonUtil.getStorageDirectory());
		LSaver saver = new LSaver(context);
		saver.deleteAllRecord();
	}
	
	public void onNewUserLogin()
	{
		
	}
}
