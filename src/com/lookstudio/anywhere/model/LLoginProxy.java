package com.lookstudio.anywhere.model;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.lookstudio.anywhere.http.LHttpCommunication;
import com.lookstudio.anywhere.http.LResponse;
import com.lookstudio.anywhere.model.TaskHandler.Task;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.LThreadFactory;

import android.content.Context;
import android.os.Handler;

public class LLoginProxy {

	public static final int ERROR_NOT_REGISTER_YET = 0x1001;
	public static final int ERROR_USERNAME         = 0x1002;
	public static final int ERROR_PASSWORD         = 0x1003;
	public static final int ERROR_NO_ERROR         = 0x1004;
	public static final boolean TEST_MODE          = false;
	
	public interface OnFinishListener
	{
		public void onFinish(boolean successful,String errorInfo);
	}
	
	public LLoginInfo getLoginInfo(Context context)
	{
		LSaver saver = new LSaver(context);
		LLoginInfo old = saver.readLoginInfo();
		return old;
	}
	
	public void register(final LRegisterInfo info,final Context context,final OnFinishListener onFinishListener)
	{
		if(TEST_MODE)
		{
			new Handler().postDelayed(new Runnable()
			{

				@Override
				public void run() {
					if(null != onFinishListener)
					{
						onFinishListener.onFinish(false, "okay,we are playing joke");
					}
					
				}
				
			}, 1*1000);
			return;
		}
		
		LThreadFactory.createThread("Thread register",new Runnable()
		{

			@Override
			public void run()
			{
				LRegisterRequest request = new LRegisterRequest(info);
				String result = LHttpCommunication.post(request);
				if(null != result)
				{
					LResponse<LRegisterResponse> response = request.parse(result);
					if(response.isResultOk())
					{
						LLog.info("succeed to register" + response.getBean().toString());
						LSaver saver = new LSaver(context);
						saver.saveRegisterInfo(info);
						saver.saveLoginInfo(info.getLoginInfo());
						
						//cache session
						LSessionInfo info = new LSessionInfo(response.getBean());
						LCacheManager.get().cache("session", info);
						
						new UserManager(context).onNewUserRegister();
						
						if(null != onFinishListener)
						{
							onFinishListener.onFinish(true, "");
						}
					}
					else
					{
						if(null != onFinishListener)
						{
							onFinishListener.onFinish(false, "");
						}
						
					}
					
				}
				else
				{
					if(null != onFinishListener)
					{
						onFinishListener.onFinish(false, "");
					}
				}
			}
			
		}).start();
		
	}
	
	public void logout(Context context)
	{
		LSaver saver = new LSaver(context);
		saver.deleteLoginInfo();
	}
	
	public void login(final LLoginInfo info,final Context context,final OnFinishListener onFinishListener)
	{
		if(TEST_MODE)
		{
			new Handler().postDelayed(new Runnable()
			{

				@Override
				public void run() {
					if(null != onFinishListener)
					{
						onFinishListener.onFinish(false, "okay,No problem");
					}
					
				}
				
			}, 1*1000);
			return;
		}
		
		LThreadFactory.createThread("Thread login",new Runnable()
		{

			@Override
			public void run() {
				
				LLoginRequest request = new LLoginRequest(info);
				String result = LHttpCommunication.post(request);
				if(null != result)
				{
					LResponse<LLoginResponse> response = request.parse(result);
					if(response.isResultOk())
					{
						LLoginResponse resp = response.getBean();
						LLog.info("login response:" + resp);
						
						LSaver saver = new LSaver(context);
						LLoginInfo old = saver.readLoginInfo();
						
						//another user logs in
						if(!info.equals(old))
						{
							new UserManager(context).onNewUserLogin();
						}
						else
						{

							TaskHandler.getHandler().exeTask(new Task()
							{

								@Override
								public boolean onRun() {
									
									LSaver saver = new LSaver(context);
									ArrayList<LDriveRecord> records = saver.readRecords();
									if((null == records) || (false == records.isEmpty()))
									{
										return false;
									}
									
									List<String> usefulFiles = new LinkedList<String>();
									for(LDriveRecord record : records)
									{
										if(null != record.comments)
										{
											for(LDriveComment comment :record.comments)
											{
												usefulFiles.addAll(comment.getPhotos());
												usefulFiles.add(comment.getRadio());
											}
										}
									}
									
									File[] files = LCommonUtil.getStorageDirectory().listFiles();
									List<File> unusefulFiles = new LinkedList<File>();
									for(File file :files)
									{
										if(false == usefulFiles.contains(file.toString()))
										{
											unusefulFiles.add(file);
										}
									}
									
									for(File file:unusefulFiles)
									{
										file.delete();
									}
									return true;
								}

								@Override
								public int delayInMillis() {
									
									return Task.ONCE;
								}
								
							});
						
						}
						saver.saveLoginInfo(info);
						
						
						LSessionInfo info = new LSessionInfo(response.getBean());
						LCacheManager.get().cache("session", info);
						
						if(null != onFinishListener)
						{
							onFinishListener.onFinish(true, "");
						}
					}
					else
					{
						if(null != onFinishListener)
						{
							onFinishListener.onFinish(false, "");
						}
					}
				}
				else
				{
					if(null != onFinishListener)
					{
						onFinishListener.onFinish(false, "");
					}
				}
			}
			
		}).start();
	}
}
  