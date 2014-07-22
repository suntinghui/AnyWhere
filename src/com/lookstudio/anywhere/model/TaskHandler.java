package com.lookstudio.anywhere.model;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class TaskHandler {

	private Handler handler;
	private static TaskHandler instance;
	
	public interface Task
	{
		public static final int ONCE = -1;
		public static final int MINUTE = 60*1000;
		
		public int     delayInMillis();
		public boolean onRun();
	}
	
	private TaskHandler(){
		instance = this;
		HandlerThread thread = new HandlerThread("TaskHanlder thread");
		thread.start();
		
		handler = new Handler(thread.getLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				Task task = (Task)msg.obj;
				
				//fail
				if(!task.onRun() && (Task.ONCE != task.delayInMillis()))
				{
					exeTaskAfter(task,task.delayInMillis());
				}
			}
		};
	}
	
	
	public static TaskHandler getHandler()
	{
		if(null == instance)
		{
			instance = new TaskHandler();
		}
		return instance;
	}
	
	public void exeTask(Task task)
	{
		handler.sendMessage(handler.obtainMessage(0, task));
	}
	
	public void exeTaskAfter(Task task,int delayMillis)
	{
		handler.sendMessageDelayed(handler.obtainMessage(0, task), delayMillis);
	}
	
	public void cancelTask(Task task)
	{
		handler.removeMessages(0, task);
	}
}
